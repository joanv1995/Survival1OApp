package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.usuario.pruebaretrofit.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private java.util.List<IA> listaIas = new ArrayList<>();
    private String[][] malla;
    //private List<Sprite> sprites = new ArrayList<Sprite>();

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                afegirIas();
                malla = llegirMapaTxt("mapaEscola");
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
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

        canvas.drawColor(getResources().getColor(R.color.Peru));

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
        for (IA ia : listaIas) {
            ia.onDraw(canvas);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
    public void afegirIas(){
        //IA ia1 = new IA("v",1,4,new PointF(25,19),new PointF(15,15));
        listaIas.add(createIA(R.drawable.bad1));
    }
    public static String[][] llegirMapaTxt(String nomTxt){
        String line="";
        int cont=1;
        String savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "mapesTxt";
        File myFile = new File(savePath, nomTxt+".txt");
        try {
            // per saber la mida del mapa (amplada i altura)
            FileReader reader = new FileReader(myFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            line = bufferedReader.readLine();
            while (bufferedReader.readLine() != null){
                cont++;
            }
            String[][] malla = new String [cont][line.length()];
            reader.close();
        } catch (Exception e){
            //log.error(e);
            //log.info("No se ha podido cargar el mapa: "+nomTxt+".txt");
            return null;
        }
        String[][] malla = new String [cont][line.length()];
        try{
            // Per omplir la malla
            FileReader reader = new FileReader(myFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            cont=0;
            while ((line = bufferedReader.readLine()) != null){
                for (int i=0; i<line.length(); i++) {
                    malla[cont][i] = String.valueOf(line.charAt(i));
                }
                cont++;
            }
            reader.close();

        } catch (Exception e){
            //log.fatal("No entenc què està passant.");
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