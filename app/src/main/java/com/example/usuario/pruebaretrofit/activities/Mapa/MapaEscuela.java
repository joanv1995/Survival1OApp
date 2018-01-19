package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.Log;

import com.example.usuario.pruebaretrofit.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

/**
 * Created by annag on 16/01/2018.
 */

public class MapaEscuela {
    private final String TAG = this.getClass().getSimpleName();

    private java.util.List<IAPoliciaEscuela> listaPolicias = new ArrayList<>();
    private java.util.List<IA> listaIas = new ArrayList<>();
    private String[][] malla;
    private int cualEsMiCamino = 0;
    private int iMiCaminoP = 0;
    private int esperaIAs;
    private Jugadora jugadora;
    private BotonesDeMapas botones;
    private PLayerStats stats;

    // cosas que inicializar en el construct
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    //rivate int canvasWidth, canvasHeight;
    //protected int margeAmpl = 0, margeAlt = 0;
    //protected int ample, altura;
    private int zoomBitmap = 5;
    private Context context;
    private GameView gameView;

    PointF[] caminoAseguir = {new PointF(25, 36),
            new PointF(175, 36),
            new PointF(25, 66),
            new PointF(175,66)};

    public MapaEscuela(Context context, GameView gameView) {

        this.context = context;
        this.gameView = gameView;

        malla = llegirMapaTxt("mapaEscola10", context);
        jugadora = createJugadora(R.drawable.bad3,new PointF(150,95));
        botones = new BotonesDeMapas();
        stats = new PLayerStats();
    }

    public PLayerStats getStats() {
        return stats;
    }
    public String[][] getMalla() {
        return malla;
    }
    public int getZoomBitmap() {
        return zoomBitmap;
    }
    public List<IA> getListaIas() {
        return listaIas;
    }
    public BotonesDeMapas getBotones() {
        return botones;
    }
    public int getCualEsMiCamino() {
        return cualEsMiCamino;
    }
    public void setCualEsMiCamino(int cualEsMiCamino) {
        this.cualEsMiCamino = cualEsMiCamino;
    }
    public Jugadora getJugadora() {
        return jugadora;
    }
    public List<IAPoliciaEscuela> getListaPolicias() {
        return listaPolicias;
    }

    private Jugadora createJugadora(int resouce, PointF pos){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new Jugadora(gameView, bmp, pos);
    }

