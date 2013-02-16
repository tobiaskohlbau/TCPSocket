package de.kohlbau.TcpSocket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

    TextView receivedTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.display_fragment, container, false);
        receivedTextView = (TextView) fragmentView.findViewById(R.id.receivedTextView);

        return fragmentView;
    }

    public void updateText(String text) {
        if (text == null) {
            receivedTextView.setText("");
        } else {
            if (receivedTextView.getText().toString().equals(getString(R.string.noTextReceived))) {
                receivedTextView.setText("");
            }
            receivedTextView.setText(receivedTextView.getText() + text + "\n");
        }
    }
}
