package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GameView extends SurfaceView {
    private final String TAG = this.getClass().getSimpleName();
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private java.util.List<IA> listaIas = new ArrayList<>();
    private String[][] malla;
    private int canvasWidth, canvasHeight;
    protected int margeAmpl = 0, margeAlt = 0;
    private Rect rectangleCanvas = new Rect();


    // per dibuixar
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    protected int ample, altura;
    private int zoomBitmap = 5;

    // per amager la barra de navegacio
    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    //private List<Sprite> sprites = new ArrayList<Sprite>();

    // saber el cami dels ias
    private int  cualEsMiCamino = 0;
    private int esperaIAs;

    public GameView(Context context) {
        super(context);
        Log.d(TAG,"constructor GameView");
        gameLoopThread = new GameLoopThread(this);

        this.setSystemUiVisibility(uiOptions);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //afegirIas();
                iasNonStop();
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
                margeAmpl = width - canvasWidth;
                margeAlt = height - canvasHeight;
                rectangleCanvas.set(margeAmpl/2 , margeAlt/2, margeAmpl/2 + canvasWidth, margeAlt/2 + canvasHeight);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceDestroyed");
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while(retry) {
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

    private Paint quinColor(String s){
        Paint paint = new Paint();
        switch (s) {
            case "T": paint.setColor(getResources().getColor(R.color.Brown));
                break;
            case "U": paint.setColor(getResources().getColor(R.color.Cornsilk));
                break;
            case "P": paint.setColor(getResources().getColor(R.color.Black));
                break;
            case "-": paint.setColor(getResources().getColor(R.color.Peru));
                break;
            default: paint.setColor(getResources().getColor(R.color.Peru));
                break;
        }
        return paint;
    }

    private int midaCanvas(int canvasT, int length){
        boolean trobat = false;
        int ampladaCanvas = 0;
        if(canvasT % length != 0) {
            for(int i = canvasT; i > length; i--) {
                if(i % length == 0){
                    ampladaCanvas = i; trobat = true;
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

        //Log.d(TAG, "onDraw");
        canvas.drawColor(getResources().getColor(R.color.Black)); // Fondo

        //Log.d(TAG,"canvasWidth " + canvas.getWidth());
        //Log.d(TAG,"canvasHeight " + canvas.getHeight());

        altura = canvasHeight / malla.length;
        ample = canvasWidth / malla[0].length;

        //Log.d(TAG,"canvasWidth " + ample);
        //Log.d(TAG,"canvasHeight " + altura);

        for(int i = 0; i < malla.length; i++) //altura
        {
            for(int j = 0; j< malla[0].length; j++ ) //amplada
            {
                x = j*ample + margeAmpl/2; y=i*altura + margeAlt/2;
                //if (!malla[i][j].contains("-")){
                    rec.set(x, y, x + ample, y + altura);
                    canvas.drawRect(rec, quinColor(malla[i][j]));
                //}
            }
        }
        int a = -1;
        for (IA ia : listaIas) {
            Paint paint = new Paint();
            //int xxx = (int) ia.getPosObjetivo().x*ample + margeAmpl/2, yyy = (int) ia.getPosObjetivo().y*altura + margeAlt/2;
            //Rect recc = new Rect(xxx-10, yyy-10, xxx+10, yyy+10); // rectangle on va la imatge dins del canvas
            //paint.setColor(getResources().getColor(R.color.Green));
            //canvas.drawRect(ia.getAnima(), paint);


            recBtm = ia.onDraw(canvas);
            if(ia.isMeQuieroMorir())
                a = listaIas.indexOf(ia);
            else {
                x = (int) ia.getPosicion().x * ample + margeAmpl / 2;
                y = (int) ia.getPosicion().y * altura + margeAlt / 2;
                rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                ////Log.d(TAG,"posY " + y);
                canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

                //paint.setColor(getResources().getColor(R.color.Green));
                //Rect rec2 = new Rect(ia.getAnima().left * ample + margeAmpl/2, ia.getAnima().top *altura + margeAlt/2,
                //  ia.getAnima().right * ample + margeAmpl/2, ia.getAnima().bottom * altura + margeAlt/2);
                //canvas.drawRect(rec2, paint);
                //Log.wtf(TAG, "ANIMA --> " + ia.getAnima().toString());
                //canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);
                int xx = (int) ia.getPosObjetivo().x * ample + margeAmpl / 2, yy = (int) ia.getPosObjetivo().y * altura + margeAlt / 2;
                Rect rec = new Rect(xx - 10, yy - 10, xx + 10, yy + 10);
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(rec, paint);
            }

        }
        if(a != -1)
            listaIas.remove(a);

        // respawn d'ias
        if(esperaIAs == 5) {
            iasNonStop();
            esperaIAs = 0;
        } else
            esperaIAs++;
    }

    private int buscarIAperPosicio(PointF p){
        for (IA ia : listaIas) {
            if(ia.getPosicion().equals(p))
                return listaIas.indexOf(ia);
        }
        return -1;
    }

    protected int hiHaUnIA(PointF p, PointF pos, int direc){
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        int cont=0;
        for(IA ia: listaIas){
            if(ia.getAnima().contains((int) p.x, (int) p.y)){// && !ia.getPosicion().equals(pos)) {
                if(Math.abs(direc - ia.getDireccio()) == 1){
                    if(!((direc == 2 || ia.getDireccio() == 2) &&
                            (direc == 1 || ia.getDireccio() == 1))){
                        if((direc == 0 || ia.getDireccio() == 0) &&
                                (direc == 1 || ia.getDireccio() == 1)){
                            return 2;
                        }else
                            return 3;
                    }
                }
                cont++;
            }
           //if (ia.getPosicion().equals(p))
           //     return true;
        }
        return cont > 1? 1:0;

    }

    private Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
    private void afegirIas(){
        // x: 5-195, y: 5-95   // x: 0-100, y: 5-45 //x=19,y=19-->15
        //IA ia1 = new IA("v",1,4,new PointF(25,19),new PointF(15,15));
        // per aque funcioni be s'han de separar minim 7 unitats
        listaIas.add(createIA(R.drawable.bad1,new PointF(158,90),new PointF(100,60)));
        listaIas.add(createIA(R.drawable.bad1,new PointF(165,90),new PointF(100,60)));
        listaIas.add(createIA(R.drawable.bad1,new PointF(172,90),new PointF(100,60)));
        listaIas.add(createIA(R.drawable.bad3,new PointF(179,90),new PointF(100,60)));
        listaIas.add(createIA(R.drawable.bad1,new PointF(186,90),new PointF(100,60)));
        listaIas.add(createIA(R.drawable.bad1,new PointF(193,90),new PointF(100,60)));
    }
    private void iasNonStop(){
        listaIas.add(createIA(R.drawable.bad1,new PointF(165,95),new PointF(100,60)));
    }

    private IA createIA(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new IA(this,bmp,"v",pos,obj); // de la malla
    }

    public String[][] llegirMapaTxt(String nomTxt){
        String line="";
        int cont=1;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open(nomTxt+".txt")));

            line = bufferedReader.readLine();
            while (bufferedReader.readLine() != null){
                cont++;
            }
            bufferedReader.close();
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
            return null;
        }
        String[][] malla = new String [cont][line.length()];
        try{
            // Per omplir la malla
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open(nomTxt+".txt")));
            cont=0;
            while ((line = bufferedReader.readLine()) != null){
                for (int i=0; i<line.length(); i++) {
                    malla[cont][i] = String.valueOf(line.charAt(i));
                }
                cont++;
            }
            bufferedReader.close();

        } catch (Exception e){
            Log.e(TAG,e.getMessage());
            return null;
        }
        return malla;

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            /*for (int i = sprites.size() - 1; i >= 0; i--) {
                Sprite sprite = sprites.get(i);
                if (sprite.isCollition(event.getX(), event.getY())) {
                    sprites.remove(sprite);
                    break;
                }
            }*/
            hideUI();
            return super.onTouchEvent(event);
        }
    }
    private void hideUI(){
        this.getHandler().postDelayed(new Runnable(){
            @Override
            public void run() {
                GameView.this.setSystemUiVisibility(uiOptions);
            }
        },60);
    }

/*
    private Sprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this,bmp);
    }

    private void createSprites() {
        sprites.add(createSprite(R.drawable.bad1));
        sprites.add(createSprite(R.drawable.bad2));
        sprites.add(createSprite(R.drawable.bad3));
        sprites.add(createSprite(R.drawable.bad4));
        sprites.add(createSprite(R.drawable.bad5));
        sprites.add(createSprite(R.drawable.bad6));
        sprites.add(createSprite(R.drawable.good1));
        sprites.add(createSprite(R.drawable.good2));
        sprites.add(createSprite(R.drawable.good3));
        sprites.add(createSprite(R.drawable.good4));
        sprites.add(createSprite(R.drawable.good5));
        sprites.add(createSprite(R.drawable.good6));
    }*/
}