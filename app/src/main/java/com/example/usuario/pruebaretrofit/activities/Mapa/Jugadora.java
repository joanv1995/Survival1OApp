package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.example.usuario.pruebaretrofit.model.Inventario;

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

    private Rect anima = new Rect();
    private PointF posicion;
    private int direccio; // direction = 0 right, 1 left, 2 up, 3 down,
    private Rect src = new Rect();
    private int speed = 4;
    private boolean meTengoQueMover = false;
    private boolean estoyEnElHospital = false;

    private Inventario inventario;

    public Jugadora(GameView gameView,Bitmap bmp, PointF posicion) {
        this.bmp = bmp;
        this.gameView = gameView;
        this.width = bmp.getWidth() / BMP_COLUMNS_JUGADORA;
        this.height = bmp.getHeight() / BMP_ROWS_JUGADORA;
        this.posicion = posicion;
        inventario = new Inventario();
    }

    public void setPosicion(PointF posicion) {
        this.posicion = posicion;
        currentFrame = ++currentFrame % BMP_COLUMNS_JUGADORA;
    }
    public void setPosicion(float posicionX, float posicionY) {
        this.posicion.set(posicionX,posicionY);
        currentFrame = ++currentFrame % BMP_COLUMNS_JUGADORA;
    }
    public void runCurrentFrame(){
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
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Bitmap getBmp() {
        return bmp;
    }
    public boolean isMeTengoQueMover() {
        return meTengoQueMover;
    }
    public void setMeTengoQueMover(boolean meTengoQueMover) {
        this.meTengoQueMover = meTengoQueMover;
    }
    public int getDireccio() {
        return direccio;
    }
    public Rect getAnima() {
        return anima;
    }
    public Inventario getInventario() {
        return inventario;
    }
    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
    public boolean isEstoyEnElHospital() {
        return estoyEnElHospital;
    }
    public void setEstoyEnElHospital(boolean estoyEnElHospital) {
        this.estoyEnElHospital = estoyEnElHospital;
    }
    public void setAnima(Rect anima) {
        this.anima = anima;
    }

    protected void calculaAnimes(int zoom){
        // [files][columnes]
        this.anima.set((int) posicion.x - zoom+1, (int) posicion.y - zoom,
                (int) posicion.x + zoom, (int) posicion.y + zoom);
    }
    protected void update(){
        // canviar la posicio
        switch (direccio){
            case 1:
                setPosicion(posicion.x - speed,posicion.y);
                break;
            case 0:
                setPosicion(posicion.x + speed,posicion.y);
                break;
            case 2:
                setPosicion(posicion.x,posicion.y - speed);
                break;
            case 3:
                setPosicion(posicion.x,posicion.y + speed);
                break;
        }
    }

    protected Rect onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");
        //if(meTengoQueMover) // si estoy apretando, cambio de posicion
        //    update();
        Log.d(TAG,"X: " + posicion.x);Log.d(TAG,"Y: " + posicion.y);
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


}
