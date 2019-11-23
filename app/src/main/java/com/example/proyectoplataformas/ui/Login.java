package com.example.proyectoplataformas.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    Button botonRegistro;
    Button botonLogin;

    EditText nombreUsuario;
    EditText nombrePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botonRegistro = (Button) findViewById(R.id.btnLugar1);
        botonLogin = (Button) findViewById(R.id.button2);

        nombreUsuario = (EditText) findViewById(R.id.editText);
        nombrePassword = (EditText) findViewById(R.id.editText2);

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistroUsuario.class);
                startActivity(intent);
            }
        });

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject postData = new JSONObject();
                try {
                    postData.put("usuario", nombreUsuario.getText().toString());
                    postData.put("contrasenia", nombrePassword.getText().toString());

                    new JSON_Task().execute("https://backendurl.com/api/usuariologin/", postData.toString());

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


                if (respuesta.equals("ok")) {
                    Usuario nuevo = Usuario.getMiUsuario();
                    nuevo.setIdUsuario(parentObject.getString("_id"));
                    nuevo.setUserName(parentObject.getString("nombre"));
                    nuevo.setPassword(parentObject.getString("contrasenia"));

                    nuevo.setDistancia(Integer.parseInt(parentObject.getString("distancia")));

                    Toast.makeText(getApplicationContext(), "Bienvenido: " + parentObject.getString("nombre") , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Inicio.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_LONG).show();
                }




            } catch (Exception e) {

            }



        }
    }
}
