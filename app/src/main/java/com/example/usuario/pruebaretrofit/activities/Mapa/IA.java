package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;
import static java.lang.Math.sqrt;

public class IA extends MuevoImagenes{
    /** Esta clase solo sirve para la clase MapaEscuela **/
    private final String TAG = this.getClass().getSimpleName();

    // Imatge bitMap
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private Bitmap bmp;

    //private GameView gameView;
    private MapaEscuela mapa;
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
    private int speed = 4; // TODO: mirar que faig amb la velocitat
    private boolean direccioX_Left, direccioY_Up; // true: up, false: down
    private int direccio; // direction = 0 right, 1 left, 2 up, 3 down,



    private Rect anima = new Rect();

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = {2, 1, 3, 0};//{ 3, 1, 0, 2 };


    // per dibuixar
    private PointF act = new PointF(), act2 = new PointF();
    private PointF p = new PointF();
    private PointF pp = new PointF();
    private Rect src = new Rect();
    private int[] vecPos = {speed, -speed, 0, 0};
    private int[] vecPos2 = {speed+1, -speed-1, 0, 0};


    /*private PointF[] caminoAseguir = {new PointF(55, 36),
                                new PointF(145, 36),
                                new PointF(55, 66),
                                new PointF(145,66)};*/
    private PointF puertaAlInfierno = new PointF(100,2);
    private boolean meVoy = false, meQuieroMorir= false;
    private int tiempoVotando = 10, tiempoVotangoPasado = 0;
    private boolean estoyCansadoDeEsperar = false;

    private boolean votando = false;
    private int contEspera = 0;

    public IA(Bitmap bmp, String idIa, PointF posicion, PointF posObjetivo, MapaEscuela mapaEscuela){
        Log.d(TAG, "inicialitzo un IA");
        //this.gameView=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.idIa = idIa;
        this.posicion = posicion;
        this.posObjetivo = posObjetivo;
        this.mapa = mapaEscuela;
        calculaRecObjetivo();
        saberDireccio();
        calculaAnimes();
    }

    private void calculaRecObjetivo(){
        rectObjetivo.set((int)posObjetivo.x-4,(int)posObjetivo.y-4,
                (int)posObjetivo.x+4,(int)posObjetivo.y+4);
    }
    private void calculaAnimes(){
        // [files][columnes]
        /*this.anima.set((int) posicion.x + matrix[0][direccio] * gameView.getZoomBitmap(),
                (int) posicion.y + matrix[1][direccio] * gameView.getZoomBitmap(),
                (int) posicion.x + matrix[2][direccio] * gameView.getZoomBitmap(),
                (int) posicion.y + matrix[3][direccio] * gameView.getZoomBitmap());*/
            this.anima.set((int) posicion.x - mapa.getZoomBitmap()+1, (int) posicion.y - mapa.getZoomBitmap(),
                    (int) posicion.x + mapa.getZoomBitmap()-1, (int) posicion.y + mapa.getZoomBitmap());
    }
    private void saberDireccio(){
        this.direccioX_Left = posicion.x >= posObjetivo.x ;
        this.direccioY_Up = posicion.y >= posObjetivo.y ;
    }

    private boolean haArribat(){
        // TODO: pensar una altre manera de fer aixo
        if(direccioY_Up){
            if (direccioX_Left)
                return posicion.y <= posObjetivo.y && posicion.x <= posObjetivo.x;
            else
                return posicion.y <= posObjetivo.y && posicion.x >= posObjetivo.x;
        } else {
            if (direccioX_Left)
                return posicion.y >= posObjetivo.y && posicion.x <= posObjetivo.x;
            else
                return posicion.y >= posObjetivo.y && posicion.x >= posObjetivo.x;
        }
    }

    public Rect getRectObjetivo() {
        return rectObjetivo;
    }

