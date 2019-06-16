package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Address
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraActivity
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.map.MapHomeInfoWindowAdapter
import br.com.iresult.gmfmobile.ui.widget.HomeStatus
import br.com.iresult.gmfmobile.ui.widget.HomeStatusView
import br.com.iresult.gmfmobile.utils.BaseAdapter
import br.com.iresult.gmfmobile.utils.MapUtils
import br.com.iresult.gmfmobile.utils.Utils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.activity_maps_home.*
import kotlinx.android.synthetic.main.fragment_adresses_bottom_sheet.*
import kotlinx.android.synthetic.main.rua_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.aprilapps.easyphotopicker.EasyImage
import pub.devrel.easypermissions.EasyPermissions

class MapsHomeActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    val viewModel: MapsHomeViewModel by viewModel()

    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var adapterRuas: BaseAdapter<Address>
    private lateinit var adapterImoveis: BaseAdapter<Ligacao>
    private lateinit var mapUtils: MapUtils

    private val markers: ArrayList<Marker> = arrayListOf()
    private val hashMarkers: HashMap<String, Marker> = hashMapOf()
    private val reverseHashMarkers: HashMap<Marker, Ligacao> = hashMapOf()
    private val streetLimits: HashMap<String, Pair<LatLng, LatLng>> = hashMapOf()
    private val polylines: ArrayList<Polyline> = arrayListOf()

    private var currentLocation: Location? = null
    private var googleApiClient: GoogleApiClient? = null
    private var userMarker: Marker? = null
    private var streetPoints: ArrayList<LatLng> = arrayListOf()

    companion object {
        const val EXTRA_BUNDLE_TASK_NAME = "EXTRA_BUNDLE_TASK_NAME"
        const val REQUEST_PERMISSIONS = 1
        const val STATUS_CONCLUIDO = "CONCLUIDO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_home)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.initViewModel(getString(R.string.google_maps_key))
        viewModel.getState().observe(this, Observer { onStateChange(it) })

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isHideable = false

        val childLayoutParams = bottomSheet.layoutParams
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        childLayoutParams.height = (displayMetrics.heightPixels * .8).toInt()
        bottomSheet.layoutParams = childLayoutParams

        backButton.setOnClickListener {
            intent.extras?.getString(EXTRA_BUNDLE_TASK_NAME)?.let { roteiro ->
                loadRuas(roteiro)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            btRoute.visibility = View.GONE
        }

        btClose.setOnClickListener { finish() }

        recyclerViewRuas.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(this@MapsHomeActivity)
            adapterRuas = BaseAdapter(R.layout.rua_item) { location: Address, itemView ->
                itemView.nomeRua.text = location.nome
                itemView.cepRua.text = location.formattedAddress()
                itemView.setOnClickListener {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    loadImoveis(location.nome)
                }
            }
            recyclerView.adapter = adapterRuas
        }

        recyclerViewImoveis.let { recyclerView ->
            recyclerView.layoutManager = GridLayoutManager(this@MapsHomeActivity, 3, RecyclerView.VERTICAL, false)
            adapterImoveis = BaseAdapter(R.layout.item_home) { home: Ligacao, homeStatusView ->

                (homeStatusView as HomeStatusView).let { homeView ->
                    homeView.tvNumberHome.text = home.numero
                    homeView.setupLayout(home.status?.let { status -> HomeStatus.valueOf(status) }
                            ?: run { HomeStatus.NONE })
                    homeView.setOnClickListener {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        hashMarkers[home.numeroMedidor]?.let { marker ->
                            marker.showInfoWindow()
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 20f), 250, null)
                        }
                    }
                }
            }
            recyclerView.adapter = adapterImoveis
        }
    }

    private fun startObservers() {

        viewModel.streets.observe(this, Observer { streets ->

            markers.forEach { it.remove() }
            markers.clear()
            reverseHashMarkers.clear()
            mMap.setInfoWindowAdapter(MapHomeInfoWindowAdapter(this@MapsHomeActivity, false))

            val options: ArrayList<MarkerOptions> = arrayListOf()
            val addressesWithLocation: ArrayList<Address> = arrayListOf()
            val streetList: ArrayList<Pair<Address, Address>> = arrayListOf()

            streets.forEach { addresses ->

                addresses.first?.let { firstAddress ->
                    val startLatLng = LatLng(firstAddress.latitude!!, firstAddress.longitude!!)

                    addressesWithLocation.add(firstAddress)

                    options.add(MarkerOptions()
                            .position(startLatLng)
                            .title("${firstAddress.nome}, ${firstAddress.bairro}")
                            .snippet("${firstAddress.ligacoes?.size
                                    ?: 0} casa(s)||${firstAddress.cep}")
                            .zIndex(2f)
                            .visible(true))

                    addresses.second?.let { secondAddress ->

                        val endLatLng = LatLng(secondAddress.latitude!!, secondAddress.longitude!!)
                        streetList.add(Pair(firstAddress, secondAddress))
                        streetPoints.add(LatLng(firstAddress.latitude, firstAddress.longitude))
                        streetPoints.add(LatLng(secondAddress.latitude, secondAddress.longitude))
                        streetLimits.put(secondAddress.nome, Pair(startLatLng, endLatLng))
                    }
                }

                return@forEach
            }

            processaStreetPolylines(streetList)
            options.forEach { markers.add(mMap.addMarker(it)) }
            moveCamera()
            adapterRuas.setItems(addressesWithLocation)
        })

        viewModel.ligacao.observe(this, Observer { ligacoes ->

            markers.forEach { it.remove() }
            markers.clear()
            hashMarkers.clear()
            reverseHashMarkers.clear()
            mMap.setInfoWindowAdapter(MapHomeInfoWindowAdapter(this@MapsHomeActivity, true))

            val ligacoesWithLocation: ArrayList<Ligacao> = arrayListOf()

            ligacoes.forEach { ligacao ->

                ligacoesWithLocation.add(ligacao.copy(latitude = ligacao.latitude, longitude = ligacao.longitude))

                val markerOptions = MarkerOptions()
                        .position(LatLng(ligacao.latitude!!, ligacao.longitude!!))
                        .title("${ligacao.nomeCliente}")
                        .snippet("Matrícula: ${ligacao.numReg}\nHid: ${ligacao.numeroMedidor}||${ligacao.numReg.toLong()}")
                        .zIndex(1f)
                        .icon(BitmapDescriptorFactory.fromBitmap(
                                getMarkerIconWithLabel(ligacao.numero ?: "",
                                        if (ligacao.status == STATUS_CONCLUIDO) R.drawable.bg_map_marker_blue
                                        else R.drawable.bg_map_marker_red)))
                        .visible(true)

                val marker = mMap.addMarker(markerOptions)
                markers.add(marker)
                ligacao.numeroMedidor?.let { hashMarkers.put(it, marker) }
                reverseHashMarkers[marker] = ligacao
            }

            adapterImoveis.setItems(viewModel.formattedHouses(ligacoesWithLocation, pending = false, finished = false, reversed = false))
            moveCamera()
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapUtils = MapUtils(this@MapsHomeActivity, googleMap)
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(-23.546896, -46.694400), 15f)))
        mMap.setInfoWindowAdapter(MapHomeInfoWindowAdapter(this@MapsHomeActivity, false))

        intent.extras?.getString(EXTRA_BUNDLE_TASK_NAME)?.let { roteiro ->
            loadRuas(roteiro)
        }

        mMap.setOnMarkerClickListener { marker ->

            if (marker.zIndex == 2f) {
                btRoute.text = getString(R.string.mapa_btn_imoveis)
                btRoute.setOnClickListener {
                    marker.title.split(",").firstOrNull()?.let {
                        loadImoveis(it)
                    }
                }

            } else {
                btRoute.text = getString(R.string.mapa_btn_rotas)
                btRoute.setOnClickListener {
                    val gmmIntentUri = Uri.parse("google.navigation:q=${marker.position.latitude},${marker.position.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }

                if (marker.isInfoWindowShown) {
                    reverseHashMarkers[marker]?.let { ligacao ->
                        viewModel.selectedAddress.value?.let { navigateToReaderAcitivty(it, ligacao) }
                    }
                }
            }

            btRoute.visibility = View.VISIBLE

            return@setOnMarkerClickListener false
        }

        mMap.setOnInfoWindowClickListener {
            reverseHashMarkers[it]?.let { ligacao ->
                viewModel.selectedAddress.value?.let { navigateToReaderAcitivty(it, ligacao) }
            }
        }

        mMap.setOnMapClickListener {
            btRoute.visibility = View.GONE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        mMap.setOnCameraMoveListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        startObservers()
        requestPermissionLocation(googleMap)
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.silver_style))
    }

    private fun processaStreetPolylines(streets: ArrayList<Pair<Address, Address>>) {

        polylines.forEach { it.remove() }
        polylines.clear()

        streets.forEach { street ->

            mapUtils.fetchRoutesAndGetDuration(
                    LatLng(street.first.latitude!!, street.first.longitude!!),
                    LatLng(street.second.latitude!!, street.second.longitude!!)) { polyline, duration ->
                polylines.add(polyline)
                moveCamera()
            }
        }
    }

    private fun moveCamera(latLng: LatLng? = null) {

        val builder = LatLngBounds.Builder()

        latLng?.let { builder.include(it) } ?: markers.forEach {
            builder.include(it.position) ?: return
        }

        userMarker?.let { builder.include(it.position) }

        if (streetPoints.isNotEmpty()) {
            streetPoints.forEach { builder.include(it) }
        }

        val bounds = builder.build()

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80))
        } catch (e: Exception) {
            Log.e("MAP", "onMapReady: ", e)
        }
    }

    private fun loadImoveis(streetName: String) {
        titleImovel.text = streetName
        recyclerViewImoveis.visibility = View.VISIBLE
        recyclerViewRuas.visibility = View.GONE
        backButton.visibility = View.VISIBLE
        titleImovel.visibility = View.VISIBLE
        titleRua.visibility = View.GONE
        separatorRua.visibility = View.GONE
        btRoute.visibility = View.GONE

        viewModel.address.value?.find { it.nome == streetName }?.let { viewModel.setSelectedAddress(it) }
        viewModel.fetchHouses(streetName)
    }

    private fun loadRuas(roteiro: String) {
        recyclerViewImoveis.visibility = View.GONE
        recyclerViewRuas.visibility = View.VISIBLE
        backButton.visibility = View.GONE
        titleImovel.visibility = View.GONE
        titleRua.visibility = View.VISIBLE
        separatorRua.visibility = View.VISIBLE
        btRoute.visibility = View.GONE

        viewModel.fetchAdressesByFileName(roteiro)
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
                    manager.requestLocationUpdates(providerCoarse, 100, 100f, this@MapsHomeActivity)

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

    private fun boundMap(location: Location) {
        if (userMarker == null) {
            val userPos = LatLng(location.latitude, location.longitude)
            userMarker = mMap.addMarker(MarkerOptions()
                    .position(userPos)
                    .icon(Utils.bitmapDescriptorFromVector(this@MapsHomeActivity,
                            R.drawable.ic_profile_menu)))
            moveCamera()
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startGoogleApiClient()
    }

    override fun onLocationChanged(location: Location?) {
        location?.let { position ->
            boundMap(location)
            userMarker?.let { userMarker ->
                currentLocation = position
                userMarker.position = LatLng(position.latitude, position.longitude)
            }
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
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

    private fun onStateChange(state: MapsHomeViewModel.State) {

        when (state) {
            MapsHomeViewModel.State.LOADING -> {
                progress.visibility = View.VISIBLE
            }
            MapsHomeViewModel.State.SUCCESS -> {
                progress.visibility = View.GONE
            }
            MapsHomeViewModel.State.ERROR -> {
                progress.visibility = View.GONE
                val dialog = SimpleDialog(this@MapsHomeActivity, getString(R.string.map_error))
                dialog.setDismissListener { finish() }
                dialog.show()
            }
        }
    }

    private fun getMarkerIconWithLabel(label: String, resId: Int): Bitmap {
        val iconGenerator = IconGenerator(this@MapsHomeActivity)
        val markerView = LayoutInflater.from(this@MapsHomeActivity).inflate(R.layout.map_marker, null)
        val imgMarker = markerView.findViewById<ImageView>(R.id.icon)
        val tvLabel = markerView.findViewById<TextView>(R.id.label)
        imgMarker.setImageResource(resId)
        imgMarker.rotation = 0F
        tvLabel.text = label
        iconGenerator.setContentView(markerView)
        iconGenerator.setBackground(null)
        return iconGenerator.makeIcon(label)
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

    private fun navigateToReaderAcitivty(address: Address, home: Ligacao) {
        val intent = Intent(this, LeituraActivity::class.java).let {
            it.putExtra(LeituraActivity.ARG_UNIDADE, home)
            it.putExtra(LeituraActivity.ARG_RUA, address)
            it.putExtra(LeituraActivity.ARG_ROTEIRO, viewModel.roteiro.value?.nome)
        }
        startActivity(intent)
    }
}
