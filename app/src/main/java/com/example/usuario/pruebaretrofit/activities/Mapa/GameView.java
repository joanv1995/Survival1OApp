package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.usuario.pruebaretrofit.R;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView {
    private final String TAG = this.getClass().getSimpleName();
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Context context;
    private int canvasWidth, canvasHeight;
    protected int margeAmpl = 0, margeAlt = 0;
    private Rect rectangleCanvas = new Rect();
    private CountDownTimer timer, timerPolice;
    //private String times;

    // per dibuixar
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    protected int ample, altura;
    private int zoomBitmap = 5;

    private MapaEscuela mapaEscuela;
    private MapaGrande mapaGrande;
    private Minijuego minijuego;
    private int anchoMalla = 200, altoMalla = 100;  // Que todas las mallas de mapas sean de 200*100
    // per amager la barra de navegacio
    //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //        | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private PLayerStats stats;
    private BotonesDeMapas botones;
    private Jugadora jugadora;

    private int count;

    private int quinMapa = 1; // mapaEscuela=0, mapaGrande = 1, minijuegos = 2

    private PointF portaEscola = new PointF(100,98);
    private PointF portaEscolaMapaGran = new PointF(201,121);

    public GameView(Context context) {
        super(context);
        this.context = context;
        Log.d(TAG, "constructor GameView");

        jugadora = createJugadora(R.mipmap.bad33, new PointF(10,10));
        botones = new BotonesDeMapas();
        stats = new PLayerStats();

        gameLoopThread = new GameLoopThread(this);
        mapaEscuela = new MapaEscuela(context, this, jugadora, botones, stats); // AQUI ESTA EL MAPA
        mapaGrande = new MapaGrande(context, this, jugadora, botones, stats);


        //minijuego = new Minijuego(context, this);
        //this.setSystemUiVisibility(uiOptions);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                timer = new CountDownTimer(180000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished;
                        count++;
                        if(count==2){
                            stats.setVotos(stats.getVotos()+1);
                            count =0;
                        }
                        mapaGrande.setTimes(String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                    }
                    @Override
                    public void onFinish() {
                        ///PARTIDA ACABADA ---  NIVEL SUPERADO
                    }
                }.start();
                timerPolice = new CountDownTimer(20000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mapaGrande.setAlerta(false);
                        long millis = millisUntilFinished;
                        mapaGrande.setTimesPolice(String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                    }
                    @Override
                    public void onFinish() {
                        mapaGrande.setAlerta(true);
                    }
                }.start();
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
                    mapaEscuela.getStats().setMedidasCanvas(canvasWidth,canvasHeight,margeAmpl,margeAlt);
                } else if (quinMapa == 1) {
                    mapaGrande.getBotones().setMedidasCanvas(canvasWidth,canvasHeight);
                    mapaGrande.getStats().setMedidasCanvas(canvasWidth,canvasHeight,margeAmpl,margeAlt);
                } else if (quinMapa == 2){
                    minijuego.getBotones().setMedidasCanvas(canvasWidth,canvasHeight);
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
    public int getCanvasWidth() {
        return canvasWidth;
    }
    public int getCanvasHeight() {
        return canvasHeight;
    }

    private int midaCanvas(int canvasT, int length) {
        boolean trobat = false;
        int ampladaCanvas = 0;

        if (canvasT % length != 0) {
           for (int i = canvasT; i > length; i--) {
                if (i % length == 0) {
                    ampladaCanvas = i;
                    trobat = true;
                    break;

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
        canvas.drawColor(getResources().getColor(R.color.Black)); // Fondo
        altura = canvasHeight / altoMalla;
        ample = canvasWidth / anchoMalla;

        if (quinMapa == 0) { //mapa escola
            canvas = mapaEscuela.dibujoElMapaEscuela(canvas, ample, altura, margeAlt, margeAmpl);

        } else if(quinMapa == 1){
            canvas = mapaGrande.dibujoElMapaGeneral(canvas, ample, altura, margeAlt, margeAmpl, anchoMalla, altoMalla);

        }else if(quinMapa == 2){
            // TODO: ALEX, qui va tu
            canvas = minijuego.dibujoMinijuego(canvas, ample, altura, margeAlt, margeAmpl);
        }
    }

    private Jugadora createJugadora(int resouce, PointF pos){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new Jugadora(this, bmp, pos);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            // direction = 0 right, 1 left, 2 up, 3 down,
            //event.getX(), event.getY()
            int x = Math.round(event.getX()), y = Math.round(event.getY());
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    if(quinMapa == 0){
                        if(mapaEscuela.getBotones().getBotonRecHorizLeft().contains(x,y)){ //boton Left
                            mapaEscuela.getJugadora().setDireccio(1);
                            mapaEscuela.getJugadora().setMeTengoQueMover(true);
                        }else if(mapaEscuela.getBotones().getBotonRecHorizRigth().contains(x,y)) {//boton Right
                            mapaEscuela.getJugadora().setDireccio(0);
                            mapaEscuela.getJugadora().setMeTengoQueMover(true);
                        } else if (mapaEscuela.getBotones().getBotonRecVertArriba().contains(x,y)){//boton Arriba
                            mapaEscuela.getJugadora().setDireccio(2);
                            mapaEscuela.getJugadora().setMeTengoQueMover(true);
                        } else if (mapaEscuela.getBotones().getBotonRecVertBajo().contains(x,y)){ //boton Abajo
                            mapaEscuela.getJugadora().setDireccio(3);
                            mapaEscuela.getJugadora().setMeTengoQueMover(true);
                        }
                        if(mapaEscuela.getBotones().getBotonCercleA().contains(x,y)){ // boton A
                            mapaEscuela.mePuedoCambiarDeMapa();
                        }
                        if(mapaEscuela.getBotones().getBotonCercleB().contains(x,y)){ // boton B


                        }
                    } else if(quinMapa == 1){
                        //mapaGrande.processButtons(x,y); // le paso la direccion
                        //mapaGrande.getJugadora().setMeTengoQueMover(true); // y le cambio el nuevo boleano para que sepa que estoy apretando el boton

                        if(mapaGrande.getBotones().getBotonRecHorizLeft().contains(x,y)){ //boton Left
                            mapaGrande.getJugadora().setDireccio(1);
                            mapaGrande.getJugadora().setMeTengoQueMover(true);
                        }else if(mapaGrande.getBotones().getBotonRecHorizRigth().contains(x,y)) {//boton Right
                            mapaGrande.getJugadora().setDireccio(0);
                            mapaGrande.getJugadora().setMeTengoQueMover(true);
                        } else if (mapaGrande.getBotones().getBotonRecVertArriba().contains(x,y)){//boton Arriba
                            mapaGrande.getJugadora().setDireccio(2);
                            mapaGrande.getJugadora().setMeTengoQueMover(true);
                        } else if (mapaGrande.getBotones().getBotonRecVertBajo().contains(x,y)){ //boton Abajo
                            mapaGrande.getJugadora().setDireccio(3);
                            mapaGrande.getJugadora().setMeTengoQueMover(true);
                        }
                        if(mapaGrande.getBotones().getBotonCercleA().contains(x,y)){ // boton A
                            mapaGrande.puedeElTranseunteSeguirme();
                        }
                        if(mapaGrande.getBotones().getBotonCercleB().contains(x,y)){ // boton B

                        }
                    }
                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:
                    if(quinMapa == 0){
                        // JOAN, reproduce lo que he hecho con el mapa grande
                        mapaEscuela.getJugadora().setMeTengoQueMover(false);
                    } else if(quinMapa == 1) {
                        mapaGrande.getJugadora().setMeTengoQueMover(false); // en el momento que dejo de apretar, le digo que pare
                    }
                    break;
            }
            return true;
            /*Log.d(TAG,"onTouchevent ");
            switch (quinMapa){
                case 0: //MapaEscola
                    mapaEscuela.interactionOneTouch(x,y);
                    break;
                case 1:
                    break;*/
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


            //hideUI();
            //return super.onTouchEvent(event);
        }
    }

    protected void deMapaGrandeAEscuela(Jugadora jugador, PLayerStats stats){
        quinMapa = 0;
        this.jugadora = jugador;
        this.stats = stats;
        jugador.setPosicion(portaEscola);

    }
    protected void deMapaEscuelaAGrande(Jugadora jugador, PLayerStats stats){
        quinMapa = 1;
        this.jugadora = jugador;
        this.stats = stats;
        jugador.setPosicion(portaEscolaMapaGran);

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