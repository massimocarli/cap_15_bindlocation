package uk.co.massimocarli.bindlocation.location

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(val app: Application) : AndroidViewModel(app) {

  private lateinit var startedLiveData: LocationLiveData

  fun getLocationLiveData(): LocationLiveData {
    if (!::startedLiveData.isInitialized) {
      val locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      startedLiveData = LocationLiveData(app, locationManager)
    }
    return startedLiveData
  }

  fun permissionUpdate() {
    startedLiveData.permissionUpdate()
  }
}