package com.example.proyectoplataformas;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoplataformas.maps.MapActivity;
import com.example.proyectoplataformas.maps.TrazarRutas;
import com.example.proyectoplataformas.model.Usuario;
import com.example.proyectoplataformas.ui.Login;

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

public class Recompensas extends AppCompatActivity {
    Button bt_trazar;
    Button bt_Llantas;
    Button bt_bebidas;
    Button bt_mantenimiento;
    Button bt_audifonos;
    Button bt_engrase;
    Button bt_guantes;


    boolean clickAudifonos = false;
    boolean clickAceite = false;
    boolean clickGuantes = false;
    boolean clickLlantas = false;
    boolean clickMantenimiento = false;
    boolean clickBebidas = false;



    TextView tvGuantes;
    TextView tvLlantas;
    TextView tvAudifonos;
    TextView tvAceite;
    TextView tvMantenimiento;
    TextView tvBebidas;

    private int llantas = 50;
    private int audifonos = 75;
    private int aceite = 30;
    private int mantenimiento = 20;
    private int guantes = 40;
    private int bebidas = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recompensas);

        bt_Llantas = (Button) findViewById(R.id.bt_Llantas);
        bt_bebidas = (Button) findViewById(R.id.bt_Bebidas);
        bt_mantenimiento = (Button) findViewById(R.id.bt_Mantenimiento);
        bt_audifonos = (Button) findViewById(R.id.bt_Audifonos);
        bt_engrase = (Button) findViewById(R.id.bt_Engrase);
        bt_guantes = (Button) findViewById(R.id.bt_Guantes);


        bt_trazar = (Button) findViewById(R.id.bt_Llantas);
        bt_trazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recompensas.this, TrazarRutas.class);
                startActivity(intent);
            }
        });

        tvAudifonos = (TextView)  findViewById(R.id.tvAudifonos);
        tvAceite = (TextView)  findViewById(R.id.tvAceite);
        tvGuantes = (TextView)  findViewById(R.id.tvGuantes);
        tvLlantas = (TextView)  findViewById(R.id.tvLlantas);
        tvMantenimiento = (TextView)  findViewById(R.id.tvMantenimiento);
        tvBebidas = (TextView)  findViewById(R.id.tvBebida);

        tvAudifonos.setText(audifonos +" mts.");
        tvAceite.setText(aceite+ " mts.");
        tvGuantes.setText(guantes+ " mts.");
        tvLlantas.setText(llantas+ " mts.");
        tvMantenimiento.setText(mantenimiento+ " mts.");
        tvBebidas.setText(bebidas+ " mts.");

        final Usuario nuevo = Usuario.getMiUsuario();
        final int metros = nuevo.getDistancia();


        bt_audifonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = nuevo.getNombre();
                String contrasenia = nuevo.getPassword();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", usuario);
                    postData.put("contrasenia",contrasenia);

                    new Recompensas.JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());
                    clickAudifonos = true;

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String usuario = nuevo.getNombre();
                String contrasenia = nuevo.getPassword();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", usuario);
                    postData.put("contrasenia",contrasenia);

                    new Recompensas.JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());
                    clickBebidas = true;

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_engrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String usuario = nuevo.getNombre();
                String contrasenia = nuevo.getPassword();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", usuario);
                    postData.put("contrasenia",contrasenia);

                    new Recompensas.JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());
                    clickAceite = true;

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_guantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = nuevo.getNombre();
                String contrasenia = nuevo.getPassword();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", usuario);
                    postData.put("contrasenia",contrasenia);

                    new Recompensas.JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());
                    clickGuantes = true;

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_Llantas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String usuario = nuevo.getNombre();
                String contrasenia = nuevo.getPassword();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", usuario);
                    postData.put("contrasenia",contrasenia);

                    new Recompensas.JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());
                    clickLlantas = true;

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_mantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = nuevo.getNombre();
                String contrasenia = nuevo.getPassword();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", usuario);
                    postData.put("contrasenia",contrasenia);

                    new Recompensas.JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());
                    clickMantenimiento = true;

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
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
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(strings[1]);

                // System.out.println("Este es el json: " + "\""  + strings[1].toString() + "\"" );
                wr.flush();
                wr.close();
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
                String respuesta = parentObject.getString("estado");
                Usuario nuevo = Usuario.getMiUsuario();
                int metros =Integer.parseInt(parentObject.getString("distancia"));

                if(clickAudifonos) {
                    if(metros >= audifonos)
                        Toast.makeText(getApplicationContext(), "Puede Canjear Audifonos", Toast.LENGTH_LONG).show();
                    else {
                        int resto = audifonos - metros;
                        Toast.makeText(getApplicationContext(), "Le faltan "+resto+ " metros", Toast.LENGTH_LONG).show();
                    }
                    clickAudifonos = false;
                    clickAceite = false;
                    clickGuantes = false;
                    clickLlantas = false;
                    clickMantenimiento = false;
                    clickBebidas = false;
                }

                if(clickAceite){
                    if(metros >= aceite)
                        Toast.makeText(getApplicationContext(), "Puede Canjear un Engrase", Toast.LENGTH_LONG).show();
                    else {
                        int resto = aceite - metros;
                        Toast.makeText(getApplicationContext(), "Le faltan "+resto+ " metros para un engrase", Toast.LENGTH_LONG).show();
                    }
                    clickAudifonos = false;
                    clickAceite = false;
                    clickGuantes = false;
                    clickLlantas = false;
                    clickMantenimiento = false;
                    clickBebidas = false;
                }

                if(clickBebidas){
                    if(metros >= bebidas)
                        Toast.makeText(getApplicationContext(), "Puede Canjear una bebida", Toast.LENGTH_LONG).show();
                    else {
                        int resto = bebidas - metros;
                        Toast.makeText(getApplicationContext(), "Le faltan "+resto+ " metros para una bebida", Toast.LENGTH_LONG).show();
                    }
                    clickAudifonos = false;
                    clickAceite = false;
                    clickGuantes = false;
                    clickLlantas = false;
                    clickMantenimiento = false;
                    clickBebidas = false;
                }

                if(clickLlantas){
                    if(metros >= llantas)
                        Toast.makeText(getApplicationContext(), "Puede Canjear una Llanta", Toast.LENGTH_LONG).show();
                    else {
                        int resto = llantas - metros;
                        Toast.makeText(getApplicationContext(), "Le faltan "+resto+ " metros para una llanta", Toast.LENGTH_LONG).show();
                    }
                    clickAudifonos = false;
                    clickAceite = false;
                    clickGuantes = false;
                    clickLlantas = false;
                    clickMantenimiento = false;
                    clickBebidas = false;
                }

                if(clickMantenimiento){
                    if(metros >= mantenimiento)
                        Toast.makeText(getApplicationContext(), "Puede Canjear un mantenimiento", Toast.LENGTH_LONG).show();
                    else {
                        int resto = mantenimiento - metros;
                        Toast.makeText(getApplicationContext(), "Le faltan "+resto+ "para un mantenimiento", Toast.LENGTH_LONG).show();
                    }

                    clickAudifonos = false;
                    clickAceite = false;
                    clickGuantes = false;
                    clickLlantas = false;
                    clickMantenimiento = false;
                    clickBebidas = false;
                }

                if(clickGuantes){
                    if(metros >= guantes)
                        Toast.makeText(getApplicationContext(), "Puede Canjear Guantes", Toast.LENGTH_LONG).show();
                    else {
                        int resto = guantes - metros;
                        Toast.makeText(getApplicationContext(), "Le faltan "+resto+ " metros para un par de guantes", Toast.LENGTH_LONG).show();
                    }
                    clickAudifonos = false;
                    clickAceite = false;
                    clickGuantes = false;
                    clickLlantas = false;
                    clickMantenimiento = false;
                    clickBebidas = false;
                }
            } catch (Exception e) {

            }
        }
    }
    }
