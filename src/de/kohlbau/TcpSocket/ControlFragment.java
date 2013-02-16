package de.kohlbau.TcpSocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class ControlFragment extends Fragment {
    OnReceivedListener onReceivedListener;

    public interface OnReceivedListener {
        public void onReceived(String text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            onReceivedListener = (OnReceivedListener) activity;
        } catch (ClassCastException e) {
            throw  new ClassCastException(activity.toString()
            + " must implement OnReceivedListener");
        }
    }


    private final static String REGEX_IPADDRESS = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private final static String REGEX_HOSTNAME = "^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$";
    private final static String TAG = "TCPSOCKET";
    ToggleButton controlToggle;
    Button deleteButton;
    Button presetButton;
    EditText ipAddressText;
    EditText ipPortText;
    private String serverIpAddress;
    private int serverIpPort;
    String mClientMsg = "test";

    Intent service;

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Object text = message.obj;
            if(message.arg1 == Activity.RESULT_OK) {
                if(text != null) {
                    onReceivedListener.onReceived(text.toString());
                } else {
                    Toast.makeText(getActivity(), R.string.connectionSuccessed, Toast.LENGTH_SHORT).show();
                }
            } else if(message.arg1 == Activity.RESULT_CANCELED) {
                if(text != null) {
                    onReceivedListener.onReceived(text.toString());
                } else {
                    Toast.makeText(getActivity(), R.string.connectionFailed, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    public ControlFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_fragment, container, false);

        controlToggle = (ToggleButton) view.findViewById(R.id.controlToggle);
        deleteButton = (Button) view.findViewById(R.id.deleteButton);
        presetButton = (Button) view.findViewById(R.id.presetButton);
        ipAddressText = (EditText) view.findViewById(R.id.ipAddressText);
        ipPortText = (EditText) view.findViewById(R.id.ipPortText);

        controlToggle.setOnCheckedChangeListener(controlListener);
        deleteButton.setOnClickListener(deleteListener);
        presetButton.setOnClickListener(presetListener);

        service = new Intent(getActivity(),  SendReceive.class);

        Messenger messenger = new Messenger(handler);
        service.putExtra("MESSENGER", messenger);

        return view;
    }

    CompoundButton.OnCheckedChangeListener controlListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                try {
                    serverIpAddress = ipAddressText.getText().toString();
                    serverIpPort = Integer.valueOf(ipPortText.getText().toString());
                    if (serverIpAddress.matches(REGEX_IPADDRESS) || serverIpAddress.matches(REGEX_HOSTNAME)) {
                        ipAddressText.setInputType(InputType.TYPE_NULL);
                        ipPortText.setInputType(InputType.TYPE_NULL);
                        service.putExtra("serverIpAddress", serverIpAddress);
                        service.putExtra("serverIpPort", serverIpPort);
                        getActivity().startService(service);
                    } else {
                        controlToggle.setChecked(false);
                        Toast.makeText(getActivity(), R.string.wrongAddress, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    controlToggle.setChecked(false);
                    Toast.makeText(getActivity(), R.string.wrongPort, Toast.LENGTH_LONG).show();
                    Log.e(TAG, e.toString());
                }
            } else {
                Toast.makeText(getActivity(), R.string.connectionClosedBySelf, Toast.LENGTH_SHORT).show();
                getActivity().stopService(service);
                ipAddressText.setInputType(InputType.TYPE_CLASS_TEXT);
                ipPortText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
    };

    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!controlToggle.isChecked()) {
                ipAddressText.setText(R.string.ipAddress);
                ipPortText.setText(R.string.ipPort);
            } else {
                onReceivedListener.onReceived(null);
            }
        }
    };

    View.OnClickListener presetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ipAddressText.setText("192.168.1.5");
            ipPortText.setText("31415");
        }
    };


}
