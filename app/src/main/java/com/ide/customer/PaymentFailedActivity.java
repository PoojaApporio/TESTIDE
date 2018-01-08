package com.ide.customer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ide.customer.manager.ApiManager;
import com.ide.customer.manager.SessionManager;
import com.ide.customer.models.DoneRideInfo;
import com.ide.customer.rentalmodule.ResultCheck;
import com.ide.customer.samwork.Config;
import com.ide.customer.trackRideModule.TrackRideAactiviySamir;
import com.ide.customer.urls.Apis;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentFailedActivity extends Activity implements ApiManager.APIFETCHER {
    private static final String TAG = "PaymentFailedActivity";
    ApiManager apiManager;
    @Bind(R.id.map_image)
    ImageView mapImage;
    @Bind(R.id.net_payble_amount)
    TextView netPaybleAmount;
    @Bind(R.id.total_distance_txt)
    TextView totalDistanceTxt;
    @Bind(R.id.total_ride_time_txt)
    TextView totalRideTimeTxt;
    @Bind(R.id.wallet_deducted_amount_txt)
    TextView walletDeductedAmountTxt;
    @Bind(R.id.pick_address_txt)
    TextView pickAddressTxt;
    @Bind(R.id.drop_address_txt)
    TextView dropAddressTxt;
    @Bind(R.id.rating_bar)
    RatingBar ratingBar;
    @Bind(R.id.distance_charges)
    TextView distanceCharges;
    @Bind(R.id.ride_time_charges_txt)
    TextView rideTimeChargesTxt;
    @Bind(R.id.waiting_charge_txt)
    TextView waitingChargeTxt;
    @Bind(R.id.night_charge_txt)
    TextView nightChargeTxt;
    @Bind(R.id.peak_charge_txt)
    TextView peakChargeTxt;
    @Bind(R.id.coupon_code_txt)
    TextView couponCodeTxt;
    @Bind(R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(R.id.coupon_layout)
    LinearLayout couponLayout;
    @Bind(R.id.tv_ride_fare)
    TextView tvRideFare;
    @Bind(R.id.cash_layout)
    LinearLayout cashLayout;
    @Bind(R.id.cash_charges_txt)
    TextView cashChargesTxt;
    @Bind(R.id.online_payment_layout)
    LinearLayout onlinePaymentLayout;
    @Bind(R.id.payment_success_layout)
    LinearLayout paymentSuccessLayout;
    @Bind(R.id.payment_failed_layout)
    CardView paymentFailedLayout;
    @Bind(R.id.paypal_btn)
    TextView paypalBtn;
    @Bind(R.id.payment_option)
    TextView paymentOption;
    @Bind(R.id.ok)
    ImageView ok;
    @Bind(R.id.comment_edt)
    EditText commentEdt;
    @Bind(R.id.driver_image)
    CircleImageView driverImage;
    @Bind(R.id.amount_paid)
    TextView amountPaid;
    @Bind(R.id.driver_name_txt)
    TextView driverNameTxt;

    public static final int PAYPAL_REQUEST_CODE = 123;
    public static String rideId;
    public static String begin_lat;
    public static String begin_long;
    public static String end_lat;
    public static String end_long;

    DoneRideInfo doneRideInfo;
    SessionManager sessionManager ;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.

    private static final int REQUEST_CODE_PAYMENT = 133;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static PayPalConfiguration config;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        rideId = super.getIntent().getExtras().getString("ride_id");
        apiManager = new ApiManager(this, this, this);
        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(this.getResources().getString(R.string.paypal_key))
                .merchantName("Example Merchant")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        apiManager.execution_method_get("" + Config.ApiKeys.Key_view_Done_Ride, "" + Apis.viewDoneRide + "done_ride_id=" + rideId + "&language_id=1", true, ApiManager.ACTION_SHOW_DIALOG);

        findViewById(R.id.paypal_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startPayPalPayment();
                } catch (Exception e) {
                    Toast.makeText(PaymentFailedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.payment_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showpaymentMethod();
                } catch (Exception e) {
                }
            }
        });

        paymentFailedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showpaymentMethod();
                } catch (Exception e) {
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiManager.execution_method_get("" + Config.ApiKeys.Key_Rating_Api, "" + Apis.rating + "ride_id=" + rideId + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&driver_id=" + doneRideInfo.getMsg().getDriver_id() + "&rating_star=" + ratingBar.getRating() + "&comment=" + commentEdt.getText().toString().replace(" ", "%20") + "&language_id=1", true, ApiManager.ACTION_SHOW_DIALOG);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void showAlert(String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("" + message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });

        dialog.show();
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if (APINAME.equals(Config.ApiKeys.Key_view_Done_Ride)) {
                doneRideInfo = new DoneRideInfo();
                doneRideInfo = gson.fromJson("" + script, DoneRideInfo.class);
                begin_lat = doneRideInfo.getMsg().getBegin_lat();
                begin_long = doneRideInfo.getMsg().getBegin_long();
                end_lat = doneRideInfo.getMsg().getEnd_lat();
                end_long = doneRideInfo.getMsg().getEnd_long();
                if (doneRideInfo.getResult() == 1) {
                    try {
                       // Glide.with(PaymentFailedActivity.this).load("https://maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=200x200&maptype=roadmap&markers=color:green|label:S|28.41218546490328,77.04312231391668&markers=color:red|label:D|28.451863901432855,77.0694337785244&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4").into(mapImage);
                        Glide.with(PaymentFailedActivity.this).load("https://maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=200x200&maptype=roadmap&markers=color:green|label:S|"+begin_lat+"+"+begin_long+"+&markers=color:red|label:D|"+end_lat+"+"+end_long+"+&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4").into(mapImage);

                    } catch (Exception e) {
                        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    netPaybleAmount.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_payable_amount());
                    totalDistanceTxt.setText("" + this.getResources().getString(R.string.total_distance) + doneRideInfo.getMsg().getDistance());
                    totalRideTimeTxt.setText(this.getResources().getString(R.string.total_ride_time) + doneRideInfo.getMsg().getRide_time());
                    pickAddressTxt.setText("" + doneRideInfo.getMsg().getBegin_location());
                    dropAddressTxt.setText("" + doneRideInfo.getMsg().getEnd_location());
                    distanceCharges.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getAmount());
                    rideTimeChargesTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getRide_time_price());
                    waitingChargeTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getWaiting_price());
                    nightChargeTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getNight_time_charge());
                    peakChargeTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getPeak_time_charge());
                    couponCodeTxt.setText(getString(R.string.coupon_bracket) + doneRideInfo.getMsg().getCoupons_code() + ")");
                    couponPriceTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getCoupons_price());
                    tvRideFare.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_amount());
                    walletDeductedAmountTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getWallet_deducted_amount());
                    cashChargesTxt.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_payable_amount());
                    amountPaid.setText("" + sessionManager.getCurrencyCode() + doneRideInfo.getMsg().getTotal_payable_amount());
                    driverNameTxt.setText(getString(R.string.rate_driver)+" "+doneRideInfo.getMsg().getDriver_name());

                    try {
                        setpaymentAccordingly(doneRideInfo.getMsg().getPayment_option_id(), doneRideInfo.getMsg().getPayment_status());
                    } catch (Exception e) {
                    }
                    try {
                        Glide.with(this).load(Apis.imageDomain + "" + doneRideInfo.getMsg().getDriver_image()).into(driverImage);
                    } catch (Exception e) {
                        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (doneRideInfo.getMsg().getPayment_status().equals("0")) {
                        showAlert("" + doneRideInfo.getMsg().getPayment_falied_message());
                        ok.setVisibility(View.GONE);
                    } else {
                        ok.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(PaymentFailedActivity.this, "" + doneRideInfo.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            if (APINAME.equals("" + Config.ApiKeys.Key_Rating_Api)) {
                ResultCheck rs = gson.fromJson("" + script, ResultCheck.class);
                if (rs.result.equals("1")) {
                    finish();
                    try {
                        TrackRideAactiviySamir.trackRideActivity.finish();
                    } catch (Exception e) {
                    }
                    try {
                        RentalTrackActivity.trackRideActivity.finish();
                    } catch (Exception e) {
                    }
                    try {
                        TrialRideConfirmDialogActivity.activity.finish();
                    } catch (Exception e) {
                    }
                }
            }
            if (APINAME.equals("" + Config.ApiKeys.Key_payment_api)) {
                ResultCheck rs = gson.fromJson("" + script, ResultCheck.class);
                if (rs.result.equals("1")) {
                    apiManager.execution_method_get("" + Config.ApiKeys.Key_view_Done_Ride, "" + Apis.viewDoneRide + "done_ride_id=" + rideId + "&language_id=1", true, ApiManager.ACTION_SHOW_DIALOG);
                } else {
                    Toast.makeText(this, "payment failed", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void setpaymentAccordingly(String mode, String payment_success_status) throws Exception {

        if (mode.equals("1")) {  // cash selected at ride booking time
            cashLayout.setVisibility(View.VISIBLE);
            paymentFailedLayout.setVisibility(View.GONE);
            onlinePaymentLayout.setVisibility(View.GONE);
            paymentSuccessLayout.setVisibility(View.GONE);
        }
        if (mode.equals("4")) {  // wallet selected
            cashLayout.setVisibility(View.GONE);
            onlinePaymentLayout.setVisibility(View.GONE);
            if (payment_success_status.equals("0")) {
                paymentFailedLayout.setVisibility(View.VISIBLE);
                paymentSuccessLayout.setVisibility(View.GONE);
            }
            if (payment_success_status.equals("1")) {
                paymentFailedLayout.setVisibility(View.GONE);
                paymentSuccessLayout.setVisibility(View.VISIBLE);
            }
        }
        if (mode.equals("3")) {  // credit card
            cashLayout.setVisibility(View.GONE);
            onlinePaymentLayout.setVisibility(View.GONE);
            if (payment_success_status.equals("0")) {
                paymentFailedLayout.setVisibility(View.VISIBLE);
                paymentSuccessLayout.setVisibility(View.GONE);
            }
            if (payment_success_status.equals("1")) {
                paymentFailedLayout.setVisibility(View.GONE);
                paymentSuccessLayout.setVisibility(View.VISIBLE);
            }
        }
        if (mode.equals("2")) { // paypal selected
            cashLayout.setVisibility(View.GONE);
            paymentFailedLayout.setVisibility(View.GONE);
            if (payment_success_status.equals("0")) {
                onlinePaymentLayout.setVisibility(View.VISIBLE);
                paymentSuccessLayout.setVisibility(View.GONE);
            }
            if (payment_success_status.equals("1")) {
                onlinePaymentLayout.setVisibility(View.GONE);
                paymentSuccessLayout.setVisibility(View.VISIBLE);
            }

        }
    }

    private void startPayPalPayment() throws Exception {
        Intent intent = new Intent(PaymentFailedActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, new PayPalPayment(new BigDecimal("" + netPaybleAmount.getText().toString().replace("" + sessionManager.getCurrencyCode(), "")), "USD", "sample item", PayPalPayment.PAYMENT_INTENT_SALE));
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }


    private void showpaymentMethod() throws Exception {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PaymentFailedActivity.this);
        builderSingle.setTitle(R.string.Payment_Failed_select_payment_option);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentFailedActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("PayPal");
        arrayAdapter.add("CASH");

        builderSingle.setNegativeButton("" + this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(PaymentFailedActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle(R.string.are_you_syre_you_want_to_proceed_with);
                builderInner.setPositiveButton("" + PaymentFailedActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (strName.equals("PayPal")) {
                            try {
                                startPayPalPayment();
                            } catch (Exception e) {
                                Toast.makeText(PaymentFailedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (strName.equals("CASH")) {
                            apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "Cash" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + sessionManager.getCurrencyCode(), "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);
                        }
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "Paypal" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + sessionManager.getCurrencyCode(), "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
}
