package com.ide.customer.urls;

public interface Apis {
    String imageDomain = "http://54.233.244.48/";
    String BaseUrl = "http://54.233.244.48/api/";
    String NewRestApiBaseUrl = "http://54.233.244.48/ide/index.php/";
   // String Loginbaseurl  = "http://ide.lk/NewDoorbi/";
    String Loginbaseurl  = "http://54.233.244.48/ide/index.php/";

    String register = BaseUrl + "register_user.php";
    String login = BaseUrl + "login_user.php";
    String deviceId = BaseUrl + "deviceid_user.php";
    String callSupport = BaseUrl + "call_support.php";
    String aboutUs = BaseUrl + "about.php?";
    String tC = BaseUrl + "tc.php?";
    String viewCities = BaseUrl + "city.php?";
    String viewCarByCities = BaseUrl + "car_by_city.php?";
    String viewRateCardCity = BaseUrl + "rate_card_city.php?";
    String viewRideInfo = BaseUrl+"view_ride_info_user.php";
    String viewDoneRide =  BaseUrl+"view_done_ride_info.php?";
    String applyCoupon = BaseUrl + "coupon.php?";
    String rideNow = BaseUrl + "ride_now.php";
    String rideLater = BaseUrl + "ride_later.php";
    String rideEstimate = BaseUrl + "ride_estimate.php";
    String cancelRide = BaseUrl + "ride_cancel.php";
    String rating = BaseUrl+ "rating_user.php?";
    String payment = BaseUrl+"payment_saved.php?";
    String resendInvoice = BaseUrl + "resend_invoice.php";
    String update = BaseUrl + "update.php";
    String cancelReason = BaseUrl + "cancel_reason_customer.php";
    String saveCard = BaseUrl + "save_card.php?";
    String deleteCard = BaseUrl + "delete_card.php?";
    String viewCard = BaseUrl + "view_card.php?";////
    String viewPaymentOption = BaseUrl + "view_payment_option.php";
    String customerSync = BaseUrl+"customer_sync.php";
    String customerSyncEnd = BaseUrl+"customer_sync_end.php";
    String ride_cancel_by_user = BaseUrl + "cancel_ride_user.php?ride_id=";
    String AddMoneyTOWallet = BaseUrl + "add_money.php?";
    String viewBalnceWallet = BaseUrl + "view_wallent_money.php?";
    String change_drop_location = BaseUrl+"change_drop_location.php?";  //  drop_lat  drop_long  drop_location  app_id (1 for user and 2 for driver) ride_id
    String getAddress = BaseUrl+"getaddress.php?latitude="; // "&longitude=";
    String user_sync =  BaseUrl+"user_sync.php?user_id=";


    String editProfile = BaseUrl + "edit_profile_user.php";
    String logout = BaseUrl + "logout_user.php";
    String changePassword = BaseUrl + "change_password_user.php";
    String forgotPassword = BaseUrl + "forgot_password_user.php";
    String checkPhone = BaseUrl + "phone.php";
    String sentOtp = BaseUrl + "otp_sent.php";
    String createPassword = BaseUrl + "create_password.php";
    String sendSocialToken = BaseUrl + "send_social_token.php";
    String registerSocial = BaseUrl + "register_user_google.php";
    String viewCars = BaseUrl + "car_type.php";
    String viewDrivers = BaseUrl + "view_driver.php";
    String viewRateCard = BaseUrl + "rate_card.php";
    String viewRides = BaseUrl + "view_rides_user.php";
    String trackDriver = BaseUrl + "view_driver_location.php";
    String forceUpdate = BaseUrl + "force_update.php";
    String payWthCard = BaseUrl + "pay_with_card.php";
    String googleNerestRoad = "https://roads.googleapis.com/v1/nearestRoads?points=" ; // &key=
    String AppConfig = "http://demo9170987.mockable.io/ApporioTaxiConfig";


