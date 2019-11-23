package com.example.proyectoplataformas.model;

public class Usuario {
    static String idUsuario;
    static String userName;
    static String idActividad;
    static String password;
    private static Usuario miUsuario;
    static int distancia;

    public  static Usuario getMiUsuario() {
        if (miUsuario==null) {
            miUsuario=new Usuario();
        }
        return miUsuario;
    }
    private Usuario (){
        idUsuario = "";
        idActividad = "";
        userName = "";
        distancia = 0;
        password = "";
    }

    public String getNombre(){
        return Usuario.userName;
    }

    public String getIdUsuario(){
        return Usuario.idUsuario;
    }

    public String getIdActividad(){
        return Usuario.idActividad;
    }

    public static void setIdUsuario(String idUsuario) {
        Usuario.idUsuario = idUsuario;
    }

    public static void setIdActividad(String idActividad) {
        Usuario.idActividad = idActividad;
    }

    public static void setUserName(String userName) {
        Usuario.userName = userName;
    }

    public static void setDistancia(int distanciaNueva){Usuario.distancia = distanciaNueva;}

    public int getDistancia(){return Usuario.distancia;}

    public static void setPassword(String pass){Usuario.password = pass;}

    public String getPassword(){return Usuario.password;}

}
