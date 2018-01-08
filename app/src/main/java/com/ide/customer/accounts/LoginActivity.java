package com.ide.customer.accounts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ide.customer.ForgotPass_Verify_OTP;
import com.ide.customer.ForgotPasswordActivity;
import com.ide.customer.R;
import com.ide.customer.TrialActivity;
import com.ide.customer.location.SamLocationRequestService;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.urls.Apis;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import static com.ide.customer.ForgotPass_ChangePass.forgotpass_changepass;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener , ApiManager.APIFETCHER{


    EditText  edt_pass_login, edt_phone_login ;
    GsonBuilder gsonBuilder;
    Gson gson;
    public static Activity activity;
    LinearLayout facebook_btn_lout, google_btn, email_layout, phone_layout;
    TextView forgot_password, app_name;
    private CallbackManager mCallbackManager;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 112;
    private String TAG = "**Apporio Login Activity";
    FrameLayout root ;
    ApiManager api_manager ;
    SessionManager sessionmanager ;
    CountryCodePicker ccp;




    private void creatInitialObjects() {
        sessionmanager = new SessionManager(LoginActivity.this);
        api_manager = new ApiManager(this , this , this );
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("onSuccess");
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                Log.i("accessToken", ""+loginResult.getAccessToken().getUserId());
                Config.facebook_ID =""+ loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("faebook response ", ""+object);
                        // Get facebook data from login
                        FacebookModel facebookdata = gson.fromJson(""+object , FacebookModel.class);
                        Config.facebook_Mail =""+ facebookdata.getEmail();
                        Config.facebook_Image =""+ "http://graph.facebook.com/"+facebookdata.getId()+"/picture?type=large";
                        Config.facebook_firstname =""+facebookdata.getFirst_name();
                        Config.facebook_Lastname =""+facebookdata.getLast_name();
                        Config.facebook_Id =""+facebookdata.getId();
                        LoginWithfacebook(""+ Config.facebook_ID);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancel() {
                Log.d("** facebook login canceled ", "Facebook login canceled");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onError(FacebookException exception) {
                Log.d("** facebook exception occur while login ", "" + exception.getLocalizedMessage());
            }
        });




        gsonBuilder = new GsonBuilder();

        gson = gsonBuilder.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


//        //        digitsButton.setAuthTheme(R.style.CustomDigitsTheme);
//        digitsButton.setCallback(new AuthCallback() {
//            @Override
//            public void success(DigitsSession session, String phoneNumber) {
//
//                Log.d("Phone ", "" + phoneNumber);
//                Log.d("session val one ", "" + session.isValidUser());
//                Log.d("session val two ", "" + session.hashCode());
//                Log.d("session val three ", "" + session.getAuthToken());
//                Log.d("session val three ", "" + session.getEmail());
//
//            }
//
//            @Override
//            public void failure(DigitsException exception) {
//                Log.d("Digits", "Sign in with Digits failure", exception);
//            }
//        });
    }
    private void init(){
        activity = this;
        edt_pass_login = (EditText) findViewById(R.id.edt_pass_login);
        edt_phone_login = (EditText) findViewById(R.id.edt_phone_login);
        facebook_btn_lout = (LinearLayout) findViewById(R.id.facebook_btn_lout);
        google_btn = (LinearLayout) findViewById(R.id.google_btn);
        email_layout = (LinearLayout) findViewById(R.id.email_layout);
        phone_layout = (LinearLayout) findViewById(R.id.phone_layout);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        app_name = (TextView) findViewById(R.id.app_name);
        root = (FrameLayout) findViewById(R.id.root);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

    }
    private void setListeners(){


        findViewById(R.id.facebook_btn_lout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends" , "email","user_birthday"));

            }
        });


        findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(edt_phone_login.getText().toString().equals("") || edt_pass_login.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, ""+LoginActivity.this.getResources().getString(R.string.require_field_missing), Toast.LENGTH_SHORT).show();
                }else{
                        PhoneSignin(edt_phone_login.getText().toString() , edt_pass_login.getText().toString());
                }

            }
        });


        findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, NormalSignUpActivity.class));
            }
        });




        findViewById(R.id.demo_user_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDemoUserDialog();
            }
        });



        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPass_Verify_OTP.class));

          //      startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });



    }


    private void showForgetPasswordDialog(){
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.forgot_pass_layout);


        dialog.show();
    }


    private void showDemoUserDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.demo_uesr_dialog);
        dialog.setCancelable(false);

        final EditText demo_name = (EditText) dialog.findViewById(R.id.demo_name);
        final EditText demo_phone_email = (EditText) dialog.findViewById(R.id.demo_phone_email);

        dialog.findViewById(R.id.back_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final   HashMap<String , String > data  = new HashMap<String, String>();
        data.put("unique_number" , ""+ Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));



        dialog.findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(demo_phone_email.getText().toString().contains("@")){
                    data.put("user_name" , ""+demo_name.getText().toString());
                    data.put("user_email" , ""+demo_phone_email.getText().toString());
                    data.put("user_phone" , "");
                    api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_DEMO_USER , ""+ Apis.URL_DEMO_SIGNUP , data, true,ApiManager.ACTION_SHOW_TOP_BAR);
                }else{
                    data.put("user_name" , ""+demo_name.getText().toString());
                    data.put("user_email" , "");
                    data.put("user_phone" , ""+demo_phone_email.getText().toString());
                    api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_DEMO_USER , ""+ Apis.URL_DEMO_SIGNUP , data, true,ApiManager.ACTION_SHOW_TOP_BAR);
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.demo_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(demo_phone_email.getText().toString().contains("@")){
                    data.put("user_name" , ""+demo_name.getText().toString());
                    data.put("user_email" , ""+demo_phone_email.getText().toString());
                    data.put("user_phone" , "");
                    api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_DEMO_USER , ""+ Apis.URL_DEMO_SIGNUP , data, true,ApiManager.ACTION_SHOW_TOP_BAR);
                }else{
                    data.put("user_name" , ""+demo_name.getText().toString());
                    data.put("user_email" , "");
                    data.put("user_phone" , ""+demo_phone_email.getText().toString());
                    api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_DEMO_USER , ""+ Apis.URL_DEMO_SIGNUP , data, true,ApiManager.ACTION_SHOW_TOP_BAR);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creatInitialObjects();
        setContentView(R.layout.activity_login_secong);
        init();
        setListeners();
    }


    @SuppressLint("LongLogTag")
    private Bundle getFacebookData(JSONObject object) throws JSONException {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
            return null ;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        } if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }if(requestCode == 2){
            try {
                JSONObject mainObject = new JSONObject(data.getStringExtra("MESSAGE"));
                if(mainObject.getString("result").equals("1")){
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("GOOGLE_DATA Emial" , ""+acct.getEmail());
            Log.d("GOOGLE_DATA ID" , ""+acct.getId());
            Log.d("GOOGLE_DATA Display name" , ""+acct.getDisplayName());
            Log.d("GOOGLE_DATA phojt url " , ""+acct.getPhotoUrl());
            Log.d("GOOGLE_DATA auth code" , ""+acct.getServerAuthCode());

            Config.google_Id = ""+acct.getId();
            Config.google_mail = ""+acct.getEmail();
            Config.google_image = ""+acct.getPhotoUrl();
            Config.google_Name = ""+acct.getDisplayName();


            LoginWithGoogle(""+acct.getId());
        } else {
            Log.d(""+TAG ,"UI updation is false");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





    public void PhoneSignin(String Phone , String password){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("phone" ,""+ ccp.getSelectedCountryCodeWithPlus()+ Phone);
        bodyparameters.put("password" ,""+ password);
        api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_PHONE_LOGIN, Apis.URL_PHONE_LOGIN, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }





    public void LoginWithfacebook(String UserId){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("facebook_id" ,""+ UserId);
        api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_FACEBOOK_LOGIN, Apis.URL_FACEBOOK_LOGIN, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }




    public void LoginWithGoogle(String UserId){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("google_id" ,""+ UserId);
        api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_GOOGLE_LOGIN, Apis.URL_GOOGLE_LOGIN, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }


    public void SignupWithGoogle(String id , String name , String mail , String image , String Password){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("google_id" ,""+ id);
        bodyparameters.put("google_name" ,""+ name);
        bodyparameters.put("google_mail" ,""+ mail);
        bodyparameters.put("google_image" ,""+ image);
        bodyparameters.put("password" ,""+ Password);
        api_manager.execution_method_post(com.ide.customer.samwork.Config.ApiKeys.KEY_GOOGLE_SINGNUP, Apis.URL_GOOGLE_SIGNUP, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }








    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_PHONE_INFO){
                if(rcheck.getResult() == 1){
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    startActivity(new Intent(LoginActivity.this , ChangePasswordActivity.class)
                            .putExtra(com.ide.customer.samwork.Config.IntentKeys.KEY_USER_ID , ""+registration_response.getDetails().getUser_id())
                            .putExtra(""+com.ide.customer.samwork.Config.IntentKeys.KEY_OLD_PASSWORD,""+registration_response.getDetails().getUser_password()));
                }else{
                    Toast.makeText(activity, ""+LoginActivity.this.getResources().getString(R.string.we_dont_find_any_account), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(rcheck.getResult() == 1){
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    String login_type = "";
                    if(APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_PHONE_LOGIN){
                        login_type = SessionManager.LOGIN_NORAL;
                    }else if (APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_FACEBOOK_LOGIN){
                        login_type = SessionManager.LOGIN_FACEBOOK;
                    }else if (APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_GOOGLE_LOGIN){
                        login_type = SessionManager.LOGIN_GOOGLE;
                    }

                    sessionmanager.createLoginSession(registration_response.getDetails().getUser_id(),
                            registration_response.getDetails().getUser_name() ,
                            registration_response.getDetails().getUser_name(),
                            registration_response.getDetails().getUser_email(),
                            registration_response.getDetails().getUser_phone(),
                            registration_response.getDetails().getUser_image(),
                            registration_response.getDetails().getUser_password(),
                            registration_response.getDetails().getLogin_logout(),
                            registration_response.getDetails().getDevice_id(),
                            registration_response.getDetails().getFacebook_id(),
                            registration_response.getDetails().getFacebook_mail(),
                            registration_response.getDetails().getFacebook_image(),
                            registration_response.getDetails().getFacebook_firstname(),
                            registration_response.getDetails().getFacebook_lastname(),
                            registration_response.getDetails().getGoogle_id(),
                            registration_response.getDetails().getGoogle_name(),
                            registration_response.getDetails().getGoogle_mail(),
                            registration_response.getDetails().getGoogle_image(),
                            login_type,
                            registration_response.getDetails().getUnique_number());

                    getLocation();
                }else {
                    ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
                    if(APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_PHONE_LOGIN  || APINAME.equals(""+ Apis.URL_DEMO_SIGNUP)){
                        Loginvaluechecker value = gson.fromJson(""+script , Loginvaluechecker.class);
                        Toast.makeText(activity, ""+value.getMessage(), Toast.LENGTH_SHORT).show();
                    }else if (APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_FACEBOOK_LOGIN){
                        startActivity(new Intent(LoginActivity.this , FaceBookSignupAcivity.class));
                    }else if (APINAME == com.ide.customer.samwork.Config.ApiKeys.KEY_GOOGLE_LOGIN){
                        if( mGoogleApiClient.isConnected()){
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @SuppressLint("LongLogTag")
                                        @Override
                                        public void onResult(Status status) {
                                            Log.d("User logged out successfuly " , ""+status.toString());
                                        }
                                    });
                        }
                        startActivity(new Intent(LoginActivity.this , GoogleSignUpActivity.class));
                    }
                }
            }}catch (Exception e){}
    }


    @Override
    public void onBackPressed() {
//        forgotpass_changepass.finish();
        super.onBackPressed();
    }


    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(LoginActivity.this, TrialActivity.class)
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(com.ide.customer.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }





    private class FacebookModel {

        /**
         * id : 1479168742128308
         * first_name : Samir
         * last_name : Goel
         * email : samirgoel3@gmail.com
         * gender : male
         * birthday : 08/07/1990
         */

        private String id;
        private String first_name;
        private String last_name;
        private String email;
        private String gender;
        private String birthday;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }



}
