package com.careeroinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.lang.Exception
import com.google.android.gms.maps.GoogleMap.OnMapClickListener

class MapsFragment : Fragment() {

    lateinit var mMap: GoogleMap
    lateinit var fragmentView: View
    lateinit var sp: SharedPreference
    var lat: Double = 7.290572
    var lng: Double = 80.633728

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(lat, lng)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Sri Lanka"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap = googleMap
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            //buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
        mMap.uiSettings.isMyLocationButtonEnabled = true;

        mMap.setOnMapClickListener { latLng ->
            lat = latLng.latitude
            lng = latLng.longitude
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Custom Location")
            mMap.clear()
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.addMarker(markerOptions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sp = SharedPreference(requireContext())
        lat = sp.getPreference("info_location_lat").toDouble()
        lng = sp.getPreference("info_location_lng").toDouble()
        fragmentView = inflater.inflate(R.layout.fragment_maps, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val searchButton: Button = fragmentView.findViewById(R.id.searchButton)
        searchButton.setOnClickListener(View.OnClickListener {
            searchLocation()
        })

        val doneButton: Button = fragmentView.findViewById(R.id.doneButton)
        doneButton.setOnClickListener(View.OnClickListener {
            if(activity?.javaClass?.simpleName == "Register")
                (activity as Register?)?.getLocation(lat.toString(), lng.toString())
            else if(activity?.javaClass?.simpleName == "Home")
                (activity as Home?)?.getLocation(lat.toString(), lng.toString())
        })
    }

    fun searchLocation() {
        mMap!!.clear()
        val locationSearch: EditText = fragmentView.findViewById(R.id.mapSearch)
        lateinit var location: String
        location = locationSearch.text.toString()
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(requireContext(),"provide location",Toast.LENGTH_SHORT).show()
        }
        else{
            val geoCoder = Geocoder(requireContext())
            try {
                addressList = geoCoder.getFromLocationName(location, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                val address = addressList!![0]
                lat = address.latitude
                lng = address.longitude
                val latLng = LatLng(address.latitude, address.longitude)
                mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
                Toast.makeText(requireContext(),address.latitude.toString() + " " + address.longitude, Toast.LENGTH_LONG).show()
            } catch (ex: Exception){
                Toast.makeText(requireContext(), "Address not found!", Toast.LENGTH_LONG).show()
            }
        }
    }

}