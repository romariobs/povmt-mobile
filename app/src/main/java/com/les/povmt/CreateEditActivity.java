package com.les.povmt;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by cesar on 25/11/16.
 */

public class CreateEditActivity extends AppCompatActivity {


    private EditText title;
    private EditText description;
    private Button button_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        if (((EditText) findViewById(R.id.title_activity) != null) &&
                ((EditText) findViewById(R.id.description_activity)) != null) {
            title = (EditText) findViewById(R.id.title_activity);
            description = (EditText) findViewById(R.id.description_activity);

        }
            button_create = (Button) findViewById(R.id.button_create);

            button_create.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            Log.i("Title--", title.getText().toString());
                            Log.i("Desc--", description.getText().toString());

                        }
                    });


        }



}






