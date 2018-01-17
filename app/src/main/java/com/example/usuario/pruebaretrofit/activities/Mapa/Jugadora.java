package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by annag on 11/01/2018.
 */

public class Jugadora {

    private final String TAG = this.getClass().getSimpleName();
    //TODO fer instancia

    // Imatge bitMap
    private static final int BMP_ROWS_JUGADORA = 4;
    private static final int BMP_COLUMNS_JUGADORA = 3;
    private Bitmap bmp;
    private static final int[] DIRECTION_TO_ANIMATION_MAP_JUGADORA = {2, 1, 3, 0};
    private GameView gameView;
    private int currentFrame = 0;

    private int width; // de la imatge
    private int height;

    private PointF posicion;
    private int direccio; // direction = 0 right, 1 left, 2 up, 3 down,
    private Rect src = new Rect();
    private int speed = 1;

    public Jugadora(GameView gameView,Bitmap bmp, PointF posicion) {
        this.bmp = bmp;
        this.gameView = gameView;
        this.width = bmp.getWidth() / BMP_COLUMNS_JUGADORA;
        this.height = bmp.getHeight() / BMP_ROWS_JUGADORA;
        this.posicion = posicion;
    }

    public void setPosicion(PointF posicion) {
        this.posicion = posicion;
    }
    public void setPosicion(float posicionX, float posicionY) {
        this.posicion.set(posicionX,posicionY);
        currentFrame = ++currentFrame % BMP_COLUMNS_JUGADORA;
    }
    public void setDireccio(int direccio) {
        this.direccio = direccio;
    }
    public PointF getPosicion() {
        return posicion;
    }
    public int getSpeed() {
        return speed;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    private void update(){
        // canviar la posicio
    }

    protected Rect onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");
        //update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        src.set(srcX, srcY, srcX + width, srcY + height); //retalla la imatge segons l'animacio
        return src;
    }

    private int getAnimationRow() {
        //double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        //int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP_JUGADORA[direccio];
    }

    /*private boolean estaDinsDeMalla(PointF p){
        return p.x <= gameView.getMalla()[0].length-gameView.getZoomBitmap()-1 && p.x >= gameView.getZoomBitmap()-1
                && p.y <= (gameView.getMalla().length-gameView.getZoomBitmap()-1) && p.y >= gameView.getZoomBitmap()-1;
    }

    private boolean esPotTrepitjar(PointF p){
        // si per les celes proximes no toca la imatge del bitmap amb taules o merdes
        int[] vec = {gameView.getZoomBitmap()-1, -(gameView.getZoomBitmap()-1), 0, 0};
        // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu
        try {
            for (int i = 0; i < 4; i++) {
                if(!gameView.getMalla()[(int) p.y + vec[vec.length-1-i]][(int) p.x + vec[i]].contains("-"))
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        // Si la cela p hi ha cami:
        return gameView.getMalla()[(int)p.y][(int)p.x].contains("-");
    }*/
}
