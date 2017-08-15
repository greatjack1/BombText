package com.wyre.bombtext;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * The main fragment for bomb text
 * Created by yaakov on 8/14/17.
 */

public class MainFragment extends Fragment {
    private EditText mNumber;
    private EditText mAmount;
    private EditText mMessage;
    private Button mSendButton;
    private CheckBox mCheckBox;
    private SmsManager manager = SmsManager.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        mNumber = (EditText) v.findViewById(R.id.TE_Phone_Number);
        mAmount = (EditText) v.findViewById(R.id.TE_amount);
        mMessage = (EditText) v.findViewById(R.id.TE_Message_To_Send);
        mSendButton = (Button) v.findViewById(R.id.btn_send_texts);
        mCheckBox = (CheckBox) v.findViewById(R.id.checkBoxUniques);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if any of the feilds are empty then return
                if (mNumber.getText().toString().equals("") || mAmount.getText().toString().equals("") || mMessage.getText().toString().equals("")) {
                    //show a message explaing why we cant send
                    DialogFragmentMessage dialog = new DialogFragmentMessage();
                    dialog.show(getFragmentManager(), "Dialog");
                    return;
                }
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.SEND_SMS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Log.d("Bomb Text", "Starting to send texts");

                                int numToSend = Integer.valueOf(mAmount.getText().toString());
                                Log.d("Bomb Text", "Number to send" + numToSend);
                                for (int i = 0; i < numToSend; i++) {
                                    try {
                                        //if unique check box is selected then append a unique number if not then send it plain
                                        if (mCheckBox.isChecked()) {
                                            manager.sendTextMessage(mNumber.getText().toString(), null, mMessage.getText().toString() + i, null, null);
                                        } else {
                                            manager.sendTextMessage(mNumber.getText().toString(), null, mMessage.getText().toString(), null, null);
                                        }
                                    } catch (Exception ex) {
                                        Log.d("Bomb Text", "Error when sending bomb text: " + ex.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();

            }
        });
        return v;
    }

    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
