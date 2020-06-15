package com.my.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HourlyForcastAdapter extends RecyclerView.Adapter<HourlyForcastAdapter.ViewHolder> {
    private List<HourlyForcastList> hourlyForcastLists;
    Context context;

    public HourlyForcastAdapter(List<HourlyForcastList> hourlyForcastLists, Context context) {
        this.hourlyForcastLists = hourlyForcastLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forcast_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyForcastList forcast = hourlyForcastLists.get(position);
        holder.hourlyTemp.setText(Math.round(forcast.getHourlyTemp())+"°C");
        holder.hourlyDescription.setText(forcast.getHourlyDescription());
        String timeH = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(forcast.getTime()*1000));
        holder.time.setText(timeH);
        String imageURL = "https://openweathermap.org/img/wn/"+forcast.getHourlyImage()+"@2x.png";
        Glide.with(context)
                .load(imageURL)
                .fitCenter()
                .into(holder.hourlyForcastImage);
    }

    @Override
    public int getItemCount() {
        return hourlyForcastLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView hourlyForcastImage;
        private TextView hourlyTemp, hourlyDescription, time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hourlyForcastImage = itemView.findViewById(R.id.hourlyImage);
            hourlyTemp = itemView.findViewById(R.id.hourlyTemp);
            hourlyDescription = itemView.findViewById(R.id.hourlyDescription);
            time = itemView.findViewById(R.id.hourlyTime);
        }
    }
}
