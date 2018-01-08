package com.ide.customer.models;

import java.util.List;

/**
 * Created by lenovo-pc on 6/23/2017.
 */

public class CarTypeResponseModel {


    /**
     * status : 1
     * currency : 0930
     * currency_iso_code : AUD
     * currency_unicode : 0024
     * message : Home Screens Cars
     * details : [{"car_type_id":"2","car_type_name":"HATCHBACK","car_type_image":"uploads/car/editcar_2.png","city_id":"56","base_fare":"100 Per 4 Miles","ride_mode":"1","currency_iso_code":"AUD","currency_unicode":"0024"},{"car_type_id":"3","car_type_name":"LUXURY","car_type_image":"uploads/car/editcar_3.png","city_id":"56","base_fare":"100 Per 4 Miles","ride_mode":"1","currency_iso_code":"AUD","currency_unicode":"0024"},{"car_type_id":"4","car_type_name":"Mini","car_type_image":"uploads/car/editcar_4.png","city_id":"56","base_fare":"178 Per 4 Miles","ride_mode":"1","currency_iso_code":"AUD","currency_unicode":"0024"},{"car_type_id":"001","car_type_name":"Rental","car_type_image":"uploads/car/editcar_2.png","city_id":"56","distance":"","base_fare":"","ride_mode":"2"}]
     */

    private int status;
    private String currency;
    private String currency_iso_code;
    private String currency_unicode;
    private String message;
    private List<DetailsBean> details;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency_iso_code() {
        return currency_iso_code;
    }

    public void setCurrency_iso_code(String currency_iso_code) {
        this.currency_iso_code = currency_iso_code;
    }

    public String getCurrency_unicode() {
        return currency_unicode;
    }

    public void setCurrency_unicode(String currency_unicode) {
        this.currency_unicode = currency_unicode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * car_type_id : 2
         * car_type_name : HATCHBACK
         * car_type_image : uploads/car/editcar_2.png
         * city_id : 56
         * base_fare : 100 Per 4 Miles
         * ride_mode : 1
         * currency_iso_code : AUD
         * currency_unicode : 0024
         * distance :
         */

        private String car_type_id;
        private String car_type_name;
        private String car_type_image;
        private String city_id;
        private String base_fare;
        private String ride_mode;
        private String currency_iso_code;
        private String currency_unicode;
        private String distance;

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

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getBase_fare() {
            return base_fare;
        }

        public void setBase_fare(String base_fare) {
            this.base_fare = base_fare;
        }

        public String getRide_mode() {
            return ride_mode;
        }

        public void setRide_mode(String ride_mode) {
            this.ride_mode = ride_mode;
        }

        public String getCurrency_iso_code() {
            return currency_iso_code;
        }

        public void setCurrency_iso_code(String currency_iso_code) {
            this.currency_iso_code = currency_iso_code;
        }

        public String getCurrency_unicode() {
            return currency_unicode;
        }

        public void setCurrency_unicode(String currency_unicode) {
            this.currency_unicode = currency_unicode;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}
