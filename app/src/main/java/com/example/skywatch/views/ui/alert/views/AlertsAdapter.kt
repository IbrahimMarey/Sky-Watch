package com.example.skywatch.views.ui.alert.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywatch.databinding.AlarmItemBinding
import com.example.skywatch.helpers.setDate
import com.example.skywatch.helpers.setTime
import com.example.skywatch.models.AlertPojo

class AlertsAdapter(private val removeClickListener: RemoveClickListener) : ListAdapter<AlertPojo, AlertsAdapter.ViewHolder>(DiffUtils) {
    class ViewHolder(val binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alertWeatherData = getItem(position)

        holder.binding.imageViewDelete.setOnClickListener{
            removeClickListener.onRemoveClick(alertWeatherData)
        }

        holder.binding.textViewEndDate.setDate(alertWeatherData.end)
        holder.binding.textViewEndTime.setTime(alertWeatherData.end)

    }



    object DiffUtils : DiffUtil.ItemCallback<AlertPojo>() {
        override fun areItemsTheSame(oldItem: AlertPojo, newItem: AlertPojo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlertPojo, newItem: AlertPojo): Boolean {
            return oldItem == newItem
        }

    }

    class RemoveClickListener(val removeClickListener : (AlertPojo) -> Unit){
        fun onRemoveClick(alertEntity: AlertPojo) = removeClickListener(alertEntity)
    }
}