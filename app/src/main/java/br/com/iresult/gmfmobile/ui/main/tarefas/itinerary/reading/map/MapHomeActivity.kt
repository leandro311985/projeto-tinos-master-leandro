package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import br.com.iresult.gmfmobile.utils.MapUtils
import br.com.iresult.gmfmobile.utils.Utils
import br.com.iresult.gmfmobile.utils.Utils.Companion.bitmapDescriptorFromVector
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.aprilapps.easyphotopicker.EasyImage
import pub.devrel.easypermissions.EasyPermissions

class MapHomeActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    val viewModel: MapHomeViewModel by viewModel()

    private lateinit var mMap: GoogleMap
    private var location: Ligacao? = null
    private var userMarker: Marker? = null
    private var targetLocation: LatLng? = null
    private var targetMarker: Marker? = null
    private var currentLocation: Location? = null
    private var googleApiClient: GoogleApiClient? = null
    private lateinit var mapUtils: MapUtils
    private val polylines: ArrayList<Polyline> = arrayListOf()

    companion object {
        const val EXTRA_LIGACAO = "EXTRA_LIGACAO"
        const val REQUEST_PERMISSIONS = 1
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_home)
        btClose.setOnClickListener { finish() }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.initViewModel(getString(R.string.google_maps_key))
        viewModel.getState().observe(this, Observer { onStateChange(it) })

        btRoute.setOnClickListener {
            location?.let {
                val gmmIntentUri = Uri.parse("google.navigation:q=${it.latitude},${it.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    private fun startObserver() {

        viewModel.address.observe(this, Observer { ligacao ->

            this@MapHomeActivity.location = ligacao
            val lat = ligacao.latitude
            val lng = ligacao.longitude

            targetLocation = LatLng(lat!!, lng!!)

            moveCamera()

            targetLocation?.let { targetLocation ->
                val options = MarkerOptions()
                        .position(targetLocation)
                        .title("${ligacao.nomeCliente}")
                        .snippet("Matrícula: ${ligacao.numReg}\nHid: ${ligacao.numeroMedidor}||...")
                        .visible(true)

                targetMarker = mMap.addMarker(options)
                targetMarker?.showInfoWindow()
            }

            requestPermissionLocation(mMap)
        })
    }

    @SuppressLint("MissingPermission")
    private fun requestPermissionLocation(googleMap: GoogleMap) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            startGoogleApiClient()
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "GMF deseja utilizar sua localização",
                    REQUEST_PERMISSIONS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapUtils = MapUtils(this@MapHomeActivity, googleMap)
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setInfoWindowAdapter(MapHomeInfoWindowAdapter(this@MapHomeActivity, false))
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(-23.546896, -46.694400), 15f)))

        intent.extras?.apply {
            val ligacao: Ligacao = getParcelable(EXTRA_LIGACAO) ?: return
            viewModel.geocodeAddress(ligacao.getCompleteAddress(), ligacao)
        }

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.silver_style))
        startObserver()
    }

    private fun boundMap(location: Location) {
        Log.e("boundMap", "")
        if (userMarker == null) {
            val userPos = LatLng(location.latitude, location.longitude)
            userMarker = mMap.addMarker(MarkerOptions()
                    .position(userPos)
                    .icon(bitmapDescriptorFromVector(this@MapHomeActivity,
                            R.drawable.ic_profile_menu)))

            currentLocation?.let { onLocationChanged(it) } ?: moveCamera()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.e("MapHomeActivity", "Permissao não concedida")
    }

    @SuppressLint("MissingPermission")
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startGoogleApiClient()
    }

    private fun startGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
            googleApiClient?.connect()
        } else if (googleApiClient?.isConnected == true) {
            onConnected(null)
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        googleApiClient?.let {
            if (it.isConnected) {
                it.disconnect()
            }
        }
    }

    override fun onDestroy() {
        EasyImage.clearConfiguration(this)
        googleApiClient?.let {
            if (it.isConnected) {
                it.disconnect()
            }
        }
        super.onDestroy()
    }

    override fun onLocationChanged(location: Location?) {
        polylines.forEach { it.remove() }
        polylines.clear()
        location?.let { position ->
            currentLocation = position
            boundMap(location)
            userMarker?.let { userMarker ->
                targetLocation?.let { targetLocation ->
                    userMarker.position = LatLng(position.latitude, position.longitude)
                    mapUtils.fetchRoutesAndGetDuration(userMarker.position, targetLocation) { polyline, duration ->
                        polylines.add(polyline)
                        var durationMin = duration / 60
                        if (durationMin == 0) {
                            durationMin = 1
                        }

                        targetMarker?.snippet = "${targetMarker?.snippet?.split("||")?.firstOrNull()}||$durationMin min"
                        targetMarker?.hideInfoWindow()
                        targetMarker?.showInfoWindow()
                        moveCamera()
                    }
                }
            }
        }
    }

    private fun moveCamera() {

        val builder = LatLngBounds.Builder()
        userMarker?.let { builder.include(it.position) }
        targetLocation?.let { builder.include(it) }
        val bounds = builder.build()

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 400))
        } catch (e: Exception) {
            Log.e("MAP", "onMapReady: ", e)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

        if (currentLocation != null) {
            onLocationChanged(currentLocation)
        }

        getLocation()
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun getLocation() {

        if (Utils.isLocationEnabled(this)) {

            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ContextCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                try {

                    val criteria = Criteria()
                    criteria.isAltitudeRequired = false
                    criteria.isBearingRequired = false
                    criteria.isCostAllowed = false
                    criteria.powerRequirement = Criteria.POWER_LOW
                    criteria.accuracy = Criteria.ACCURACY_FINE
                    criteria.accuracy = Criteria.ACCURACY_COARSE
                    val providerCoarse = manager.getBestProvider(criteria, true)
                    manager.requestLocationUpdates(providerCoarse, 100, 100f, this@MapHomeActivity)

                } catch (e: Exception) {
                    Log.e("MAP", "localizacao: ", e)
                }

            }

        } else {

            val builder = android.app.AlertDialog.Builder(this)
            builder.setMessage("Olá, a usa localização está desabilitada, gostaria de habilitar?")
            builder.setPositiveButton("Sim") { dialog, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                dialog.dismiss()
            }
            builder.setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    private fun onStateChange(state: MapHomeViewModel.State) {

        when (state) {
            MapHomeViewModel.State.LOADING -> {
                progress.visibility = View.VISIBLE
            }
            MapHomeViewModel.State.SUCCESS -> {
                progress.visibility = View.GONE
            }
            MapHomeViewModel.State.ERROR -> {
                progress.visibility = View.GONE
                val dialog = SimpleDialog(this@MapHomeActivity, getString(R.string.map_error))
                dialog.setDismissListener { finish() }
                dialog.show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                var permissionCount = 0
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        permissionCount++
                    }
                }

                if (permissionCount == 2) {
                    startGoogleApiClient()
                }
            }
        }
    }
}
