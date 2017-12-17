package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class IA {

    private final String TAG = this.getClass().getSimpleName();

    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    /*private int x = 0;
    private int y = 0;
    private int xSpeed = 0;
    private int ySpeed = 0;*/
    private GameView gameView;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width;
    private int height;

    private String idIa;
    private PointF posicion;
    private PointF posObjetivo;

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

    public IA(GameView gameView, Bitmap bmp, String idIa, PointF posicion, PointF posObjetivo) {
        Log.d(TAG, "inicialitzo un IA");
        this.gameView=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.idIa = idIa;
        this.posicion = posicion;
        this.posObjetivo = posObjetivo;
        /*Random rnd = new Random();
        xSpeed = rnd.nextInt(10)-5;
        ySpeed = rnd.nextInt(10)-5;
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);*/
    }

    public PointF getPosicion() {
        return posicion;
    }
    public void setPosicion(PointF posicion) {
        this.posicion = posicion;
    }
    public PointF getPosObjetivo() {
        return posObjetivo;
    }
    public void setPosObjetivo(PointF posObjetivo) {
        this.posObjetivo = posObjetivo;
    }
    public Bitmap getBmp() {
        return bmp;
    }

    private void update() {
        Log.d(TAG, "Update: moc una casella");
        /*if (x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;*/


        if (!posicion.equals(posObjetivo)) {
            PointF act = null;
            try {
                //double distancia = getPosicion().length(getPosObjetivo().x,getPosObjetivo().y);
                double min = 1000;
                act = new PointF();
                int[] vecPos = {1, -1, 0, 0};
                // de les quatre celes del voltant, miro quina es la que esta mes aprop del objectiu
                for (int i = 0; i < 4; i++) {
                    PointF p = new PointF((int) getPosicion().x + vecPos[i], (int) getPosicion().y + vecPos[vecPos.length-1-i]);
                    if(getPosObjetivo().length(p.x,p.y) < min ) {
                        min = getPosObjetivo().length(p.x,p.y);
                        act.set(p);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
            // si m'ho ha calculat bé, actualitzo posicio
            if(!act.equals(new PointF()))
                setPosicion(act);
            else {
                Log.e(TAG,"No calcula la pròxima casella");
            }
            currentFrame = ++currentFrame % BMP_COLUMNS;
        }
    }

    public Rect onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");
        update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        return src;
        //Rect dst = new Rect((int)getPosicion().x, (int)getPosicion().y, (int)getPosicion().x + width, (int)getPosicion().y + height);
        //canvas.drawBitmap(bmp, src, dst, null);
    }

    private int getAnimationRow() {
        //double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        //int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[1];
    }
/*
    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }*/
}