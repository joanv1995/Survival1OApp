package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.example.usuario.pruebaretrofit.R;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

/**
 * Created by annag on 17/01/2018.
 */

public class MapaGrande {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private GameView gameView;

    private String[][] malla;
    private String caracterAnterior, caracterJugadora = "@";
    private int zoomBitmap = 5;

    private BotonesDeMapas botones;
    private Jugadora jugadora;

    private int x = 0, y = 0, altoInit, anchoInit;
    private Rect rec = new Rect(), recBtm = new Rect();



    public MapaGrande(Context context, GameView gameView) {
        this.context = context;
        this.gameView = gameView;

        malla = llegirMapaTxt("mapaGeneral3", context);
        jugadora = createJugadora(R.drawable.bad3,new PointF(150,95));
        botones = new BotonesDeMapas();
    }

    public String[][] getMalla() {
        return malla;
    }
    public BotonesDeMapas getBotones() {
        return botones;
    }
    public Jugadora getJugadora() {
        return jugadora;
    }

    private Paint quinColor(String s) {
        Paint paint = new Paint();
        switch (s) {
            case "H":
                paint.setColor(context.getResources().getColor(R.color.SteelBlue));
                break;
            case "N":
                paint.setColor(context.getResources().getColor(R.color.Gray));
                break;
            case "P":
                paint.setColor(context.getResources().getColor(R.color.Black));
                break;
            case "K":
                paint.setColor(context.getResources().getColor(R.color.Sienna));
                break;
            case "C":
                paint.setColor(context.getResources().getColor(R.color.Gray));
                break;
            case "E":
                paint.setColor(context.getResources().getColor(R.color.Chartreuse));
                break;
            case "-":
                paint.setColor(context.getResources().getColor(R.color.Beige));
                break;
            default:
                paint.setColor(context.getResources().getColor(R.color.Peru));
                break;
        }
        return paint;
    }

    protected Canvas dibujoElMapaGeneral(Canvas canvas, int ample, int altura, int margeAlt, int margeAmpl, int anchoMalla, int altoMalla){
        //startTime = System.currentTimeMillis();
        //jugadora.getPosicion().x +- 100
        //jugadora.getPosicion().y +- 50
        boolean espero = false;
        if(jugadora.isMeTengoQueMover())
            espero = !moverJugadora();

        // dibujo el mapa
        altoInit = (int) jugadora.getPosicion().y - altoMalla/2;
        if(altoInit < 0)
            altoInit = 0;
        else if (jugadora.getPosicion().y > (malla.length - (zoomBitmap + altoMalla/2)))
            altoInit = malla.length - (altoMalla + zoomBitmap);


        boolean estaJug = false; int estaJugCont = 0, xx = 0, yy = 0;
        for (int i = 0; i < altoMalla; i++) //altura
        {
            anchoInit = (int) jugadora.getPosicion().x - anchoMalla/2;
            if(anchoInit < 0) //TODO el mateix pero amb els costats de la dreta i a baix
                anchoInit = 0;
            else if (jugadora.getPosicion().x > (malla[0].length - anchoMalla/2))
                anchoInit = malla[0].length - anchoMalla;

            for (int j = 0; j < anchoMalla; j++) //amplada
            {
                if(altoInit == (int) jugadora.getPosicion().y && anchoInit == (int) jugadora.getPosicion().x){
                    xx = j; yy = i;
                }

                    x = j * ample + margeAmpl / 2;
                    y = i * altura + margeAlt / 2;
                    rec.set(x, y, x + ample, y + altura);
                    /*if(malla[altoInit][anchoInit].contains(caracterJugadora)){
                        xx = anchoInit; yy = altoInit;
                    } else*/
                    if(estaDinsDeMalla(new PointF(anchoInit,altoInit),malla,zoomBitmap)) {
                        canvas.drawRect(rec, quinColor(malla[altoInit][anchoInit]));
                    }
                /*if(malla[altoInit][anchoInit].equals(caracterJugadora)){
                    rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                    canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);
                    estaJug = true;
                } else if(estaJug){
                    estaJugCont++;
                    if(estaJugCont > zoomBitmap)
                        estaJug = false;
                } else if(!estaJug)
                    canvas.drawRect(rec, quinColor(malla[altoInit][anchoInit]));*/
                    anchoInit++;

            }
            altoInit++;
        }
        //recBtm = jugadora.onDraw(canvas);
        //if(jugadora.isMeTengoQueMover())
        //moverJugadoraEnMalla();
        //if(!espero) {
            recBtm = jugadora.onDraw(canvas);
            x = xx * ample + margeAmpl / 2;
            y = yy * altura + margeAlt / 2;
            rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
            canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);
        //}
        //rec.set(xx - zoomBitmap * ample, yy - zoomBitmap * altura, xx + z
            // oomBitmap * ample, yy + zoomBitmap * altura);
        //canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);
        //startTime = System.currentTimeMillis()-startTime;
        //Log.d(TAG,"Pintar mapa: " + startTime);
        Paint paint = new Paint();
        int a = -1;
        //startTime = System.currentTimeMillis();
        /*for (IA ia : listaIas) {
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
        } else
            esperaIAs++;
*/
        // JOAN!!! Aqui se dibuja el player
        /*recBtm = jugadora.onDraw(canvas);
        //if(jugadora.isMeTengoQueMover())
            //moverJugadoraEnMalla();
        x = (int) jugadora.getPosicion().x * ample + margeAmpl / 2;
        y = (int) jugadora.getPosicion().y * altura + margeAlt / 2;
        rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
        canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);*/
        // FIN

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

