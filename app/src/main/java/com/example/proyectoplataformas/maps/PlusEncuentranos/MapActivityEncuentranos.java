package com.example.proyectoplataformas.maps.PlusEncuentranos;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.proyectoplataformas.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivityEncuentranos extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    Button getDirection;
    private Polyline currentPolyline;
    int indice=0;
    public ArrayList<MarkerOptions> lugaresA =  new ArrayList<>();
    double latOrigen=0;
    double lonOrigen=0;
    LatLng origen;
    MarkerOptions ubicacion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        Bundle extras = getIntent().getExtras();
        indice = extras.getInt("Lugar");
        latOrigen = extras.getDouble("Lat");
        lonOrigen = extras.getDouble("Long");
        System.out.println("Origen recibido: "+ latOrigen + " " + lonOrigen);
        origen=new LatLng(latOrigen,lonOrigen);
        System.out.println("EL indice recibido es : " + indice);
        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                CameraPosition camara = new CameraPosition.Builder()
                        .target(lugaresA.get(indice).getPosition())
                        .zoom(14)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));
                new FetchURL(MapActivityEncuentranos.this).execute(getUrl(origen,lugaresA.get(indice).getPosition(), "driving"), "driving");
                System.out.println("Estoy en fetch!!!");
            }
        });
        ubicacion = new MarkerOptions().position(new LatLng(latOrigen, lonOrigen)).title("Mi Ubicacion").icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle1));
        lugaresA.add(new MarkerOptions().position(new LatLng(-16.417319, -71.513772)).title("Mall Aventura").icon(BitmapDescriptorFactory.fromResource(R.drawable.mall)));
        lugaresA.add(new MarkerOptions().position(new LatLng(-16.390252, -71.546363)).title("Mall Plaza").icon(BitmapDescriptorFactory.fromResource(R.drawable.aventura)));
        lugaresA.add(new MarkerOptions().position(new LatLng(-16.410787, -71.520313)).title("Parque Lambramani").icon(BitmapDescriptorFactory.fromResource(R.drawable.parque)));

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(ubicacion);
        for(int i=0;i<lugaresA.size();i++)
            mMap.addMarker(lugaresA.get(i));
        float zoom = 17;
        LatLng latLng = new LatLng(-16.406385, -71.524844);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(latLng));

        UiSettings settings = googleMap.getUiSettings();

        settings.setZoomControlsEnabled(true);
        CameraPosition camara = new CameraPosition.Builder()
                .target(lugaresA.get(indice).getPosition())
                .zoom(17)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));
    }

    private String getUrl(LatLng orig, LatLng dest, String directionMode) {

        String str_origin = "origin=" + orig.latitude + "," + orig.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key2);
        System.out.println("LA URL ES: "+ url);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
