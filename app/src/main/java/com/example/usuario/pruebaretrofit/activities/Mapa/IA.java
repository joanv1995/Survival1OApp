package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

import static java.lang.Math.sqrt;

public class IA {

    private final String TAG = this.getClass().getSimpleName();

    // Imatge bitMap
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private Bitmap bmp;

    private GameView gameView;
    private int currentFrame = 0;
    private int width; // del canvas
    private int height;

    private String idIa;
    private PointF posicion;
    private PointF posObjetivo;
    private PointF posAntiga;
    private boolean enEspera = false;
    private int speed = 1; // TODO: mirar que faig amb la velocitat
    private boolean direccioX_Left, direccioY_Up; // true: up, false: down
    private int direccio; // direction = 0 right, 1 left, 2 up, 3 down,

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = {2, 1, 3, 0};//{ 3, 1, 0, 2 };

    // per dibuixar
    PointF act = new PointF();
    PointF p = new PointF();
    Rect src = new Rect();

    public IA(GameView gameView, Bitmap bmp, String idIa, PointF posicion, PointF posObjetivo) {
        Log.d(TAG, "inicialitzo un IA");
        this.gameView=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.idIa = idIa;
        this.posicion = posicion;
        this.posObjetivo = posObjetivo;
        saberDireccio();
        /*Random rnd = new Random();
        xSpeed = rnd.nextInt(10)-5;
        ySpeed = rnd.nextInt(10)-5;
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);*/
    }

    private void saberDireccio(){
        this.direccioX_Left = posicion.x >= posObjetivo.x ;
        this.direccioY_Up = posicion.y >= posObjetivo.y ;
    }

    private boolean haArribat(){
        if(direccioY_Up){
            if (direccioX_Left)
                return posicion.y <= posObjetivo.y && posicion.x <= posObjetivo.x;
            else
                return posicion.y <= posObjetivo.y && posicion.x >= posObjetivo.x;
        } else {
            if (direccioX_Left)
                return posicion.y >= posObjetivo.y && posicion.x <= posObjetivo.x;
            else
                return posicion.y >= posObjetivo.y && posicion.x >= posObjetivo.x;
        }
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
        if (!posicion.equals(posObjetivo)) {
            act = new PointF();
            try {
                double min = 10000000;
                int[] vecPos = {speed, -speed, 0, 0};
                // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu
                for (int i = 0; i < 4; i++) {
                    p.set((int) getPosicion().x + vecPos[i], (int) getPosicion().y + vecPos[vecPos.length-1-i]);
                    if(calculaDistancia(p,getPosObjetivo()) < min && esPotTrepitjar(p) && !p.equals(posAntiga)){
                        direccio = i;
                        min = calculaDistancia(p,getPosObjetivo());
                        act.set(p);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
            // si m'ho ha calculat bé, actualitzo posicio
            if(!enEspera && estaDinsDeMalla(act)){ //&& !act.equals(getPosicion())) {
                posAntiga = new PointF(getPosicion().x,getPosicion().y);
                setPosicion(act);
                if(haArribat())
                    enEspera = true;
            }else {
                if(!estaDinsDeMalla(act))
                    Log.e(TAG,"No esta dins la malla");
            }

            if (enEspera)
                Log.d(TAG,"Ha arribat");

            Log.d(TAG,"posX " + getPosicion().x);
            Log.d(TAG,"posY " + getPosicion().y);

            currentFrame = ++currentFrame % BMP_COLUMNS;
        }
    }

    private double calculaDistancia(PointF p1, PointF p2){
        return  sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y));
    }

    protected Rect onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");
        update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        src.set(srcX, srcY, srcX + width, srcY + height); //retalla la imatge segons l'animacio
        return src;
    }

    private int getAnimationRow() {
        //double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        //int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direccio];
    }
    private boolean estaDinsDeMalla(PointF p){
        return p.x <= gameView.getMalla()[0].length && p.x >= 0
                && p.y <= (gameView.getMalla().length-gameView.getZoomBitmap()-1) && p.y >= gameView.getZoomBitmap()-1;
    }

    private boolean esPotTrepitjar(PointF p){
        int[] vec = {gameView.getZoomBitmap()-1, -(gameView.getZoomBitmap()-1), 0, 0};
        // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu
        for (int i = 0; i < 4; i++) {
            if(!gameView.getMalla()[(int) p.y + vec[vec.length-1-i]][(int) p.x + vec[i]].contains("-"))
                return false; //TODO: als costats ho fa be, pero quan va cap a munt es pot treure espai
        }
        return gameView.getMalla()[(int)p.y][(int)p.x].contains("-");
    }
/*
    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }*/
}