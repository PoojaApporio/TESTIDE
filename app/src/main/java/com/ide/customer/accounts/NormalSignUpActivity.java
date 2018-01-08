package com.ide.customer.accounts;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ide.customer.R;
import com.ide.customer.TrialActivity;
import com.ide.customer.Verify_OTP;
import com.ide.customer.location.SamLocationRequestService;
import com.ide.customer.manager.LanguageManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class NormalSignUpActivity extends Activity implements ApiManager.APIFETCHER {

    TextView  email_verifier_txt, phone_txt   ;
    EditText  your_name_edit  ,edt_pass_signup , phone_edt   , email_edttt ;
    ProgressBar progress_bar ;

 //   private int Email_validator_Hide = 0 , Email_validator_Invalid = 1 , Email_validator_CHECKING = 2 , Email_validator_Available = 3 , Email_validator_Already_exsist = 4 ;

    ProgressDialog loading_dialouge ;
    private static final int KEY_REGISTER = 110;

    LinearLayout root , phone_layout;
    GsonBuilder gsonBuilder ;
    Gson gson;

    String language_id, getPhone_Number;
    LanguageManager languageManager;
    ApiManager apimanager ;
    SessionManager sessionmanager ;
    CountryCodePicker ccp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_sign_up);
        languageManager = new LanguageManager(this);
        edt_pass_signup = (EditText) findViewById(R.id.edt_pass_signup);
        your_name_edit = (EditText) findViewById(R.id.your_name_edit);
   //     phone_edt = (EditText) findViewById(R.id.phone_edt);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        email_verifier_txt= (TextView) findViewById(R.id.email_verifier_txt);
        root = (LinearLayout) findViewById(R.id.root);
        email_edttt  = (EditText) findViewById(R.id.email_edttt);
     //   ccp = (CountryCodePicker) findViewById(R.id.ccp);
        phone_txt = (TextView) findViewById(R.id.phone_txt);
        apimanager = new ApiManager(this , this , this  );
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        loading_dialouge = new ProgressDialog(this);
        sessionmanager = new SessionManager(NormalSignUpActivity.this);
        loading_dialouge.setTitle(""+this.getResources().getString(R.string.loading));
        phone_layout = (LinearLayout) findViewById(R.id.edt_phone_layout);

        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finilalizeActivity();
            }
        });

        phone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NormalSignUpActivity.this, Verify_OTP.class);
                startActivityForResult(intent, KEY_REGISTER);
            }
        });




//
//        ic_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                email_edt.setText("");
//                setEmailText(Email_validator_Hide);
//            }
//        });
//
//
//        email_edt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length() == 0 ){
//                    setEmailText(Email_validator_Hide);
//                }else {
//                    if (email_edt.getText().toString().matches(RentalConfig.emailPattern)  && email_edt.getText().toString().contains(".com")) {
//                        setEmailText(Email_validator_CHECKING);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                setEmailText(Email_validator_Available);
//                                //setEmailText(Email_validator_Already_exsist);
//                            }
//                        }, 2000);
//                    }else{
//                        setEmailText(Email_validator_Invalid);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        findViewById(R.id.ll_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(your_name_edit.getText().toString().equals("") || email_edttt.getText().toString().equals("") || edt_pass_signup.getText().toString().equals("") || phone_txt.getText().toString().equals("")){
                    Toast.makeText(NormalSignUpActivity.this, getResources().getString(R.string.field_missing), Toast.LENGTH_SHORT).show();
                }else {
                    HashMap<String , String > bodyparameters  = new HashMap<String, String>();
                    bodyparameters.put("first_name" ,""+your_name_edit.getText().toString());
                    bodyparameters.put("last_name" ,".");
                    bodyparameters.put("phone" ,""+getPhone_Number);
                    bodyparameters.put("password" ,""+edt_pass_signup.getText().toString());
                    bodyparameters.put("user_email" ,""+email_edttt.getText().toString());
                    bodyparameters.put("language_id", language_id );
                    apimanager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_REGISTER ,""+  Apis.URL_REGISTER, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
                }
            }
        });
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(APINAME.equals(com.ide.customer.samwork.Config.ApiKeys.KEY_REGISTER)){
                if(rcheck.getResult() == 1 ){
                    RegistrationModel response = gson.fromJson("" + script, RegistrationModel.class);
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    sessionmanager.createLoginSession(registration_response.getDetails().getUser_id() , registration_response.getDetails().getUser_name() , registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_email(),registration_response.getDetails().getUser_phone(),registration_response.getDetails().getUser_image(),registration_response.getDetails().getUser_password(),registration_response.getDetails().getLogin_logout(),registration_response.getDetails().getDevice_id(),
                            registration_response.getDetails().getFacebook_id(),registration_response.getDetails().getFacebook_mail(),registration_response.getDetails().getFacebook_image(),registration_response.getDetails().getFacebook_firstname(),registration_response.getDetails().getFacebook_lastname(),
                            registration_response.getDetails().getGoogle_id(),registration_response.getDetails().getGoogle_name(),registration_response.getDetails().getGoogle_mail(),registration_response.getDetails().getGoogle_image(), SessionManager.LOGIN_NORAL, registration_response.getDetails().getUnique_number());
//                LoginActivity.activity.finish();
//                finish();
                    getLocation();
                }else if (rcheck.getResult() == 0 ){
                    ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);

                    Toast.makeText(this, ""+rr.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }}catch (Exception e){}

    }





    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(NormalSignUpActivity.this, TrialActivity.class)
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finilalizeActivity();
                    } catch (Exception e) {
                        Toast.makeText(NormalSignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    private void finilalizeActivity() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        try {
            if(sessionmanager.isLoggedIn()){
                LoginActivity.activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case KEY_REGISTER :

                getPhone_Number = data.getExtras().getString("phone_number");
                phone_txt.setText(getPhone_Number);
                break;

        }
    }
}
