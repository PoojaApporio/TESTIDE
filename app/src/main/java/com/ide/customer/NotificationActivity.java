package com.ide.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.models.NotificationResponse;
import com.ide.customer.others.SingletonGson;
import com.ide.customer.samwork.Config;
import com.ide.customer.urls.Apis;
import com.bumptech.glide.Glide;
import com.sam.placer.PlaceHolderView;
import com.sam.placer.annotations.Click;
import com.sam.placer.annotations.Layout;
import com.sam.placer.annotations.Position;
import com.sam.placer.annotations.Resolve;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationActivity extends Activity implements ApiManager.APIFETCHER {

    ApiManager apiManager ;
    @Bind(R.id.placeHolder) PlaceHolderView placeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        HashMap<String ,String > data = new HashMap<>();
        data.put("application" , "1");
        data.put("id" , ""+new SessionManager(this).getUserDetails().get(SessionManager.USER_ID));
        apiManager.execution_method_post(Config.ApiKeys.KEY_REST_NOTIFICATIONS,""+ Apis.Notifications,data,true, ApiManager.ACTION_SHOW_TOP_BAR);
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{  if(APINAME.equals(""+Config.ApiKeys.KEY_REST_NOTIFICATIONS)){
            NotificationResponse notificationResponse = SingletonGson.getInstance().fromJson(""+script, NotificationResponse.class);
            for(int i =0  ; i < notificationResponse.getDetails().size() ; i++){
                placeHolder.addView(new HolderNotifications(notificationResponse.getDetails().get(i)));
            }
        }}catch (Exception e){}


    }


    @Layout(R.layout.holder_notification)
    private class HolderNotifications{


        @com.sam.placer.annotations.View(R.id.image) private ImageView image;
        @com.sam.placer.annotations.View(R.id.heading_txt) private TextView heading_txt;
        @com.sam.placer.annotations.View(R.id.info_text) private TextView info_text;

        @Position
        private int mPosition ;
        NotificationResponse.DetailsBean mDetailBean ;

        public HolderNotifications(NotificationResponse.DetailsBean detailsBean) {
            mDetailBean = detailsBean;
        }


        @Resolve
        private void onResolved() {
            Glide.with(NotificationActivity.this).load(Apis.imageDomain+""+ mDetailBean.getPush_image()).into(image);
            heading_txt.setText(""+mDetailBean.getPush_message_heading());
            info_text.setText(""+mDetailBean.getPush_message());
        }

        @Click(R.id.root)
        private void OnClick(){
            if(!mDetailBean.getPush_web_url().equals("")){
                startActivity(new Intent(NotificationActivity.this , NotificationWebViewActivity.class).putExtra("web_url" , ""+mDetailBean.getPush_web_url()));
            }else{
                startActivity(new Intent(NotificationActivity.this , NotificationWebViewActivity.class).putExtra("web_url" , "https://www.ide.com/"));
            }

        }


    }

}
