package com.ide.customer.accounts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ide.customer.PhotoUploadAdapter;
import com.ide.customer.R;
import com.ide.customer.TrialActivity;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.models.EditProfileResponse;
import com.ide.customer.others.ImageCompressMode;
import com.ide.customer.others.SingletonGson;
import com.ide.customer.rentalmodule.RentalConfig;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditProfileActivity extends Activity implements ApiManager.APIFETCHER {

    TextView name_txt , email_address_txt  , phone_txt ;
    SessionManager sessionmanager ;
    ImageView iv_profile_pic_upload  , ic_camera  ;
    ArrayList<String> upload_options;

    String imagePath = ""  , imagePathCompressed = "" ;
    Gson gson ;
    ApiManager apiManager ;
    Uri selectedImage;
    Bitmap bitmap1;

    private static final int RC_CAMERA_PERM = 123;
    private static final int CAMERS_PICKER = 122;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this  , this , this );
        gson = new GsonBuilder().create();
        sessionmanager = new SessionManager(EditProfileActivity.this);
        setContentView(R.layout.activity_edit_profile);
        name_txt = (TextView) findViewById(R.id.name_txt);
        email_address_txt = (TextView) findViewById(R.id.email_address_txt);
        phone_txt = (TextView) findViewById(R.id.phone_txt);
        iv_profile_pic_upload = (ImageView) findViewById(R.id.iv_profile_pic_upload);
        ic_camera  = (ImageView) findViewById(R.id.ic_camera);

        upload_options = new ArrayList<>();
        upload_options.add("Câmera");
        upload_options.add("Fotos");

        if(sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_NORAL)){
            ic_camera.setVisibility(View.VISIBLE);
        }else {
            ic_camera.setVisibility(View.GONE);
        }


       try{if(sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_FACEBOOK)){
           Glide.with(this).load(""+sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_IMAGE)).into(iv_profile_pic_upload);
       }else if (sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_GOOGLE)){
           Picasso.with(this).load(""+sessionmanager.getUserDetails().get(SessionManager.GOOGLE_IMAGE)).placeholder(R.drawable.ic_google).into(iv_profile_pic_upload);
       }else if (sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_NORAL)){
           Picasso.with(this).load(String.valueOf(sessionmanager.getUserDetails().get(SessionManager.USER_IMAGE))).placeholder(R.drawable.ic_google).into(iv_profile_pic_upload);

           Log.d("**IMGAR==SESSION==", ""+sessionmanager.getUserDetails().get(SessionManager.USER_IMAGE));
       }}catch (Exception e ){}


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });   //   {facebook_id=1479168742128308}
        findViewById(R.id.edt_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(EditProfileActivity.this , EditEmailAddressActivity.class));
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , String > data  = new HashMap<String, String>();
                data.put("user_id" , ""+sessionmanager.getUserDetails().get(SessionManager.USER_ID));
                data.put("unique_id" , ""+ Settings.Secure.getString(EditProfileActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                apiManager.execution_method_post(""+com.ide.customer.samwork.Config.ApiKeys.KEY_LOGOUT , ""+ Apis.URL_LOGOUT , data , true,ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });

      /* ic_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i1.setType("image*//*");
                startActivityForResult(i1, 101);
            }
        });*/


        iv_profile_pic_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  showReasonDialog();

                final Dialog dialog = new Dialog(EditProfileActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                dialog.setContentView(R.layout.dialog_upload_options);

                ListView lv_cars = (ListView) dialog.findViewById(R.id.lv_car_model);
                lv_cars.setAdapter(new PhotoUploadAdapter(EditProfileActivity.this, upload_options));

                lv_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Configuration config = getBaseContext().getResources().getConfiguration();

                        switch (position){
                            case 0:
                                try{
                                    cameraTask();
                                }catch (Exception e){}
                                break;

                            case 1:
                                Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                i1.setType("image/*");
                                startActivityForResult(i1, 101);
                                break;
                        }

                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        findViewById(R.id.ll_done_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("**IMAGE==", ""+imagePathCompressed);
                HashMap<String , String > data_image = new HashMap<>();
                data_image.put("user_name" , ""+sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME));
                data_image.put("user_email" , ""+sessionmanager.getEmail());
                data_image.put("user_id" , ""+sessionmanager.getUserDetails().get(SessionManager.USER_ID));
                apiManager.execution_method_post_single_image("" + Config.ApiKeys.KEY_EDIT_PROFILE,Apis.URL_EDIT_PROFILE,""+ imagePathCompressed,"user_image",data_image,true,ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if((sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME)).equals(sessionmanager.getUserDetails().get(SessionManager.USER_LAST_NAME))){
            name_txt.setText(""+sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME));

        }else{
            name_txt.setText(""+sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME)+" "+sessionmanager.getUserDetails().get(SessionManager.USER_LAST_NAME));
        }
        phone_txt.setText(""+sessionmanager.getUserDetails().get(SessionManager.USER_PHONE));
        email_address_txt.setText(""+sessionmanager.getEmail());
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(APINAME.equals(""+Config.ApiKeys.KEY_EDIT_PROFILE)){
                EditProfileResponse editProfileResponse = SingletonGson.getInstance().fromJson(""+script , EditProfileResponse.class);
                sessionmanager.createLoginSession(editProfileResponse.getDetails().getUser_id(),
                        editProfileResponse.getDetails().getUser_name() ,
                        editProfileResponse.getDetails().getUser_name(),
                        editProfileResponse.getDetails().getUser_email(),
                        editProfileResponse.getDetails().getUser_phone(),
                        editProfileResponse.getDetails().getUser_image(),
                        editProfileResponse.getDetails().getUser_password(),
                        editProfileResponse.getDetails().getLogin_logout(),
                        editProfileResponse.getDetails().getDevice_id(),
                        editProfileResponse.getDetails().getFacebook_id(),
                        editProfileResponse.getDetails().getFacebook_mail(),
                        editProfileResponse.getDetails().getFacebook_image(),
                        editProfileResponse.getDetails().getFacebook_firstname(),
                        editProfileResponse.getDetails().getFacebook_lastname(),
                        editProfileResponse.getDetails().getGoogle_id(),
                        editProfileResponse.getDetails().getGoogle_name(),
                        editProfileResponse.getDetails().getGoogle_mail(),
                        editProfileResponse.getDetails().getGoogle_image(),
                        sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE),
                        editProfileResponse.getDetails().getUnique_number());
                Picasso.with(this).load(editProfileResponse.getDetails().getUser_image()).placeholder(R.drawable.ic_google).into(iv_profile_pic_upload);

                Log.d("**image_profile==",editProfileResponse.getDetails().getUser_image());
                finish();
            }else{
                finilalizeActivity();
                sessionmanager.logoutUser();
            }}catch (Exception E){}

    }


    private void finilalizeActivity() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        JSONObject no_data = new JSONObject();
        try {
            if(sessionmanager.isLoggedIn()){
                no_data.put("result" , "1");
            }else{
                no_data.put("result" , "0");
            }

            no_data.put("response","EditProfile  Activity Cancelled.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent=new Intent();
        intent.putExtra("MESSAGE",""+no_data);
        setResult(3,intent);
        finish();
        try {
            TrialActivity.mainActivity.finish();
        }catch (Exception e ){

        }
        startActivity(new Intent(this , LoginActivity.class));
    }




/*

    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == Activity.RESULT_OK) {
            try {
                if (req == 101) {
                    selectedImage = data.getData();
                    imagePath = getPath(selectedImage);

                    ImageCompressMode imageCompressMode = new ImageCompressMode(this);
                    imagePathCompressed = imageCompressMode.compressImage(imagePath);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    // Set the Image in ImageView after decoding the String
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, options);
                    final int REQUIRED_SIZE = 300;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bitmap1 = BitmapFactory.decodeFile(filePath, options);
                    iv_profile_pic_upload.setImageBitmap(bitmap1);
                }
            } catch (Exception e) {}
        }
    }
*/




    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

        switch (req){
            case 101 :
                if (res == Activity.RESULT_OK) {

                    try {
                        selectedImage = data.getData();
                        imagePath = getPath(selectedImage);

                        ImageCompressMode imageCompressMode = new ImageCompressMode(this);
                        imagePathCompressed = imageCompressMode.compressImage(imagePath);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();


                        // Set the Image in ImageView after decoding the String
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(filePath, options);
                        final int REQUIRED_SIZE = 300;
                        int scale = 1;
                        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                            scale *= 2;
                        options.inSampleSize = scale;
                        options.inJustDecodeBounds = false;
                        bitmap1 = BitmapFactory.decodeFile(filePath, options);
                        iv_profile_pic_upload.setImageBitmap(bitmap1);
                    } catch (Exception e) {
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case CAMERS_PICKER :
                if (res == Activity.RESULT_OK) {
                    try {
                        if (res == RESULT_OK) {
                            bitmap1 = (Bitmap) data.getExtras().get("data");
                            iv_profile_pic_upload.setImageBitmap(bitmap1);
                            Uri tempUri = getImageUri(getApplicationContext(), bitmap1);
                            imagePathCompressed = new ImageCompressMode(this).compressImage(getPath(tempUri));
                        }

                    } catch (Exception e) {
                        Log.d("res         ",  e.toString());
                    }
                }
                break;        }
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask()throws Exception {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)) {
            try{ // Have permission, do the thing!
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERS_PICKER);}catch (Exception e){}
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera), RC_CAMERA_PERM, android.Manifest.permission.CAMERA);
        }
    }

}
