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

public class GameView extends SurfaceView {
    private final String TAG = this.getClass().getSimpleName();
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private BotonesDeMapas botones;
    private java.util.List<IA> listaIas = new ArrayList<>();
    private String[][] malla;
    private int canvasWidth, canvasHeight;
    protected int margeAmpl = 0, margeAlt = 0;
    private Rect rectangleCanvas = new Rect();

    private Jugadora jugadora;

    // per dibuixar
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    protected int ample, altura;
    private int zoomBitmap = 5;

    // per amager la barra de navegacio
    //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //        | View.SYSTEM_UI_FLAG_FULLSCREEN;
    //private List<Sprite> sprites = new ArrayList<Sprite>();

    // saber el cami dels ias
    private int cualEsMiCamino = 0;
    private int esperaIAs;

    private int quinMapa = 0; // mapaEscuela=0, mapaGrande = 1, minijuegos = 2

    public GameView(Context context) {
        super(context);
        Log.d(TAG, "constructor GameView");
        gameLoopThread = new GameLoopThread(this);
        botones = new BotonesDeMapas();
        //this.setSystemUiVisibility(uiOptions);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //afegirIas();
                iasNonStop();
                jugadora = createJugadora(R.drawable.bad3,new PointF(150,95));
                malla = llegirMapaTxt("mapaEscola10");
                Log.d(TAG, "Creo els ias: " + listaIas.size() + " i la malla: " + malla.length);
                gameLoopThread.setRunning(true);
                Log.d(TAG, "Run = true");
                gameLoopThread.start();
                Log.d(TAG, "gameLoopThread.start");

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged");
                Log.d(TAG, "width " + width);
                Log.d(TAG, "height " + height);
                canvasHeight = midaCanvas(height, malla.length);
                canvasWidth = midaCanvas(width, malla[0].length);
                botones.setMedidasCanvas(canvasWidth,canvasHeight);
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

    public int getCualEsMiCamino() {
        return cualEsMiCamino;
    }

    public void setCualEsMiCamino(int cualEsMiCamino) {
        this.cualEsMiCamino = cualEsMiCamino;
    }

    public String[][] getMalla() {
        return malla;
    }

    public int getZoomBitmap() {
        return zoomBitmap;
    }

    private Paint quinColor(String s) {
        Paint paint = new Paint();
        switch (s) {
            case "T":
                paint.setColor(getResources().getColor(R.color.Brown));
                break;
            case "U":
                paint.setColor(getResources().getColor(R.color.Cornsilk));
                break;
            case "P":
                paint.setColor(getResources().getColor(R.color.Black));
                break;
            case "-":
                paint.setColor(getResources().getColor(R.color.Peru));
                break;
            default:
                paint.setColor(getResources().getColor(R.color.Peru));
                break;
        }
        return paint;
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

        altura = canvasHeight / malla.length;
        ample = canvasWidth / malla[0].length;

        //Log.d(TAG,"canvasWidth " + ample);
        //Log.d(TAG,"canvasHeight " + altura);

        if (quinMapa == 0) { //Mapa escola

            dibujoElMapaEscuela(canvas);

        } else if(quinMapa == 1){


        }else if(quinMapa == 2){
            // TODO: ALEX, qui va tu

        }
    }

    private void dibujoElMapaEscuela(Canvas canvas){
        //startTime = System.currentTimeMillis();
        for (int i = 0; i < malla.length; i++) //altura
        {
            for (int j = 0; j < malla[0].length; j++) //amplada
            {
                x = j * ample + margeAmpl / 2;
                y = i * altura + margeAlt / 2;
                rec.set(x, y, x + ample, y + altura);
                canvas.drawRect(rec, quinColor(malla[i][j]));
            }
        }
        //startTime = System.currentTimeMillis()-startTime;
        //Log.d(TAG,"Pintar mapa: " + startTime);
        Paint paint = new Paint();
        int a = -1;
        //startTime = System.currentTimeMillis();
        for (IA ia : listaIas) {
            //startTime2 = System.currentTimeMillis();
            recBtm = ia.onDraw(canvas);
            if (ia.isMeQuieroMorir())
                a = listaIas.indexOf(ia);
            else {
                x = (int) ia.getPosicion().x * ample + margeAmpl / 2;
                y = (int) ia.getPosicion().y * altura + margeAlt / 2;
                rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

                int xx = (int) ia.getPosObjetivo().x * ample + margeAmpl / 2, yy = (int) ia.getPosObjetivo().y * altura + margeAlt / 2;
                Rect rec = new Rect(xx - 10, yy - 10, xx + 10, yy + 10);
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(rec, paint);
            }
            //startTime2 = System.currentTimeMillis()-startTime2;
            //Log.d(TAG,"Dibuixar un Ia: " + startTime2);
        }
        if (a != -1)
            listaIas.remove(a);

        // respawn d'ias
        if (esperaIAs == 5) {
            iasNonStop();
            esperaIAs = 0;
        } else
            esperaIAs++;

        // JOAN!!! Aqui se dibuja el player
        recBtm = jugadora.onDraw(canvas);
        x = (int) jugadora.getPosicion().x * ample + margeAmpl / 2;
        y = (int) jugadora.getPosicion().y * altura + margeAlt / 2;
        rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
        canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);
        // FIN


        //startTime = System.currentTimeMillis()-startTime;
        //Log.d(TAG,"Dibuixar tots els Ias: " + startTime);

        // JOAN!! Aqui se pintan los "botones"
        // los cojo de la clase BotonesDeMapas
        // Poner botones
        paint.setColor(getResources().getColor(R.color.Cornsilk));
        canvas.drawRect(botones.getRecVerticalEntero(),paint);
        paint.setColor(getResources().getColor(R.color.Green));
        canvas.drawRect(botones.getBotonRecVertArriba(), paint);
        canvas.drawRect(botones.getBotonRecVertBajo(), paint);


        paint.setColor(getResources().getColor(R.color.AntiqueWhite));
        canvas.drawRect(botones.getRecHorizontalEntero(), paint);
        paint.setColor(getResources().getColor(R.color.Green));
        canvas.drawRect(botones.getBotonRecHorizLeft(), paint);
        canvas.drawRect(botones.getBotonRecHorizRigth(), paint);

        paint.setColor(getResources().getColor(R.color.AntiqueWhite));

        canvas.drawRect(botones.getBotonCercleA(), paint);
        canvas.drawRect(botones.getBotonCercleB(), paint);
        paint.setColor(getResources().getColor(R.color.Green));
        canvas.drawCircle(botones.getCentreX1(), botones.getCentreY1(), botones.getRadi(), paint);
        canvas.drawCircle(botones.getCentreX2(), botones.getCentreY2(), botones.getRadi(), paint);
    }

