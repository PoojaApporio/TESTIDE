package com.ide.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ide.customer.manager.SessionManager;
import com.ide.customer.models.NewDoneRidemodel;
import com.ide.customer.samir.customviews.TypeFaceTextMonixRegular;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentalReceiptActivity extends Activity implements ApiManager.APIFETCHER {

    ApiManager apiManager;
    Gson gson;
    NewDoneRidemodel done_ride_response;
    Dialog dialog;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    @Bind(R.id.pick_location_txt)
    TextView pickLocationTxt;
    @Bind(R.id.start_meter_txt)
    TextView startMeterTxt;
    @Bind(R.id.drop_location_txt)
    TextView dropLocationTxt;
    @Bind(R.id.end_meter_txt)
    TextView endMeterTxt;
    @Bind(R.id.total_payble_top)
    TextView totalPaybleTop;
    @Bind(R.id.tv_ride_distance)
    TextView tvRideDistance;
    @Bind(R.id.total_hours_txt)
    TextView totalHoursTxt;
    @Bind(R.id.base_package_txt)
    TextView basePackageTxt;
    @Bind(R.id.base_package_price)
    TextView basePackagePrice;
    @Bind(R.id.extra_distance_txt)
    TextView extraDistanceTxt;
    @Bind(R.id.extra_distance_price_txt)
    TextView extraDistancePriceTxt;
    @Bind(R.id.extra_time_txt)
    TextView extraTimeTxt;
    @Bind(R.id.extra_time_price_txt)
    TextView extraTimePriceTxt;
    @Bind(R.id.total_price_txt)
    TextView totalPriceTxt;
    @Bind(R.id.coupon_txt)
    TextView couponTxt;
    @Bind(R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(R.id.total_payble_bottom)
    TextView totalPaybleBottom;
    @Bind(R.id.ll_make_payment)
    LinearLayout ll_make_payment;
    @Bind(R.id.night_time_txt)
    TextView nightTimeTxt;
    @Bind(R.id.night_time_price_txt)
    TextView nightTimePriceTxt;
    @Bind(R.id.peak_time_txt)
    TextView peakTimeTxt;
    @Bind(R.id.peak_time_price_txt)
    TextView peakTimePriceTxt;
    @Bind(R.id.tv_change_text)
    TypeFaceTextMonixRegular tvChangeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(this);
        gson = new GsonBuilder().create();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("" + this.getResources().getString(R.string.loading));
        setContentView(R.layout.activity_rental_receipt);
        ButterKnife.bind(this);

        HashMap<String, String> data = new HashMap<>();
        data.put("rental_booking_id", "" + getIntent().getExtras().getString("ride_id"));
        apiManager.execution_method_post(Config.ApiKeys.KEY_REST_DONE_RIDE_INFO, "" + Apis.Done_Ride_Info, data, true,ApiManager.ACTION_SHOW_DIALOG);


//        Log.d("**********coming from " , ""+getIntent().getExtras().get("coming_from"));

        clearNotification();

        ll_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("rental_booking_id", "" + done_ride_response.getDetails().getRental_booking_id());
                data.put("amount_paid", "" + done_ride_response.getDetails().getFinal_bill_amount());
                data.put("payment_status", "Done");
                apiManager.execution_method_post("" + Config.ApiKeys.KEY_RENTAL_MAKE_PAYMENT, "" + Apis.URL_RENTAL_PAYMENT, data, true,ApiManager.ACTION_SHOW_DIALOG);
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        finalizeActivity();
//    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{switch (APINAME) {
            case Config.ApiKeys.KEY_RENTAL_MAKE_PAYMENT:
//                ResultStatusChecker rs = gson.fromJson("" + script, ResultStatusChecker.class);
//                if (rs.getStatus() == 1) {
                dialogForRating();
//                }else {
//                    shodialogForPaymentDone();
//                }
                break;
            case Config.ApiKeys.KEY_REST_DONE_RIDE_INFO:
                switch (APINAME) {
                    case Config.ApiKeys.KEY_REST_DONE_RIDE_INFO:
                        done_ride_response = gson.fromJson("" + script, NewDoneRidemodel.class);
                        setView();
                        break;
                    case Config.ApiKeys.KEY_REST_RATING:
                        finalizeActivity();
                        break;
                }
                break;
            case Config.ApiKeys.KEY_RENTAL_RATING:
                dialog.dismiss();
//                if (rs_check.getStatus() == 1) {
                Toast.makeText(this, "Rating Successful", Toast.LENGTH_SHORT).show();
                finish();
                try {
                    RentalTrackActivity.trackRideActivity.finish();
                } catch (Exception e) {
                }
                if (!Config.app_loaded_from_initial) {
                    startActivity(new Intent(RentalReceiptActivity.this, TrialSplashActivity.class));
                }

//                }
//                else {
//                    Toast.makeText(this, "" + rs_check.getMessage(), Toast.LENGTH_SHORT).show();
//                }
                break;
        }}catch (Exception e){}
    }

    private void finalizeActivity() {
        finish();
        try {
            RentalTrackActivity.trackRideActivity.finish();
        } catch (Exception e) {

        }

        if (!Config.app_loaded_from_initial) {
            startActivity(new Intent(RentalReceiptActivity.this, TrialSplashActivity.class));
        }
    }

    private void setView() {

        pickLocationTxt.setText("" + done_ride_response.getDetails().getBegin_location());
        dropLocationTxt.setText("" + done_ride_response.getDetails().getEnd_location());
        startMeterTxt.setText("" + done_ride_response.getDetails().getStart_meter_reading());
        endMeterTxt.setText("" + done_ride_response.getDetails().getEnd_meter_reading());

        totalPaybleTop.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getFinal_bill_amount());
        tvRideDistance.setText("" + done_ride_response.getDetails().getTotal_distance_travel());
        totalHoursTxt.setText("" + done_ride_response.getDetails().getTotal_time_travel());

        basePackageTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__package) + done_ride_response.getDetails().getRental_package_distance() + " " + getString(R.string.RENTAL_RECEIPT_ACTIVITY__for) + done_ride_response.getDetails().getRental_package_hours() + " " + getString(R.string.RENTAL_RECEIPT_ACTIVITY__hours));
        basePackagePrice.setText(sessionManager.getCurrencyCode() + "" + done_ride_response.getDetails().getRental_package_price());

        extraDistanceTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__extra_distance_travel) + done_ride_response.getDetails().getExtra_distance_travel() + " )");
        extraDistancePriceTxt.setText(sessionManager.getCurrencyCode() + "" + done_ride_response.getDetails().getExtra_distance_travel_charge());

        extraTimePriceTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__extra_time) + sessionManager.getCurrencyCode() + done_ride_response.getDetails().getExtra_hours_travel() + ")");
        extraTimePriceTxt.setText("" + done_ride_response.getDetails().getExtra_hours_travel_charge());

        totalPriceTxt.setText(sessionManager.getCurrencyCode() + "" + done_ride_response.getDetails().getTotal_amount());  /// need to be changes later
        totalPaybleBottom.setText(sessionManager.getCurrencyCode() + "" + done_ride_response.getDetails().getFinal_bill_amount());

        if (!done_ride_response.getDetails().getCoupan_code().equals("")) {
            couponPriceTxt.setVisibility(View.VISIBLE);
            couponTxt.setVisibility(View.VISIBLE);
            couponTxt.setText("Coupon Applied " + "(" + done_ride_response.getDetails().getCoupan_code() + ")");
            couponPriceTxt.setText("" + sessionManager.getCurrencyCode() + done_ride_response.getDetails().getCoupan_price());
        } else {
            couponPriceTxt.setVisibility(View.GONE);
            couponTxt.setVisibility(View.GONE);
        }
    }


    public void dialogForRating() {
        dialog = new Dialog(RentalReceiptActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_rating);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        LinearLayout lldone = (LinearLayout) dialog.findViewById(R.id.lldone);
        final EditText comments = (EditText) dialog.findViewById(R.id.comments);

        lldone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingBar.getRating());
                if (rating.equals("0.0")) {
                    Toast.makeText(RentalReceiptActivity.this, getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("rating_star", "" + ratingBar.getRating());
                    data.put("rental_booking_id", "" + done_ride_response.getDetails().getRental_booking_id());
                    data.put("comment", "" + comments.getText().toString());
                    data.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
                    data.put("driver_id", "" + done_ride_response.getDetails().getDriver_id());
                    data.put("app_id", "1");
                    apiManager.execution_method_post(Config.ApiKeys.KEY_RENTAL_RATING, Apis.Rating, data, true,ApiManager.ACTION_SHOW_DIALOG);
                }
            }
        });
        dialog.show();
    }


    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }



    private void shodialogForPaymentDone() {
        final Dialog dialog = new Dialog(RentalReceiptActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_payment_already_done);
        dialog.findViewById(R.id.ll_ok_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeActivity();
            }
        });

        dialog.show();
    }


}
