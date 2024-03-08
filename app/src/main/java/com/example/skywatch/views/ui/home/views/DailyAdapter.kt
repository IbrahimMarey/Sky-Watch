package com.example.skywatch.views.ui.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
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


class DailyAdapter(var timeZone: TimeZone) : ListAdapter<DailyItem, DailyAdapter.ViewHolder>(DiffUtils) {
    class ViewHolder(val binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daily = getItem(position)

        if (position == 0) {
            holder.binding.textViewDay.text = holder.binding.root.context.getString(R.string.today)
        } else {
            daily.dt?.let { holder.binding.textViewDay.setDay(it,timeZone) }
        }

        holder.binding.imgDaily.setImageResource(getWeatherImg(daily?.weather?.get(0)?.icon?:"null"))
//        holder.binding.lottieDaily.setAnimation(getWeatherLottie(daily?.weather?.get(0)?.icon?:"null"))
        /*daily?.weather?.get(0)?.icon?.let {
            holder.binding.imageViewWeatherIcon.setImageFromWeatherIconId2x(
                it
            )
        }*/
//        holder.binding.textViewTempDegree.text =
//            buildString {
//                append(daily.temp.day.roundToInt())
//                append(holder.binding.root.context.getString(R.string.degree))
//            }

        holder.binding.textViewTempDegree.setTemp(
            daily.temp?.day?.toInt()?:-1,
            holder.itemView.context
        )
    }

    object DiffUtils : DiffUtil.ItemCallback<DailyItem>() {
        override fun areItemsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
            return oldItem == newItem
        }

    }
}