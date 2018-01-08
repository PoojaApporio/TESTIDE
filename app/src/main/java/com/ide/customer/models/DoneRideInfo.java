
package com.ide.customer.models;

public class DoneRideInfo {


    /**
     * result : 1
     * msg : {"done_ride_id":"28","begin_location":"68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","end_location":"68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","waiting_time":"0 Min","waiting_price":"0.00","ride_time":"0 Min","ride_time_price":"00.00","distance":"0.00 Miles","amount":"100.00","tot_time":"0","user_id":"1","username":"ankit .","user_phone":"+919540956147","user_image":"","driver_id":"8","driver_name":"sara","driver_image":"","payment_option_id":"1","payment_option_name":"Cash","ride_id":"74","ride_date":"Monday, Sep 25","car_type_id":"3","car_type_name":"LUXURY","car_type_image":"uploads/car/editcar_3.png","coupons_code":"","coupons_price":"0.00","total_amount":"100","peak_time_charge":"0.00","night_time_charge":"0.00","wallet_deducted_amount":"0.00","payment_falied_message":"","payment_status":"1","ride_image":"https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=200x200&maptype=roadmap&markers=color:green|label:S|28.412048340385475,77.04332482069731&markers=color:red|label:D|28.4592693,77.0724192&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4","total_payable_amount":"100","begin_lat":"28.4121961","begin_long":"77.043257","end_lat":"28.4121901","end_long":"77.0432701"}
     */

    private int result;
    private MsgBean msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * done_ride_id : 28
         * begin_location : 68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * end_location : 68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * waiting_time : 0 Min
         * waiting_price : 0.00
         * ride_time : 0 Min
         * ride_time_price : 00.00
         * distance : 0.00 Miles
         * amount : 100.00
         * tot_time : 0
         * user_id : 1
         * username : ankit .
         * user_phone : +919540956147
         * user_image :
         * driver_id : 8
         * driver_name : sara
         * driver_image :
         * payment_option_id : 1
         * payment_option_name : Cash
         * ride_id : 74
         * ride_date : Monday, Sep 25
         * car_type_id : 3
         * car_type_name : LUXURY
         * car_type_image : uploads/car/editcar_3.png
         * coupons_code :
         * coupons_price : 0.00
         * total_amount : 100
         * peak_time_charge : 0.00
         * night_time_charge : 0.00
         * wallet_deducted_amount : 0.00
         * payment_falied_message :
         * payment_status : 1
         * ride_image : https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=200x200&maptype=roadmap&markers=color:green|label:S|28.412048340385475,77.04332482069731&markers=color:red|label:D|28.4592693,77.0724192&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4
         * total_payable_amount : 100
         * begin_lat : 28.4121961
         * begin_long : 77.043257
         * end_lat : 28.4121901
         * end_long : 77.0432701
         */

        private String done_ride_id;
        private String begin_location;
        private String end_location;
        private String waiting_time;
        private String waiting_price;
        private String ride_time;
        private String ride_time_price;
        private String distance;
        private String amount;
        private String tot_time;
        private String user_id;
        private String username;
        private String user_phone;
        private String user_image;
        private String driver_id;
        private String driver_name;
        private String driver_image;
        private String payment_option_id;
        private String payment_option_name;
        private String ride_id;
        private String ride_date;
        private String car_type_id;
        private String car_type_name;
        private String car_type_image;
        private String coupons_code;
        private String coupons_price;
        private String total_amount;
        private String peak_time_charge;
        private String night_time_charge;
        private String wallet_deducted_amount;
        private String payment_falied_message;
        private String payment_status;
        private String ride_image;
        private String total_payable_amount;
        private String begin_lat;
        private String begin_long;
        private String end_lat;
        private String end_long;

        public String getDone_ride_id() {
            return done_ride_id;
        }

        public void setDone_ride_id(String done_ride_id) {
            this.done_ride_id = done_ride_id;
        }

        public String getBegin_location() {
            return begin_location;
        }

        public void setBegin_location(String begin_location) {
            this.begin_location = begin_location;
        }

        public String getEnd_location() {
            return end_location;
        }

        public void setEnd_location(String end_location) {
            this.end_location = end_location;
        }

