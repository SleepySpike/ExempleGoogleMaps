package com.example.exemplegooglemaps;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mMap;
    long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; //en mètre
    long MIN_TIME_BW_UPDATES = 1000 * 60 * 2; //en millisecond (2 mn)
    LocationManager locationManager;
    Context context;

    Location location;
    double altitudeLocation;
    double latitude;
    double longitude;
    boolean checkGPS;
    boolean checkNetwork;
    String typePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        context = this;

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // On choisis le mode d'affichage
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // On voit les building en 3D
        mMap.setBuildingsEnabled(true);

        InitMarker();

        //Je test si j'ai donné l'autorisation à utiliser la géolocalisation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // On autorise la géolocalisation
            mMap.setMyLocationEnabled(true);

            //Mécanisme de géolocalisation
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            //Notre position en latitude/longitude
            Location location = null;

            if (locationManager != null) {
                //Mode gps actif
                boolean modeGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                //mode network actif
                boolean modeNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (modeGps) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) context);

                    //récupération de la dernière position connue en version GPS
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (modeNetwork) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) context);

                    //récupération de la dernière position connue en version Network
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    //Toast -> aucun mode actif
                }

                //On verfie que l'on a bien une position
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    altitudeLocation = location.getAltitude();

                    //on affiche notre position
                    LatLng moi = new LatLng(latitude, longitude);

                    //On met un markeur sur notre position
                    mMap.addMarker(new MarkerOptions().position(moi).title("je suis ici"));
                    //On détermine le niveau du zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moi, 15));
                } else {
                    Toast.makeText(context, "Je n'ai pas l'autorisation", Toast.LENGTH_LONG).show();
                }

            }

            //Je n'ai pas donné l'autorisation
            else {
                Toast.makeText(context, "Je n'ai pas l'autorisation", Toast.LENGTH_LONG).show();
            }

        }
    }

        private void InitMarker()
        {

            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            LatLng paris = new LatLng(48.8566, 2.3522);

            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.addMarker(new MarkerOptions().position(paris).title("Ici c'est Paris"));

            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 15));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(paris, 15));

        }

        @Override
        public void onLocationChanged (Location location){
            // Efface les markers
            mMap.clear();

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            // on affiche notre position sur la carte
            LatLng moi = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(moi).title("Je suis ici !"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moi, 20));
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled (String provider){

        }

        @Override
        public void onProviderDisabled (String provider){
        }}
