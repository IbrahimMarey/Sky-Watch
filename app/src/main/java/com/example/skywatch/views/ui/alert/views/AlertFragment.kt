package com.example.skywatch.views.ui.alert.views

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.databinding.FragmentAlertBinding
import com.example.skywatch.views.ui.alert.SlideshowViewModel

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import com.example.skywatch.Constants
import com.example.skywatch.R

private const val CHANNEL_ID = "my_channel_id"
private const val NOTIFICATION_ID = 1

class AlertFragment : Fragment() {

    private lateinit var _binding: FragmentAlertBinding
    lateinit var notificationManager:NotificationManager
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isNotificationPermissionGranted = notificationManager.areNotificationsEnabled()
        if (!isNotificationPermissionGranted) {
            showNotificationPermissionDialog()
        }
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textSlideshow.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Channel Name"
                val descriptionText = "Channel Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }
            sendNotification()
            Log.i("TAG", "onViewCreated: ")
        }
    }

    override fun onResume() {
        super.onResume()
        binding.addToAlert.setOnClickListener{
            val action = AlertFragmentDirections.actionNavAlertToMapsFragment(Constants.AlertNavType)
            Navigation.findNavController(it).navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
    private fun sendNotification()
    {
        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.notification)

        val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("El-Kaff Hena")
            .setContentText("Wal3 Wal3")
            .setLargeIcon(imageBitmap)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
        with(NotificationManagerCompat.from(requireActivity()))
        {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID,builder.build())
        }

    }
    private fun showNotificationPermissionDialog()
    {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.notification_permission_title))
            .setMessage(getString(R.string.notification_permmission_desc))
            .setPositiveButton(getString(R.string.permission_request)) { dialog: DialogInterface, _: Int ->
                // Open app settings to allow notification permission
                openAppSettings()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dismiss)) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .setCancelable(false)

        val dialog = alertDialogBuilder.create()
        dialog.show()
    }
    private fun openAppSettings()
    {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}