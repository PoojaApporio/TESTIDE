package com.ide.customer.samwork;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ide.customer.R;
import com.ide.customer.StatusSession.DBHelper;
import com.ide.customer.StatusSession.RideSessionEvent;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.others.Constants;
import com.ide.customer.samir.customviews.pulse.PulsatorLayout;
import com.ide.customer.urls.Apis;
import com.google.firebase.database.FirebaseDatabase;


import butterknife.Bind;
import butterknife.ButterKnife;

public class RideLoadrActivity extends Activity implements ApiManager.APIFETCHER {

    public static Activity activity ;
    @Bind(R.id.root_layout) LinearLayout root_layout ;

    Runnable mRunnable  ;
    Handler mHandler ;
    ApiManager apiManager;
    SessionManager sessionManager ;
    DBHelper dbHelper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_loadr);
        mHandler = new Handler();
        activity = this ;
        apiManager = new ApiManager(this , this , this );
        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        ButterKnife.bind(this);
        root_layout.setMinimumHeight(Config.Screen_height(this));
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();


        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiManager.execution_method_get("sudden_cancel", Apis.ride_cancel_by_user+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id), false,ApiManager.ACTION_SHOW_TOAST);
                FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id) , ""+Config.Status.RIDE_EXPIRE_BY_CLICKING_CROSS , "Not yet generated" , "0"));
                dbHelper.deleteRowByid(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
                mHandler.removeCallbacks(mRunnable);
            }
        });



        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(Constants.rideLoaderActivity == 1 ){
                    apiManager.execution_method_get(Config.ApiKeys.Key_Cancel_by_user , Apis.ride_cancel_by_user+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id),false,ApiManager.ACTION_SHOW_TOAST);
                    FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id) , ""+""+Config.Status.RIDE_EXPIRE_AUTOMATICALLY_VIA_PULSE ,"Not yet generated" , "0"));
                    dbHelper.deleteRowByid(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
                }
            }
        };
        mHandler.postDelayed(mRunnable, 60000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.is_main_Activity_open = true;
        Constants.rideLoaderActivity = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.is_main_Activity_open = false;
        Constants.rideLoaderActivity = 0;
    }


    public void dialogForNoDriverAcceptRideDialooe() {
        final Dialog dialog = new Dialog(RideLoadrActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_no_driver_accept_request_trial);

        dialog.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });


        try{
            dialog.show();
        }catch (Exception e ){
            Log.d("**exception_caught in trial ride confirm dialog activity " , ""+e.getMessage());
        }
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ if(APINAME.equals("sudden_cancel")){
            finish();
            Toast.makeText(activity, "You Just have Cancel the ride, You can book it again in order to get new ride. ", Toast.LENGTH_SHORT).show();
        }
        else{
            dialogForNoDriverAcceptRideDialooe();
        }}catch (Exception e){}

    }
}
