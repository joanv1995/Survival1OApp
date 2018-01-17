package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.usuario.pruebaretrofit.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

public class GameView extends SurfaceView {
    private final String TAG = this.getClass().getSimpleName();
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private int canvasWidth, canvasHeight;
    protected int margeAmpl = 0, margeAlt = 0;
    private Rect rectangleCanvas = new Rect();

    // per dibuixar
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    protected int ample, altura;
    private int zoomBitmap = 5;

    private MapaEscuela mapaEscuela;
    private MapaGrande mapaGrande;
    private int anchoMalla = 200, altoMalla = 100;  // Que todas las mallas de mapas sean de 200*100
    // per amager la barra de navegacio
    //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //        | View.SYSTEM_UI_FLAG_FULLSCREEN;


    private int quinMapa = 1; // mapaEscuela=0, mapaGrande = 1, minijuegos = 2

    public GameView(Context context) {
        super(context);
        Log.d(TAG, "constructor GameView");
        gameLoopThread = new GameLoopThread(this);
        mapaEscuela = new MapaEscuela(context, this); // AQUI ESTA EL MAPA
        mapaGrande = new MapaGrande(context, this);
        //this.setSystemUiVisibility(uiOptions);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                Log.d(TAG, "gameLoopThread.start");

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged");
                Log.d(TAG, "width " + width);
                Log.d(TAG, "height " + height);
                canvasHeight = midaCanvas(height, altoMalla);
                canvasWidth = midaCanvas(width, anchoMalla);
                if(quinMapa == 0){
                    mapaEscuela.getBotones().setMedidasCanvas(canvasWidth,canvasHeight);
                } else if (quinMapa == 1) {
                    // TODO: lo mismo que arriba
                }
                margeAmpl = width - canvasWidth;
                margeAlt = height - canvasHeight;
                rectangleCanvas.set(margeAmpl / 2, margeAlt / 2, margeAmpl / 2 + canvasWidth, margeAlt / 2 + canvasHeight);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceDestroyed");
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        // DO NOTHING
                    }
                }
            }
        });
    }

    public int getZoomBitmap() {
        return zoomBitmap;
    }

    private int midaCanvas(int canvasT, int length) {
        boolean trobat = false;
        int ampladaCanvas = 0;

        if (canvasT % length != 0) {
           for (int i = canvasT; i > length; i--) {
                if (i % length == 0) {
                    ampladaCanvas = i;
                    trobat = true;
                    break;// TODO; mirar esto porque no redondeo hacia abajo

                }
            }
        } else
            ampladaCanvas = canvasT;
        if (!trobat)
            Log.e(TAG, "No s'ha pogut adaptar el mapa");
        return ampladaCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long startTime, startTime2;
        //Log.d(TAG, "onDraw");
        canvas.drawColor(getResources().getColor(R.color.Black)); // Fondo

        //Log.d(TAG,"canvasWidth " + canvas.getWidth());
        //Log.d(TAG,"canvasHeight " + canvas.getHeight());

        altura = canvasHeight / altoMalla;
        ample = canvasWidth / anchoMalla;

        //Log.d(TAG,"canvasWidth " + ample);
        //Log.d(TAG,"canvasHeight " + altura);

        if (quinMapa == 0) { //Mapa escola

            canvas = mapaEscuela.dibujoElMapaEscuela(canvas, ample, altura, margeAlt, margeAmpl);

        } else if(quinMapa == 1){
            canvas = mapaGrande.dibujoElMapaGeneral(canvas, ample, altura, margeAlt, margeAmpl, anchoMalla, altoMalla);

        }else if(quinMapa == 2){
            // TODO: ALEX, qui va tu

        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            // direction = 0 right, 1 left, 2 up, 3 down,
            //event.getX(), event.getY()
            int x = Math.round(event.getX()), y = Math.round(event.getY());

            switch (quinMapa){
                case 0: //MapaEscola
                    mapaEscuela.interactionOneTouch(x,y);
                /*
                    if(mapaEscuela.getBotones().getBotonRecHorizLeft().contains(x,y)){
                        Log.d(TAG,"boton Left");
                        mapaEscuela.getJugadora().setDireccio(1);
                        mapaEscuela.getJugadora().setPosicion(mapaEscuela.getJugadora().getPosicion().x - mapaEscuela.getJugadora().getSpeed(),mapaEscuela.getJugadora().getPosicion().y);
                    }else if(mapaEscuela.getBotones().getBotonRecHorizRigth().contains(x,y)) {
                        Log.d(TAG, "boton Right");
                        mapaEscuela.getJugadora().setDireccio(0);
                        mapaEscuela.getJugadora().setPosicion(mapaEscuela.getJugadora().getPosicion().x + mapaEscuela.getJugadora().getSpeed(),mapaEscuela.getJugadora().getPosicion().y);
                    } else if (mapaEscuela.getBotones().getBotonRecVertArriba().contains(x,y)){
                        Log.d(TAG, "boton Arriba");
                        mapaEscuela.getJugadora().setDireccio(2);
                        mapaEscuela.getJugadora().setPosicion(mapaEscuela.getJugadora().getPosicion().x,mapaEscuela.getJugadora().getPosicion().y - mapaEscuela.getJugadora().getSpeed());
                    } else if (mapaEscuela.getBotones().getBotonRecVertBajo().contains(x,y)){
                        Log.d(TAG, "boton Abajo");
                        mapaEscuela.getJugadora().setDireccio(3);
                        mapaEscuela.getJugadora().setPosicion(mapaEscuela.getJugadora().getPosicion().x,mapaEscuela.getJugadora().getPosicion().y + mapaEscuela.getJugadora().getSpeed());
                    }
                    if(mapaEscuela.getBotones().getBotonCercleA().contains(x,y)){
                        Log.d(TAG, "boton A");
                    }
                    if(mapaEscuela.getBotones().getBotonCercleB().contains(x,y)){
                        Log.d(TAG, "boton B");
                    }*/
            }

            //hideUI();
            return super.onTouchEvent(event);
        }
    }

    private void hideUI() {
        /*this.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GameView.this.setSystemUiVisibility(uiOptions);
            }
        }, 0);*/
    }

}