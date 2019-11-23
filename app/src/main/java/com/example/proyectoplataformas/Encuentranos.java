package com.example.proyectoplataformas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.proyectoplataformas.maps.PlusEncuentranos.MapActivityEncuentranos;
import com.example.proyectoplataformas.model.ScaleListener;
import com.google.android.gms.maps.model.LatLng;

public class Encuentranos extends AppCompatActivity {
    Button botonLugar1,botonLugar2,botonLugar3;
    private LocationManager locationManager;
    private LocationListener listener;
    private Double lat = 0.0;
    private Double lon = 0.0;
    private ImageView iV3;
    private ImageView iV4;
    private ImageView iV5;
    private ScaleGestureDetector detector3;
    private ScaleGestureDetector detector4;
    private ScaleGestureDetector detector5;

    private float escalaX3;
    private float escalaY3;

    private float escalaX4;
    private float escalaY4;

    private float escalaX5;
    private float escalaY5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentranos);

        iV3 = (ImageView) findViewById(R.id.imageView3);
        iV4 = (ImageView) findViewById(R.id.imageView4);
        iV5 = (ImageView) findViewById(R.id.imageView5);

        escalaX3 = iV3.getScaleX();
        escalaY3 = iV3.getScaleY();
        escalaX4 = iV4.getScaleX();
        escalaY4 = iV4.getScaleY();
        escalaX5 = iV5.getScaleX();
        escalaY5 = iV5.getScaleY();

        detector3 = new ScaleGestureDetector(this,new ScaleListener(iV3));
        detector4 = new ScaleGestureDetector(this,new ScaleListener(iV4));
        detector5 = new ScaleGestureDetector(this,new ScaleListener(iV5));

        botonLugar1 = (Button) findViewById(R.id.btnLugar1);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        System.out.println("Estoy en ObtenrUbicac!!!");
        listener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                System.out.println("Latitud: "+ location.getLatitude()+ " Longitud "+ location.getLongitude());
                System.out.println("Tiempo: "+ location.getTime()+ " Velocidad: "+ location.getSpeed());
                System.out.println(location.toString());
                LatLng posicion = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lon = location.getLongitude();
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
                System.out.println("La PRIMERA posicion a enviar es: Lat: "  + lat+ " lon: "+ lon);
                botonLugar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Encuentranos.this, MapActivityEncuentranos.class);
                        intent.putExtra("Lugar", 0);
                        intent.putExtra("Lat", lat);
                        intent.putExtra("Long", lon);
                        startActivity(intent);
                    }
                });
                botonLugar2 = (Button) findViewById(R.id.btnLugar2);

                botonLugar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Encuentranos.this, MapActivityEncuentranos.class);
                        intent.putExtra("Lugar", 1);
                        intent.putExtra("Lat", lat);
                        intent.putExtra("Long", lon);
                        startActivity(intent);
                    }
                });
                botonLugar3 = (Button) findViewById(R.id.btnLugar3);

                botonLugar3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Encuentranos.this, MapActivityEncuentranos.class);
                        intent.putExtra("Lugar", 2);
                        intent.putExtra("Lat", lat);
                        intent.putExtra("Long", lon);
                        startActivity(intent);
                    }
                });
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(Encuentranos.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Encuentranos.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
        }
        locationManager.requestLocationUpdates("gps", 0, 0, listener);

        System.out.println("La ULTIMA posicion a enviar es: Lat: "  + lat+ " lon: "+ lon);
    }

    public boolean onTouchEvent(MotionEvent event){
        detector3.onTouchEvent(event);
        detector4.onTouchEvent(event);
        detector5.onTouchEvent(event);
        return true;
    }
}
