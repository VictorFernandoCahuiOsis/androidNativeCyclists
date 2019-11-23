
package com.example.proyectoplataformas.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectoplataformas.R;
import com.example.proyectoplataformas.model.NotificacionUsuario;

import java.util.ArrayList;

public class AdaptadorNotificacion extends BaseAdapter {
    private ArrayList<NotificacionUsuario> listItems;
    private Context context;

    public AdaptadorNotificacion (Context context, ArrayList <NotificacionUsuario> listItems) {
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

        final NotificacionUsuario item = (NotificacionUsuario)getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.items_notificacion, null);

        TextView titulo = (TextView)convertView.findViewById(R.id.tvTitulo);
        TextView descripcion = (TextView)convertView.findViewById(R.id.tvDescripcion);

        titulo.setText(item.titulo);
        descripcion.setText(item.descripcion);

        return convertView;
    }
}
