package com.les.povmt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.les.povmt.models.User;
import com.les.povmt.network.RestClient;
import com.les.povmt.network.VolleySingleton;
import com.les.povmt.util.Constants;
import com.les.povmt.util.ImageUtils;
import com.les.povmt.util.Messages;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;


/**
 * Class to create new activities.
 *
 * @author Cesar
 */

public class CreateActivity extends AppCompatActivity {
    private final int SELECT_FILE = 1;
    private final int PICK_CAMERA_IMAGE = 2;
    private ImageView imgView;
    private Button button_pick;
    private final String TAG = this.getClass().getSimpleName();

    private EditText title;
    private MaterialBetterSpinner spn;
    private String priority;
    private RadioGroup group;
    private String category;
    private EditText description;
    private Button button_create;
    private final Context mContext = this;

    private final String INPUT_FILE_NAME = "tempCreatePicIn";
    private final String OUTPUT_FILE_NAME = "tempCreatePicOu";
    private final int IMAGE_HEIGHT = 500;
    private final int IMAGE_WIDTH = IMAGE_HEIGHT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        deleteFiles();

        imgView = (ImageView) findViewById(R.id.photo_thumnail);
        button_pick = (Button) findViewById(R.id.button_pick);

        title = (EditText) findViewById(R.id.title_activity);
        description = (EditText) findViewById(R.id.description_activity);
        spn = (MaterialBetterSpinner) findViewById(R.id.priority_activity);

        deleteFiles ();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listPriority, android.R.layout.simple_spinner_item);
        spn.setAdapter(adapter);
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

        group = (RadioGroup) findViewById(R.id.group);
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

        button_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
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

        //Rename to this use camelcase ... should be buttonCreate
        button_create = (Button) findViewById(R.id.button_create);

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        createActivity();
        finish();
    }

    private void createActivity(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject json = new JSONObject(response);
                    int status = 0;
                    String activity = "";

                    String id = json.getJSONObject("activity").getString("id");

                    String input = Environment.getExternalStorageDirectory() + File.separator + INPUT_FILE_NAME + ".jpg";
                    String output = Environment.getExternalStorageDirectory() + File.separator + OUTPUT_FILE_NAME + ".jpg";

                    ImageUtils.getBitmap(input, output);

                    Bitmap bMap = BitmapFactory.decodeFile(output);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bMap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                    File f = null;
                    f = new File(Environment.getExternalStorageDirectory() + File.separator + id +".jpg");
                    ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();


                    if (json.has(Constants.TAG_STATUS)) {
                        status = json.getInt(Constants.TAG_STATUS);
                    }

                    if (json.has(Constants.TAG_ACTIVITY)) {
                        activity = json.getString(Constants.TAG_ACTIVITY);
                    }

                    if (status == RestClient.HTTP_CREATED) {
                        finish();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                        builder.setMessage(Messages.CREATE_ACTIVITY_ERROR_MSG).setNegativeButton("Retry", null).create().show();
                    }

                    deleteFiles ();
                } catch (Exception e) {

                    Log.e(TAG, e.getMessage());
                }
            }
        };

        Map<String, String> parameters = new HashMap<>();

        parameters.put(Constants.TAG_TITLE, title.getText().toString());
        parameters.put(Constants.TAG_DESCRIPTION, description.getText().toString());
        parameters.put(Constants.TAG_CREATOR, User.getCurrentUser().getId());
        parameters.put(Constants.TAG_PRIORITY, priority);
        parameters.put(Constants.TAG_CATEGORY, category);

        RestClient.post(mContext, RestClient.ACTIVITY_ENDPOINT_URL, parameters, responseListener);

    }

    public File savebitmap(Bitmap bmp) throws IOException { /////
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

        File f = null;
        f = new File(Environment.getExternalStorageDirectory() + File.separator + INPUT_FILE_NAME +".jpg");
        ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    private void deleteFiles () {
        ImageUtils.deleteFile(Environment.getExternalStorageDirectory() + File.separator + INPUT_FILE_NAME +".jpg");
        ImageUtils.deleteFile(Environment.getExternalStorageDirectory() + File.separator + OUTPUT_FILE_NAME +".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode != 0){
            Bitmap imageBitmap = null;

            switch (requestCode) {
                case SELECT_FILE:
                    Uri uri = imageReturnedIntent.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imgView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true));
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
                ActivityCompat.requestPermissions(CreateActivity.this,
                        new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                savebitmap(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) { /////
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CreateActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CreateActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}





