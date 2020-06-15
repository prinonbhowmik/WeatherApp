package com.my.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androdocs.httprequest.HttpRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String API = "618e3a096dcd96b86ffa64b35ef140e1";

    private FusedLocationProviderClient mFusedLocationClient;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, feels_like, windTxt, pressureTxt, humidityTxt, sunRise,
            sunSet, dewPoint, cloudness, visibility, uvi;
    private RecyclerView hourlyRecycleView;
    private ProgressBar progressBar;
    private HourlyForcastAdapter hourlyForcastAdapter;
    private List<HourlyForcastList> hourlyForcastLists;
    private RelativeLayout mainContainer;
    private SwipeRefreshLayout refreshLayout;
    private LottieAnimationView loadinWeather;

    private ApiInterface api;
    private StringBuilder stringBuilder;

    private boolean isContinue = false;
    private boolean isGPS = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            findweather(wayLatitude, wayLongitude);
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                            Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        hourlyForcastLists = new ArrayList<>();
        hourlyRecycleView = findViewById(R.id.hourlyForcastRecycleView);
        hourlyForcastAdapter = new HourlyForcastAdapter(hourlyForcastLists, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hourlyRecycleView.setLayoutManager(layoutManager);
        hourlyRecycleView.setAdapter(hourlyForcastAdapter);

        if (!isGPS) {
            Toast.makeText(this, "Plese turn On GPS!", Toast.LENGTH_SHORT).show();        }
        isContinue = false;
        getLocation();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            findweather(wayLatitude, wayLongitude);
                        } else {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    }
                });
            }
        }
    }

    private void findweather(final double lat, final double lon) {

        Call<WeatherResponse> call = api.getWeather(lat, lon, "metric", API);
        call.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        Toast.makeText(MainActivity.this, "Can't get weather data!", Toast.LENGTH_SHORT).show();
                    } else {
                        WeatherResponse weatherResponse = response.body();
                        String address = "";
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                        List<Address> addresses;

                        try {
                            addresses = geocoder.getFromLocation(lat, lon, 1);
                            if (addresses.size() > 0) {
                                address = addresses.get(0).getLocality();
                                addressTxt.setText(address);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        //  addressTxt.setText(weatherResponse.name+","+weatherResponse.sys.country);
                        float updatedAt = weatherResponse.currentWeather.dt;
                        // Date time  = new Date(updatedAt.longValue());

                        String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date((long) updatedAt * 1000));
                        updated_atTxt.setText(updatedAtText);

                        String riseTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weatherResponse.currentWeather.sunrise * 1000));
                        sunRise.setText(riseTime);
                        String setTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weatherResponse.currentWeather.sunset * 1000));
                        sunSet.setText(setTime);


                        statusTxt.setText(weatherResponse.currentWeather.weather.get(0).description);
                        tempTxt.setText(Math.round(weatherResponse.currentWeather.temp) + "°C");
                        feels_like.setText("Feels Like: " + Math.round(weatherResponse.currentWeather.feels_like) + "°C");
                        windTxt.setText(Math.round(weatherResponse.currentWeather.wind_speed) + " Meter/Sec");
                        pressureTxt.setText(String.valueOf(weatherResponse.currentWeather.pressure));
                        humidityTxt.setText(Math.round(weatherResponse.currentWeather.humidity) + "%");

                        dewPoint.setText(Math.round(weatherResponse.currentWeather.dew_point) + "°C");
                        cloudness.setText(weatherResponse.currentWeather.clouds + "%");
                        float v = (float) weatherResponse.currentWeather.visibility / 1000;
                        visibility.setText(v + " km");
                        if ((weatherResponse.currentWeather.uvi >= 0) && (weatherResponse.currentWeather.uvi <= 2)) {
                            uvi.setText("Low");
                        } else if ((weatherResponse.currentWeather.uvi >= 3) && (weatherResponse.currentWeather.uvi <= 5)) {
                            uvi.setText("Moderate");
                        } else if ((weatherResponse.currentWeather.uvi >= 6) && (weatherResponse.currentWeather.uvi <= 7)) {
                            uvi.setText("High");
                        } else if ((weatherResponse.currentWeather.uvi >= 8) && (weatherResponse.currentWeather.uvi <= 10)) {
                            uvi.setText("Very high");
                        } else if (weatherResponse.currentWeather.uvi >= 11) {
                            uvi.setText("Extreme");
                        }
                        hourlyForcastLists.clear();
                        for (int i = 1; i <= 24; i++) {
                            HourlyForcastList forcastList = new HourlyForcastList(
                                    weatherResponse.hourlyWeather.get(i).hourlyWeatherForcasts.get(0).icon,
                                    weatherResponse.hourlyWeather.get(i).temp,
                                    weatherResponse.hourlyWeather.get(i).hourlyWeatherForcasts.get(0).description,
                                    weatherResponse.hourlyWeather.get(i).dt);
                            hourlyForcastLists.add(forcastList);
                            hourlyForcastAdapter.notifyDataSetChanged();
                        }
                        mainContainer.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }

    private void init() {

        mainContainer = findViewById(R.id.mainContainer);
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        feels_like = findViewById(R.id.feels_like);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        sunRise = findViewById(R.id.sunrise);
        sunSet = findViewById(R.id.sunset);
        dewPoint = findViewById(R.id.dewPoint);
        cloudness = findViewById(R.id.cloudiness);
        visibility = findViewById(R.id.visibility);
        uvi = findViewById(R.id.uv);
        api = ApiUtils.getUserService();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
    }

}

