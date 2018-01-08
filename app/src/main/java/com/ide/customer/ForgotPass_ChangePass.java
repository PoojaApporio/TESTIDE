package com.ide.customer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ide.customer.accounts.LoginActivity;
import com.ide.customer.accounts.RegistrationModel;
import com.ide.customer.accounts.ResultCheckMessage;
import com.ide.customer.location.SamLocationRequestService;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;

import java.util.HashMap;


public class ForgotPass_ChangePass extends AppCompatActivity implements ApiManager.APIFETCHER {

    ApiManager apiManager;
    GsonBuilder gsonBuilder;
    RegistrationModel.OTP_Details otp_details;
    private EditText new_password, confirm_password;
    private LinearLayout password_submit;
    SessionManager sessionManager;
    Gson gson;
    String input_new_pass, input_confirm_pass;
    String phone_number;
    public static Activity forgotpass_changepass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this, this, this);
        sessionManager = new SessionManager(ForgotPass_ChangePass.this);
        otp_details = new RegistrationModel.OTP_Details();
        setContentView(R.layout.activity_forgotpass_changepass);

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        forgotpass_changepass = this;

        new_password = (EditText) findViewById(R.id.edt_enter_pass);
        confirm_password = (EditText) findViewById(R.id.new_pass_edt);
        password_submit = (LinearLayout) findViewById(R.id.password_submit);

        phone_number = getIntent().getStringExtra("phone_number");
        Log.e("phone_number--", phone_number);

        password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_new_pass = new_password.getText().toString().trim();
                Log.e("input_new_password", input_new_pass);
                input_confirm_pass = confirm_password.getText().toString().trim();
                Log.e("confirm_new_password", input_confirm_pass);

                if (input_new_pass.equals("")) {
                    Toast.makeText(ForgotPass_ChangePass.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                } else if (input_confirm_pass.equals("")) {
                    Toast.makeText(ForgotPass_ChangePass.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                } else if (!confirm_password.getText().toString().equals(new_password.getText().toString())) {
                    Toast.makeText(ForgotPass_ChangePass.this, "Incorrect Password. Please try again!", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(phone_number, input_confirm_pass);
                }

            }
        });
    }

    private void changePassword(String phone_number, String input_confirm_pass) {
        HashMap<String, String> bodyparameters = new HashMap<String, String>();
        bodyparameters.put("phone", phone_number);
        bodyparameters.put("password", input_confirm_pass);
        bodyparameters.put("type", "1");
        bodyparameters.put("status", "2");
        apiManager.execution_method_post(Config.ApiKeys.KEY_FORGOTPASS_CONFIRMPASS, "" + Apis.FORGOTPASS_CONFIRMPASS, bodyparameters, true, ApiManager.ACTION_SHOW_TOP_BAR);
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        if (APINAME.equals("" + Config.ApiKeys.KEY_FORGOTPASS_CONFIRMPASS)) {

            ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
            Log.e("rrrr---model value--", String.valueOf(rr.getResult()));
            Log.e("rrrr---model value message--", String.valueOf(rr.getMessage()));

            int res = rr.getResult();
            if (res == 1){
                //update the password in session
                sessionManager.createNewPassword(input_confirm_pass);
                Toast.makeText(this, "New Password has changed successfully !", Toast.LENGTH_SHORT).show();
                getLocation();
            }

        }
    }

    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(ForgotPass_ChangePass.this, LoginActivity.class)
                                .putExtra(Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(Config.IntentKeys.CITY_NAME, "No City" ));
                    } catch (Exception e) {
                     //   Toast.makeText(ForgotPass_ChangePass.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPass_ChangePass.this, TrialSplashActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}