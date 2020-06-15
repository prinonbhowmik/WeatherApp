package com.my.weatherapp;

import com.google.gson.annotations.SerializedName;

public class HourlyWeatherForcast {
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;
}
