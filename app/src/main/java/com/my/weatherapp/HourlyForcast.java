package com.my.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HourlyForcast {
    @SerializedName("dt")
    public long dt;
    @SerializedName("temp")
    public Float temp;
    @SerializedName("weather")
    public ArrayList<HourlyWeatherForcast> hourlyWeatherForcasts = new ArrayList<HourlyWeatherForcast>();
}
