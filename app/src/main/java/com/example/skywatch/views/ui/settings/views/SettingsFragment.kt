package com.example.skywatch.views.ui.settings.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.navigation.Navigation
import com.example.skywatch.Constants
import com.example.skywatch.R
import com.example.skywatch.databinding.FragmentSettingsBinding
import com.example.skywatch.helpers.SettingSharedPreferences
import com.example.skywatch.helpers.changeLanguageLocaleTo
import com.example.skywatch.helpers.getLanguageLocale

class SettingsFragment : Fragment() {
    private lateinit var binding:FragmentSettingsBinding
    var isLocationOpen = false
    var isLanguageOpen = false
    var isTempOpen = false
    var isWindOpen = false
    var isNotificationOpen = false
    private val settingSharedPreferences by lazy {
        SettingSharedPreferences.getInstance(requireActivity().application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        when(getLanguageLocale())
        {
            "ar"-> binding.radioLanguageArabic.toggle()
            "en"->binding.radioLanguageEnglish.toggle()
            "de-rDE"->binding.radioLanguageGerman.toggle()
        }
        /*if (getLanguageLocale() == "ar") {
            binding.radioLanguageArabic.toggle()
        } else {
            binding.radioLanguageEnglish.toggle()
        }*/
        when (settingSharedPreferences.getWindSpeedPref()) {
            SettingSharedPreferences.METER_PER_SECOND -> binding.radioWindMeter.toggle()
            SettingSharedPreferences.MILE_PER_HOUR -> binding.radioWindMile.toggle()
        }

        when (settingSharedPreferences.getLocationPref()) {
            SettingSharedPreferences.GPS -> binding.radioLocationGPs.toggle()
            SettingSharedPreferences.MAP -> binding.radioLocationMap.toggle()
        }

        when (settingSharedPreferences.getTempPref()) {
            SettingSharedPreferences.CELSIUS -> binding.radioTempCelsius.toggle()
            SettingSharedPreferences.KELVIN -> binding.radioTempKelvin.toggle()
            SettingSharedPreferences.FAHRENHEIT -> binding.radioTempFahrenheit.toggle()
        }

        binding.languageGroup.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radioLanguageArabic -> changeLanguageLocaleTo("ar")
                R.id.radioLanguageEnglish -> changeLanguageLocaleTo("en")
                R.id.radioLanguageGerman -> changeLanguageLocaleTo("de-rDE")
            }
        }
        binding.locationGroup.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radioLocationGPs -> {
                    settingSharedPreferences.setLocationPref(
                        SettingSharedPreferences.GPS
                    )
                    requireActivity().recreate()
                }
                R.id.radioLocationMap -> {
                    val action = SettingsFragmentDirections.actionSettingsFragmentToMapsFragment(Constants.SettingNavType)
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }
        binding.windGroup.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radioWindMeter -> settingSharedPreferences.setWindSpeedPref(
                    SettingSharedPreferences.METER_PER_SECOND
                )
                R.id.radioWindMile -> settingSharedPreferences.setWindSpeedPref(
                    SettingSharedPreferences.MILE_PER_HOUR
                )
            }
        }
        binding.tempGroup.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radioTempCelsius -> settingSharedPreferences.setTempPref(
                    SettingSharedPreferences.CELSIUS
                )
                R.id.radioTempKelvin -> settingSharedPreferences.setTempPref(
                    SettingSharedPreferences.KELVIN
                )
                R.id.radioTempFahrenheit -> settingSharedPreferences.setTempPref(
                    SettingSharedPreferences.FAHRENHEIT
                )
            }
        }

    }

    private fun setUpUI()
    {
        setUpLocationItem()
        setUpLanguageItem()
        setUpTemperatureItem()
        setUpWindItem()
        setUpNotificationItem()
    }
    private fun setUpLocationItem(){
        binding.locationGroup.visibility = View.GONE
        binding.icLocationShowMore.setOnClickListener {
            if (isLocationOpen)
            {
                isLocationOpen = false
                binding.locationGroup.visibility = View.GONE
                binding.icLocationShowMore.setImageResource(R.drawable.ic_show_more)
            }else
            {
                isLocationOpen = true
                binding.locationGroup.visibility = View.VISIBLE
                binding.icLocationShowMore.setImageResource(R.drawable.ic_show_less)
            }
        }
    }
    private fun setUpLanguageItem(){
        binding.languageGroup.visibility = View.GONE
        binding.icLanguageShowMore.setOnClickListener {
            if (isLanguageOpen)
            {
                isLanguageOpen = false
                binding.languageGroup.visibility = View.GONE
                binding.icLanguageShowMore.setImageResource(R.drawable.ic_show_more)
            }else
            {
                isLanguageOpen = true
                binding.languageGroup.visibility = View.VISIBLE
                binding.icLanguageShowMore.setImageResource(R.drawable.ic_show_less)
            }
        }
    }
    private fun setUpTemperatureItem(){
        binding.tempGroup.visibility = View.GONE
        binding.icTempShowMore.setOnClickListener {
            if (isTempOpen)
            {
                isTempOpen = false
                binding.tempGroup.visibility = View.GONE
                binding.icTempShowMore.setImageResource(R.drawable.ic_show_more)
            }else
            {
                isTempOpen = true
                binding.tempGroup.visibility = View.VISIBLE
                binding.icTempShowMore.setImageResource(R.drawable.ic_show_less)
            }
        }
    }
    private fun setUpWindItem(){
        binding.windGroup.visibility = View.GONE
        binding.icWindShowMore.setOnClickListener {
            if (isWindOpen)
            {
                isWindOpen = false
                binding.windGroup.visibility = View.GONE
                binding.icWindShowMore.setImageResource(R.drawable.ic_show_more)
            }else
            {
                isWindOpen = true
                binding.windGroup.visibility = View.VISIBLE
                binding.icWindShowMore.setImageResource(R.drawable.ic_show_less)
            }
        }
    }
    private fun setUpNotificationItem(){
        binding.notificationGroup.visibility = View.GONE
        binding.icNotifictionShowMore.setOnClickListener {
            if (isNotificationOpen)
            {
                isNotificationOpen = false
                binding.notificationGroup.visibility = View.GONE
                binding.icNotifictionShowMore.setImageResource(R.drawable.ic_show_more)
            }else
            {
                isNotificationOpen = true
                binding.notificationGroup.visibility = View.VISIBLE
                binding.icNotifictionShowMore.setImageResource(R.drawable.ic_show_less)
            }
        }
    }
}