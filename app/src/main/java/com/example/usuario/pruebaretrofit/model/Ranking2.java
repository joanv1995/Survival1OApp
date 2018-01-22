package com.example.usuario.pruebaretrofit.model;

/**
 * Created by usuario on 21/01/2018.
 */
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ranking2 implements Serializable
{

    @SerializedName("idmapa")
    @Expose
    private String idmapa;
    @SerializedName("puntuaciontot")
    @Expose
    private Integer puntuaciontot;
    @SerializedName("seguidores")
    @Expose
    private Integer seguidores;
    @SerializedName("usuario")
    @Expose
    private String usuario;
    @SerializedName("votos")
    @Expose
    private Integer votos;
    private final static long serialVersionUID = 3505279293833851666L;

    public String getIdmapa() {
        return idmapa;
    }

    public void setIdmapa(String idMapa) {
        this.idmapa = idMapa;
    }

    public Integer getPuntuaciontot() {
        return puntuaciontot;
    }

    public void setPuntuaciontot(Integer puntuaciontot) {
        this.puntuaciontot = puntuaciontot;
    }

    public Integer getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Integer seguidores) {
        this.seguidores = seguidores;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getVotos() {
        return votos;
    }

    public void setVotos(Integer votos) {
        this.votos = votos;
    }

}