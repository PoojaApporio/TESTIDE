package com.ide.customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

import com.ide.customer.StatusSession.DBHelper;
import com.ide.customer.StatusSession.RideSessionEvent;
import com.ide.customer.others.Constants;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.samwork.Config;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.urls.Apis;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentalRideLoaderActivity extends Activity implements ApiManager.APIFETCHER{

    public static Activity activity ;
    @Bind(R.id.root_layout) LinearLayout root_layout ;

    Runnable mRunnable ;
    Handler mHandler ;
    ApiManager apiManager;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_ride_loader);
        mHandler = new Handler();
        dbHelper = new DBHelper(this);
        activity = this ;
        apiManager = new ApiManager(this , this , this );
        ButterKnife.bind(this);
        root_layout.setMinimumHeight(com.ide.customer.samwork.Config.Screen_height(this));
        com.ide.customer.samir.customviews.pulse.PulsatorLayout pulsator = (com.ide.customer.samir.customviews.pulse.PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();

        FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id) , ""+Config.Status.RENTAL_BOOKED , "Not yet generated" , "0"));
        new DBHelper(RentalRideLoaderActivity.this).insertRow(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id) , ""+Config.Status.RENTAL_BOOKED);


        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String , String > data  = new HashMap<String, String>();
                data.put("rental_booking_id" , ""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
                data.put("user_id" , ""+new SessionManager(RentalRideLoaderActivity.this).getUserDetails().get(SessionManager.USER_ID));
                data.put("cancel_reason_id" , "0");
                apiManager.execution_method_post("sudden_cancel" , ""+ Apis.CancelRentalRide , data, true,ApiManager.ACTION_SHOW_TOAST);
                mHandler.removeCallbacks(mRunnable);
            }
        });




        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(Constants.isRentalRideloaderIsOpen){
                    HashMap<String , String > data  = new HashMap<String, String>();
                    data.put("rental_booking_id" , ""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
                    data.put("user_id" , ""+new SessionManager(RentalRideLoaderActivity.this).getUserDetails().get(SessionManager.USER_ID));
                    data.put("cancel_reason_id" , "0");
                    FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id) , ""+""+Config.Status.RIDE_EXPIRE_AUTOMATICALLY_VIA_PULSE ,"Not yet generated" , "0"));
                    dbHelper.deleteRowByid(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
                    apiManager.execution_method_post(""+Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE , ""+Apis.CancelRentalRide , data, true,ApiManager.ACTION_SHOW_TOAST);

                }
            }
        };

        mHandler.postDelayed(mRunnable, 60000);



    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RideSessionEvent event){
        try{
            if(event.getRide_id().equals(""+getIntent().getExtras().getString("ride_id")) ){
                if(event.getRide_status().equals(""+Config.Status.RENTAL_ACCEPTED) || event.getRide_status().equals(""+Config.Status.RENTAL_ARRIVED)||event.getRide_status().equals(""+Config.Status.RENTAl_RIDE_STARTED)){  // driver accepted the request
                    finish();
                    startActivity(new Intent(RentalRideLoaderActivity.this, RentalTrackActivity.class)
                            .putExtra("ride_id", ""+event.getRide_id())
                            .putExtra("ride_status", ""+event.getRide_status()));
                }if(event.getRide_status().equals(""+Config.Status.RENTAL_RIDE_REJECTED)){  // driver rejected the request
                    finish();
                    Toast.makeText(this, "Driver has rejected the request , BOOK AGAIN!", Toast.LENGTH_SHORT).show();
                }
        }}catch (Exception e ){}

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {EventBus.getDefault().register(this);}catch (Exception e){}
        Constants.isRentalRideloaderIsOpen = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        try {EventBus.getDefault().unregister(this);}catch (Exception e){}
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.is_main_Activity_open = false;
        Constants.isRentalRideloaderIsOpen = false;

    }

    @Override
    public void onBackPressed() {

    }



    public void dialogForNoDriverAcceptRideDialooe() {
        final Dialog dialog = new Dialog(RentalRideLoaderActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
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
        try {
            if(APINAME.equals("sudden_cancel")){
                finish();
                FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id) , ""+Config.Status.RIDE_EXPIRE_BY_CLICKING_CROSS , "Not yet generated" , "0"));
                dbHelper.deleteRowByid(""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
                Toast.makeText(activity, "You Just have Cancel the ride, You can book it again in order to get new ride. ", Toast.LENGTH_SHORT).show();
            }else{
                dialogForNoDriverAcceptRideDialooe();
            }
        }catch (Exception e){}

    }




}
