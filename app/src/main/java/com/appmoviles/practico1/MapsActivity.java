package com.appmoviles.practico1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 11 ;

    private GoogleMap mMap;
    private LocationManager manager;

    private Marker me;

    private LatLng[] biblioteca;
    private LatLng[] edificioM;
    private LatLng[] saman;

    private FloatingActionButton fab_canje;
    private FloatingActionButton fab_preguntas;

    private boolean facil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        facil=true;

        fab_canje = findViewById(R.id.fab_canje);
        fab_canje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, Canje.class);
                startActivity(i);
            }
        });

        fab_preguntas = findViewById(R.id.fab_preguntas);
        fab_preguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, Preguntas.class);
                i.putExtra("FACIL", facil);
                startActivity(i);
            }
        });
        deshabilitar();
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

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_CODE);

        initZones();
        addPolylines();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(this, "Not Enough Permission", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        moving(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
            else if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        moving(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        }

    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,18));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);


    }

    private void addPolylines(){
        mMap.addPolyline(new PolylineOptions()
                .add(biblioteca[0],biblioteca[1], biblioteca[2], biblioteca[3], biblioteca[0])
                .width(5).color(Color.RED));
        mMap.addPolyline(new PolylineOptions()
                .add(edificioM[0], edificioM[1], edificioM[2], edificioM[3], edificioM[0])
                .width(5).color(Color.BLACK));
        mMap.addPolyline(new PolylineOptions()
                .add(saman[0], saman[1], saman[2], saman[3], saman[0])
                .width(5).color(Color.BLACK));

    }

    public void initZones(){
        biblioteca = new LatLng[4];
        biblioteca[0] = new LatLng(3.341937,-76.530084); // arriba izquierda
        biblioteca[1] = new LatLng(3.341937, -76.529800); // arriba derecha
        biblioteca[2] = new LatLng(3.341659, -76.529800); // abajo derecha
        biblioteca[3] = new LatLng(3.341659,-76.530084); // abajo izquierda


        edificioM = new LatLng[4];
        edificioM[0] = new LatLng(3.342928,-76.53080);
        edificioM[1] = new LatLng(3.342928,-76.530057);
        edificioM[2] = new LatLng(3.342291, -76.530057);
        edificioM[3] = new LatLng(3.342291,-76.53080);

        saman = new LatLng[4];
        saman[0] = new LatLng(3.341910,-76.530583);
        saman[1] = new LatLng(3.341910,-76.530358);
        saman[2] = new LatLng(3.341712, -76.530358);
        saman[3] = new LatLng(3.341712,-76.530583);



    }

    private boolean checkPlace(LatLng myPosition, LatLng[] place){

        Log.e(">>>","Checking");

        if(myPosition.latitude<=place[0].latitude && myPosition.latitude>=place[3].latitude
                && myPosition.longitude>=place[0].longitude && myPosition.longitude<=place[1].longitude){
            Log.e(">>>","true");
            return true;
        }
        else{
            Log.e(">>>","false");
            return false;

        }
    }

    public void moving(Location location){
        String msj = "LAT: "+location.getLatitude()+ " , LONG: "+location.getLongitude();
        Log.e(">>>","LAT: "+location.getLatitude()+ " , LONG: "+location.getLongitude());

        if(me != null) me.remove();
        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        me = mMap.addMarker(new MarkerOptions().position(myPosition)
                .title("Me").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        moveToCurrentLocation(myPosition);
        if(checkPlace(myPosition, biblioteca)){
            fab_canje.setEnabled(true);
            fab_canje.setVisibility(View.VISIBLE);
            fab_preguntas.setEnabled(false);
            fab_preguntas.setVisibility(View.INVISIBLE);
            Toast.makeText(MapsActivity.this, "Te encuentras en la zona de canje", Toast.LENGTH_SHORT).show();
            Log.e(">>>","En zona de Canje");
        }
        else if(checkPlace(myPosition, edificioM) || checkPlace(myPosition, saman)){
            fab_preguntas.setEnabled(true);
            fab_preguntas.setVisibility(View.VISIBLE);
            fab_canje.setEnabled(false);
            fab_canje.setVisibility(View.INVISIBLE);
            Toast.makeText(MapsActivity.this, "Te encuentras en zona de preguntas", Toast.LENGTH_SHORT).show();
            Log.e(">>>","En zona de Preguntas");
            if(checkPlace(myPosition, edificioM)){
                facil=true;
            }else{
                facil=false;
            }
        }
        else {
            deshabilitar();
        }
    }

    private void deshabilitar(){
        fab_canje.setEnabled(false);
        fab_canje.setVisibility(View.INVISIBLE);
        fab_preguntas.setEnabled(false);
        fab_preguntas.setVisibility(View.INVISIBLE);
    }
}
