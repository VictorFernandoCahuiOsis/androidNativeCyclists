package com.example.proyectoplataformas.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoplataformas.Inicio;
import com.example.proyectoplataformas.R;
import com.example.proyectoplataformas.controller.MyApplication;
import com.example.proyectoplataformas.model.Usuario;
import com.example.proyectoplataformas.service.ActivityService;
import com.example.proyectoplataformas.ui.Login;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String tiempo = "";
    private LocationManager locationManager;
    private LocationListener listener;
    private int distancia=0;
    private Chronometer chronometer;
    private long pauseOffset=0;
    private boolean running;
    private boolean flag = false;
    float velocidadGlobal = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Tiempo: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        Button startServiceButton = (Button)findViewById(R.id.btnInicio);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                startChronometer(v);
                Intent intent = new Intent(MapActivity.this, ActivityService.class);
                intent.setAction(ActivityService.ACTION_START_FOREGROUND_SERVICE);
                startService(intent);
            }
        });

        Button stopServiceButton = (Button)findViewById(R.id.btnSalir);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                pauseChronometer(v);
                resetChronometer(v);
                Intent intent = new Intent(MapActivity.this, ActivityService.class);
                intent.setAction(ActivityService.ACTION_STOP_FOREGROUND_SERVICE);
                startService(intent);
                // Poner sel servicio para actualzar los datos de la actividad


                int vel = (int)velocidadGlobal;
                int cal = 56;
                int dist = distancia;

                String tie = "" + (int)(Double.parseDouble(tiempo)/1000);
                System.out.println("Este es el tiempo: " + tie + " Y ets es la variable timepo: " + tiempo);
                Usuario nuevo = Usuario.getMiUsuario();
                String actividadID = nuevo.getIdActividad();


                JSONObject postData = new JSONObject();
                try {
                    postData.put("tiempo", tie);
                    postData.put("distancia",dist );
                    postData.put("calorias", cal);
                    postData.put("velocidad",vel );
                    postData.put("idActividad", actividadID);



                    new JSON_TaskFin().execute("https://backendurl.com/api/actividadclave/", postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button pause = (Button)findViewById(R.id.btnPausar);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                pauseChronometer(v);
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapaUbi);
        mapFragment.getMapAsync(this);
    }
    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);
            System.out.println("Tiempo de Pause:" + pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        System.out.println("cronometro tiempo: " + (SystemClock.elapsedRealtime() - chronometer.getBase()));
        tiempo+=(SystemClock.elapsedRealtime() - chronometer.getBase());
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().isCompassEnabled();
        mMap.getUiSettings().isMapToolbarEnabled();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {

            public void onLocationChanged(Location location) {
                TextView txtDistancia = (TextView) findViewById(R.id.textViewMapDist);
                TextView txtVelocidad = (TextView) findViewById(R.id.textViewMapSpeed);
                float zoom = 17;
                // Called when a new location is found by the network location provider.
                System.out.println("Latitud: "+ location.getLatitude()+ " Longitud "+ location.getLongitude());
                System.out.println("Tiempo: "+ location.getTime()+ " Velocidad: "+ location.getSpeed());
                System.out.println(location.toString());
                String txt = "Mi Nueva ubicación es: \n"
                        + "Latitud = " + location.getLatitude() + "\n"
                        + "Longitud = " + location.getLongitude() + "\n"
                        + "distancia = " + distancia + "\n"
                        + "Velocidad = " + location.getSpeed() + "\n";

                if(flag==true) {
                    distancia+=1;
                    txtDistancia.setText(distancia + " M");
                    txtVelocidad.setText(location.getSpeed() + " m/s");
                    velocidadGlobal = location.getSpeed();
                }
                ((MyApplication) getApplication()).setLongitude(location.getLongitude());
                ((MyApplication) getApplication()).setLatitude(location.getLatitude());
                ((MyApplication) getApplication()).addCont();


                // Mandar a la base de datos



                System.out.println(txt);
                //texto = (TextView) findViewById(R.id.tvMensaje);
                //texto.setText(txt);
                LatLng posicion = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, zoom));
                CameraPosition camara = new CameraPosition.Builder()
                        .target(posicion)
                        .zoom(17)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));



                if( ((MyApplication) getApplication()).getFlag()){

                    ((MyApplication) getApplication()).setChronometer(chronometer);
                    ((MyApplication) getApplication()).setDistance(distancia);
                    ((MyApplication) getApplication()).setSpeed(location.getSpeed());
                    Intent intent = new Intent(MapActivity.this, ActivityService.class);
                    intent.setAction(ActivityService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                    //Guardo los puntos de cada actividad

                    Usuario nuevo = Usuario.getMiUsuario();

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("latitud", location.getLatitude());
                        postData.put("longitud", location.getLongitude());
                        postData.put("idActividad", nuevo.getIdActividad());
                        new JSON_Task().execute("https://backendurl.com/api/punto/", postData.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }




            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, listener);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(terminalCotum));

        int permissioncheck = ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissioncheck == 0) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().isMyLocationButtonEnabled();
    }

    class JSON_Task extends AsyncTask<String, String, String> {

        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        Reader reader = null;
        String line = "";
        StringBuffer buffer = new StringBuffer();

        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                wr.writeBytes(strings[1]);

                // System.out.println("Este es el json: " + "\""  + strings[1].toString() + "\"" );
                wr.flush ();
                wr.close ();
                wr.close();
                connection.connect();

                //Get Response
                InputStream stream = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(stream));


                if (stream != null) {

                    reader = new BufferedReader(new InputStreamReader(stream));

                    int maxReadSize = 10000000;

                    reader = new InputStreamReader(stream, "UTF-8");
                    char[] rawBuffer = new char[maxReadSize];
                    int readSize;
                    while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
                        if (readSize > maxReadSize) {
                            readSize = maxReadSize;
                        }
                        buffer.append(rawBuffer, 0, readSize);
                        maxReadSize -= readSize;
                    }
                }
                return buffer.toString();


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String finalJson = s;




        }
    }

    class JSON_TaskFin extends AsyncTask<String, String, String> {

        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        Reader reader = null;
        String line = "";
        StringBuffer buffer = new StringBuffer();

        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                wr.writeBytes(strings[1]);

                // System.out.println("Este es el json: " + "\""  + strings[1].toString() + "\"" );
                wr.flush ();
                wr.close ();
                wr.close();
                connection.connect();

                //Get Response
                InputStream stream = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(stream));


                if (stream != null) {

                    reader = new BufferedReader(new InputStreamReader(stream));

                    int maxReadSize = 10000000;

                    reader = new InputStreamReader(stream, "UTF-8");
                    char[] rawBuffer = new char[maxReadSize];
                    int readSize;
                    while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
                        if (readSize > maxReadSize) {
                            readSize = maxReadSize;
                        }
                        buffer.append(rawBuffer, 0, readSize);
                        maxReadSize -= readSize;
                    }
                }
                return buffer.toString();


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String finalJson = s;

            Toast.makeText(getApplicationContext(),"Gracias por utilizar nuestra aplicacacion.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MapActivity.this, Inicio.class);
            startActivity(intent);


        }
    }

}

