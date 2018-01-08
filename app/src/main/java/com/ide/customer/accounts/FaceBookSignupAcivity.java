package com.ide.customer.accounts;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ide.customer.R;
import com.ide.customer.TrialActivity;
import com.ide.customer.location.SamLocationRequestService;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceBookSignupAcivity extends AppCompatActivity implements ApiManager.APIFETCHER {



    ApiManager apiManager ;
    GsonBuilder gsonBuilder;
    Gson gson;
    SessionManager sessionManager ;
    TextView facebook_name_txt  , facebook_email_txt ;
    EditText phone_edt ;
    CircleImageView facebook_image ;
    CountryCodePicker ccp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this , this , this  );
        sessionManager = new SessionManager(FaceBookSignupAcivity.this);
        setContentView(R.layout.activity_face_book_signup_acivity);


        facebook_name_txt = (TextView) findViewById(R.id.facebook_name_txt);
        facebook_email_txt = (TextView) findViewById(R.id.facebook_email_txt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        facebook_image = (CircleImageView) findViewById(R.id.facebook_image);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        facebook_name_txt.setText(""+ Config.facebook_firstname+" "+Config.facebook_Lastname);
        facebook_email_txt.setText(""+Config.facebook_Mail);


        Glide.with(this).load(""+Config.facebook_Image).into(facebook_image);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






        findViewById(R.id.ll_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facebook_name_txt.getText().toString().equals("")  || phone_edt.getText().toString().equals("")){
                    Toast.makeText(FaceBookSignupAcivity.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }else{
                    SignUpWithfacebook(Config.facebook_ID , Config.facebook_Mail , Config.facebook_Image , Config.facebook_firstname , Config.facebook_Lastname , phone_edt.getText().toString());
                }
            }
        });




}




    public  void SignUpWithfacebook(String facebook_user_id , String mail , String image , String firstname ,String lastname , String Phone){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("facebook_id" ,""+ facebook_user_id);
        bodyparameters.put("facebook_mail" ,mail);
        bodyparameters.put("facebook_image" ,image);
        bodyparameters.put("facebook_firstname" ,firstname);
        bodyparameters.put("facebook_lastname" , lastname);
        bodyparameters.put("phone" ,"+"+ccp.getSelectedCountryCode()+ Phone);
        bodyparameters.put("first_name" ,""+ firstname);

        bodyparameters.put("last_name" ,""+ lastname);
        apiManager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_FACEBOOK_SIGNUP,  Apis.URL_FACEBOOK_SIGNUP, bodyparameters , true,ApiManager.ACTION_SHOW_TOP_BAR);
    }





    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(rcheck.getResult() == 1){
                RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                sessionManager.createLoginSession(registration_response.getDetails().getUser_id() , registration_response.getDetails().getUser_name() , registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_email(),registration_response.getDetails().getUser_phone(),registration_response.getDetails().getUser_image(),registration_response.getDetails().getUser_password(),registration_response.getDetails().getLogin_logout(),registration_response.getDetails().getDevice_id(),
                        registration_response.getDetails().getFacebook_id(),registration_response.getDetails().getFacebook_mail(),registration_response.getDetails().getFacebook_image(),registration_response.getDetails().getFacebook_firstname(),registration_response.getDetails().getFacebook_lastname(),
                        registration_response.getDetails().getGoogle_id(),registration_response.getDetails().getGoogle_name(),registration_response.getDetails().getGoogle_mail(),registration_response.getDetails().getGoogle_image(), SessionManager.LOGIN_FACEBOOK , registration_response.getDetails().getUnique_number());
                finilalizeActivity();
            }else {
                ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
                Toast.makeText(this, ""+rr.getMessage(), Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}

    }








    private void finilalizeActivity() {

        try {
            LoginActivity.activity.finish();
        }catch (Exception e){

        }


        try{
            TrialActivity.mainActivity.finish();
        }catch (Exception e ){

        }

        getLocation();

    }



    private void getLocation() {
        try {
            new SamLocationRequestService(this , true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(FaceBookSignupAcivity.this, TrialActivity.class)
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "----")
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(FaceBookSignupAcivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


            });
        } catch (Exception e) {
        }
    }


}