    private Jugadora createJugadora(int resouce, PointF pos){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new Jugadora(gameView, bmp, pos);
    }

    protected void processButtons(int x, int y){ // ya que los botones se inicializan aqui, el metodo de cambiar direccion no lo puedo poner en jugadora
        if(this.getBotones().getBotonRecHorizLeft().contains(x,y)){ //boton Left
            jugadora.setDireccio(1);
            //jugadora.setPosicion(jugadora.getPosicion().x - jugadora.getSpeed(),jugadora.getPosicion().y);
        }else if(this.getBotones().getBotonRecHorizRigth().contains(x,y)) {//boton Right
            jugadora.setDireccio(0);
            //jugadora.setPosicion(jugadora.getPosicion().x + jugadora.getSpeed(),jugadora.getPosicion().y);
        } else if (this.getBotones().getBotonRecVertArriba().contains(x,y)){//boton Arriba
            jugadora.setDireccio(2);
            //jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y - jugadora.getSpeed());
        } else if (this.getBotones().getBotonRecVertBajo().contains(x,y)){ //boton Abajo
            jugadora.setDireccio(3);
            //jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y + jugadora.getSpeed());
        }
        if(this.getBotones().getBotonCercleA().contains(x,y)){
            Log.d(TAG, "boton A");
        }
        if(this.getBotones().getBotonCercleB().contains(x,y)){
            Log.d(TAG, "boton B");

        }
    }

    private boolean moverJugadora(){
        PointF p = new PointF();
        switch (jugadora.getDireccio()){
            case 1:
                p.set(jugadora.getPosicion().x - jugadora.getSpeed(),jugadora.getPosicion().y);
                //jugadora.setPosicion(jugadora.getPosicion().x - jugadora.getSpeed(),jugadora.getPosicion().y);
                break;
            case 0:
                p.set(jugadora.getPosicion().x + jugadora.getSpeed(),jugadora.getPosicion().y);
                //jugadora.setPosicion(jugadora.getPosicion().x + jugadora.getSpeed(),jugadora.getPosicion().y);
                break;
            case 2:
                p.set(jugadora.getPosicion().x,jugadora.getPosicion().y - jugadora.getSpeed());
                //jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y - jugadora.getSpeed());
                break;
            case 3:
                p.set(jugadora.getPosicion().x,jugadora.getPosicion().y + jugadora.getSpeed());
                //jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y + jugadora.getSpeed());
                break;
        }
        if(estaDinsDeMalla(p,malla,zoomBitmap)){
            jugadora.setPosicion(p);
            return true;
        } else{
            return false;
        }


    }

    protected void moverJugadoraEnMalla(){ //NO VA
        String s = malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x];
        String ss=malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x].replace( s, caracterAnterior);
        switch (jugadora.getDireccio()){
            case 1:
                caracterAnterior = malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x - jugadora.getSpeed()];
                s = malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x - jugadora.getSpeed()];
                malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x - jugadora.getSpeed()].replace(s, caracterJugadora);
                jugadora.setPosicion(jugadora.getPosicion().x - jugadora.getSpeed(),jugadora.getPosicion().y);
                break;
            case 0:
                caracterAnterior = malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x + jugadora.getSpeed()];
                s = malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x + jugadora.getSpeed()];// = caracterJugadora;
                malla[(int) jugadora.getPosicion().y][(int) jugadora.getPosicion().x + jugadora.getSpeed()].replace(s,caracterJugadora);
                jugadora.setPosicion(jugadora.getPosicion().x + jugadora.getSpeed(),jugadora.getPosicion().y);
                break;
            case 2:
                caracterAnterior = malla[(int) jugadora.getPosicion().y - jugadora.getSpeed()][(int) jugadora.getPosicion().x];
                s = malla[(int) jugadora.getPosicion().y - jugadora.getSpeed()][(int) jugadora.getPosicion().x];// =caracterJugadora;
                malla[(int) jugadora.getPosicion().y - jugadora.getSpeed()][(int) jugadora.getPosicion().x].replace(s, caracterJugadora);
                jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y - jugadora.getSpeed());
                break;
            case 3:
                caracterAnterior = malla[(int) jugadora.getPosicion().y + jugadora.getSpeed()][(int) jugadora.getPosicion().x];
                s = malla[(int) jugadora.getPosicion().y + jugadora.getSpeed()][(int) jugadora.getPosicion().x]; //= caracterJugadora;
                malla[(int) jugadora.getPosicion().y + jugadora.getSpeed()][(int) jugadora.getPosicion().x].replace(s,caracterJugadora);
                jugadora.setPosicion(jugadora.getPosicion().x,jugadora.getPosicion().y + jugadora.getSpeed());
                break;
        }
    }
}
