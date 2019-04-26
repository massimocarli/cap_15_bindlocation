package uk.co.massimocarli.bindlocation.location

import android.location.Location

data class LocationModel(val location: Location?) {
  //  val asText: String
//    get() =
//      if (location != null) {
//        "Lat: ${location.latitude} Long: ${location.longitude}"
//      } else {
//        "EMPTY"
//      }

//  companion object {
//    val DEFAULT_TEXT = "EMPTY"
//  }

  val asText: String?
    get() = location?.run { "Lat: ${latitude} Long: ${longitude}" }

  val DEFAULT_TEXT = "EMPTY"

  val DEFAULT_INT = 0
}