        public String getWaiting_time() {
            return waiting_time;
        }

        public void setWaiting_time(String waiting_time) {
            this.waiting_time = waiting_time;
        }

        public String getWaiting_price() {
            return waiting_price;
        }

        public void setWaiting_price(String waiting_price) {
            this.waiting_price = waiting_price;
        }

        public String getRide_time() {
            return ride_time;
        }

        public void setRide_time(String ride_time) {
            this.ride_time = ride_time;
        }

        public String getRide_time_price() {
            return ride_time_price;
        }

        public void setRide_time_price(String ride_time_price) {
            this.ride_time_price = ride_time_price;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTot_time() {
            return tot_time;
        }

        public void setTot_time(String tot_time) {
            this.tot_time = tot_time;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getDriver_name() {
            return driver_name;
        }

        public void setDriver_name(String driver_name) {
            this.driver_name = driver_name;
        }

        public String getDriver_image() {
            return driver_image;
        }

        public void setDriver_image(String driver_image) {
            this.driver_image = driver_image;
        }

        public String getPayment_option_id() {
            return payment_option_id;
        }

        public void setPayment_option_id(String payment_option_id) {
            this.payment_option_id = payment_option_id;
        }

        public String getPayment_option_name() {
            return payment_option_name;
        }

        public void setPayment_option_name(String payment_option_name) {
            this.payment_option_name = payment_option_name;
        }

        public String getRide_id() {
            return ride_id;
        }

        public void setRide_id(String ride_id) {
            this.ride_id = ride_id;
        }

        public String getRide_date() {
            return ride_date;
        }

        public void setRide_date(String ride_date) {
            this.ride_date = ride_date;
        }

        public String getCar_type_id() {
            return car_type_id;
        }

        public void setCar_type_id(String car_type_id) {
            this.car_type_id = car_type_id;
        }

        public String getCar_type_name() {
            return car_type_name;
        }

        public void setCar_type_name(String car_type_name) {
            this.car_type_name = car_type_name;
        }

        public String getCar_type_image() {
            return car_type_image;
        }

        public void setCar_type_image(String car_type_image) {
            this.car_type_image = car_type_image;
        }

        public String getCoupons_code() {
            return coupons_code;
        }

        public void setCoupons_code(String coupons_code) {
            this.coupons_code = coupons_code;
        }

        public String getCoupons_price() {
            return coupons_price;
        }

        public void setCoupons_price(String coupons_price) {
            this.coupons_price = coupons_price;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getPeak_time_charge() {
            return peak_time_charge;
        }

        public void setPeak_time_charge(String peak_time_charge) {
            this.peak_time_charge = peak_time_charge;
        }

        public String getNight_time_charge() {
            return night_time_charge;
        }

        public void setNight_time_charge(String night_time_charge) {
            this.night_time_charge = night_time_charge;
        }

        public String getWallet_deducted_amount() {
            return wallet_deducted_amount;
        }

        public void setWallet_deducted_amount(String wallet_deducted_amount) {
            this.wallet_deducted_amount = wallet_deducted_amount;
        }

        public String getPayment_falied_message() {
            return payment_falied_message;
        }

        public void setPayment_falied_message(String payment_falied_message) {
            this.payment_falied_message = payment_falied_message;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getRide_image() {
            return ride_image;
        }

        public void setRide_image(String ride_image) {
            this.ride_image = ride_image;
        }

        public String getTotal_payable_amount() {
            return total_payable_amount;
        }

        public void setTotal_payable_amount(String total_payable_amount) {
            this.total_payable_amount = total_payable_amount;
        }

        public String getBegin_lat() {
            return begin_lat;
        }

        public void setBegin_lat(String begin_lat) {
            this.begin_lat = begin_lat;
        }

        public String getBegin_long() {
            return begin_long;
        }

        public void setBegin_long(String begin_long) {
            this.begin_long = begin_long;
        }

        public String getEnd_lat() {
            return end_lat;
        }

        public void setEnd_lat(String end_lat) {
            this.end_lat = end_lat;
        }

        public String getEnd_long() {
            return end_long;
        }

        public void setEnd_long(String end_long) {
            this.end_long = end_long;
        }
    }
}
