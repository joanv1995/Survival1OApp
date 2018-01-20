package com.example.usuario.pruebaretrofit.model;

import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class Usuario2 implements Serializable
{

    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("puntFinal")
    @Expose
    private int puntFinal;
    @SerializedName("idMapa")
    @Expose
    private String idMapa;
    @SerializedName("response")
    @Expose
    private int response;
    private final static long serialVersionUID = 3122917227009467179L;

    public Usuario2(){}

    public Usuario2(EditText userName, String admin){}

    public Usuario2(String nombre, String password, String correo) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    public Usuario2(String nombre, String password, String correo, int puntFinal, int response) {
        this.nombre = nombre;
        this.password = password;
        this.correo = correo;
        this.puntFinal = puntFinal;
        this.response = response;
    }

    public Usuario2(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntFinal() {
        return puntFinal;
    }

    public void setPuntFinal(int puntFinal) {
        this.puntFinal = puntFinal;
    }

    public String getIdMapa() {
        return idMapa;
    }

    public void setIdMapa(String idMapa) {
        this.idMapa = idMapa;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }


}