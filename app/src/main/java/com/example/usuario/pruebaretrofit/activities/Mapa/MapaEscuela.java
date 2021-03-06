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
    private int esperaPolis;
    private String times;

    private Jugadora jugadora;
    private BotonesDeMapas botones;
    private PLayerStats stats;


    private Rect rectUrna;
    Rect rectEstavotando00 = new Rect(50,30,70,41);
    Rect rectEstavotando10 = new Rect(131,30,151,41);
    Rect rectEstavotando01 = new Rect(50,60,70,71);
    Rect rectEstavotando11 = new Rect(131,60,151,71);

    Rect cancelandoUrna00 = new Rect(11,30,31,41);
    Rect cancelandoUrna10 = new Rect(171,30,191,41);
    Rect cancelandoUrna01 = new Rect(11,60,31,71);
    Rect cancelandoUrna11 = new Rect(171,60,191,71);

    Rect urna00;
    Rect urna10;
    Rect urna01;
    Rect urna11;
    int counter = 0;

    boolean urna00Blocked = false;
    boolean urna10Blocked = false;
    boolean urna01Blocked = false;
    boolean urna11Blocked = false;
    Bitmap redcross;
    Bitmap votado;

    // cosas que inicializar en el construct
    private int x = 0, y = 0;
    private Rect rec = new Rect(), recBtm = new Rect();
    //rivate int canvasWidth, canvasHeight;
    //protected int margeAmpl = 0, margeAlt = 0;
    //protected int ample, altura;
    private int zoomBitmap = 5;
    private Context context;
    private GameView gameView;
    private int numeroPolicias = 0;

    PointF[] caminoAseguir = {new PointF(25, 36),
            new PointF(175, 36),
            new PointF(25, 66),
            new PointF(175,66)};

    private PointF[] caminoAseguirVotantes = {new PointF(55, 36),
            new PointF(145, 36),
            new PointF(55, 66),
            new PointF(145,66)};

    java.util.List<PointF> caminoAseguirVotanteLista = new ArrayList<>();

    public MapaEscuela(Context context, GameView gameView, Jugadora jugadora, BotonesDeMapas botones, PLayerStats stats) {
        redcross = BitmapFactory.decodeResource(context.getResources(), R.mipmap.redcross2);
        votado = BitmapFactory.decodeResource(context.getResources(),R.mipmap.votinggreen2);
        this.jugadora = jugadora;
        this.botones = botones;
        this.stats = stats;

        for(PointF p: this.caminoAseguirVotantes){
            this.caminoAseguirVotanteLista.add(p);
        }
        this.context = context;
        this.gameView = gameView;

        malla = llegirMapaTxt("mapaEscola10", context);
        iasNonStop();
        //jugadora = createJugadora(R.mipmap.bad33,new PointF(150,95));
        //botones = new BotonesDeMapas();
        //stats = new PLayerStats();
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
    public String getTimes() {
        return times;
    }
    public void setTimes(String times) {
        this.times = times;
    }
    public int getNumeroPolicias() {
        return numeroPolicias;
    }
    public void setNumeroPolicias(int numeroPolicias) {
        this.numeroPolicias = numeroPolicias;
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
            }

            if(ia.isVotando()){
                counter++;
                if(counter ==10){
                    stats.setVotos(stats.getVotos()+1);
                    counter = 0;
                }
                rectUrna = new Rect();
                if(rectEstavotando00.contains((int)ia.getPosicion().x,(int)ia.getPosicion().y)) {
                    int[] xs;
                    xs = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando00.left -18,rectEstavotando00.top +2);
                    int[] ys;
                    ys = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando00.right -20,rectEstavotando00.bottom -2);
                    urna00 = new Rect(xs[0],xs[1],ys[0],ys[1]);
                    rectUrna.set(xs[0], xs[1], ys[0], ys[1]);
                }
                else if(rectEstavotando10.contains((int)ia.getPosicion().x,(int)ia.getPosicion().y)) {
                    int[] xs;
                    xs = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando10.left +21,rectEstavotando10.top +2);
                    int[] ys;
                    ys = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando10.right +19,rectEstavotando10.bottom -2);
                    urna10 = new Rect(xs[0],xs[1],ys[0],ys[1]);
                    rectUrna.set(xs[0], xs[1], ys[0], ys[1]);
                }
                else if(rectEstavotando01.contains((int)ia.getPosicion().x,(int)ia.getPosicion().y)) {
                    int[] xs;
                    xs = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando01.left -18,rectEstavotando01.top +2);
                    int[] ys;
                    ys = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando01.right -19,rectEstavotando01.bottom -2);
                    urna01 = new Rect(xs[0],xs[1],ys[0],ys[1]);

                    rectUrna.set(xs[0], xs[1], ys[0], ys[1]);
                }
                else if(rectEstavotando11.contains((int)ia.getPosicion().x,(int)ia.getPosicion().y)) {
                    int[] xs;
                    xs = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando11.left +21,rectEstavotando11.top +2);
                    int[] ys;
                    ys = convertMallaToCanvas(ample,altura,margeAmpl,margeAlt,rectEstavotando11.right +19,rectEstavotando11.bottom -2);
                    urna11 = new Rect(xs[0],xs[1],ys[0],ys[1]);

                    rectUrna.set(xs[0], xs[1], ys[0], ys[1]);
                }
                canvas.drawBitmap(votado,null,rectUrna,null);
            }
            //startTime2 = System.currentTimeMillis()-startTime2;
            //Log.d(TAG,"Dibuixar un Ia: " + startTime2);
        }
        if (a != -1)
            listaIas.remove(a);

        //if (esperaPolis == 40) {
            if (iMiCaminoP < numeroPolicias) {
                iaPoliNonStop(caminoAseguir[iMiCaminoP]);
                iMiCaminoP++;
            }
         //   esperaPolis = 0;
        //} else
        //    esperaPolis++;


        // respawn d'ias
        if (esperaIAs == 20) {
            iasNonStop();
            esperaIAs = 0;
        }
        else
            esperaIAs++;

        a = -1;
        //IAS
        //startTime = System.currentTimeMillis();
        for (IAPoliciaEscuela ia : listaPolicias) {
            //startTime2 = System.currentTimeMillis();
            recBtm = ia.onDraw(canvas, jugadora, zoomBitmap);
            if (ia.isMeQuieroMorir())
                a = listaPolicias.indexOf(ia);
            else {
                x = (int) ia.getPosicion().x * ample + margeAmpl / 2;
                y = (int) ia.getPosicion().y * altura + margeAlt / 2;
                rec.set(x - zoomBitmap * ample, y - zoomBitmap * altura, x + zoomBitmap * ample, y + zoomBitmap * altura);
                canvas.drawBitmap(ia.getBmp(), recBtm, rec, null);

            }
            //startTime2 = System.currentTimeMillis()-startTime2;
            //Log.d(TAG,"Dibuixar un Ia: " + startTime2);
            if (ia.isCancelandoUrna())
            {
                rectUrna = new Rect();
                if (cancelandoUrna00.contains((int) ia.getPosicion().x, (int) ia.getPosicion().y)) {
                    rectUrna = urna00;
                    for (PointF f : caminoAseguirVotantes) {
                        if (rectEstavotando00.contains((int) f.x, (int) f.y)) {
                            caminoAseguirVotanteLista.remove(f);
                            urna00Blocked = true;
                            break;
                        }
                    }
                }
                else if (cancelandoUrna10.contains((int) ia.getPosicion().x, (int) ia.getPosicion().y)) {
                rectUrna = urna10;
                for (PointF f : caminoAseguirVotantes) {
                    if (rectEstavotando10.contains((int) f.x, (int) f.y)) {
                        caminoAseguirVotanteLista.remove(f);
                        urna10Blocked = true;
                        break;
                    }
                }
            } else if (cancelandoUrna01.contains((int) ia.getPosicion().x, (int) ia.getPosicion().y)) {

                rectUrna = urna01;
                for (PointF f : caminoAseguirVotantes) {
                    if (rectEstavotando01.contains((int) f.x, (int) f.y)) {
                        caminoAseguirVotanteLista.remove(f);
                        urna01Blocked = true;
                        break;
                    }
                }
            } else if (cancelandoUrna11.contains((int) ia.getPosicion().x, (int) ia.getPosicion().y)) {

                rectUrna = urna11;
                for (PointF f : caminoAseguirVotantes) {
                    if (rectEstavotando11.contains((int) f.x, (int) f.y)) {
                        caminoAseguirVotanteLista.remove(f);
                        urna11Blocked = true;
                        break;
                    }
                }
            }
            try {
                canvas.drawBitmap(redcross, null, rectUrna, null);
            } catch(Exception e) {

            }
            }
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

        // Imagen urna votante votando



        // Pintamos cruz en las urnas PARA SIEMPRE si estan bloqueadas
        try {
            if (urna00Blocked) {
                canvas.drawBitmap(redcross, null, urna00, null);
            }
            if (urna10Blocked) {
                canvas.drawBitmap(redcross, null, urna10, null);
            }
            if (urna01Blocked) {
                canvas.drawBitmap(redcross, null, urna01, null);
            }
            if (urna11Blocked) {
                canvas.drawBitmap(redcross, null, urna11, null);
            }
        } catch(Exception e){

        }
        // JOAN!! Aqui se pintan los "botones"
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.bddown2);
        canvas.drawBitmap(bitmap, null, botones.getBotonRecVertBajo(), null);

        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.bdtop2);
        canvas.drawBitmap(bitmap2, null, botones.getBotonRecVertArriba(), null);


        // paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));
        // canvas.drawRect(botones.getRecHorizontalEntero(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));

        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.bdleft2);
        canvas.drawBitmap(bitmap3, null, botones.getBotonRecHorizLeft(), null);

        Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.bdright2);
        canvas.drawBitmap(bitmap4, null, botones.getBotonRecHorizRigth(), null);


        //canvas.drawRect(botones.getBotonRecHorizLeft(), paint);
        //canvas.drawRect(botones.getBotonRecHorizRigth(), paint);

        paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));
        Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.circlebutton2);
        canvas.drawBitmap(bitmap5, null, botones.getBotonCercleA(), null);
        Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.circlebutton2);
        canvas.drawBitmap(bitmap6, null, botones.getBotonCercleB(), null);
        // los cojo de la clase BotonesDeMapas
        // MOSTRAMOS TIMER /////

        TextPaint paintTimer = new TextPaint();
        paintTimer.setColor(context.getResources().getColor(R.color.DarkBlue));
        paintTimer.setTextSize(28);
        paintTimer.setTypeface(Typeface.create("Arial",Typeface.BOLD));
        paintTimer.setStyle(Paint.Style.FILL);
        paintTimer.setStrokeWidth(2);
        canvas.drawText(""+times,gameView.getCanvasWidth() - stats.getMargenX() + margeAmpl/2,stats.getLiniavida(),paintTimer);
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
        //canvas.drawRect(botones.getRecVerticalEntero(),paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
        //canvas.drawRect(botones.getBotonRecVertArriba(), paint);
        //canvas.drawRect(botones.getBotonRecVertBajo(), paint);


        paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));
        //canvas.drawRect(botones.getRecHorizontalEntero(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
       // canvas.drawRect(botones.getBotonRecHorizLeft(), paint);
        //canvas.drawRect(botones.getBotonRecHorizRigth(), paint);

        paint.setColor(context.getResources().getColor(R.color.AntiqueWhite));

        //canvas.drawRect(botones.getBotonCercleA(), paint);
       // canvas.drawRect(botones.getBotonCercleB(), paint);
        paint.setColor(context.getResources().getColor(R.color.Green));
       // canvas.drawCircle(botones.getCentreX1(), botones.getCentreY1(), botones.getRadi(), paint);
       // canvas.drawCircle(botones.getCentreX2(), botones.getCentreY2(), botones.getRadi(), paint);

        return canvas;
    }
    private void iasNonStop() {
        listaIas.add(createIA(R.mipmap.bad11, new PointF(165, 6), new PointF(100, 45)));
    }
    private IA createIA(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IA(bmp, "v", pos, obj, this); // de la malla
    }
    private void iaPoliNonStop(PointF p) {
        listaPolicias.add(createIAPoli(R.mipmap.bad44, new PointF(100, 95), p));
    }
    private IAPoliciaEscuela createIAPoli(int resouce, PointF pos, PointF obj) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resouce);
        return new IAPoliciaEscuela(bmp, "v", pos, obj, this); // de la malla
    }

    protected PointF cambioPosObjetivoIA(){
        PointF p = new PointF();
        if (!caminoAseguirVotanteLista.isEmpty()) {
            while(cualEsMiCamino >= caminoAseguirVotanteLista.size()){
                cualEsMiCamino--;
            }
            p = caminoAseguirVotanteLista.get(cualEsMiCamino);

            cualEsMiCamino++;
            if(cualEsMiCamino >= caminoAseguirVotanteLista.size())
                cualEsMiCamino=0;
        }

        return  p;
    }

    protected void mePuedoCambiarDeMapa(){
        Rect r = new Rect(95,94,106,100);
        if(r.contains((int) jugadora.getPosicion().x,(int) jugadora.getPosicion().y)){
            gameView.deMapaEscuelaAGrande(jugadora,stats);
        }
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
    ///DE MOMENTO...NO SE USA ( NEVER KNOWS)
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