    private int buscarIAperPosicio(PointF p) {
        for (IA ia : listaIas) {
            if (ia.getPosicion().equals(p))
                return listaIas.indexOf(ia);
        }
        return -1;
    }

    protected int hiHaUnIA(PointF p, PointF pos, int direc) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        int cont = 0;
        for (IA ia : listaIas) {
            if (ia.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
                if (Math.abs(direc - ia.getDireccio()) == 1) {
                    if (!((direc == 2 || ia.getDireccio() == 2) &&
                            (direc == 1 || ia.getDireccio() == 1))) {
                        if ((direc == 0 || ia.getDireccio() == 0) &&
                                (direc == 1 || ia.getDireccio() == 1)) {
                            return 2;
                        } else
                            return 3;
                    }
                }
                cont++;
            }
        }
        return cont > 1 ? 1 : 0;

    }

    private Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    private void afegirIas() {
        // x: 5-195, y: 5-95   // x: 0-100, y: 5-45 //x=19,y=19-->15
        //IA ia1 = new IA("v",1,4,new PointF(25,19),new PointF(15,15));
        // per aque funcioni be s'han de separar minim 7 unitats
        listaIas.add(createIA(R.drawable.bad1, new PointF(158, 90), new PointF(100, 60)));
        listaIas.add(createIA(R.drawable.bad1, new PointF(165, 90), new PointF(100, 60)));
        listaIas.add(createIA(R.drawable.bad1, new PointF(172, 90), new PointF(100, 60)));
        listaIas.add(createIA(R.drawable.bad3, new PointF(179, 90), new PointF(100, 60)));
        listaIas.add(createIA(R.drawable.bad1, new PointF(186, 90), new PointF(100, 60)));
        listaIas.add(createIA(R.drawable.bad1, new PointF(193, 90), new PointF(100, 60)));
    }

    private void iasNonStop() {
        listaIas.add(createIA(R.drawable.bad1, new PointF(165, 95), new PointF(100, 60)));
    }

    private IA createIA(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new IA(this, bmp, "v", pos, obj); // de la malla
    }
    private Jugadora createJugadora(int resouce, PointF pos){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Jugadora(this, bmp, pos);
    }

    public String[][] llegirMapaTxt(String nomTxt) {
        String line = "";
        int cont = 1;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open(nomTxt + ".txt")));

            line = bufferedReader.readLine();
            while (bufferedReader.readLine() != null) {
                cont++;
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        String[][] malla = new String[cont][line.length()];
        try {
            // Per omplir la malla
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open(nomTxt + ".txt")));
            cont = 0;
            while ((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    malla[cont][i] = String.valueOf(line.charAt(i));
                }
                cont++;
            }
            bufferedReader.close();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return malla;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            // direction = 0 right, 1 left, 2 up, 3 down,
            //event.getX(), event.getY()
            int x = Math.round(event.getX()), y = Math.round(event.getY());

            if (botones.getBotonRecHorizLeft().contains(x,y)){
                Log.d(TAG, "boton Left");
                jugadora.setDireccio(1);
                jugadora.setPosicion(jugadora.getPosicion().x - jugadora.getSpeed(),jugadora.getPosicion().y);
            } else if(botones.getBotonRecHorizRigth().contains(x,y)) {
                Log.d(TAG, "boton Right");
                jugadora.setDireccio(0);
                jugadora.setPosicion(jugadora.getPosicion().x + jugadora.getSpeed(),jugadora.getPosicion().y);
            } else if (botones.getBotonRecVertArriba().contains(x,y)){
                Log.d(TAG, "boton Arriba");
                jugadora.setDireccio(2);
                jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y - jugadora.getSpeed());
            } else if (botones.getBotonRecVertBajo().contains(x,y)){
                Log.d(TAG, "boton Abajo");
                jugadora.setDireccio(3);
                jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y + jugadora.getSpeed());
            }

            if(botones.getBotonCercleA().contains(x,y)){
                Log.d(TAG, "boton A");
            }
            if(botones.getBotonCercleB().contains(x,y)){
                Log.d(TAG, "boton B");
            }
            /*for (int i = sprites.size() - 1; i >= 0; i--) {
                Sprite sprite = sprites.get(i);
                if (sprite.isCollition(event.getX(), event.getY())) {
                    sprites.remove(sprite);
                    break;
                }
            }*/
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