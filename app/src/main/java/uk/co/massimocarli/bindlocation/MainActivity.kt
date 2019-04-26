package uk.co.massimocarli.bindlocation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import uk.co.massimocarli.bindlocation.databinding.ActivityMainBinding
import uk.co.massimocarli.bindlocation.location.LocationData
import uk.co.massimocarli.bindlocation.location.LocationModel
import uk.co.massimocarli.bindlocation.location.LocationViewModel
import uk.co.massimocarli.bindlocation.location.PermissionRequest

class MainActivity : AppCompatActivity() {

  lateinit var locationViewModel: LocationViewModel
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(
      this,
      R.layout.activity_main
    )
    locationViewModel =
      ViewModelProviders.of(
        this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
      ).get(LocationViewModel::class.java)
    locationViewModel.getLocationLiveData()
      .observe(this, Observer {
        when (it) {
          is PermissionRequest -> requestLocationPermission()
          is LocationData -> displayLocation(it.location)
        }
      })
  }

//  private fun displayLocation(location: Location?) {
//    location?.run {
//      binding.location = LocationModel(location)
//    }
//  }

  private fun displayLocation(location: Location?) {
    binding.location = LocationModel(null)
  }

  companion object {
    const val LOCATION_PERMISSION_REQUEST_ID = 1
    const val REQUIRED_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
  }

  fun requestLocationPermission() {
    if (ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      // We check if we have to provide a reason for the Location permission request
      if (ActivityCompat.shouldShowRequestPermissionRationale(
          this,
          REQUIRED_PERMISSION
        )
      ) {
        // In this case we have to show a Dialog which explain the permission request to the user
        AlertDialog.Builder(this)
          .setTitle(R.string.location_request_dialog_title)
          .setMessage(R.string.location_request_dialog_reason)
          .setPositiveButton(android.R.string.ok) { _, _ ->
            ActivityCompat.requestPermissions(
              this,
              arrayOf(REQUIRED_PERMISSION),
              LOCATION_PERMISSION_REQUEST_ID
            )
          }
          .create()
          .show()
      } else {
        ActivityCompat.requestPermissions(
          this,
          arrayOf(REQUIRED_PERMISSION),
          LOCATION_PERMISSION_REQUEST_ID
        )
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == LOCATION_PERMISSION_REQUEST_ID) {
      // In this case we check if the user has given permission
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // We can't generate the ON_START event so we notify the PermissionLiveDataDecorator that the permission is now
        // available
        locationViewModel.permissionUpdate()
      } else {
        // We cannot use the app so we explain to the user and exit
        AlertDialog.Builder(this)
          .setTitle(R.string.location_request_dialog_title)
          .setMessage(R.string.location_request_dialog_close)
          .setPositiveButton(android.R.string.ok) { _, _ ->
            finish()
          }
          .create()
          .show()
      }
    }
  }
}
