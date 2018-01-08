package com.ide.customer.rentalmodule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ide.customer.R;
import com.ide.customer.manager.ApiManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class RidelaterConfirmActivity extends Activity implements ApiManager.APIFETCHER{


    ApiManager apiManager ;

    TextView source_txt , selected_date_txt , selected_time_txt ;
    ImageView car_image ;
    Gson gson ;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        apiManager = new ApiManager(this , this , this);
        setContentView(R.layout.activity_ridelater_confirm);
        source_txt = (TextView) findViewById(R.id.source_txt);
        selected_date_txt = (TextView) findViewById(R.id.selected_date_txt);
        selected_time_txt = (TextView) findViewById(R.id.selected_time_txt);
        car_image = (ImageView) findViewById(R.id.car_image);





        source_txt.setText(""+ RentalConfig.User_location);
        Glide.with(this).load(""+ RentalConfig.Image_Base_Url+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getCar_type_image()).into(car_image);
        selected_date_txt.setText(""+getIntent().getExtras().getString("booking_date"));
        selected_time_txt.setText(""+getIntent().getExtras().getString("booking_time"));




        findViewById(R.id.confirm_lout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , String > data = new HashMap<String, String>();
                data.put("booking_type" , "2");
                data.put("pickup_lat" , ""+ RentalConfig.User_latitude);
                data.put("pickup_long" , ""+ RentalConfig.User_longitude);
                data.put("pickup_location" , ""+ RentalConfig.User_location);
                data.put("car_type_id" , ""+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getCar_type_id());
                data.put("rentcard_id" , ""+ RentalConfig.SELECTED_RENTAL_CAR_BEAN.getRentcard_id());
                data.put("user_id" , ""+ RentalConfig.User_id);
                data.put("booking_date" , ""+getIntent().getExtras().getString("booking_date"));
                data.put("booking_time" , ""+getIntent().getExtras().getString("booking_time"));
                data.put("coupan_code" , ""+getIntent().getExtras().getString("coupon"));
                data.put("payment_option_id" , ""+getIntent().getExtras().getString("payment_id"));
                apiManager.execution_method_post(RentalConfig.ApiKyes.KEY_RENTAl_Book_car , ""+ RentalConfig.Base_Url+ RentalConfig.APIS.book_ride , data,true,ApiManager.ACTION_SHOW_TOAST);
            }
        });







    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultStatusChecker rs = gson.fromJson(""+script , ResultStatusChecker.class);
            if(rs.getStatus() == 1){
                RentalConfig.PostedRequest_RENTAL = true ;
                RentalConfig.PostedRentalType = 2 ;
                Toast.makeText(this, ""+rs.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                try{
                    ConfirmRentalActivity.activity.finish();
                }catch (Exception e ){

                }
            }else {
                finish();
                Toast.makeText(this, ""+rs.getMessage(), Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}


    }
}
