package com.example.proyectoplataformas.model;

import java.util.ArrayList;

public class ListaPunto {
    static ArrayList <Punto> puntos;
    private static ListaPunto miListaPunto;

    public  static ListaPunto getMiListaPunto() {
        if (miListaPunto==null) {
            miListaPunto=new ListaPunto();
        }
        return miListaPunto;
    }
    private ListaPunto (){
        puntos = new ArrayList <Punto> ();
    }

    public ArrayList<Punto> getListaPunto(){
        return ListaPunto.puntos;
    }

    public static void addPunto(Punto a) {
        ListaPunto.puntos.add(a);
    }

    public static void eliminarTodos() {
        ListaPunto.puntos.clear();
    }
    public static int longitudLista () {
        return puntos.size();
    }

}
