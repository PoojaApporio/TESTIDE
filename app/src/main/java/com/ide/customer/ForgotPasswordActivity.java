package com.ide.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.LanguageManager;
import com.ide.customer.rentalmodule.DeviceId;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;

import java.util.HashMap;
import java.util.List;

public class ForgotPasswordActivity extends AppCompatActivity implements ApiManager.APIFETCHER {

    Button submit;
    LinearLayout ll_back_forgot;
    EditText email;
    public static Activity forgotpassword;

    ProgressDialog pd;

    ApiManager apiManager;
    LanguageManager languageManager;
    String language_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
//        getSupportActionBar().hide();
        forgotpassword = this;

        pd = new ProgressDialog(this);
        pd.setMessage(ForgotPasswordActivity.this.getResources().getString(R.string.loading));

        apiManager = new ApiManager(this, this, this);
        languageManager=new LanguageManager(this);
    //    language_id=languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);
        language_id="2";

        submit = (Button) findViewById(R.id.forgotdone);
        email = (EditText) findViewById(R.id.forgotemail);
        ll_back_forgot = (LinearLayout) findViewById(R.id.ll_back_forgot);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString().trim();
                if (email.equals("")) {
                    Toast.makeText(ForgotPasswordActivity.this,ForgotPasswordActivity.this.getResources().getString(R.string.please_enter_email) , Toast.LENGTH_SHORT).show();
                } else {
                     HashMap<String , String > data  = new HashMap<String, String>();
                    data.put("user_email", email1);
                    data.put("language_id", "2");

                    apiManager.execution_method_post(Config.ApiKeys.FORGOT_PASSWORD, Apis.FORGOT_PASSWORD, data, true, ApiManager.ACTION_SHOW_TOAST);
                }

            }
        });

        ll_back_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if (APINAME.equals("KEY_FORGOT_PASSWORD")) {

     //       try {


            ForgotResponse deviceId;
            deviceId = gson.fromJson(""+script, ForgotResponse.class);

            Log.e("Device===", ""+script);


            if (deviceId.getResult() == 1) {
                String message = deviceId.getMessage();
                Log.e("MESSAGE==", String.valueOf(message));

                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "" + deviceId.getMessage(), Toast.LENGTH_LONG).show();
            }
          /*  }catch (Exception e){
                Toast.makeText(ForgotPasswordActivity.this, R.string.execution_error, Toast.LENGTH_SHORT).show();
            }*/

        }
    }

}