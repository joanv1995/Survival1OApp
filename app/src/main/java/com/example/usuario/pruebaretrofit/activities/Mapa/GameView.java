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

    // per dibuixar
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    private int ample, altura;

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

    private Paint quinColor(String s){
        Paint paint = new Paint();
        switch (s) {
            case "T": paint.setColor(getResources().getColor(R.color.Brown));
                break;
            case "U": paint.setColor(getResources().getColor(R.color.Cornsilk));
                break;
            case "P": paint.setColor(getResources().getColor(R.color.Black));
                break;
            default: paint.setColor(getResources().getColor(R.color.Peru));
                break;
        }
        return paint;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        canvas.drawColor(getResources().getColor(R.color.Peru)); // Fondo ("-")

        Log.d(TAG,"canvasWidth " + canvas.getWidth());
        Log.d(TAG,"canvasHeight " + canvas.getHeight());
        ample = canvas.getWidth() / malla[0].length ;
        altura = canvas.getHeight() / malla.length;

        //int i = malla.length; // files
        //int j = malla[1].length; // columnes
        for(int i = 0; i < malla.length; i++) //altura
        {
            for(int j = 0; j<  malla[1].length; j++ ) //amplada
            {
                x = j*ample; y=i*altura;
                if (!malla[i][j].contains("-")){
                    //x = j*ample; y=i*altura;
                    rec.set(x, y, x+ample, y+altura);
                    canvas.drawRect(rec, quinColor(malla[i][j]));
                }
            }
        }

        for (IA ia : listaIas) {
            recBtm = ia.onDraw(canvas);
            x = (int) ia.getPosicion().x*ample; y=(int) ia.getPosicion().y*altura;
            rec.set(x, y, x+ample, y+altura);
            canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);
        }


/*
        int xx = canvas.getWidth(), yy=canvas.getHeight();
        Rect rec = new Rect(xx-50, yy-50, xx, yy); // rectangle on va la imatge dins del canvas
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawRect(rec, paint);
*/

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

    private int buscarIAperPosicio(PointF p){
        for (IA ia : listaIas) {
            if(ia.getPosicion().equals(p))
                return listaIas.indexOf(ia);
        }
        return -1;
    }

    private Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
    private void afegirIas(){
        //IA ia1 = new IA("v",1,4,new PointF(25,19),new PointF(15,15));
        listaIas.add(createIA(R.drawable.bad1));
    }

    private IA createIA(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new IA(this,bmp,"v",new PointF(25,19),new PointF(15,15));
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