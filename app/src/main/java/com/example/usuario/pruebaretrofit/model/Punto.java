package com.example.usuario.pruebaretrofit.model;


import android.graphics.PointF;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static java.lang.Math.sqrt;

public class Punto implements Serializable {

    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;


    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    private double calculaDistancia(Punto p2){
        return  sqrt((x - p2.getX())*(x - p2.getX()) + (y - p2.getY())*(y - p2.getY()));
    }
}
