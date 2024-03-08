package com.example.skywatch.views.ui.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywatch.R
import com.example.skywatch.databinding.HourItemBinding
import com.example.skywatch.helpers.getWeatherImg
import com.example.skywatch.helpers.getWeatherLottie
import com.example.skywatch.helpers.setImageFromWeatherIconId2x
import com.example.skywatch.helpers.setTemp
import com.example.skywatch.helpers.setTime
import com.example.skywatch.models.HourlyItem
import java.util.TimeZone


class HourlyAdapter(var timeZone: TimeZone) : ListAdapter<HourlyItem, HourlyAdapter.ViewHolder>(DiffUtils) {
    class ViewHolder(val binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourly = getItem(position)
        if (position == 0) {
            holder.binding.hourTvHourItem.text = holder.binding.root.context.getString(R.string.now)
        } else {
            holder.binding.hourTvHourItem.setTime(hourly.dt,timeZone)
        }
        holder.binding.tempTvHourItem.setTemp(hourly.temp?.toInt() ?:-1,holder.itemView.context)
        holder.binding.imgHourly.setImageResource(getWeatherImg(hourly?.weather?.get(0)?.icon?:"null"))
//        holder.binding.lottieHourly.setAnimation(getWeatherLottie(hourly?.weather?.get(0)?.icon?:"null"))
    }

    object DiffUtils : DiffUtil.ItemCallback<HourlyItem>() {
        override fun areItemsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
            return oldItem == newItem
        }

    }
}
/*
* override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val hourly = getItem(position)
    if (position == 0) {
        holder.binding.hourTvHourItem.text = holder.binding.root.context.getString(R.string.now)
    } else {
        holder.binding.hourTvHourItem.setTime(hourly.dt, timeZone)
    }
    holder.binding.tempTvHourItem.setTemp(hourly.temp?.toInt() ?: -1, holder.itemView.context)

    val animation = if (hourly.weather != null && hourly.weather.isNotEmpty() && hourly.weather[0].icon != null) {
        getWeatherLottie(hourly.weather[0].icon)
    } else {
        getWeatherLottie("default") // Replace "default" with your default animation name
    }

    try {
        holder.binding.lottieHourly.setAnimation(animation)
    } catch (e: JsonDataException) {
        // Handle the exception gracefully
        e.printStackTrace()
        // Set a default animation or handle the error as per your app requirements
        holder.binding.lottieHourly.setAnimation(getWeatherLottie("default"))
    }
}*/