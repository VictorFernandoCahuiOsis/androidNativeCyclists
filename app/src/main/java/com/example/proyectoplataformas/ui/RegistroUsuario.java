package com.example.proyectoplataformas.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectoplataformas.R;
import com.example.proyectoplataformas.Inicio;
import com.example.proyectoplataformas.model.Usuario;

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

public class RegistroUsuario extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button botonRegistro;

    EditText nombreUsuario;
    EditText nombrePassword;
    EditText nombreCorreo;
    EditText nombreEdad;
    String nombreSexo;
    EditText nombrePeso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        botonRegistro = (Button) findViewById(R.id.button2);

        nombreUsuario = (EditText) findViewById(R.id.editText);
        nombrePassword = (EditText) findViewById(R.id.editText2);
        nombreCorreo = (EditText) findViewById(R.id.editText3);
        nombreEdad = (EditText) findViewById(R.id.editText5);
        nombrePeso = (EditText) findViewById(R.id.editText6);
        nombreSexo = "Masculino";


        Spinner dropdown = findViewById(R.id.spinner2);
        String[] items = new String[]{"Masculino", "Femenino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        //Ahora hay que guardar los datos


        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", nombreUsuario.getText().toString());
                    postData.put("contrasenia", nombrePassword.getText().toString());
                    postData.put("correo", nombreCorreo.getText().toString());
                    postData.put("edad", Integer.parseInt(nombreEdad.getText().toString()));
                    postData.put("sexo", nombreSexo);
                    postData.put("peso", Integer.parseInt(nombrePeso.getText().toString()));
                    postData.put("puntos", 0);

                    new JSON_Task().execute("https://backendurl.com/api/usuario/", postData.toString());

                } catch (JSONException e) {
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

                    int maxReadSize = 40000;

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
                nuevo.setIdUsuario(parentObject.getString("_id"));
                nuevo.setUserName(parentObject.getString("nombre"));
                nuevo.setPassword(parentObject.getString("contrasenia"));
                nuevo.setDistancia(Integer.parseInt(parentObject.getString("distancia")));
                Toast.makeText(getApplicationContext(), "Bienvenido: " + parentObject.getString("nombre") , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistroUsuario.this, Inicio.class);
                startActivity(intent);

            } catch (Exception e) {

            }



        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                nombreSexo = "Masculino";
                break;
            case 1:
                nombreSexo = "Femenino";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
