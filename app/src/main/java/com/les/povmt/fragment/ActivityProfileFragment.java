package com.les.povmt.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.les.povmt.EditActivity;
import com.les.povmt.R;
import com.les.povmt.models.Activity;

import org.w3c.dom.Text;

import java.io.File;

public class ActivityProfileFragment extends Fragment {
    private Activity activity;

    public ActivityProfileFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_activity, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ListUserActivity.this, "Cesar", Toast.LENGTH_SHORT).show();
                startEditActivity();
            }
        });
        ImageView image = (ImageView) v.findViewById(R.id.imageActivity);

        Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + activity.getId() +".jpg");
        image.setImageBitmap(bMap);

        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(activity.getDescription());
        TextView category = (TextView) v.findViewById(R.id.category);
        if(activity.getCategory().equals("LEISURE"))
            category.setText("Lazer");
        else
            category.setText("Trabalho");

        TextView priority = (TextView) v.findViewById(R.id.priority);
        if(activity.getPriority().equals("HIGH"))
            priority.setText("Alta");
        else if (activity.getPriority().equals("MEDIUM"))
            priority.setText("Média");
        else
            priority.setText("Baixa");

        return v;
    }


    private void startEditActivity() {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra("activity", activity);
        startActivity(intent);
        try {
            getActivity().finish();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
