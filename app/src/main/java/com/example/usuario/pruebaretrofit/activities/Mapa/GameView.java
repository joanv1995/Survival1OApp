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
    private int margeAmpl = 0, margeAlt = 0;
    private Rect rectangleCanvas = new Rect();


    // per dibuixar
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    private int ample, altura;
    private int zoomBitmap = 5;

    // per amager la barra de navegacio
    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    //private List<Sprite> sprites = new ArrayList<Sprite>();

    public GameView(Context context) {
        super(context);
        Log.d(TAG,"constructor GameView");
        gameLoopThread = new GameLoopThread(this);

        this.setSystemUiVisibility(uiOptions);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                afegirIas();
                malla = llegirMapaTxt("mapaEscola7");
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

        Log.d(TAG, "onDraw");
        canvas.drawColor(getResources().getColor(R.color.Black)); // Fondo ("-")

        Log.d(TAG,"canvasWidth " + canvas.getWidth());
        Log.d(TAG,"canvasHeight " + canvas.getHeight());

        altura = canvasHeight / malla.length;
        ample = canvasWidth / malla[0].length;

        Log.d(TAG,"canvasWidth " + ample);
        Log.d(TAG,"canvasHeight " + altura);

        for(int i = 0; i < malla.length; i++) //altura
        {
            for(int j = 0; j< malla[0].length; j++ ) //amplada
            {
                x = j*ample + margeAmpl/2; y=i*altura + margeAlt/2;
                //if (!malla[i][j].contains("-")){
                    //x = j*ample; y=i*altura;
                    rec.set(x, y, x + ample, y + altura);
                    canvas.drawRect(rec, quinColor(malla[i][j]));
                //}
            }
        }
        for (IA ia : listaIas) {
            recBtm = ia.onDraw(canvas);
            x = (int) ia.getPosicion().x*ample + margeAmpl/2; y=(int) ia.getPosicion().y*altura + margeAlt/2;
            rec.set(x - zoomBitmap*ample, y - zoomBitmap*altura, x + zoomBitmap*ample, y + zoomBitmap*altura);
            ////Log.d(TAG,"posY " + y);
            canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

            int xx = (int) ia.getPosObjetivo().x*ample + margeAmpl/2, yy = (int) ia.getPosObjetivo().y*altura + margeAlt/2;
            Rect rec = new Rect(xx-10, yy-10, xx+10, yy+10); // rectangle on va la imatge dins del canvas
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            canvas.drawRect(rec, paint);
        }


/*
        int xx = canvas.getWidth(), yy=canvas.getHeight();
        Rect rec = new Rect(xx-50, yy-50, xx, yy); // rectangle on va la imatge dins del canvas
        //Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawRect(rec, paint);*/


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
        // x: 0-200, y: 5-95   // x: 0-100, y: 5-45 //x=19,y=19-->15
        //IA ia1 = new IA("v",1,4,new PointF(25,19),new PointF(15,15));
        listaIas.add(createIA(R.drawable.bad1,new PointF(170,95),new PointF(170,5)));
        //listaIas.add(createIA(R.drawable.bad1,new PointF(5,19),new PointF(100,19)));
        //listaIas.add(createIA(R.drawable.bad1,new PointF(7,10),new PointF(100,10)));
        //listaIas.add(createIA(R.drawable.bad3,new PointF(10,15),new PointF(100,15)));
        //listaIas.add(createIA(R.drawable.bad1,new PointF(10,10),new PointF(100,95)));
        //listaIas.add(createIA(R.drawable.bad1,new PointF(10,10),new PointF(0,150)));

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