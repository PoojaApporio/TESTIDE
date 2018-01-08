package com.ide.customer.holders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ide.customer.R;
import com.ide.customer.TrialActivity;
import com.ide.customer.models.CarTypeResponseModel;
import com.ide.customer.rentalmodule.IntroForRentalPackage;
import com.ide.customer.rentalmodule.RentalConfig;
import com.ide.customer.samwork.RideSession;
import com.ide.customer.urls.Apis;
import com.ide.customer.manager.SessionManager;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.sam.placer.PlaceHolderView;
import com.sam.placer.annotations.Click;
import com.sam.placer.annotations.Layout;
import com.sam.placer.annotations.Position;
import com.sam.placer.annotations.Resolve;
import com.sam.placer.annotations.View;

/**
 * Created by lenovo-pc on 5/7/2017.
 */

@Layout(R.layout.holder_car_type)
public class HolderCarType {

    @View(R.id.placeholder) private PlaceHolderView placeholder;

    Context mContext ;
    CarTypeResponseModel mcarTypeResponse ;
    Activity mActivity;
    
    RideSession mridesession ;
    SessionManager sessionManager ;

    GeoQuery mgeoQuery;
    GeoQueryEventListener mgeoQueryEventListene ;
    OncategorySelected mListener ;


    public HolderCarType(Context context, CarTypeResponseModel car_type_response, Activity activity, RideSession ridesession, GeoQuery geoQuery, GeoQueryEventListener geoQueryEventListener , OncategorySelected listener ){
        mContext = context ;
        mcarTypeResponse = car_type_response;
        mActivity = activity ;
        mridesession = ridesession ;
        mgeoQuery = geoQuery ;
        mgeoQueryEventListene = geoQueryEventListener ;
        mListener = listener ;
        sessionManager = new SessionManager(context);
    }


    @Resolve
    private void onResolved() {

        placeholder.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        for(int i =0 ; i < mcarTypeResponse.getDetails().size() ; i++){
            placeholder.addView(new HoldercarTypeNEW());
        }
        resolveSelectionOfcategory(0);
    }


    private void resolveSelectionOfcategory(int position) {
        mridesession.setCategory(mcarTypeResponse.getDetails().get(position).getCar_type_id(), mcarTypeResponse.getDetails().get(position).getCar_type_name(), mcarTypeResponse.getDetails().get(position).getCar_type_image() , ""+mcarTypeResponse.getDetails().get(position).getCity_id());

    }

    @Layout(R.layout.item)

    public class HoldercarTypeNEW{

        @Position
        private int mPosition ;
        @View(R.id.car_image) private ImageView car_image;
        @View(R.id.car_type_name_txt) private TextView car_type_name_txt;
        @View(R.id.root_width_layout) private LinearLayout root_width_layout;
        @View(R.id.base_fare_txt) private TextView base_fare_txt;






        @Resolve
        private void onResolved() {
            root_width_layout.setMinimumWidth(getScreenWidth());
            Glide.with(mContext).load(""+ Apis.imageDomain+mcarTypeResponse.getDetails().get(mPosition).getCar_type_image()).into(car_image);

            Log.d("**CAR_IMAGE==", ""+ Apis.imageDomain+mcarTypeResponse.getDetails().get(mPosition).getCar_type_image());

            car_type_name_txt.setText(""+mcarTypeResponse.getDetails().get(mPosition).getCar_type_name());
            base_fare_txt.setText(""+mcarTypeResponse.getDetails().get(mPosition).getBase_fare());
           setBackColor();
        }

        private void setBackColor() {
            if(mridesession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID).equals(""+mcarTypeResponse.getDetails().get(mPosition).getCar_type_id())){
                root_width_layout.setBackgroundColor(mContext.getResources().getColor(R.color.color_select_car_type));
                car_type_name_txt.setTextColor(mContext.getResources().getColor(R.color.pure_white));
                base_fare_txt.setTextColor(mContext.getResources().getColor(R.color.pure_white));
            }else {
                root_width_layout.setBackgroundColor(mContext.getResources().getColor(R.color.pure_white));

                car_type_name_txt.setTextColor(mContext.getResources().getColor(R.color.color_select_car_type));
                base_fare_txt.setTextColor(mContext.getResources().getColor(R.color.icons_8_muted_yellow_dark));
            }
            placeholder.refresh();
        }


        @Click(R.id.root)
        private void OnClickRoot(){
            switch (mcarTypeResponse.getDetails().get(mPosition).getRide_mode()){
                case "1":
                    if(!mridesession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID).equals(mcarTypeResponse.getDetails().get(mPosition).getCar_type_id())){
                        resolveSelectionOfcategory(mPosition);
                        setBackColor();
                        mListener.oncategorySelected(mcarTypeResponse.getDetails().get(mPosition).getCar_type_id());
                    }
                    break;
                case  "2":
                    mContext.startActivity(new Intent(mContext , IntroForRentalPackage.class)
                            .putExtra(""+ RentalConfig.IntentKeys.Baseurl, ""+Apis.NewRestApiBaseUrl)
                            .putExtra(""+ RentalConfig.IntentKeys.apibaseUrl , ""+ Apis.BaseUrl)
                            .putExtra(""+ RentalConfig.IntentKeys.CityId, ""+mcarTypeResponse.getDetails().get(mPosition).getCity_id())
                            .putExtra(""+ RentalConfig.IntentKeys.Image_base_url, ""+Apis.imageDomain)
                            .putExtra(""+ RentalConfig.IntentKeys.user_id, ""+sessionManager.getUserDetails().get(SessionManager.USER_ID))
                            .putExtra("" + RentalConfig.IntentKeys.user_latitude, ""+ TrialActivity.PICK_LATITUDE)
                            .putExtra(""+ RentalConfig.IntentKeys.user_longitude, ""+TrialActivity.PICK_LONGITUDE)
                            .putExtra(""+ RentalConfig.IntentKeys.user_location, ""+TrialActivity.PICK_ADDRESS)
                            .putExtra(""+ RentalConfig.IntentKeys.currency_symbol, ""+sessionManager.getCurrencyCode()));
                    break;
            }

        }
    }


    private int  getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return (width /3);
    }


    public interface OncategorySelected {
        public void oncategorySelected (String category_id);
    }
}
