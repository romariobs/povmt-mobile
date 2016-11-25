package com.les.povmt;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class RegisterTi extends AppCompatActivity {

    private EditText mDurationTi;
    private EditText mDateTi;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ti);
        mDurationTi = (EditText) findViewById(R.id.editx_duration);
        mDateTi = (EditText) findViewById(R.id.editx_date);
        mCalendar = Calendar.getInstance();


        mDurationTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = View.inflate(RegisterTi.this, R.layout.dialog_duration, null);

                final AlertDialog alertDialog = new AlertDialog.Builder(RegisterTi.this).create();


                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.duration_picker);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(00);
                timePicker.setCurrentMinute(00);

                dialogView.findViewById(R.id.duration_set).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        mDurationTi.setText(String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        mDateTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = View.inflate(RegisterTi.this, R.layout.dialog_date, null);

                final AlertDialog alertDialog = new AlertDialog.Builder(RegisterTi.this).create();


                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                datePicker.init(mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH),null);

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mDateTi.setText(String.format("%02d-%02d-%02d", datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
    }


}
