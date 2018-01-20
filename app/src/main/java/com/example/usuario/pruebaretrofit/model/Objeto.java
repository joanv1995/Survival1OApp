
package com.example.usuario.pruebaretrofit.model;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;

import com.example.usuario.pruebaretrofit.activities.Mapa.Jugadora;
import com.example.usuario.pruebaretrofit.activities.Mapa.MapaEscuela;
import com.example.usuario.pruebaretrofit.activities.Mapa.MapaGrande;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

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
    @SerializedName("posicion")
    @Expose
    private PointF posicion;
    @SerializedName("tamanoObjCelda")
    @Expose
    private Integer tamanoObjCelda;
    private final static long serialVersionUID = 4325323640616557743L;

    /** Noves coses **/
    private final String TAG = this.getClass().getSimpleName();

    private Bitmap bmp;
    //private int width; // de la imatge
    //private int height;

    private MapaGrande mapa;
    private Rect anima = new Rect();
    private Rect src;
    private int zoomDeCaracteres;

    private boolean enInventario = false;
    private boolean meEstanDestruyendo = false, destruido = false;
    private int contadorDeLaDestruccion = 0;

    int minX = 281, minY = 81;
    int maxX = 361, maxY = 120;
    Random r = new Random();


    public Objeto(String nombreObjeto, PointF posicion, Bitmap bmp, MapaGrande mapa, int zoom) {
        this.nombreObjeto = nombreObjeto;
        this.posicion = posicion;
        this.bmp = bmp;
        this.mapa = mapa;
        this.zoomDeCaracteres = zoom;
        //this.width = bmp.getWidth();
        //this.height = bmp.getHeight();
        calculaAnimes();
        src = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
    }
    public Objeto(String nombreObjeto,  Bitmap bmp, MapaGrande mapa, int zoom) {
        minX = zoom; minY = zoom; maxX = mapa.getMalla()[0].length - zoom; maxY = mapa.getMalla().length - zoom;
        this.nombreObjeto = nombreObjeto;
        PointF p = new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        while (!esPotTrepitjar(p, mapa.getMalla(), zoom)){
            p.set(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        }
        this.posicion = p;
        this.bmp = bmp;
        this.mapa = mapa;
        this.zoomDeCaracteres = zoom;
        //this.width = bmp.getWidth();
        //this.height = bmp.getHeight();
        calculaAnimes();
        src = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
    }

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
    /*public Integer getPeso() {
        return peso;
    }*/
    public void setPeso(Integer peso) {
        this.peso = peso;
    }
    public PointF getPosicion() {
        return posicion;
    }
    public void setPosicion(PointF posicion) {
        this.posicion = posicion;
    }
    public Integer getTamanoObjCelda() {
        return tamanoObjCelda;
    }
    public void setTamanoObjCelda(Integer tamanoObjCelda) {
        this.tamanoObjCelda = tamanoObjCelda;
    }
    public Bitmap getBmp() {
        return bmp;
    }
    public int getZoomDeCaracteres() {
        return zoomDeCaracteres;
    }
    public Rect getAnima() {
        return anima;
    }
    public boolean isEnInventario() {
        return enInventario;
    }
    public boolean isDestruido() {
        return destruido;
    }
    public void setDestruido(boolean destruido) {
        this.destruido = destruido;
    }
    public void setEnInventario(boolean enInventario) {
        this.enInventario = enInventario;
    }
    public boolean isMeEstanDestruyendo() {
        return meEstanDestruyendo;
    }
    public void setMeEstanDestruyendo(boolean meEstanDestruyendo) {
        this.meEstanDestruyendo = meEstanDestruyendo;
    }
    public void runContadorDeLaDestruccion(){
        contadorDeLaDestruccion++;
    }

    private void calculaAnimes(){
        this.anima.set((int) posicion.x - zoomDeCaracteres+1, (int) posicion.y - zoomDeCaracteres,
                (int) posicion.x + zoomDeCaracteres-1, (int) posicion.y + zoomDeCaracteres);
    }
    public Rect onDraw(Canvas canvas) {
        //Log.d(TAG,"onDraw");
        //update(jugadora,zoomBitmap);
        //int srcX = currentFrame * width;
        //int srcY = getAnimationRow() * height;
        //src.set(srcX, srcY, srcX + width, srcY + height); //retalla la imatge segons l'animacio
        if(contadorDeLaDestruccion > 10){
            destruido = true;
        }
        return src;
    }

}
