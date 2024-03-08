package com.example.skywatch.views.ui.favorite.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywatch.Constants
import com.example.skywatch.databinding.FavoriteItemBinding
import com.example.skywatch.helpers.getAddressEnglish
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.LocationLatLngPojo

class FavoriteAdapter(private val itemClick:(LocationLatLngPojo)->Unit, private val delItem:(FavoritePojo)->Unit, private val context: Context) :
    ListAdapter<FavoritePojo, FavoriteAdapter.FavViewHolder>(ProductsDiff())
{
    lateinit var binding : FavoriteItemBinding
    class FavViewHolder(var binding : FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        binding = FavoriteItemBinding.inflate(inflater,parent,false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int)
    {
        var favoritePojo: FavoritePojo = getItem(position)
        holder.binding.favItemTitle.text = getAddressEnglish(context,favoritePojo.lat,favoritePojo.lng)

        holder.binding.favItemDel.setOnClickListener {
            delItem(favoritePojo)
        }
        holder.binding.favItemTitle.setOnClickListener{
            var locationLatLngPojo = LocationLatLngPojo(Constants.FavNavType,favoritePojo.lat,favoritePojo.lng)
            itemClick(locationLatLngPojo)
        }

    }
}

class ProductsDiff: DiffUtil.ItemCallback<FavoritePojo>()
{
    override fun areItemsTheSame(oldItem: FavoritePojo, newItem: FavoritePojo): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: FavoritePojo, newItem: FavoritePojo): Boolean {
        return oldItem==newItem
    }

}