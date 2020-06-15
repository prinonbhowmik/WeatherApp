package com.my.weatherapp;

public class ApiUtils {

    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    public static ApiInterface getUserService(){

        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
