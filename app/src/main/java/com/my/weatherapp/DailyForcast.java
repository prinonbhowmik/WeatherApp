package com.my.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DailyForcast {
    @SerializedName("dt")
    public long dt;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
    @SerializedName("temp")
    public DailyTemp dailyTemps;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("wind_speed")
    public float wind_speed;
    @SerializedName("clouds")
    public int clouds;
    @SerializedName("dew_point")
    public float dew_point;
    @SerializedName("weather")
    public ArrayList<DailyWeatherForcast> dailyWeatherForcasts = new ArrayList<DailyWeatherForcast>();
}
