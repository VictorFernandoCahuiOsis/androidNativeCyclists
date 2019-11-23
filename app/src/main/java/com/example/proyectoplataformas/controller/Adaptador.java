package com.example.proyectoplataformas.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoplataformas.Inicio;
import com.example.proyectoplataformas.maps.TrazarRutas;
import com.example.proyectoplataformas.model.Actividad;
import com.example.proyectoplataformas.R;
import com.example.proyectoplataformas.model.ListaPunto;
import com.example.proyectoplataformas.model.Punto;
import com.example.proyectoplataformas.ui.ActividadHistorial;
import com.example.proyectoplataformas.ui.Login;

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

public class Adaptador extends BaseAdapter {

    private ArrayList<Actividad> listItems;
    private Context context;

    public Adaptador (Context context, ArrayList <Actividad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount () {
        return listItems.size();
    }

    @Override
    public Object getItem (int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        final Actividad item = (Actividad)getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);

        /*TextView nombreText = (TextView)convertView.findViewById(R.id.nombreCampo);
        TextView apellidoText = (TextView)convertView.findViewById(R.id.apellidoCampo);
        TextView edadText = (TextView)convertView.findViewById(R.id.edadCampo);
        TextView correoText = (TextView)convertView.findViewById(R.id.correoCampo);
        */

        TextView mensajeText = (TextView)convertView.findViewById(R.id.textViewItemMsg);
        TextView tiempoText = (TextView)convertView.findViewById(R.id.textViewItemTime);
        TextView distanciaText = (TextView)convertView.findViewById(R.id.textViewItemDist);
        TextView velocidadText = (TextView)convertView.findViewById(R.id.textViewItemSpeed);
        TextView caloriasText = (TextView)convertView.findViewById(R.id.textViewItemCalo);

        /*nombreText.setText(item.distancia);
        apellidoText.setText(item.calorias);
        edadText.setText(item.descripcion);
        correoText.setText(item.tiempo);
        */
        distanciaText.setText(item.distancia);
        caloriasText.setText(item.calorias);
        mensajeText.setText(item.descripcion);
        tiempoText.setText(item.tiempo);
        velocidadText.setText(item.velocidad);

        Button startServiceButton = (Button)convertView.findViewById(R.id.buttonVer);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSON_Task().execute("https://backendurl.com/api/puntoactividad/" + item.id);
            }
        });




        return convertView;
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

            ListaPunto miListaPunto = ListaPunto.getMiListaPunto();
            miListaPunto.eliminarTodos();
            String finalJson = s;

            try {

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("punto");
                JSONObject nuevo = null;
                for (int i = 0; i <parentArray.length(); i++ ) {
                    nuevo = parentArray.getJSONObject(i);

                    miListaPunto.addPunto(new Punto(nuevo.getString("latitud"), nuevo.getString("longitud")));
                }

                Intent intent = new Intent(context, TrazarRutas.class);
                context.startActivity(intent);
                //System.out.println("Esta es la lista de puntos:" + miListaPunto.longitudLista());
            } catch (Exception e) {
                System.err.println("error");
            }

        }
    }

}
