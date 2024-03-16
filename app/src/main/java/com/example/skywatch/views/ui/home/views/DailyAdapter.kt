package com.example.skywatch.views.ui.home.views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywatch.R
import com.example.skywatch.databinding.DailyItemBinding
import com.example.skywatch.helpers.getWeatherImg
import com.example.skywatch.helpers.getWeatherLottie
import com.example.skywatch.helpers.setDay
import com.example.skywatch.helpers.setImageFromWeatherIconId2x
import com.example.skywatch.helpers.setTemp
import com.example.skywatch.models.DailyItem
import java.util.TimeZone


class DailyAdapter(var timeZone: TimeZone,var dailyClick:(DailyItem)->Unit,var recyclerView: RecyclerView) : ListAdapter<DailyItem, DailyAdapter.ViewHolder>(DiffUtils) {
    class ViewHolder(val binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daily = getItem(position)

        holder.binding.flagCard.setOnClickListener {
            dailyClick(daily)
            for (i in 0 until   itemCount)
            {
                val otherHolder = recyclerView.findViewHolderForAdapterPosition(i) as? ViewHolder
                if (otherHolder != null /*&& otherHolder != holder*/) {
                    otherHolder.binding.dailyFlag.setImageResource(R.drawable.flag)
                }
            }
            holder.binding.dailyFlag.setImageResource(R.drawable.selected_flag)
            /*val selectedColor = ContextCompat.getColor(holder.itemView.context, R.color.app_color)
            val defaultColor = Color.WHITE

            holder.binding.dailyFlag.setImageResource(R.drawable.cloud)
            // Set clicked card background color
            holder.binding.flagCard.setBackgroundColor(selectedColor)
            holder.binding.flagCard.radius = 16.0f
            // Set background color of other cards
            for (i in 0 until   itemCount) {
                val otherHolder = recyclerView.findViewHolderForAdapterPosition(i) as? ViewHolder
                if (otherHolder != null && otherHolder != holder) {
                    otherHolder.binding.flagCard.setBackgroundColor(defaultColor)
                }
            }*/
        }

        if (position == 0) {
            holder.binding.textViewDay.text = holder.binding.root.context.getString(R.string.today)
            holder.binding.dailyFlag.setImageResource(R.drawable.selected_flag)
            /*val color = ContextCompat.getColor(holder.itemView.context, R.color.app_color)
            holder.binding.flagCard.setBackgroundColor(color)*/
        } else {
            daily.dt?.let { holder.binding.textViewDay.setDay(it, timeZone) }
        }

        holder.binding.imgDaily.setImageResource(getWeatherImg(daily?.weather?.get(0)?.icon ?: "null"))
        holder.binding.textViewTempDegree.setTemp(
            daily.temp?.day?.toInt() ?: -1,
            holder.itemView.context
        )
    }
/*
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daily = getItem(position)
        holder.binding.flagCard.setOnClickListener{
            dailyClick(daily)
            val color = ContextCompat.getColor(holder.itemView.context, R.color.app_color)
            holder.binding.flagCard.setBackgroundColor(color)

        }
        if (position == 0) {
            holder.binding.textViewDay.text = holder.binding.root.context.getString(R.string.today)
            val color = ContextCompat.getColor(holder.itemView.context, R.color.app_color)
            holder.binding.flagCard.setBackgroundColor(color)
        } else {
            daily.dt?.let { holder.binding.textViewDay.setDay(it,timeZone) }
        }
        holder.binding.imgDaily.setImageResource(getWeatherImg(daily?.weather?.get(0)?.icon?:"null"))
        holder.binding.textViewTempDegree.setTemp(
            daily.temp?.day?.toInt()?:-1,
            holder.itemView.context
        )
    }*/

    object DiffUtils : DiffUtil.ItemCallback<DailyItem>() {
        override fun areItemsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
            return oldItem == newItem
        }

    }
}