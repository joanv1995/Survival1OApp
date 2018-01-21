package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;

import com.example.usuario.pruebaretrofit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.esPotTrepitjar;
import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.estaDinsDeMalla;
import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.llegirMapaTxt;

/**
 * Created by annag on 20/01/2018.
 */

public class Minijuego {
    private final String TAG = this.getClass().getSimpleName();

    private String[][] malla;
    private Jugadora jugadora;
    private BotonesDeMapas botones;

    // cosas que inicializar en el construct
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    //private int canvasWidth, canvasHeight;
    //protected int margeAmpl = 0, margeAlt = 0;
    //protected int ample, altura;
    private int zoomBitmap = 5;
    private Context context;
    private GameView gameView;

    private List<IAMinijuego> policias;
    private int spawn=0,tspawn=0;
    private int parados=0,pasan=0;
    private String puntuacion;


    public Minijuego(Context context, GameView gameView) {

        this.context = context;
        this.gameView = gameView;
        jugadora=new Jugadora(gameView,BitmapFactory.decodeResource(context.getResources(),R.mipmap.bad44),new PointF(195,50));
        malla = llegirMapaTxt("mapaMinijuego", context);
        botones = new BotonesDeMapas();
        PointF meta=new PointF(95,0);
        policias=new ArrayList<IAMinijuego>();

    }
    public PointF spawn(){
        Random generator=new Random();
        int pos0=generator.nextInt(100);
        if(pos0<5) pos0=5;
        if(pos0>95) pos0=95;
        PointF pos=new PointF(150,50);
        return pos;
        //spawn++;
    } //Random elije a que altura spawnea el poli y lo añade al array

    public String[][] getMalla() {
        return malla;
    }
    public int getZoomBitmap() {
        return zoomBitmap;
    }
    public BotonesDeMapas getBotones() {
        return botones;
    }

    private Paint quinColor(String s) {
        Paint paint = new Paint();
        switch (s) {
            case "+":
                paint.setColor(context.getResources().getColor(R.color.Brown));
                break;
            case "-":
                paint.setColor(context.getResources().getColor(R.color.Olive));
                break;
            default:
                paint.setColor(context.getResources().getColor(R.color.Black));
                break;
        }
        return paint;
    }
    private boolean meta(IAMinijuego poli){
        int margen1=(int)poli.getPosicion().y + 3;
        int margen2=(int)poli.getPosicion().y - 3;
        if(jugadora.getPosicion().y<margen1 && jugadora.getPosicion().y>margen2){
            return true;
        }else{return false;}
    }
    private IAMinijuego[] eliminado(IAMinijuego[] polis){
        IAMinijuego[] quedan=new IAMinijuego[polis.length-1];
        for(int i=1;i<polis.length;i++)
        {
            quedan[i-1]=polis[i];
        }
        return quedan;

    }

    protected Canvas dibujoMinijuego(Canvas canvas, int ample, int altura, int margeAlt, int margeAmpl){
        //startTime = System.currentTimeMillis();
        /*boolean espero = false;
        if(jugadora.isMeTengoQueMover())
            espero = !moverJugadora();
*/
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
        Paint paint = new Paint();
        int a = -1;
        if(tspawn<0) tspawn=10;
        boolean eliminar=false;
        for(IAMinijuego ia:policias){

            //startTime2 = System.currentTimeMillis();
            recBtm = ia.onDraw(canvas,jugadora,zoomBitmap);

                x = (int) ia.getPosicion().x * ample + margeAmpl / 2;
                y = (int) ia.getPosicion().y * altura + margeAlt / 2;
                rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

                int xx = (int) ia.getPosObjetivo().x * ample + margeAmpl / 2, yy = (int) ia.getPosObjetivo().y * altura + margeAlt / 2;
                Rect rec = new Rect(xx - 10, yy - 10, xx + 10, yy + 10);
                paint.setColor(context.getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(rec, paint);

            //startTime2 = System.currentTimeMillis()-startTime2;
            //Log.d(TAG,"Dibuixar un Ia: " + startTime2);

            boolean metas=false;
            if(ia.getPosicion().x>185)
                    metas=meta(ia);
            if(ia.getPosicion().x>=190 && metas)
            {
                parados++;
                eliminar=true;
            }
            else if(ia.getPosicion().x>=190 && !metas){
                pasan++;
                eliminar=true;
            }
        }
        if(eliminar)
            policias.remove(0);
        if(tspawn==0&&spawn<4){
            PointF obj=new PointF(195,2);
            policias.add(new IAMinijuego(BitmapFactory.decodeResource(context.getResources(),R.mipmap.bad44),"polis",spawn(),obj,this));
            spawn++;
        }
        tspawn=tspawn-1;

        if(policias.size()==0 &&spawn==4)
        {
            if(parados>=3)
                puntuacion="Victoria";
            else
                puntuacion="Derrota";
        }
        // JOAN!!! Aqui se dibuja el player

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

    /*private void iasNonStop() {
        //listaIas.add(createIA(R.drawable.bad1, new PointF(165, 95), new PointF(100, 60)));
    }
    private IA createIA(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IA(bmp, "v", pos, obj, this); // de la malla //TODO borrar gameview
    }*/


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
    /*public void interactionOneTouch(int x, int y){
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



    }*/

}
