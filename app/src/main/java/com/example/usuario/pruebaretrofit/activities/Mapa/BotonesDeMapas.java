package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Rect;

/**
 * Created by annag on 10/01/2018.
 */

public class BotonesDeMapas {

    //private GameView gameView;
    //private int canvasHeight, canvasWidth;

    // dibuixar botons
    private int botonRectAncho, botonRectAlto;

    private int centreRect , centreRectY;
    private Rect recVerticalEntero = new Rect();
    private Rect botonRecVertArriba= new Rect();
    private Rect botonRecVertBajo= new Rect();

    private Rect recHorizontalEntero= new Rect();
    private Rect botonRecHorizLeft= new Rect();
    private Rect botonRecHorizRigth= new Rect();

    private int centreX1, centreY1, radi;
    private int centreX2, centreY2;
    private Rect botonCercleA = new Rect();
    private Rect botonCercleB = new Rect();


    public BotonesDeMapas() {    }

    public Rect getRecVerticalEntero() {
        return recVerticalEntero;
    }
    public Rect getBotonRecVertArriba() {
        return botonRecVertArriba;
    }
    public Rect getBotonRecVertBajo() {
        return botonRecVertBajo;
    }
    public Rect getRecHorizontalEntero() {
        return recHorizontalEntero;
    }
    public Rect getBotonRecHorizLeft() {
        return botonRecHorizLeft;
    }
    public Rect getBotonRecHorizRigth() {
        return botonRecHorizRigth;
    }
    public Rect getBotonCercleA() {
        return botonCercleA;
    }
    public Rect getBotonCercleB() {
        return botonCercleB;
    }
    public int getCentreX1() {
        return centreX1;
    }
    public int getCentreY1() {
        return centreY1;
    }
    public int getRadi() {
        return radi;
    }
    public int getCentreX2() {
        return centreX2;
    }
    public int getCentreY2() {
        return centreY2;
    }

    public void setMedidasCanvas(int canvasWidth, int canvasHeight){
        //this.canvasHeight = canvasHeight;
        //this.canvasWidth = canvasWidth;

        botonRectAncho = (canvasWidth*110)/1920;//100;
        botonRectAlto = (canvasHeight*310)/1008;//300;
        centreRect = (canvasWidth*260)/1920;//260;
        centreRectY = canvasHeight-(canvasHeight*150)/1008;//150;

        centreX1 = canvasWidth- centreRect +(canvasWidth*260)/1920;//200;
        centreY1 = centreRectY -(canvasHeight*70)/1008;//70;
        radi = (canvasHeight*70)/1008;//70; // botons rodons (el rectangle es per fer el contains
        centreX2 = canvasWidth- centreRect +(canvasWidth*110)/1920;//110;
        centreY2 = centreRectY +(canvasHeight*80)/1008;//80;

        calculaBotones();
    }

    private void calculaBotones(){
        recVerticalEntero.set(centreRect -botonRectAncho/2, centreRectY -botonRectAlto/2, centreRect + botonRectAncho/2, centreRectY + botonRectAlto/2);
        botonRecVertArriba.set(centreRect -botonRectAncho/2, centreRectY -botonRectAlto/2, centreRect + botonRectAncho/2, centreRectY - botonRectAncho/2);
        botonRecVertBajo.set(centreRect -botonRectAncho/2, centreRectY + botonRectAncho/2, centreRect + botonRectAncho/2, centreRectY + botonRectAlto/2);

        recHorizontalEntero.set(centreRect -botonRectAlto/2, centreRectY -botonRectAncho/2, centreRect + botonRectAlto/2, centreRectY + botonRectAncho/2);
        botonRecHorizLeft.set(centreRect -botonRectAlto/2, centreRectY -botonRectAncho/2, centreRect -botonRectAncho/2, centreRectY + botonRectAncho/2);
        botonRecHorizRigth.set(centreRect + botonRectAncho/2, centreRectY -botonRectAncho/2, centreRect + botonRectAlto/2, centreRectY + botonRectAncho/2);

        botonCercleA.set(centreX1 - radi, centreY1 - radi, centreX1 + radi, centreY1 + radi);
        botonCercleB.set(centreX2 - radi, centreY2 - radi, centreX2 + radi, centreY2 + radi);
    }
}
