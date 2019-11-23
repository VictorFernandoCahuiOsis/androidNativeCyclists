package com.example.proyectoplataformas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectoplataformas.ui.Login;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {


    Button botonLista;
    Button botonServicio;
    Button botonMapa;
    private GestureDetector gesto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonLista = (Button) findViewById(R.id.btnLugar1);
        //botonServicio = (Button) findViewById(R.id.buttonServicio);
        botonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        this.gesto = new GestureDetector(this,this);
        gesto.setOnDoubleTapListener(this);

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
            NotificationChannel chanel = new
                    NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanel);

        }
        MyFirebaseMessagingService firebaseId = new MyFirebaseMessagingService();
        firebaseId.onTokenRefresh();

        if (compruebaConexion(this)) {
            Toast.makeText(getApplicationContext(), "Si hay conexion a Internet" , Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Verifique su conexion a Internet" , Toast.LENGTH_LONG).show();
        }

    }

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gesto.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        return false;
    }
}
