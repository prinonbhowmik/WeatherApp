package com.my.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("lat")
    public float lat;

    @SerializedName("lon")
    public float lon;

    @SerializedName("current")
    public CurrentWeather currentWeather;

    @SerializedName("hourly")
    public ArrayList<HourlyForcast> hourlyWeather = new ArrayList<HourlyForcast>();

    @SerializedName("daily")
    public ArrayList<DailyForcast> dailyForcasts = new ArrayList<DailyForcast>();
}

