package com.my.weatherapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("onecall")
    Call<WeatherResponse> getWeather(@Query("lat") Double lat,
                                     @Query("lon") Double lon,
                                     @Query("units") String units,
                                     @Query("appid")String api);
}