    /////////////////////// rest Apis
    String ActivesRides = NewRestApiBaseUrl+"User/Active_Ride_History";  // user_id
    String Sos = NewRestApiBaseUrl+"Common/SOS?";
    String car_type = NewRestApiBaseUrl+"Rental/Car_Type"; // city_name   latitude  longitude
    String RideSync = NewRestApiBaseUrl+"Rental/Ride_Sync";   //  rental_booking_id      app_id= 2 for driver and 1 for customer
    String Ride_Info = NewRestApiBaseUrl+"Rental/Ride_Info"; //  rental_booking_id
    String Done_Ride_Info = NewRestApiBaseUrl+"Rental/Done_Ride_Info"; // rental_booking_id
    String Rating = NewRestApiBaseUrl+"Rental/Rating";  // rating_star  rental_booking_id  comment  user_id  driver_id  app_id
    String RideHistory = NewRestApiBaseUrl+"User/Ride_History";  //  user_id
    String RideDetails = NewRestApiBaseUrl+"User/Ride_Details";  //  ride_mode  booking_id
    String CancelRentalRide = NewRestApiBaseUrl+"Rental/Rental_User_Cancel_Ride";  // rental_booking_id   user_id
    String ShareRideurl_API = NewRestApiBaseUrl+"Common/Share_Ride_url";  //    ride_id
    String Shareurl = NewRestApiBaseUrl+"Ride/Track/" ;  //  here we will attach the encrypted id we are getting from ShareRideurl_API

    ////////////statics url
    String SosRequest = NewRestApiBaseUrl+"Common/SOS_Request";// Post parameters  ride_id  driver_id  user_id  sos_number application (1 for user and 2 for driver) latitude  longitude
    String URL_REGISTER = NewRestApiBaseUrl+"Account/Signup"; // //first_name  last_name  email  phone  password
    String URL_PHONE_LOGIN = NewRestApiBaseUrl+"Account/Login"; // email=   &passw
    String URL_CHANGE_PASSWORD = NewRestApiBaseUrl+"Account/Change_password";//  user_id=1  &old_password=1  &new_password=1
    String URL_PHONE_INFO = "http://www.apporiotaxi.com/Account/phone";
    String URL_FACEBOOK_SIGNUP = NewRestApiBaseUrl+"Account/facebook_signup";
    String URL_FACEBOOK_LOGIN = NewRestApiBaseUrl+"Account/facebook_login";
    String URL_GOOGLE_LOGIN = NewRestApiBaseUrl+"Account/google_login";
    String URL_GOOGLE_SIGNUP = NewRestApiBaseUrl+"Account/google_signup";
    String URL_UPDATE_EMAIL = NewRestApiBaseUrl+"Account/update_email";
    String URL_LOGOUT = NewRestApiBaseUrl+"Account/Logout";  // user_id   unique_id
    String URL_DEMO_SIGNUP = NewRestApiBaseUrl+"Demo_Account/Signup";
    String URL_EDIT_PROFILE = NewRestApiBaseUrl+"Account/Edit_Profile"; // user_name  user_email  user_id  user_image
    String URL_RENTAL_PAYMENT = NewRestApiBaseUrl+"Rental/Payment";  // rental_booking_id  amount_paid  payment_status
    String CustomerSupport = NewRestApiBaseUrl+"Common/Customer_support";  //  application(1 for user and 2 for driver)  name  email  phone  query  driver_id  user_id

    String Notifications = NewRestApiBaseUrl+"Common/Notification"; // application  (1 for user and 2 for driver)
    String FORGOT_PASSWORD  = Loginbaseurl + "Account/forgot_password_user";
 String FORGOTPASS_CONFIRMPASS = BaseUrl + "forgot_password.php";
 String FORGOTPASS_OTP = BaseUrl + "otp_sent.php";
 String SEND_OTP = BaseUrl+ "otp_sent.php";

 String AppVersions = BaseUrl+"app_version.php";


}