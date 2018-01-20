package com.example.usuario.pruebaretrofit.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inventario implements Serializable {

    @SerializedName("listaObjetos")
    @Expose
    private List<Objeto> listaObjetos = null;
    @SerializedName("pesoMax")
    @Expose
    private Integer pesoMax;
    @SerializedName("pesoActual")
    @Expose
    private Integer pesoActual;
    private final static long serialVersionUID = 2614813741752022240L;

    public List<Objeto> getListaObjetos() {
        return listaObjetos;
    }



    public Integer getPesoMax() {
        return pesoMax;
    }

    public void setPesoMax(Integer pesoMax) {
        this.pesoMax = pesoMax;
    }


    public void setListaObjetos(List<Objeto> listaObjetos) {
        this.listaObjetos = listaObjetos;
        this.pesoActual = 0;
        for (Objeto o : listaObjetos) {
            //pesoActual = pesoActual + o.getPeso();
        }
    }
}


