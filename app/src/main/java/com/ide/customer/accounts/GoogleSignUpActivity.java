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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleSignUpActivity extends AppCompatActivity implements ApiManager.APIFETCHER {


    ApiManager apiManager ;
    GsonBuilder gsonBuilder;
    Gson gson;
    SessionManager sessionManager ;
    TextView facebook_name_txt  , google_email_txt ;
    EditText phone_edt ;
    CircleImageView google_image ;
    CountryCodePicker ccp ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this , this , this  );
        sessionManager = new SessionManager(GoogleSignUpActivity.this);
        setContentView(R.layout.activity_google_sign_up);
        facebook_name_txt = (TextView) findViewById(R.id.facebook_name_txt);
        google_email_txt = (TextView) findViewById(R.id.google_email_txt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        google_image = (CircleImageView) findViewById(R.id.google_image);
        facebook_name_txt.setText(""+ Config.google_Name);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);


        google_email_txt.setText(""+Config.google_mail);
        Picasso.with(this).load(""+Config.google_image).placeholder(R.drawable.ic_google).into(google_image);

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
                    Toast.makeText(GoogleSignUpActivity.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }else{
                    SignupWithGoogle(Config.google_Id , Config.google_mail, Config.google_image, Config.google_Name,phone_edt.getText().toString());
                }
            }
        });
    }




    public void SignupWithGoogle(String google_user_id , String mail , String image , String fullname , String Phone){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("google_id" ,""+ google_user_id);
        bodyparameters.put("google_name" ,""+ fullname);
        bodyparameters.put("google_mail" ,""+ mail);
        bodyparameters.put("google_image" ,""+ image);
        bodyparameters.put("phone" ,"+"+ ccp.getSelectedCountryCode()+Phone);
        bodyparameters.put("first_name" ,""+ fullname);
        bodyparameters.put("last_name" ,".");
        apiManager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_GOOGLE_SINGNUP,  Apis.URL_GOOGLE_SIGNUP, bodyparameters , true ,ApiManager.ACTION_SHOW_TOP_BAR);
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);
            if(rcheck.getResult() == 1){
                RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                sessionManager.createLoginSession(registration_response.getDetails().getUser_id() , registration_response.getDetails().getUser_name() , registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_email(),registration_response.getDetails().getUser_phone(),registration_response.getDetails().getUser_image(),registration_response.getDetails().getUser_password(),registration_response.getDetails().getLogin_logout(),registration_response.getDetails().getDevice_id(),
                        registration_response.getDetails().getFacebook_id(),registration_response.getDetails().getFacebook_mail(),registration_response.getDetails().getFacebook_image(),registration_response.getDetails().getFacebook_firstname(),registration_response.getDetails().getFacebook_lastname(),
                        registration_response.getDetails().getGoogle_id(),registration_response.getDetails().getGoogle_name(),registration_response.getDetails().getGoogle_mail(),registration_response.getDetails().getGoogle_image() , SessionManager.LOGIN_GOOGLE, registration_response.getDetails().getUnique_number());
                finilalizeActivity();

            }else {
                ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
                Toast.makeText(this, ""+rr.getMessage(), Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}

    }






    private void finilalizeActivity() {


        try{
            LoginActivity.activity.finish();
        }catch (Exception e ){

        }

        try{
            TrialActivity.mainActivity.finish();
        }catch (Exception e ){

        }

        getLocation();

    }




    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(GoogleSignUpActivity.this, TrialActivity.class)
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(GoogleSignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


}
