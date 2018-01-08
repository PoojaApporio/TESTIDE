package com.ide.customer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ide.customer.adapter.SosAdapter;
import com.ide.customer.models.NewSosModel;
import com.ide.customer.manager.ApiManager;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SosActivity extends Activity implements ApiManager.APIFETCHER {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.list)
    ListView list;

    ApiManager apiManager ;
    Gson gson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson =  new GsonBuilder().create();
        apiManager = new ApiManager(this , this , this );
        setContentView(R.layout.activity_sos);
        ButterKnife.bind(this);


        apiManager.execution_method_get(""+ Config.ApiKeys.KEY_SOS, ""+ Apis.Sos, true , ApiManager.ACTION_SHOW_TOAST);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{NewSosModel newsos_response = gson.fromJson(""+script , NewSosModel.class);
            list.setAdapter(new SosAdapter(this , newsos_response));}catch (Exception e){}
    }
}
