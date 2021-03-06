package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

/**
 * Created by annag on 18/01/2018.
 */

public class IATranseunte {
    // TODO: aquet ia es mou en cercles, fer que si es troba el jugador de cara, es pari
    private final String TAG = this.getClass().getSimpleName();

    // Imatge bitMap
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private Bitmap bmp;

    //private GameView gameView;
    private MapaGrande mapa;
    private int currentFrame = 0;

    private int width; // de la imatge
    private int height;

    // fin
    private String idIa;
    private PointF posicion;
    private PointF posObjetivo; // canviar la posicio objectiu per un rectangle
    private Rect rectObjetivo = new Rect();
    private PointF posAntiga;
    private boolean enEspera = false;
    private int speed = 2; // TODO: mirar que faig amb la velocitat
    private boolean direccioX_Left, direccioY_Up; // true: up, false: down
    private int direccio; // direction = 0 right, 1 left, 2 up, 3 down,
    private Rect anima = new Rect();
    private boolean hihaIaInoEmPucMoure;
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = {2, 1, 3, 0};//{ 3, 1, 0, 2 };


    // per dibuixar
    private PointF act = new PointF(), act2 = new PointF();
    private PointF p = new PointF();
    private PointF pp = new PointF();
    private Rect src = new Rect();
    int[] vecPos = {speed, -speed, 0, 0};
    int[] vecPos2 = {speed+1, -speed-1, 0, 0};

    /*PointF[] caminoAseguir = {new PointF(281, 81),
            new PointF(361, 81),
            new PointF(361, 120),
            new PointF(281,120)};*/
    int minX = 281, minY = 81;
    int maxX = 361, maxY = 120;
    Random r = new Random();
    private boolean laEstoySiguiendo = false;
    private boolean meParoAdefender = false;
    private boolean meVoyAlHospital = false;
    private boolean meEstoyEncarando = false;
    private int contadorEncaro = 0;

    PointF puertaAlInfierno = new PointF(100,2);
    private boolean meVoy = false, meQuieroMorir= false;
    int tiempoVotando = 10, tiempoVotangoPasado = 0;
    private boolean estoyCansadoDeEsperar = false;
    private int contEspera = 0;


