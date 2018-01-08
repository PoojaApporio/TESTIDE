package com.ide.customer.StatusSession;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimelySessionService  extends Service{

    private DBHelper dbHelper ;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    boolean is_method_running = false ;

    private static String TAG = "TimelySessionService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dbHelper = new DBHelper(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("RideTable");
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimelySessionService.TimeDisplayTimerTask(), 0, 3000);
    }



    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateLocation();
                }
            });
        }

        private void updateLocation() {
            final ArrayList<String> ids = dbHelper.getAllRideId() ;
//            Log.i(""+TAG , "Ride IDS = "+ids);
            if(is_method_running == false){

            }
            if(ids.size() > 0){
                is_method_running = true ;
                for(int i = 0 ; i < ids.size() ; i++){ // for updating all exsisting IDS
                        try{
                            final int finalI = i;
                            myRef.child(""+ids.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try{ if(dataSnapshot.child("ride_status").getValue().equals("2")||dataSnapshot.child("ride_status").getValue().equals("4")||dataSnapshot.child("ride_status").getValue().equals("9")||dataSnapshot.child("ride_status").getValue().equals("14")||dataSnapshot.child("ride_status").getValue().equals("15")||dataSnapshot.child("ride_status").getValue().equals("18") || dataSnapshot.child("ride_status").getValue().equals("7")){ // if ride is cancelled by driver or user or expired remove it from database
                                        dbHelper.deleteRowByid(""+ids.get(finalI));
                                    }
                                        EventBus.getDefault().post(new RideSessionEvent(ids.get(finalI) , ""+dataSnapshot.child("ride_status").getValue() ,""+dataSnapshot.child("done_ride_id").getValue() , ""+dataSnapshot.child("changed_destination").getValue()));
                                        dbHelper.updateRow(""+ids.get(finalI) , ""+dataSnapshot.child("ride_status").getValue());}catch (Exception e){}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.i(""+TAG , "Data Fetched from firebase cancelled "+databaseError.getMessage());
                                }
                            });
                        }catch (Exception e){
                            Log.e(""+TAG , "TAXI EXCEPTION "+e.getMessage());
                            is_method_running = false ;
                        }
                }
                is_method_running = false ;
            }else {
                Log.i(""+TAG , "You don't have any ride id to update");
            }
        }
    }
}
