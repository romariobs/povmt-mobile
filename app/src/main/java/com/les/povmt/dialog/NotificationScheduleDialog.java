package com.les.povmt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.les.povmt.R;

import static android.R.string.no;
import static android.R.string.yes;

/**
 * Created by romario on 12/10/2016.
 */

public class NotificationScheduleDialog extends Dialog {

    private Context mContext;
    private String mTime;
    private Button mButton;
    private EditText mEtNewTime;


    public NotificationScheduleDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notification_schedule);
        mButton = (Button) findViewById(R.id.bt_dp_set);
        //mEtNewTime = (EditText) findViewById(R.id.editx_new_time);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });






    }
}
