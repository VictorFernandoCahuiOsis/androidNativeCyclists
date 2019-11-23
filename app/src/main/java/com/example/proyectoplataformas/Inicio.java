package com.example.proyectoplataformas;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.proyectoplataformas.maps.MapActivity;
import com.example.proyectoplataformas.ui.ActividadHistorial;

import com.example.proyectoplataformas.model.Usuario;

import com.example.proyectoplataformas.ui.Login;
import com.example.proyectoplataformas.ui.Notificaciones;
import com.example.proyectoplataformas.ui.RegistroUsuario;

import android.widget.TextView;
import android.widget.Toast;

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

import javax.net.ssl.HttpsURLConnection;

public class Inicio extends AppCompatActivity {

    Button bt_iniciar;
    Button bt_historial;
    Button bt_recompensas;
    Button bt_notificaciones;
    Button bt_salir;
    Button bt_encuentranos;
    TextView tv_usuario;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        bt_iniciar = (Button) findViewById(R.id.btIniciarRecorrido);

        bt_historial = (Button) findViewById(R.id.btHistorial);
        bt_recompensas = (Button) findViewById(R.id.btRecompensas);
        bt_notificaciones = (Button) findViewById(R.id.btNotificaciones);
        bt_encuentranos = (Button) findViewById(R.id.btEncuentranos);
        bt_salir = (Button) findViewById(R.id.btSalir);
        tv_usuario = (TextView) findViewById(R.id.idNotificacion);

        Usuario nuevo = Usuario.getMiUsuario();
        String nombre = nuevo.getNombre();
        tv_usuario.setText(nombre);


        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Usuario nuevo = Usuario.getMiUsuario();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("idUsuario", nuevo.getIdUsuario());
                    postData.put("descripcion", "Es es una nueva actividad");
                    postData.put("tiempo", "0");
                    postData.put("distancia", 0);
                    postData.put("calorias", 0);
                    postData.put("velocidad", 0);


                    new JSON_Task().execute("https://backendurl.com/api/actividad/", postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        bt_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, ActividadHistorial.class);
                startActivity(intent);
            }
        });

        bt_recompensas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Recompensas.class);
                startActivity(intent);
            }
        });

        bt_notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Notificaciones.class);
                startActivity(intent);
            }
        });

        bt_encuentranos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Encuentranos.class);
                startActivity(intent);
            }
        });

        bt_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Login.class);
                startActivity(intent);
            }
        });
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

                    int maxReadSize = 4000;

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


            try {
                JSONObject parentObject = new JSONObject(finalJson);

                Usuario nuevo = Usuario.getMiUsuario();

                nuevo.setIdActividad(""+ parentObject.getString("_id"));


                Intent intent = new Intent(Inicio.this, MapActivity.class);
                startActivity(intent);

            } catch (Exception e) {

            }





        }
    }
}