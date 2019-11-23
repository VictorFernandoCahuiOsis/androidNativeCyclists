package com.example.proyectoplataformas.ui;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.proyectoplataformas.R;
import com.example.proyectoplataformas.controller.Adaptador;
import com.example.proyectoplataformas.model.Actividad;
import com.example.proyectoplataformas.model.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ActividadHistorial extends AppCompatActivity {

    private ListView lvItems;
    private Adaptador adaptador;

    public ArrayList<Actividad> listItems =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_historial);
        lvItems = (ListView)findViewById(R.id.lvItems);

        Usuario miUsuario = Usuario.getMiUsuario();


        new JSON_Task().execute("https://backendurl.com/api/actividadusuario/" + miUsuario.getIdUsuario());
        //distancia.setText("dasdasdsa");
    }

    class JSON_Task extends AsyncTask<String, String, String> {

        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        Reader reader = null;
        String line = "";

        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                StringBuffer buffer = new StringBuffer();

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.

                stream = connection.getInputStream();


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

            try {

                listItems.clear();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("actividad");
                JSONObject nuevo = null;
                for (int i = 0; i <parentArray.length(); i++ ) {
                    nuevo = parentArray.getJSONObject(i);
                    listItems.add(new Actividad(nuevo.getString("_id"),nuevo.getString("idUsuario"),nuevo.getString("descripcion"), nuevo.getString("fechaInicio"),
                            nuevo.getString("fechaFin"), nuevo.getString("tiempo"), nuevo.getString("distancia"), nuevo.getString("calorias"), nuevo.getString("velocidad")));
                }

                adaptador = new Adaptador(ActividadHistorial.this,listItems);
                lvItems.setAdapter(adaptador);
                //distancia.setText(""+listItems.size());
                //distancia.setText("ffffff");


                //adaptador = new Adaptador(ListarCiclista.this,listItems);
                //lvItems.setAdapter(adaptador);

            } catch (Exception e) {
                System.err.println("error");
            }

        }
    }
}
