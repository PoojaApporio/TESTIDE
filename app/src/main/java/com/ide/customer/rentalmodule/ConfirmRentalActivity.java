package com.ide.customer.rentalmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ide.customer.R;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.models.ViewPaymentOption;
import com.ide.customer.switchdatetimepicker.SwitchDateTimeDialogFragment;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import java.util.Calendar;
import java.util.HashMap;

public class ConfirmRentalActivity extends FragmentActivity implements ApiManager.APIFETCHER , TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "ConfirmRentalActivity";
    TextView terms_txt  , car_type_name,  car_models_list,  selected_package_name,  base_fare_txt, base_fare_price , additional_distance_fare_txt , additional_distance_fare_price , additional_time_txt , additional_time_price , coupon_tx , payment_txt  ;
    ImageView car_image ;
    ApiManager apiManager ;
    Gson gson ;
    private String COUPON_CODE = "";
    private String PAYMENT_ID  = "";

    public static Activity activity ;
    ViewPaymentOption viewPaymentOption;
    String LATERDATE, LATERTIME;
    SessionManager sessionManager ;



    SwitchDateTimeDialogFragment dateTimeFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        activity = this ;
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_confirm_rental);
        apiManager = new ApiManager(this , this , this);
        RentalConfig.is_confirm_rental_activity_is_open = true ;
        terms_txt = (TextView) findViewById(R.id.terms_txt);
        car_type_name = (TextView) findViewById(R.id.car_type_name);
        car_models_list = (TextView) findViewById(R.id.car_models_list);
        selected_package_name = (TextView) findViewById(R.id.selected_package_name);
        base_fare_txt = (TextView) findViewById(R.id.base_fare_txt);
        base_fare_price = (TextView) findViewById(R.id.base_fare_price);
        additional_distance_fare_txt = (TextView) findViewById(R.id.additional_distance_fare_txt);
        additional_distance_fare_price = (TextView) findViewById(R.id.additional_distance_fare_price);
        additional_time_txt = (TextView) findViewById(R.id.additional_time_txt);
        additional_time_price = (TextView) findViewById(R.id.additional_time_price);
        car_image = (ImageView) findViewById(R.id.car_image);
        coupon_tx = (TextView) findViewById(R.id.coupon_tx);
        payment_txt = (TextView) findViewById(R.id.payment_txt);

        HashMap<String ,String> data3 = new HashMap<>();
        data3.put("language_id" , "1");
        apiManager.execution_method_post(RentalConfig.ApiKyes.KEY_PaymentOption , ""+ RentalConfig.APIS.viewPaymentOption, data3,true , ApiManager.ACTION_SHOW_TOAST);


        findViewById(R.id.apply_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ConfirmRentalActivity.this, CouponActivity.class), 101);
                overridePendingTransition(R.anim.animation3, R.anim.animation4);
            }
        });

        findViewById(R.id.package_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmRentalActivity.this , RentalPackageActivity.class));
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finalizeActivity ();
            }
        });


        findViewById(R.id.payment_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForSelectPayment();
            }
        });


        findViewById(R.id.book_later_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ConfirmRentalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.setMinDate(calendar);
                dpd.setAccentColor(ConfirmRentalActivity.this.getResources().getColor(R.color.colorPrimary));
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                dpd.show(getFragmentManager(), "Date Picker Dialog");
            }
        });

        findViewById(R.id.ride_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , String > data = new HashMap<String, String>();
                data.put("booking_type" , "1");
                data.put("pickup_lat" , ""+ RentalConfig.User_latitude);
                data.put("pickup_long" , ""+ RentalConfig.User_longitude);
                data.put("pickup_location" , ""+ RentalConfig.User_location);
                data.put("car_type_id" , ""+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getCar_type_id());
                data.put("rentcard_id" , ""+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getRentcard_id());
                data.put("user_id" , ""+ RentalConfig.User_id);
                data.put("coupan_code" , ""+COUPON_CODE);
                data.put("payment_option_id" , ""+PAYMENT_ID);
                apiManager.execution_method_post(RentalConfig.ApiKyes.KEY_RENTAl_Book_car , ""+ RentalConfig.Base_Url+ RentalConfig.APIS.book_ride , data,true , ApiManager.ACTION_SHOW_TOAST);
            }
        });
    }



    public static String getmonthname(int val){
        String retutningval ;
        if(val == 0){
            retutningval = "JANUARY";
        }else if (val == 1){
            retutningval = "FEBRUARY";
        }else if (val == 2){
            retutningval = "MARCH";
        }else if (val == 3){
            retutningval = "APRIL";
        }else if (val == 4){
            retutningval = "MAY";
        }else if (val == 5){
            retutningval = "JUNE";
        }else if (val == 6){
            retutningval = "JULY";
        }else if (val == 7){
            retutningval = "AUGUST";
        }else if (val == 8){
            retutningval = "SEPTEMBER";
        }else if (val == 9){
            retutningval = "OCTOBER";
        }else if (val == 10){
            retutningval = "NOVEMBER";
        }else if (val == 11){
            retutningval = "DECEMBER";
        }else {
            retutningval = "WRONG_DATE";
        }
        return retutningval;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setViewAccordingSelection ();
    }

    private void finalizeActivity() {
        finish();
        try{
            RentalCarTypeActivity.activity.finish();
        }catch (Exception e){
        }
    }

    @Override
    public void onBackPressed() {
        finalizeActivity();
    }

    private void setViewAccordingSelection() {
        car_type_name.setText(""+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getCar_type_name());
        Glide.with(this).load(""+ RentalConfig.Image_Base_Url+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getCar_type_image()).into(car_image);
        selected_package_name.setText(""+ RentalConfig.SELECTED_PACKAGE_NAME);

        base_fare_txt.setText("includes "+ RentalConfig.SELECTED_PACKAGE.getRental_category_hours()+" hours "+ RentalConfig.SELECTED_PACKAGE.getRental_category_kilometer());
        base_fare_price.setText(RentalConfig.currency_symbol+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getPrice());

        additional_distance_fare_txt.setText("After First "+ RentalConfig.SELECTED_PACKAGE.getRental_category_kilometer());
        additional_distance_fare_price.setText(""+sessionManager.getCurrencyCode()+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getPrice_per_kms());

        additional_time_txt.setText("After First "+ RentalConfig.SELECTED_PACKAGE.getRental_category_hours()+" hours ");
        additional_time_price.setText(RentalConfig.currency_symbol+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getPrice_per_hrs());
        terms_txt.setText(Html.fromHtml(""+ RentalConfig.SELECTED_PACKAGE.getRental_category_description()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RentalConfig.is_confirm_rental_activity_is_open = false ;
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try{switch (APINAME){
            case RentalConfig.ApiKyes.KEY_RENTAl_Book_car :
                ResultStatusChecker rs = gson.fromJson("" +script, ResultStatusChecker.class);
                if(rs.getStatus() == 1){
                    RentalConfig.PostedRequest_RENTAL = true ;
                    RentalConfig.PostedRentalType = 1 ;
                    Toast.makeText(activity, ""+rs.getMessage(), Toast.LENGTH_SHORT).show();
                    RentalConfig.rental_ride_now_response  = gson.fromJson(""+script , RentalRidenowModel.class);
                    finish();
                }else {
                    Toast.makeText(activity, ""+rs.getMessage(), Toast.LENGTH_SHORT).show();
                }break ;
            case RentalConfig.ApiKyes.KEY_PaymentOption :
                ResultCheck resultCheck;
                resultCheck = gson.fromJson(""+script, ResultCheck.class);
                if (resultCheck.result.equals("1")) {
                    viewPaymentOption = gson.fromJson(""+script, ViewPaymentOption.class);
                    payment_txt.setText(""+viewPaymentOption.getMsg().get(0).getPayment_option_name());
                    PAYMENT_ID = viewPaymentOption.getMsg().get(0).getPayment_option_id();
                } else {
                    Toast.makeText(this, "Problem in fetching payment option", Toast.LENGTH_SHORT).show();
                }
                break ;
        }}catch (Exception e){}

    }



    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == Activity.RESULT_OK) {
            try {
                if (req == 101) {
                    COUPON_CODE = data.getStringExtra("coupon_code");
                    coupon_tx.setText("Coupon Applied \n"+data.getStringExtra("coupon_code"));
                }   if (req == 103) {
                    payment_txt.setText("credit Card");
                    PAYMENT_ID = "3";
                }
            } catch (Exception e) {
                Log.e("res ", e.toString());
            }
        }
    }




    public void dialogForSelectPayment() {
        final Dialog dialog = new Dialog(ConfirmRentalActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_payment_option);

        ListView lv_payment_option = (ListView) dialog.findViewById(R.id.lv_payment_option);
        try {
            if(viewPaymentOption.getMsg().size() >0){
                lv_payment_option.setAdapter(new PaymentAdapter(ConfirmRentalActivity.this, viewPaymentOption));
            }
        }catch (Exception e){

        }


        lv_payment_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    payment_txt.setText(""+viewPaymentOption.getMsg().get(position).getPayment_option_name());
                    PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPayment_option_id();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        LATERDATE = dayOfMonth + "/" + month + "/" + year;
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        tpd.setAccentColor(this.getResources().getColor(R.color.colorPrimary));
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        tpd.show(getFragmentManager(), "Time Picker Dialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        LATERTIME = hourString + ":" + minuteString;


        startActivity(new Intent(ConfirmRentalActivity.this, RidelaterConfirmActivity.class)
                .putExtra("booking_date", "" + LATERDATE)
                .putExtra("booking_time", "" +LATERTIME));
    }
}
