
package com.example.usuario.pruebaretrofit.model;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Objeto implements Serializable
{

    @SerializedName("descripcionObjeto")
    @Expose
    private String descripcionObjeto;
    @SerializedName("nombreObjeto")
    @Expose
    private String nombreObjeto;
    @SerializedName("peso")
    @Expose
    private Integer peso;
    @SerializedName("posicionObjeto")
    @Expose
    private Punto posicionObjeto;
    @SerializedName("tamanoObjCelda")
    @Expose
    private Integer tamanoObjCelda;
    private final static long serialVersionUID = 4325323640616557743L;

    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }

    public void setDescripcionObjeto(String descripcionObjeto) {
        this.descripcionObjeto = descripcionObjeto;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Punto getPosicionObjeto() {
        return posicionObjeto;
    }

    public void setPosicionObjeto(Punto posicionObjeto) {
        this.posicionObjeto = posicionObjeto;
    }

    public Integer getTamanoObjCelda() {
        return tamanoObjCelda;
    }

    public void setTamanoObjCelda(Integer tamanoObjCelda) {
        this.tamanoObjCelda = tamanoObjCelda;
    }



}
