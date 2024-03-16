package com.example.skywatch.views.ui.favorite.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skywatch.Constants
import com.example.skywatch.R
import com.example.skywatch.databinding.FragmentFavoriteBinding
import com.example.skywatch.helpers.NetworkConnectivity
import com.example.skywatch.local.LocalDataSource
import com.example.skywatch.local.SkyWatchDatabase
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.LocationLatLngPojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.models.status.FavoriteStatus
import com.example.skywatch.remote.RemoteDataSource
import com.example.skywatch.remote.RetrofitHelper
import com.example.skywatch.views.ui.favorite.viewModel.FavoriteViewModel
import com.example.skywatch.views.ui.favorite.viewModel.FavoriteViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        favoriteViewModelFactory = FavoriteViewModelFactory(
            WeatherRepo.getInstance(
                RemoteDataSource.getInstance(RetrofitHelper.service),
                LocalDataSource.getInstance(SkyWatchDatabase.getInstance(requireActivity()).SkyWatchDao())
                )
        )
        favoriteViewModel = ViewModelProvider(this , favoriteViewModelFactory)[FavoriteViewModel::class.java]

        val delFavItem:(FavoritePojo)->Unit=
            {pojo->
                delFavSnack(pojo)
            }
        val itemClick:(LocationLatLngPojo)->Unit=
            {
                var homeDirections = FavoriteFragmentDirections.actionNavFavToNavHome()
                homeDirections.setLatLng(it)
                Navigation.findNavController(requireView()).navigate(homeDirections)
            }
        favoriteAdapter = FavoriteAdapter(itemClick,delFavItem,requireActivity())
        layoutManager = LinearLayoutManager(requireActivity())
        return root
    }
    private fun delFavSnack(favoritePojo: FavoritePojo){
        Snackbar.make(requireView(), getString(R.string.ask_del_fav), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.del_item)) {
                favoriteViewModel.delFromFavorites(favoritePojo)
            }.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favProgress.visibility=View.VISIBLE
        binding.favNotFound.visibility = View.GONE
        binding.favRecycler.visibility = View.GONE
        binding.favRecycler.adapter = favoriteAdapter
        binding.favRecycler.layoutManager = layoutManager
        lifecycleScope.launch(Dispatchers.Main) {
            favoriteViewModel.favoriteList.collectLatest {
                when(it)
                {
                    is FavoriteStatus.Loading->{
                        binding.favProgress.visibility=View.VISIBLE
                        binding.favNotFound.visibility = View.GONE
                        binding.favRecycler.visibility = View.GONE
                    }
                    is FavoriteStatus.Success->{
                        if (it.favoriteList.isNullOrEmpty())
                        {
                            binding.favProgress.visibility=View.GONE
                            binding.favNotFound.visibility = View.VISIBLE
                            binding.favRecycler.visibility = View.GONE
                        }
                        else{
                            binding.favProgress.visibility=View.GONE
                            binding.favNotFound.visibility = View.GONE
                            binding.favRecycler.visibility = View.VISIBLE
                            favoriteAdapter.submitList(it.favoriteList)
                        }
                    }
                    is FavoriteStatus.Failure->{
                        binding.favProgress.visibility=View.GONE
                        binding.favNotFound.visibility = View.VISIBLE
                        binding.favRecycler.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.errMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.addToFav.setOnClickListener {
            if (NetworkConnectivity.getInstance(requireActivity().application).isOnline())
            {
                var action:FavoriteFragmentDirections.ActionNavFavToMapsFragment  = FavoriteFragmentDirections.actionNavFavToMapsFragment(Constants.FavNavType)
                Navigation.findNavController(it).navigate(action)
            }else
            {
                Snackbar.make(requireView(), getString(R.string.check_your_connection), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.dismiss)) {
                    }.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}