    public IATranseunte(Bitmap bmp, String idIa, PointF posicion, PointF posObjetivo, MapaGrande mapaEscuela){
        //this.gameView=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.idIa = idIa;
        this.posicion = posicion;
        this.posObjetivo = posObjetivo;
        this.mapa = mapaEscuela;
        calculaRecObjetivo();
        calculaAnimes();
    }
    public IATranseunte(Bitmap bmp, String idIa, MapaGrande mapaEscuela, int zoom){
        this.mapa = mapaEscuela;
        minX = zoom; minY = zoom; maxX = mapa.getMalla()[0].length - zoom; maxY = mapa.getMalla().length - zoom;
        PointF p = new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        PointF pp = new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        while (!esPotTrepitjar(p, mapa.getMalla(), zoom)){
            p.set(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        }
        while (!esPotTrepitjar(pp, mapa.getMalla(), zoom)){
            pp.set(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        }
        this.posicion = p;
        this.posObjetivo = pp;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.idIa = idIa;
        //this.posicion = new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        //this.posObjetivo = new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
        calculaRecObjetivo();
        calculaAnimes();
    }

    protected void calculaRecObjetivo(){
        rectObjetivo.set((int)posObjetivo.x-4,(int)posObjetivo.y-4,
                (int)posObjetivo.x+4,(int)posObjetivo.y+4);
    }
    private void calculaAnimes(){
        // [files][columnes]
        this.anima.set((int) posicion.x - mapa.getZoomBitmap()+1, (int) posicion.y - mapa.getZoomBitmap(),
                (int) posicion.x + mapa.getZoomBitmap()-1, (int) posicion.y + mapa.getZoomBitmap());
    }

    public PointF getPosicion() {
        return posicion;
    }
    public void setPosicion(PointF posicion) {
        this.posicion = posicion;
    }
    public PointF getPosObjetivo() {
        return posObjetivo;
    }
    public void setPosObjetivo(PointF posObjetivo) {
        this.posObjetivo = posObjetivo;
    }
    public Bitmap getBmp() {
        return bmp;
    }
    public Rect getAnima() {
        return anima;
    }
    public int getDireccio() {
        return direccio;
    }
    public boolean isMeQuieroMorir() {
        return meQuieroMorir;
    }
    public boolean isLaEstoySiguiendo() {
        return laEstoySiguiendo;
    }
    public void setLaEstoySiguiendo(boolean laEstoySiguiendo) {
        this.laEstoySiguiendo = laEstoySiguiendo;
    }
    public boolean isMeParoAdefender() {
        return meParoAdefender;
    }
    public void setMeParoAdefender(boolean meParoAdefender) {
        this.meParoAdefender = meParoAdefender;
    }
    public void setRectObjetivo(Rect rectObjetivo) {
        this.rectObjetivo = rectObjetivo;
    }
    public boolean isMeEstoyEncarando() {
        return meEstoyEncarando;
    }
    public void setMeEstoyEncarando(boolean meEstoyEncarando) {
        this.meEstoyEncarando = meEstoyEncarando;
    }
    public void runContadorEncaro(){
        contadorEncaro++;
    }

    private void update(Jugadora jugadora, int zoomBitmap) {
        //Log.d(TAG, "Update: moc una casella");
       /* if(haArribat())
            enEspera = true;
        else
            enEspera = false;*/
        if (!rectObjetivo.contains((int)posicion.x,(int)posicion.y)){//!posicion.equals(posObjetivo) && !enEspera) {
            act = new PointF();//.set(posicion.x, posicion.y); // si no es troba ningun punt, act es (0,0)
            try {
                double min = 10000000;
                //int[] vecPos = {speed, -speed, 0, 0};
                //int[] vecPos2 = {speed+1, -speed-1, 0, 0};
                // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu

                for (int i = 0; i < 4; i++) {
                    p.set((int) getPosicion().x + vecPos[i], (int) getPosicion().y + vecPos[vecPos.length-1-i]);
                    pp.set((int) getPosicion().x + vecPos2[i], (int) getPosicion().y + vecPos2[vecPos2.length-1-i]);

                    int d = hiHaUnTrans(p,direccio, mapa.getListaTranseuntes());//gameView.listaIas);
                    int dd = hiHaUnPoli(p,direccio, mapa.getListaPolicias());
                    if(d == 2 || dd == 2){
                        //TODO fer que el que vagin cap a dalt o cap a baix sigui random
                        // 2: estan en horitzontal, han d'escapar un per baix i l'altre per dalt
                        // ferho aqui o al començament (millor idea ja que aqui es calcula 4 cops
                        p.set((int) posicion.x, (int) posicion.y - speed);
                        pp.set((int) posicion.x, (int) posicion.y - speed - 1);
                        if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                            direccio = 2;
                            act.set(p); //forço a que baixi cap a baix
                            act2.set(pp);
                            break;
                        } else {
                            p.set((int) posicion.x, (int) posicion.y + speed);
                            pp.set((int) posicion.x, (int) posicion.y + speed + 1);
                            if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                    && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                                direccio = 3;
                                act.set(p); //forço a que baixi cap a baix
                                act2.set(pp);
                                break;
                            }
                        }
                    }else if (d==3 || dd == 3){
                        // 3: estan en vertical, han d'escapar un per la dreta i l'altre per l'esquerra
                        p.set((int) posicion.x - speed, (int) posicion.y);
                        pp.set((int) posicion.x - speed - 1, (int) posicion.y);
                        if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                            direccio = 1;
                            act.set(p); //forço a que baixi cap a baix
                            act2.set(pp);
                            break;
                        } else {
                            p.set((int) posicion.x + speed, (int) posicion.y);
                            pp.set((int) posicion.x + speed + 1, (int) posicion.y);
                            if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                    && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                                direccio = 0;
                                act.set(p); //forço a que baixi cap a baix
                                act2.set(pp);
                                break;
                            }
                        }
                    } else {
                        double distancia = calculaDistancia(p, getPosObjetivo());
                        if (distancia < min && estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                            direccio = i;
                            min = calculaDistancia(p, getPosObjetivo());
                            act.set(p);
                            act2.set(pp);
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                enEspera=true;
            }

            // si m'ho ha calculat bé, actualitzo posicio
            if((!enEspera && hiHaUnTrans(act2,direccio, mapa.getListaTranseuntes()) == 0
                    && (hiHaUnPoli(act2, direccio, mapa.getListaPolicias()) == 0 || meVoyAlHospital)
                    && hiHaLaJugadora(act2,  direccio, jugadora)==0) || estoyCansadoDeEsperar){//gameView.hiHaUnIA(act2,direccio) == 0){// && !hihaIaInoEmPucMoure){ //&& !act.equals(getPosicion())) {
                posAntiga = new PointF(getPosicion().x,getPosicion().y);
                if(!act.equals(0,0))
                    setPosicion(act);
                calculaAnimes();
                currentFrame = ++currentFrame % BMP_COLUMNS;
                estoyCansadoDeEsperar = false;
            }else {
                /*if(!estaDinsDeMalla(act, mapa.getMalla(), mapa.getZoomBitmap()))
                    Log.e(TAG,"No esta dins la malla");*/
                contEspera++;
                if(contEspera > 40) {
                    estoyCansadoDeEsperar = true;
                    contEspera = 0;
                }
            }
        } else { // ha arribat a la posició objectiu

            if(!laEstoySiguiendo && !meParoAdefender && !meVoyAlHospital) {
                //posObjetivo.set(new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY));
                PointF p = new PointF(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
                while (!esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap())){
                    p.set(r.nextInt(maxX - minX + 1) + minX, r.nextInt(maxY - minY + 1) + minY);
                }
                posObjetivo.set(p);
                calculaRecObjetivo();
                posAntiga = new PointF();
            } else if(meParoAdefender){
                enEspera = true;

                    if(contadorEncaro > 10){
                        posObjetivo.set(81,60);
                        calculaRecObjetivo();
                        meVoyAlHospital = true;
                        meParoAdefender = false;
                        enEspera = false;
                    }
            } else if(meVoyAlHospital) {
                meQuieroMorir = true;
            }

        }
    }
    protected Rect onDraw(Canvas canvas, Jugadora jugadora, int zoomBitmap) {
        //Log.d(TAG,"onDraw");
        update(jugadora,zoomBitmap);
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        src.set(srcX, srcY, srcX + width, srcY + height); //retalla la imatge segons l'animacio
        return src;
    }

    private int getAnimationRow() {
        //double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        //int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direccio];
    }
}
