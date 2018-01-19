package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.Log;

import com.example.usuario.pruebaretrofit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private Rect cuadradoMapa = new Rect();

    private CountDownTimer timer;
    private String times;

    private CountDownTimer timerPolice;
    private String timesPolice;
    private boolean alerta = false;

    private PLayerStats stats;


    private BotonesDeMapas botones;
    private Jugadora jugadora;

    private int x = 0, y = 0, altoInit, anchoInit;
    private Rect rec = new Rect(), recBtm = new Rect();

    private java.util.List<IAPolicias> listaPolicias = new ArrayList<>();
    private java.util.List<IATranseunte> listaTranseuntes = new ArrayList<>();
    private int numTranseuntes = 0;
    private int esperaIAs;

    public PLayerStats getStats() {
        return stats;
    }

    public MapaGrande(Context context, GameView gameView) {
        timer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                times = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            }

            @Override
            public void onFinish() {

                ///PARTIDA ACABADA ---  NIVEL SUPERADO

            }
        }.start();
        timerPolice = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alerta = false;
                long millis = millisUntilFinished;
                timesPolice = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            }

            @Override
            public void onFinish() {

                alerta = true;


            }
        }.start();

        this.context = context;
        this.gameView = gameView;

        malla = llegirMapaTxt("mapaGeneral3", context);
        jugadora = createJugadora(R.drawable.bad3,new PointF(150,100));
        botones = new BotonesDeMapas();
        stats = new PLayerStats();
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
    public int getZoomBitmap() {
        return zoomBitmap;
    }
    public List<IAPolicias> getListaPolicias() {
        return listaPolicias;
    }
    public List<IATranseunte> getListaTranseuntes() {
        return listaTranseuntes;
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

        anchoInit = (int) jugadora.getPosicion().x - anchoMalla/2;
        if(anchoInit < 0)
            anchoInit = 0;
        else if (jugadora.getPosicion().x > (malla[0].length - anchoMalla/2))
            anchoInit = malla[0].length - anchoMalla;

        cuadradoMapa.set(anchoInit + zoomBitmap/3, altoInit + zoomBitmap/3,
                anchoInit + anchoMalla - zoomBitmap/3,altoInit + altoMalla - zoomBitmap/3);

        boolean estaJug = false; int estaJugCont = 0, xx = 0, yy = 0;
        for (int i = 0; i < altoMalla; i++) //altura
        {
            anchoInit = (int) jugadora.getPosicion().x - anchoMalla/2;
            if(anchoInit < 0)
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

                if (estaDinsDeMalla(new PointF(anchoInit, altoInit), malla, zoomBitmap)) {
                    canvas.drawRect(rec, quinColor(malla[altoInit][anchoInit]));
                }
                anchoInit++;

            }
            altoInit++;
        }
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.Olive));
        if(espero)
            jugadora.runCurrentFrame();
        recBtm = jugadora.onDraw(canvas);
        x = xx * ample + margeAmpl / 2;
        y = yy * altura + margeAlt / 2;
        rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);

        canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);
        //canvas.drawRect((jugadora.getAnima().left-cuadradoMapa.left)*ample+ margeAmpl / 2,
        //        (jugadora.getAnima().top - cuadradoMapa.top)*altura+ margeAlt / 2,(jugadora.getAnima().right - cuadradoMapa.top)*ample+ margeAmpl / 2,
        //        (jugadora.getAnima().bottom-cuadradoMapa.top)*altura+ margeAlt / 2,paint);


        int a = -1;
        for (IAPolicias ia : listaPolicias) {
             if (ia.isMeQuieroMorir())
                a = listaPolicias.indexOf(ia);
            else {
                recBtm = ia.onDraw(canvas, jugadora);
                if (cuadradoMapa.contains((int) ia.getPosicion().x, (int) ia.getPosicion().y)) {
                    //recBtm = ia.onDraw(canvas);
                    x = (int) (ia.getPosicion().x - cuadradoMapa.left) * ample + margeAmpl / 2;
                    y = (int) (ia.getPosicion().y - cuadradoMapa.top) * altura + margeAlt / 2;
                    rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                    canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);


                    int xxx = (int) (ia.getPosObjetivo().x - cuadradoMapa.left) * ample + margeAmpl / 2, yyy = (int) (ia.getPosObjetivo().y - cuadradoMapa.top) * altura + margeAlt / 2;
                    Rect rec = new Rect(xxx - 10, yyy - 10, xxx + 10, yyy + 10);
                    paint.setColor(context.getResources().getColor(R.color.colorPrimary));
                    canvas.drawRect(rec, paint);
                }
            }
        }

        // respawn d'ias
        if (esperaIAs == 10) {
            polisNonStop();
            esperaIAs = 0;
            if(numTranseuntes < 6) {
                transNonStop();
                numTranseuntes++;
            }
        } else
            esperaIAs++;

        if (a != -1)
            listaPolicias.remove(a);

        for (IATranseunte ia : listaTranseuntes) {
            //startTime2 = System.currentTimeMillis();
            if(ia.isLaEstoySiguiendo()){
                ia.setPosObjetivo(jugadora.getPosicion());
            }
            if (ia.isMeQuieroMorir())
                a = listaPolicias.indexOf(ia);
            else {
                recBtm = ia.onDraw(canvas, jugadora,zoomBitmap);
                if (cuadradoMapa.contains((int) ia.getPosicion().x, (int) ia.getPosicion().y)) {
                    //recBtm = ia.onDraw(canvas);
                    x = (int) (ia.getPosicion().x - cuadradoMapa.left) * ample + margeAmpl / 2;
                    y = (int) (ia.getPosicion().y - cuadradoMapa.top) * altura + margeAlt / 2;
                    rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                    canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);


                    int xxx = (int) (ia.getPosObjetivo().x - cuadradoMapa.left) * ample + margeAmpl / 2, yyy = (int) (ia.getPosObjetivo().y - cuadradoMapa.top) * altura + margeAlt / 2;
                    Rect rec = new Rect(xxx - 10, yyy - 10, xxx + 10, yyy + 10);
                    paint.setColor(context.getResources().getColor(R.color.colorPrimary));
                    canvas.drawRect(rec, paint);
                }
            }
        }
        /*
        // respawn d'ias
        if (esperaIAs == 5) {
            iasNonStop();
            esperaIAs = 0;
        } else
            esperaIAs++;

        // JOAN!!! Aqui se dibuja el player
        /*recBtm = jugadora.onDraw(canvas);
        //if(jugadora.isMeTengoQueMover())
            //moverJugadoraEnMalla();
        x = (int) jugadora.getPosicion().x * ample + margeAmpl / 2;
        y = (int) jugadora.getPosicion().y * altura + margeAlt / 2;
        rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
        canvas.drawBitmap(jugadora.getBmp(), recBtm, rec, null);*/
        // FIN

        TextPaint paintTimer = new TextPaint();
        paintTimer.setColor(context.getResources().getColor(R.color.DarkBlue));
        paintTimer.setTextSize(28);
        paintTimer.setTypeface(Typeface.create("Arial",Typeface.BOLD));
        paintTimer.setStyle(Paint.Style.FILL);
        paintTimer.setStrokeWidth(2);
        canvas.drawText(""+times,stats.getMargenX()+ gameView.getCanvasWidth()-100,stats.getLiniavida(),paintTimer);

        TextPaint paintTimerPolice = new TextPaint();
        if(!alerta)
        {
                paintTimerPolice.setColor(context.getResources().getColor(R.color.Red));
                paintTimerPolice.setTextSize(28);
                paintTimerPolice.setTypeface(Typeface.create("Arial",Typeface.BOLD));
                paintTimerPolice.setStyle(Paint.Style.FILL);
                paintTimerPolice.setStrokeWidth(2);
        }
        else
        {
                paintTimerPolice.setColor(context.getResources().getColor(R.color.Red));
                paintTimerPolice.setTextSize(34);
                paintTimerPolice.setTypeface(Typeface.create("Arial",Typeface.BOLD));
                paintTimerPolice.setStyle(Paint.Style.FILL);
                paintTimerPolice.setStrokeWidth(2);
                timesPolice = "ALERTA";

        }
        canvas.drawText(""+timesPolice,stats.getMargenX()+ gameView.getCanvasWidth()-100,stats.getLiniaseguidores(),paintTimerPolice);




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
        //canvas.drawRect(botones.getRecVerticalEntero(),paint);
        paint.setColor(context.getResources().getColor(R.color.Green));

        //canvas.drawRect(botones.getBotonRecVertArriba(), paint);
        //canvas.drawRect(botones.getBotonRecVertBajo(), paint);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.bddown);
        canvas.drawBitmap(bitmap, null, botones.getBotonRecVertBajo(), null);

        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.bdtop);
        canvas.drawBitmap(bitmap2, null, botones.getBotonRecVertArriba(), null);



       // paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));
       // canvas.drawRect(botones.getRecHorizontalEntero(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));

        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.bdleft);
        canvas.drawBitmap(bitmap3, null, botones.getBotonRecHorizLeft(), null);

        Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.bdright);
        canvas.drawBitmap(bitmap4, null, botones.getBotonRecHorizRigth(), null);


        //canvas.drawRect(botones.getBotonRecHorizLeft(), paint);
        //canvas.drawRect(botones.getBotonRecHorizRigth(), paint);

        paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));
        Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(),R.drawable.circlebutton);
        canvas.drawBitmap(bitmap5, null, botones.getBotonCercleA(), null);
        Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(),R.drawable.circlebutton);
        canvas.drawBitmap(bitmap6, null, botones.getBotonCercleB(), null);



        //canvas.drawRect(botones.getBotonCercleA(), paint);
        //canvas.drawRect(botones.getBotonCercleB(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
        //canvas.drawCircle(botones.getCentreX1(), botones.getCentreY1(), botones.getRadi(), paint);
        //canvas.drawCircle(botones.getCentreX2(), botones.getCentreY2(), botones.getRadi(), paint);

        return canvas;
    }

    private Jugadora createJugadora(int resouce, PointF pos){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new Jugadora(gameView, bmp, pos);
    }

    private void polisNonStop() {
        listaPolicias.add(createPoli(R.drawable.bad4, new PointF(10, 10), new PointF(201, 120)));
    }
    private IAPolicias createPoli(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IAPolicias(bmp, "poli", pos, obj, this); // de la malla
    }
    private void transNonStop() {
        listaTranseuntes.add(createTrans(R.drawable.good3 ));
    }
    private IATranseunte createTrans(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IATranseunte(bmp, "tra", this); // de la malla
    }

    protected void puedeElTranseunteSeguirme(){
        int a = hiHaUnTransEnRectangle(jugadora.getPosicion(), jugadora.getDireccio(), listaTranseuntes, zoomBitmap);
        if(a != -1){
            listaTranseuntes.get(a).setLaEstoySiguiendo(true);
            listaTranseuntes.get(a).setPosObjetivo(jugadora.getPosicion());
            Log.d(TAG,"Lo ha encontrado");
        }
    }

    /*protected void processButtons(int x, int y){ // ya que los botones se inicializan aqui, el metodo de cambiar direccion no lo puedo poner en jugadora
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
