package com.les.povmt;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.les.povmt.models.Activity;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.util.Constants;
import com.les.povmt.util.Messages;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by cesar on 26/11/16.
 */

public class EditActivity extends AppCompatActivity {
    private final int SELECT_FILE = 1; /////
    private final int PICK_CAMERA_IMAGE = 2; /////
    private ImageView imgView; /////
    private Button button_pick; /////

    private final String TAG = this.getClass().getSimpleName();

    private final String WORK_STR = "WORK";

    private EditText title;
    private MaterialBetterSpinner spn;
    private int position = 0;
    private String priority = "";
    private RadioGroup group;
    private String category;
    private EditText description;
    private Button button_create;
    private final Context mContext = this;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        activity = getIntent().getExtras().getParcelable("activity");

        imgView = (ImageView) findViewById(R.id.photo_thumnail); /////
        Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + activity.getId() +".jpg");
        imgView.setImageBitmap(bMap);

        button_pick = (Button) findViewById(R.id.button_pick); /////

        title = (EditText) findViewById(R.id.title_activity);
        description = (EditText) findViewById(R.id.description_activity);
        spn = (MaterialBetterSpinner) findViewById(R.id.priority_activity);
        group = (RadioGroup) findViewById(R.id.group);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listPriority, android.R.layout.simple_spinner_item);
        spn.setAdapter(adapter);

        title.setText(activity.getTitle());
        description.setText(activity.getDescription());

        priority = activity.getPriority();

        switch (priority){
            case Constants.LOW:
                position = 0;
                spn.setText("Baixa");
                break;

            case Constants.MEDIUM:
                position = 1;
                spn.setText("Média");
                break;
            case Constants.HIGH:
                position = 2;
                spn.setText("Alta");
                break;
        }

       ((RadioButton) findViewById(activity.getCategory().equals(WORK_STR) ?
               R.id.radioButton_job : R.id.radioButton_recreation)).setChecked(true);


        category = activity.getCategory();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);

                category = button.getText().toString();
                //Handle the case where the user don't select any thing
                //In this case no put the parameter to send in the request.
                if (category.equals("Trabalho")){
                    category = Constants.WORK;
                }
                else {
                    category = Constants.LEISURE;
                }
                //Check this better!
            }
        });



        button_pick.setOnClickListener(new View.OnClickListener() { /////
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("Imagem da Atividade");
                builder.setItems(new CharSequence[]{"Galeria", "Câmera"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;

                        switch (which) {
                            // GET IMAGE FROM GALLERY
                            case 0:
                                intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                Intent chooser = Intent.createChooser(intent, "Escolha uma Imagem");
                                startActivityForResult(chooser, SELECT_FILE);
                                break;

                            // GET IMAGE FROM CAMERA
                            case 1:
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, PICK_CAMERA_IMAGE);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        priority = Constants.LOW;
                        break;
                    case 1:
                        priority = Constants.MEDIUM;
                        break;
                    case 2:
                        priority = Constants.HIGH;
                        break;
                }
            }
        });

        button_create = (Button) findViewById(R.id.button_create);
        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            validate();
                            onBackPressed();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editActivity();
        finish();
    }

    private void validate () throws Exception {
        String error = null;

        if (title.getText().toString().trim().isEmpty()) {
            error = "Título vazio";
        } else if (priority == null || priority.trim().isEmpty())  {
            error = "Prioridade vazia";
        } else if (category == null || category.trim().isEmpty()) {
            error = "Categoria vazia";
        }

        if (error != null) throw new Exception(error);
    }

    private void editActivity() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject json = new JSONObject(response);

                    int status = 0;
                    String activity = "";

                    if (json.has(Constants.TAG_STATUS)) {
                        status = json.getInt(Constants.TAG_STATUS);
                    }

                    if (json.has(Constants.TAG_ACTIVITY)) {
                        activity = json.getString(Constants.TAG_ACTIVITY);
                    }

                    if (status == RestClient.HTTP_OK) {
                        finish();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setMessage(Messages.EDIT_ACTIVITY_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {

                    Log.e(TAG, e.getMessage());
                }
            };

        };

        activity.setTitle(title.getText().toString());
        activity.setPriority(priority);
        activity.setCategory(category);
        activity.setDescription(description.getText().toString());

        Map<String, String> parameters = new HashMap<>();

        parameters.put(Constants.TAG_TITLE, title.getText().toString());
        parameters.put(Constants.TAG_DESCRIPTION, description.getText().toString());
        parameters.put(Constants.TAG_CREATOR, User.getCurrentUser().getId());
        parameters.put(Constants.TAG_PRIORITY, priority);
        parameters.put(Constants.TAG_CATEGORY, category);
        RestClient.put(mContext, RestClient.ACTIVITY_ENDPOINT_URL+"/"+ activity.getId() , parameters, responseListener);

    }

    public File savebitmap(Bitmap bmp) throws IOException { /////
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getId() +".jpg");
        ActivityCompat.requestPermissions(EditActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) { /////
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    // permission denied
                    Toast.makeText(EditActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(EditActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { /////
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode != 0){
            Bitmap imageBitmap = null;

            switch (requestCode) {
                case SELECT_FILE:
                    Uri uri = imageReturnedIntent.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imgView.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case PICK_CAMERA_IMAGE:
                    if (resultCode == RESULT_OK) {
                        Bundle extras = imageReturnedIntent.getExtras();
                        imageBitmap = (Bitmap) extras.get("data");
                        imgView.setImageBitmap(imageBitmap);
                    }
                    break;
            }

            try {
                ActivityCompat.requestPermissions(EditActivity.this,
                        new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                savebitmap(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
