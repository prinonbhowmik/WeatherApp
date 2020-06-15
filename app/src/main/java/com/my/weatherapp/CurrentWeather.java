package com.my.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrentWeather {
    @SerializedName("dt")
    public float dt;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
    @SerializedName("temp")
    public float temp;
    @SerializedName("feels_like")
    public float feels_like;
    @SerializedName("pressure")
    public long pressure;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("dew_point")
    public float dew_point;
    @SerializedName("uvi")
    public float uvi;
    @SerializedName("visibility")
    public long visibility;
    @SerializedName("clouds")
    public int clouds;
    @SerializedName("wind_speed")
    public float wind_speed;
    @SerializedName("wind_gust")
    public float wind_gust;
    @SerializedName("wind_deg")
    public int wind_deg;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<Weather>();
}
