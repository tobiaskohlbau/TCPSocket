package de.kohlbau.TcpSocket;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements ControlFragment.OnReceivedListener {
    CollectionPagerAdapter mCollectionPageAdapter;
    ViewPager mViewPager;

    public void  onReceived(String text) {
        ((DisplayFragment) ((CollectionPagerAdapter) mViewPager.getAdapter()).getFragment(1)).updateText(text);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mCollectionPageAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPageAdapter);
    }



    /*
    public void onReceived(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
    }

    public void onReceivedText(String text) {
        DisplayFragment display = (DisplayFragment) ((CollectionPagerAdapter) mViewPager.getAdapter()).getFragment(1);
        display.updateText(text);
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
    } */

        //String languageToLoad = "de";
        //Locale locale = new Locale(languageToLoad);
        //Locale.setDefault(locale);
        //Configuration config = new Configuration();
        //config.locale = locale;
        //getBaseContext().getResources().updateConfiguration(config,
        //        getBaseContext().getResources().getDisplayMetrics());

         /*
        receivedTextView = (TextView) findViewById(R.id.receivedTextView);
        controlToggle = (ToggleButton) findViewById(R.id.controlToggle);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        presetButton = (Button) findViewById(R.id.presetButton);
        ipAddressText = (EditText) findViewById(R.id.ipAddressText);
        ipPortText = (EditText) findViewById(R.id.ipPortText);


        controlToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        serverIpAddress = ipAddressText.getText().toString();
                        serverIpPort = Integer.valueOf(ipPortText.getText().toString());
                        if (serverIpAddress.matches(REGEX_IPADDRESS) || serverIpAddress.matches(REGEX_HOSTNAME)) {
                            ipAddressText.setInputType(InputType.TYPE_NULL);
                            ipPortText.setInputType(InputType.TYPE_NULL);
                            m_Thread = new Thread(new myThread());
                            m_Thread.start();
                        } else {
                            controlToggle.setChecked(false);
                            Toast.makeText(getBaseContext(), R.string.wrongAddress, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        controlToggle.setChecked(false);
                        Toast.makeText(getBaseContext(), R.string.wrongPort, Toast.LENGTH_LONG).show();
                        Log.e(TAG, e.toString());
                    }
                } else {
                    m_Thread.interrupt();
                    ipAddressText.setInputType(InputType.TYPE_CLASS_TEXT);
                    ipPortText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });

        deleteButton.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!controlToggle.isChecked()) {
                    ipAddressText.setText(R.string.ipAddress);
                    ipPortText.setText(R.string.ipPort);
                } else {
                    receivedTextView.setText("");
                }
            }
        });

        presetButton.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipAddressText.setText("192.168.1.7");
                ipPortText.setText("31415");
            }
        });

    }

    class myThread implements Runnable {
        @Override
        public void run() {
            Socket client = null;
            int i = CONNECTION_TRIES;

            while(i > 0) {
                try {
                    InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                    client = new Socket(serverAddr, serverIpPort);
                    i = 0;
                } catch (UnknownHostException e) {
                    Log.e(TAG, e.toString());
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
                try {
                    Thread.sleep(TIME_BETWEEN_TRIES);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                    client = null;
                }
                i--;
            }

            if(client == null) {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        controlToggle.setChecked(false);
                        Toast.makeText(getBaseContext(), R.string.connectionFailed, Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }   else {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), R.string.connectionSuccessed, Toast.LENGTH_SHORT).show();
                    }
                });
            }



            while (!Thread.currentThread().isInterrupted()) {
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    mClientMsg = input.readLine();
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mClientMsg != null) {
                                if(receivedTextView.getText().equals(getResources().getString(R.string.noTextReceived))) {
                                    receivedTextView.setText("");
                                }
                                receivedTextView.setText(receivedTextView.getText() + mClientMsg + "\n");
                            }
                        }
                    });
                    if (client.isClosed()) {
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                receivedTextView.setText("");
                                controlToggle.setChecked(false);
                            }
                        });
                    }
                    Thread.sleep(500);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                } catch (InterruptedException e1) {
                    try {
                        client.close();
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                receivedTextView.setText("");
                                controlToggle.setChecked(false);
                            }
                        });
                    } catch (Exception e2) {
                        Log.v(TAG, e2.toString());
                    }
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    */

    public class CollectionPagerAdapter extends FragmentPagerAdapter {

        private Map<Integer, Fragment> mPageReferenceMap = new HashMap<Integer, Fragment>();

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ControlFragment controlFragment = new ControlFragment();
                    mPageReferenceMap.put(position, controlFragment);
                    return controlFragment;
                case 1:
                    DisplayFragment displayFragment = new DisplayFragment();
                    mPageReferenceMap.put(position, displayFragment);
                    return displayFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return("Control");
                case 1:
                    return("Display");
            }
            return null;
        }

        public Fragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }
    }
}

