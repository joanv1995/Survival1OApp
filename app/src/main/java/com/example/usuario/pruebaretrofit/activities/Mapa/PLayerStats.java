package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Rect;

/**
 * Created by usuario on 18/01/2018.
 */

public class PLayerStats {

    private int votos;
    private int seguidores;
    private double vida;
    private int margenX;
    private int liniavida;
    private int liniaseguidores;
    private int liniavotos;



    private Rect stats = new Rect();
    private int statsRectAncho;
    private int statsRectAlto;
    private int centroRectX;
    private int centroRectY;

    public int getMargenX() {
        return margenX;
    }

    public void setMargenX(int margenX) {
        this.margenX = margenX;
    }

    public int getLiniavida() {
        return liniavida;
    }

    public void setLiniavida(int liniavida) {
        this.liniavida = liniavida;
    }

    public int getLiniaseguidores() {
        return liniaseguidores;
    }

    public void setLiniaseguidores(int liniaseguidores) {
        this.liniaseguidores = liniaseguidores;
    }

    public int getLiniavotos() {
        return liniavotos;
    }

    public void setLiniavotos(int liniavotos) {
        this.liniavotos = liniavotos;
    }

    public PLayerStats(){
        this.votos = 0;
        this.seguidores = 0;
        this.vida = 100;



    }

    public Rect getStats() {
        return stats;
    }

    public void setStats(Rect stats) {
        this.stats = stats;
    }

    public int getStatsRectAncho() {
        return statsRectAncho;
    }

    public void setStatsRectAncho(int statsRectAncho) {
        this.statsRectAncho = statsRectAncho;
    }

    public int getStatsRectAlto() {
        return statsRectAlto;
    }

    public void setStatsRectAlto(int statsRectAlto) {
        this.statsRectAlto = statsRectAlto;
    }

    public int getCentroRectX() {
        return centroRectX;
    }

    public void setCentroRectX(int centroRectX) {
        this.centroRectX = centroRectX;
    }

    public int getCentroRectY() {
        return centroRectY;
    }

    public void setCentroRectY(int centroRectY) {
        this.centroRectY = centroRectY;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public double getVida() {
        return vida;
    }

    public void setVida(double vida) {
        this.vida = vida;
        if(!hayVida())
            this.vida = 0;
    }
    public void setMedidasCanvas(int canvasWidth, int canvasHeight, int margenAmple, int margenAlt){

        this.statsRectAncho = (canvasWidth*350)/1184;//(canvasWidth*350)/1920;//350;
        this.statsRectAlto = (canvasHeight*200)/720;//(canvasHeight*200)/1008;//200;
        centroRectX = (canvasWidth*350)/1184;//(canvasWidth*350)/1920;//350;
        centroRectY = (canvasHeight*150)/720;//(canvasHeight*150)/1008;//150;
        liniavida = margenAlt/2 + (canvasHeight*80)/720;//(canvasHeight*100)/720;//(canvasHeight*120)/1008;//20;       //100
        liniaseguidores = margenAlt/2 + (canvasHeight*110)/720;//(canvasHeight*150)/720;//(canvasHeight*170)/1008;//40; //160
        liniavotos = margenAlt/2 + (canvasHeight*140)/720;//(canvasHeight*200)/720;//(canvasHeight*220)/1008;//60;      //220
        margenX = margenAmple/2+(canvasWidth*30)/1184;//(canvasWidth*150)/1184;//(canvasWidth*220)/1920;//50
        calculaPosRectangulo();
    }
    public void calculaPosRectangulo(){

        stats.set(centroRectX -statsRectAncho/2, centroRectY -statsRectAlto/2, centroRectX + statsRectAncho/2, centroRectY + statsRectAlto/2);
    }

    public boolean hayVida(){
        return !(vida <= 0);
    }
}