    protected Canvas dibujoElMapaEscuela(Canvas canvas, int ample, int altura, int margeAlt, int margeAmpl){
        //startTime = System.currentTimeMillis();
        boolean espero = false;
        if(jugadora.isMeTengoQueMover())
            espero = !moverJugadora();

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
            recBtm = ia.onDraw(canvas,jugadora,zoomBitmap);
            if (ia.isMeQuieroMorir())
                a = listaIas.indexOf(ia);
            else {
                x = (int) ia.getPosicion().x * ample + margeAmpl / 2;
                y = (int) ia.getPosicion().y * altura + margeAlt / 2;
                rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

                int xx = (int) ia.getPosObjetivo().x * ample + margeAmpl / 2, yy = (int) ia.getPosObjetivo().y * altura + margeAlt / 2;
                Rect rec = new Rect(xx - 10, yy - 10, xx + 10, yy + 10);
                paint.setColor(context.getResources().getColor(R.color.colorPrimary));
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
            if(iMiCaminoP < 4) {
                iaPoliNonStop(caminoAseguir[iMiCaminoP]);
                iMiCaminoP++;
            }
        } else {
            esperaIAs++;
        }
        a = -1;
        //startTime = System.currentTimeMillis();
        for (IAPoliciaEscuela ia : listaPolicias) {
            //startTime2 = System.currentTimeMillis();
            recBtm = ia.onDraw(canvas,jugadora,zoomBitmap);
            if (ia.isMeQuieroMorir())
                a = listaPolicias.indexOf(ia);
            else {
                x = (int) ia.getPosicion().x * ample + margeAmpl / 2;
                y = (int) ia.getPosicion().y * altura + margeAlt / 2;
                rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

                int xx = (int) ia.getPosObjetivo().x * ample + margeAmpl / 2, yy = (int) ia.getPosObjetivo().y * altura + margeAlt / 2;
                Rect rec = new Rect(xx - 10, yy - 10, xx + 10, yy + 10);
                paint.setColor(context.getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(rec, paint);
            }
            //startTime2 = System.currentTimeMillis()-startTime2;
            //Log.d(TAG,"Dibuixar un Ia: " + startTime2);
        }
        if (a != -1)
            listaPolicias.remove(a);


        // JOAN!!! Aqui se dibuja el player
        if(espero)
            jugadora.runCurrentFrame();
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
        // MOSTRAMOS TIMER /////

        TextPaint paintTimer = new TextPaint();
        paintTimer.setColor(context.getResources().getColor(R.color.DarkBlue));
        paintTimer.setTextSize(28);
        paintTimer.setTypeface(Typeface.create("Arial",Typeface.BOLD));
        paintTimer.setStyle(Paint.Style.FILL);
        paintTimer.setStrokeWidth(2);
        canvas.drawText("pasa timer",stats.getMargenX()+ gameView.getCanvasWidth()-100,stats.getLiniavida(),paintTimer);
        //Poner Rectangulo stats
        //paint.setColor(context.getResources().getColor(R.color.Orange));
        //canvas.drawRect(stats.getStats(),paint);

        TextPaint paintStats = new TextPaint();

        paintStats.setColor(context.getResources().getColor(R.color.Black));
        paintStats.setTextSize(28);
        paintStats.setTypeface(Typeface.create("Arial",Typeface.BOLD));
        paintStats.setStyle(Paint.Style.FILL);
        paintStats.setStrokeWidth(1);
        canvas.drawText("Vida  "+stats.getVida(), stats.getMargenX(), stats.getLiniavida(),paintStats);
        canvas.drawText("Votos  "+stats.getVotos(), stats.getMargenX(),stats.getLiniavotos(),paintStats);
        canvas.drawText("Seguidores  "+stats.getSeguidores(), stats.getMargenX(), stats.getLiniaseguidores(),paintStats);
        // Poner botones
        paint.setColor(context.getResources().getColor(R.color.Cornsilk));
        canvas.drawRect(botones.getRecVerticalEntero(),paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
        canvas.drawRect(botones.getBotonRecVertArriba(), paint);
        canvas.drawRect(botones.getBotonRecVertBajo(), paint);


        paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));
        canvas.drawRect(botones.getRecHorizontalEntero(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
        canvas.drawRect(botones.getBotonRecHorizLeft(), paint);
        canvas.drawRect(botones.getBotonRecHorizRigth(), paint);

        paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));

        canvas.drawRect(botones.getBotonCercleA(), paint);
        canvas.drawRect(botones.getBotonCercleB(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
        canvas.drawCircle(botones.getCentreX1(), botones.getCentreY1(), botones.getRadi(), paint);
        canvas.drawCircle(botones.getCentreX2(), botones.getCentreY2(), botones.getRadi(), paint);

        return canvas;
    }
    private void iasNonStop() {
        listaIas.add(createIA(R.drawable.bad1, new PointF(165, 95), new PointF(100, 60)));
    }
    private IA createIA(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IA(bmp, "v", pos, obj, this); // de la malla //TODO borrar gameview
    }
    private void iaPoliNonStop(PointF p) {
        listaPolicias.add(createIAPoli(R.drawable.bad4, new PointF(100, 95), p));
    }
    private IAPoliciaEscuela createIAPoli(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IAPoliciaEscuela(bmp, "v", pos, obj, this); // de la malla //TODO borrar gameview
    }

    private boolean moverJugadora(){
        PointF p = new PointF();
        switch (jugadora.getDireccio()){
            case 1:
                p.set(jugadora.getPosicion().x - jugadora.getSpeed(),jugadora.getPosicion().y);
                break;
            case 0:
                p.set(jugadora.getPosicion().x + jugadora.getSpeed(),jugadora.getPosicion().y);
                break;
            case 2:
                p.set(jugadora.getPosicion().x,jugadora.getPosicion().y - jugadora.getSpeed());
                break;
            case 3:
                p.set(jugadora.getPosicion().x,jugadora.getPosicion().y + jugadora.getSpeed());
                break;
        }
        if(estaDinsDeMalla(p,malla,zoomBitmap) && esPotTrepitjar(p,malla,zoomBitmap/2)){
            jugadora.setPosicion(p);
            jugadora.calculaAnimes(zoomBitmap);
            return true;
        } else{
            return false;
        }
    }
    public void interactionOneTouch(int x, int y){
        if(this.getBotones().getBotonRecHorizLeft().contains(x,y)){
            Log.d(TAG,"boton Left");
            this.getJugadora().setDireccio(1);
            this.getJugadora().setPosicion(this.getJugadora().getPosicion().x - this.getJugadora().getSpeed(),this.getJugadora().getPosicion().y);
        }else if(this.getBotones().getBotonRecHorizRigth().contains(x,y)) {
            Log.d(TAG, "boton Right");
            this.getJugadora().setDireccio(0);
            this.getJugadora().setPosicion(this.getJugadora().getPosicion().x + this.getJugadora().getSpeed(),this.getJugadora().getPosicion().y);
        } else if (this.getBotones().getBotonRecVertArriba().contains(x,y)){
            Log.d(TAG, "boton Arriba");
            this.getJugadora().setDireccio(2);
            this.getJugadora().setPosicion(this.getJugadora().getPosicion().x,this.getJugadora().getPosicion().y - this.getJugadora().getSpeed());
        } else if (this.getBotones().getBotonRecVertBajo().contains(x,y)){
            Log.d(TAG, "boton Abajo");
            this.getJugadora().setDireccio(3);
            this.getJugadora().setPosicion(this.getJugadora().getPosicion().x,this.getJugadora().getPosicion().y + this.getJugadora().getSpeed());
        }
        if(this.getBotones().getBotonCercleA().contains(x,y)){
            Log.d(TAG, "boton A");
        }
        if(this.getBotones().getBotonCercleB().contains(x,y)){
            Log.d(TAG, "boton B");
        }

        // mover player , interaccion de una sola respuesta por cada click



    }
    private Paint quinColor(String s) {
        Paint paint = new Paint();
        switch (s) {
            case "T":
                paint.setColor(context.getResources().getColor(R.color.Brown));
                break;
            case "U":
                paint.setColor(context.getResources().getColor(R.color.Cornsilk));
                break;
            case "P":
                paint.setColor(context.getResources().getColor(R.color.Black));
                break;
            case "-":
                paint.setColor(context.getResources().getColor(R.color.Peru));
                break;
            default:
                paint.setColor(context.getResources().getColor(R.color.Peru));
                break;
        }
        return paint;
    }


}
