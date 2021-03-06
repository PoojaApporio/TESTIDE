package com.ide.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.ide.customer.accounts.RegistrationModel;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;

import java.util.HashMap;


public class Verify_OTP extends AppCompatActivity implements ApiManager.APIFETCHER {

    ApiManager apiManager ;
    GsonBuilder gsonBuilder;
    RegistrationModel.OTP_Details otp_details;
    private TextView otpError_txt;
    private EditText otp_input, edt_enter_phone;
    private LinearLayout submit_otp_layout;
    SessionManager sessionManager ;
    Gson gson;
    String code, input_phone_number, otp;
    CountryCodePicker codePicker;
    private static final int KEY_REGISTER = 110;
    Button generate_otp;
    LinearLayout submit_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this , this , this  );
        sessionManager = new SessionManager(Verify_OTP.this);
        otp_details = new RegistrationModel.OTP_Details();
        setContentView(R.layout.activity_verify__otp);

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        submit_otp = (LinearLayout) findViewById(R.id.otp_submit);
        generate_otp = (Button) findViewById(R.id.generate_otp);
        edt_enter_phone = (EditText)findViewById(R.id.edt_enter_phone);
        otp_input = (EditText)findViewById(R.id.otp_edt);
        otpError_txt = (TextView)findViewById(R.id.otp_verifier_txt);
        submit_otp_layout = (LinearLayout)findViewById(R.id.otp_submit);
        codePicker = (CountryCodePicker)findViewById(R.id.otp_ccp);

        submit_otp.setEnabled(false);

        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone_number = edt_enter_phone.getText().toString().trim();
                Log.e("input_phone_number====", input_phone_number);
                code = codePicker.getSelectedCountryCodeWithPlus();
                Log.e("COUNTRY_CODE_PICKER===", code);
                if (input_phone_number.equals("")){
                    Toast.makeText(Verify_OTP.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }else {
                    getOTP(code+input_phone_number);

                }
            }
        });

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

 //               input_OTP = otp_input.getText().toString().trim();
//                Log.e("otp_details--getOTP", otp);
//                Log.e("otp_details--edittext", otp_input.getText().toString());

                if (otp_input.getText().toString().equals("")) {
                    Toast.makeText(Verify_OTP.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                } else if (!otp_input.getText().toString().equals(otp)) {
                    Log.e("otp_details--elseIF", String.valueOf(otp_details.getOtp()));
                    Toast.makeText(Verify_OTP.this, "Invalid OTP !", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("otp_details--lastelse", String.valueOf(getIntent().getStringExtra("otp")));
                    Log.e("otp_details--edittext", otp_input.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra("phone_number", code + input_phone_number);
                    setResult(KEY_REGISTER, intent);
                    finish();


                }
            }
            });
    }


    private void getOTP(String phone) {
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("phone" , phone);
        bodyparameters.put("type" ,"1");
        bodyparameters.put("status", "1");
        apiManager.execution_method_post(Config.ApiKeys.KEY_VERIFY_OTP ,""+  Apis.SEND_OTP, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {

        if(APINAME.equals(""+Config.ApiKeys.KEY_VERIFY_OTP)) {

            try {
                RegistrationModel.OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.OTP_Details.class);
                Log.e("**OTP_SCRIPT-----", String.valueOf(script));

                if (otp_response.getResult() == 1) {
                    submit_otp.setEnabled(true);
                    Log.e("**RCHHECKK---", String.valueOf(otp_response.getResult()));
                    otp = otp_response.getOtp();

                    otp_input.requestFocus();
                    Log.d("otp==normal sign up==", otp);

                } else {
                    Toast.makeText(this, "Phone number already registered!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
            }

        }



    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Verify_OTP.this, TrialSplashActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