    public boolean isVotando() {
        return votando;
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



    private void update(Jugadora jugadora, int zoomBitmap) {
        Log.d(TAG, "Update: moc una casella");
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

                    int d = hiHaUnIA(p,direccio, mapa.getListaIas(),zoomBitmap);//gameView.listaIas);
                    int dd = hiHaUnPoliAEscola(p,direccio, mapa.getListaPolicias(), zoomBitmap);
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
                        //if (distancia < min && /*estaDinsDeMalla(p)*/ estaDinsDeMalla2(p,gameView.getMalla(),gameView.getZoomBitmap()) && esPotTrepitjar(p) && !p.equals(posAntiga)) {
                    }
                }

            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                enEspera=true;
            }

            // si m'ho ha calculat bé, actualitzo posicio
            if((!enEspera && hiHaUnIA(act2,direccio, mapa.getListaIas(),zoomBitmap) == 0
                    && hiHaUnPoliAEscola(act2, direccio, mapa.getListaPolicias(),zoomBitmap) == 0
                    && hiHaLaJugadora(act2,  direccio, jugadora)==0) || estoyCansadoDeEsperar){//gameView.hiHaUnIA(act2,direccio) == 0){// && !hihaIaInoEmPucMoure){ //&& !act.equals(getPosicion())) {
                posAntiga = new PointF(getPosicion().x,getPosicion().y);
                if(!act.equals(0,0))
                    setPosicion(act);
                calculaAnimes();
                currentFrame = ++currentFrame % BMP_COLUMNS;
                estoyCansadoDeEsperar = false;
            }else {
                if(!estaDinsDeMalla(act, mapa.getMalla(), mapa.getZoomBitmap()))
                    Log.e(TAG,"No esta dins la malla");
                contEspera++;
                if(contEspera > 20) {
                    estoyCansadoDeEsperar = true;
                    contEspera = 0;
                }
            }

            if (enEspera)
                Log.d(TAG,"Ha arribat");

            //Log.d(TAG,"posX " + getPosicion().x);
            //Log.d(TAG,"posY " + getPosicion().y);

        } else { // ha arribat a la posició objectiu
            if(!meVoy) {
                // se'n van a les taules
                posObjetivo.set(mapa.cambioPosObjetivoIA(this));
                //mapa.setCualEsMiCamino(mapa.getCualEsMiCamino() + 1);
                calculaRecObjetivo();
                posAntiga = new PointF();
                meVoy = true;
            } else {
                if(tiempoVotangoPasado >= tiempoVotando) {
                    votando = false;
                    posObjetivo.set(puertaAlInfierno);
                    posAntiga = new PointF();
                    calculaRecObjetivo();
                    if (rectObjetivo.contains((int) posicion.x, (int) posicion.y))
                        meQuieroMorir = true;
                } else {
                    //// VOTANTE VOTANDO
                    votando = true;
                    tiempoVotangoPasado++;
                }
            }
            //saberDireccio();
        }
    }

    /*private double calculaDistancia(PointF p1, PointF p2){
        return  sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y));
    }*/

    protected Rect onDraw(Canvas canvas, Jugadora jugadora, int zoomBitmap) {
        Log.d(TAG,"onDraw");
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
    /*private boolean estaDinsDeMalla(PointF p){
        return p.x <= gameView.getMalla()[0].length-gameView.getZoomBitmap()-1 && p.x >= gameView.getZoomBitmap()-1
                && p.y <= (gameView.getMalla().length-gameView.getZoomBitmap()-1) && p.y >= gameView.getZoomBitmap()-1;
    }
    private boolean esPotTrepitjar(PointF p){
        // si per les celes proximes no toca la imatge del bitmap amb taules o merdes
        int[] vec = {gameView.getZoomBitmap()-1, -(gameView.getZoomBitmap()-1), 0, 0};
        // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu
        try {
            for (int i = 0; i < 4; i++) {
                if(!gameView.getMalla()[(int) p.y + vec[vec.length-1-i]][(int) p.x + vec[i]].contains("-"))
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        // Si la cela p hi ha cami:
        return gameView.getMalla()[(int)p.y][(int)p.x].contains("-");
    }*/

}