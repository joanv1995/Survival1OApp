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
    //private List<Sprite> sprites = new ArrayList<Sprite>();

    public GameView(Context context) {
        super(context);
        Log.d(TAG,"constructor GameView");
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                afegirIas();
                malla = llegirMapaTxt("mapaEscola");
                Log.d(TAG, "Creo els ias: " + listaIas.size() + " i la malla: " + malla.length);
                gameLoopThread.setRunning(true);
                Log.d(TAG, "Run = true");
                gameLoopThread.start();
                Log.d(TAG, "gameLoopThread.start");
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "surfaceChanged");
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

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        canvas.drawColor(getResources().getColor(R.color.Peru));

        Log.d(TAG,"canvasWidth " + canvas.getWidth());
        Log.d(TAG,"canvasHeight " + canvas.getHeight());
        int ample = canvas.getWidth() / malla[0].length ;
        int altura = canvas.getHeight() / malla.length;

        //int i = malla.length; // files
        //int j = malla[1].length; // columnes
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.Brown));
        for(int i = 0; i < malla.length; i++) //altura
        {
            for(int j = 0; j<  malla[1].length; j++ ) //amplada
            {
                if (malla[i][j].contains("T")){
                    int xx = j*ample, yy=i*altura;
                    Rect rec = new Rect(xx, yy, xx+ample, yy+altura);
                    canvas.drawRect(rec, paint);
                }

            }

        }
/*
        int xx = canvas.getWidth(), yy=canvas.getHeight();
        Rect rec = new Rect(xx-50, yy-50, xx, yy); // rectangle on va la imatge dins del canvas
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawRect(rec, paint);

        int xxx = 0, yyy=0;
        Rect rec2 = new Rect(xxx, yyy, xxx+50, yyy+50); // rectangle on va la imatge dins del canvas
        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.Aqua));
        canvas.drawRect(rec2, paint2);

        int x = canvas.getWidth(), y=0;
        Rect rec1 = new Rect(x-100, y, x, y+50); // rectangle on va la imatge dins del canvas
        Paint paint1 = new Paint();
        paint2.setColor(getResources().getColor(R.color.Black));
        canvas.drawRect(rec1, paint1);

        int x1 = 0, y1=canvas.getHeight();
        Rect re = new Rect(x1, y1-100, x1+50, y1); // rectangle on va la imatge dins del canvas
        Paint pain = new Paint();
        paint2.setColor(getResources().getColor(R.color.Cornsilk));
        canvas.drawRect(re, pain);/*

        /*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.school);
        bmp = getResizedBitmap(bmp,100,80);
        int width = bmp.getWidth(), height = bmp.getHeight();
        Rect src = new Rect(0 , 0, width, height); // rectangle de la imatge
        int x = 50, y=50;
        Rect dst = new Rect(x, y, x+width, y+height); // rectangle on va la imatge dins del canvas
        canvas.drawBitmap(bmp, src, dst,null);
        int xx = 50, yy=200;
        Rect rec = new Rect(xx, yy, xx+width, yy+height); // rectangle on va la imatge dins del canvas

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawRect(rec, paint);*/
        /*for (IA ia : listaIas) {
            ia.onDraw(canvas);
        }*/
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
    public void afegirIas(){
        //IA ia1 = new IA("v",1,4,new PointF(25,19),new PointF(15,15));
        listaIas.add(createIA(R.drawable.bad1));
    }
    public String[][] llegirMapaTxt(String nomTxt){
        String line="";
        int cont=1;
        //final String nomTxt2 = nomTxt;
        //final AssetManager assets = getContext().getResources().getAssets();

//File myFile = new File(savePath, nomTxt+".txt");
        //String savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "assets";
        try {
            //InputStream file = this.getContext().getAssets().open(nomTxt);

            // per saber la mida del mapa (amplada i altura)
            //FileReader reader = new FileReader(file);
            //Scanner reader = new Scanner(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open(nomTxt+".txt")));

            line = bufferedReader.readLine();
            while (bufferedReader.readLine() != null){
                cont++;
            }
            //String[][] malla = new String [cont][line.length()];
            bufferedReader.close();
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
            //log.info("No se ha podido cargar el mapa: "+nomTxt+".txt");
            return null;
        }
        String[][] malla = new String [cont][line.length()];
        try{
            // Per omplir la malla
            //FileReader reader = new FileReader(myFile);
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
        //String[][] malla = String [][];
        return malla;

    }

    private IA createIA(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new IA(this,bmp,"v");
    }
    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            for (int i = sprites.size() - 1; i >= 0; i--) {
                Sprite sprite = sprites.get(i);
                if (sprite.isCollition(event.getX(), event.getY())) {
                    sprites.remove(sprite);
                    break;
                }
            }
            return super.onTouchEvent(event);
        }
    }

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