package com.ide.customer.rentalmodule;

/**
 * Created by lenovo-pc on 6/22/2017.
 */

public class RentalConfig {

    public static String currency_symbol = "\u20ac";

    public static String Base_Url = "";
    public static String api_base_url = "";
    public static String Image_Base_Url = "";
    public static String City_Id = "";
    public static String User_id = "";
    public static String User_latitude = "";
    public static String User_longitude = "";
    public static String User_location = "";


    public static RentalPackageResponse response ;

    public static  RentalPackageResponse.DetailsBean SELECTED_PACKAGE ;
    public static String SELECTED_PACKAGE_ID = "";
    public static String SELECTED_PACKAGE_NAME = "";
    public static int SELECTED_PACKAGE_POSITION ;

    public static RentalPackageResponse.DetailsBean.RentalPakageCarBean SELECTED_RENTAL_CAR_BEAN ;


    public static boolean is_confirm_rental_activity_is_open = false ;

    public static int Minimum_time_for_ride_later = 15 ;
    public static int rideLoaderActivity;

    public static boolean PostedRequest_RENTAL = false ;
    public static int PostedRentalType = 0 ;
    public static RentalRidenowModel rental_ride_now_response ;
    // need to be removed

    public static String RideId = "";


    public interface APIS{
        String RentalPackage = Base_Url+"Rental/Rental_Package";
        String book_ride = "Rental/Book_Car";  //    booking_type  pickup_lat  pickup_long  pickup_location  car_type_id rentcard_id  user_id ,if booking_type = 2 add more  parameters  booking_date  booking_time

        String coupon = api_base_url+"coupon.php";
        String viewPaymentOption =  api_base_url+"view_payment_option.php";

    }

    public interface ApiKyes {
        String KEY_RENTAl_PACKAGE = "Rental_Package";
        String KEY_RENTAl_Book_car = "Book_Car";
        String KEY_RENTAl_Coupons = "coupon";
        String KEY_PaymentOption = "paymentOption";
    }


    public interface IntentKeys {
        String Baseurl = "base_urls";
        String apibaseUrl = "api_base_urls";
        String CityId = "cityid";
        String Image_base_url = "image_base_url";
        String user_id = "user_id";
        String user_location = "user_location";
        String user_latitude = "user_latitude";
        String user_longitude = "user_longitude";

        String currency_symbol = "currency_sumbol";
    }


}
