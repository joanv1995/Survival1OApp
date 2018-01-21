package com.example.usuario.pruebaretrofit.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class ListaUsuariosResponse implements Serializable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("puntFinal")
    @Expose
    private Integer puntFinal;
    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("idMapa")
    @Expose
    private String idMapa;
    private final static long serialVersionUID = -545375899782709746L;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPuntFinal() {
        return puntFinal;
    }

    public void setPuntFinal(Integer puntFinal) {
        this.puntFinal = puntFinal;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public String getIdMapa() {
        return idMapa;
    }

    public void setIdMapa(String idMapa) {
        this.idMapa = idMapa;
    }

}