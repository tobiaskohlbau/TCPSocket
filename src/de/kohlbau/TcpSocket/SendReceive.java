package de.kohlbau.TcpSocket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class SendReceive extends Service {
    private final static int TAG = R.string.serviceName;
    final static int CONNECTION_TRIES = 4;
    final static long TIME_BETWEEN_TRIES = 500;
    private String serverIpAddress;
    private int serverIpPort;
    String mClientMsg = "";

    Thread m_Thread;

    Bundle extras;
    Messenger messenger;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        extras = intent.getExtras();
        if (extras != null) {
            messenger = (Messenger) extras.get("MESSENGER");
            serverIpAddress = extras.get("serverIpAddress").toString();
            serverIpPort = Integer.valueOf(extras.get("serverIpPort").toString());
        }
        m_Thread = new Thread(new Connection());
        m_Thread.start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(getString(TAG), "Service killed");
        m_Thread.interrupt();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class Connection implements Runnable {
        @Override
        public void run() {
            Socket client = null;
            int i = CONNECTION_TRIES;

            while (i > 0) {

                try {
                    InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                    client = new Socket(serverAddr, serverIpPort);
                    i = 0;
                } catch (UnknownHostException e) {
                    Log.e(getString(TAG), e.toString());
                } catch (IOException e) {
                    Log.e(getString(TAG), e.toString());
                }
                try {
                    Thread.sleep(TIME_BETWEEN_TRIES);
                } catch (InterruptedException e) {
                    Log.e(getString(TAG), e.toString());
                    client = null;
                }
                i--;
            }

            Message connection;
            connection = Message.obtain();
            if (client == null) {
                connection.arg1 = Activity.RESULT_CANCELED;
                return;
            } else {
                connection.arg1 = Activity.RESULT_OK;
            }

            try {
                messenger.send(connection);
            } catch (RemoteException e) {
                Log.w(getClass().getName(), "Exception sending message", e);
            }


            while (!Thread.currentThread().isInterrupted()) {
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    mClientMsg = input.readLine();
                    if (mClientMsg != null) {
                        Message msg;
                        msg = Message.obtain();
                        msg.arg1 = Activity.RESULT_OK;
                        msg.obj = mClientMsg;
                        try {
                            messenger.send(msg);
                        } catch (RemoteException e) {
                            Log.w(getClass().getName(), "Exception sending message", e);
                        }
                    }
                    if (client.isClosed()) {
                        connection.arg1 = Activity.RESULT_CANCELED;
                        connection.obj = getString(R.string.connectionClosedByOpposite);
                        return;
                    }
                    Thread.sleep(500);
                } catch (IOException e) {
                    Log.e(getString(TAG), e.toString());
                } catch (InterruptedException e1) {
                    try {
                        client.close();
                    } catch (Exception e2) {
                        Log.v(getString(TAG), e2.toString());
                    }
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
