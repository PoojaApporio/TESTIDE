package com.ide.customer.rentalmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ide.customer.R;
import com.ide.customer.manager.ApiManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class IntroForRentalPackage extends Activity implements ApiManager.APIFETCHER{

    ApiManager apiManager;
    Gson gson ;
    LinearLayout book_btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this);
        gson  = new GsonBuilder().create();
        setContentView(R.layout.activity_intro_for_rental_package);
        book_btn = (LinearLayout) findViewById(R.id.book_btn);
        try{ RentalConfig.Base_Url = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.Baseurl);
            RentalConfig.api_base_url = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.apibaseUrl);
            RentalConfig.Image_Base_Url = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.Image_base_url);
            RentalConfig.City_Id = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.CityId);
            RentalConfig.User_id = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.user_id);
            RentalConfig.User_latitude = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.user_latitude);
            RentalConfig.User_longitude = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.user_longitude);
            RentalConfig.User_location = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.user_location);
            RentalConfig.currency_symbol = getIntent().getStringExtra(""+ RentalConfig.IntentKeys.currency_symbol);}catch (Exception e){}


        HashMap<String , String >data = new HashMap<>();
        data.put("city_id" , RentalConfig.City_Id);

        if(RentalConfig.response == null){
            apiManager.execution_method_post(""+ RentalConfig.ApiKyes.KEY_RENTAl_PACKAGE , ""+ RentalConfig.APIS.RentalPackage , data,true , ApiManager.ACTION_SHOW_TOAST);
        }



        findViewById(R.id.book_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroForRentalPackage.this , RentalPackageActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultStatusChecker resultStatusChecker = gson.fromJson(""+script ,ResultStatusChecker.class);
            if(resultStatusChecker.getStatus() == 1){
                RentalConfig.response = gson.fromJson("" +script,RentalPackageResponse.class);
            }else {
                Toast.makeText(this, ""+resultStatusChecker.getStatus() +"  "+resultStatusChecker.getMessage(), Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}

    }
}
