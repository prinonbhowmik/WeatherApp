package com.my.weatherapp;

public class HourlyForcastList {
    private String hourlyImage;
    private Float hourlyTemp;
    private String hourlyDescription;
    private Long time;

    public HourlyForcastList(String hourlyImage, Float hourlyTemp, String hourlyDescription, Long time) {
        this.hourlyImage = hourlyImage;
        this.hourlyTemp = hourlyTemp;
        this.hourlyDescription = hourlyDescription;
        this.time = time;
    }

    public String getHourlyImage() {
        return hourlyImage;
    }

    public Float getHourlyTemp() {
        return hourlyTemp;
    }

    public String getHourlyDescription() {
        return hourlyDescription;
    }

    public Long getTime() {
        return time;
    }
}
