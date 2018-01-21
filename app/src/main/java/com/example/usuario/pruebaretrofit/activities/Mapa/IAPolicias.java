package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.example.usuario.pruebaretrofit.model.Objeto;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

/**
 * Created by annag on 18/01/2018.
 */

public class IAPolicias {
    /** Esta clase solo sirve para la clase MapaGrande **/
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

    PointF puertaAlInfierno = new PointF(203,119);
    private boolean meVoy = false, meQuieroMorir= false;
    private boolean meEncaroConTrans = false;
    private boolean estoyDestruyendoObjeto = false;
    private boolean estoyCansadoDeEsperar = false;
    private int contEspera = 0;

    public IAPolicias(Bitmap bmp, String idIa, PointF posicion, PointF posObjetivo, MapaGrande mapa){
        Log.d(TAG, "inicialitzo un IA");
        //this.gameView=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.idIa = idIa;
        this.posicion = posicion;
        this.posObjetivo = posObjetivo;
        this.mapa = mapa;
        calculaRecObjetivo();
        //saberDireccio();
        calculaAnimes();
    }

    public int getSpeed() {
        return speed;
    }
    public int getDireccio() {
        return direccio;
    }
    public Rect getAnima() {
        return anima;
    }
    public PointF getPosicion() {
        return posicion;
    }
    public Bitmap getBmp() {
        return bmp;
    }
    public PointF getPosObjetivo() {
        return posObjetivo;
    }
    public void setAnima(Rect anima) {
        this.anima = anima;
    }
    public boolean isMeQuieroMorir() {
        return meQuieroMorir;
    }
    public boolean isMeEncaroConTrans() {
        return meEncaroConTrans;
    }
    public void setMeEncaroConTrans(boolean meEncaroConTrans) {
        this.meEncaroConTrans = meEncaroConTrans;
    }
    public boolean isEstoyDestruyendoObjeto() {
        return estoyDestruyendoObjeto;
    }
    public void setEstoyDestruyendoObjeto(boolean estoyDestruyendoObjeto) {
        this.estoyDestruyendoObjeto = estoyDestruyendoObjeto;
    }

    private void calculaRecObjetivo(){
        rectObjetivo.set((int)posObjetivo.x-4,(int)posObjetivo.y-4,
                (int)posObjetivo.x+4,(int)posObjetivo.y+4);
    }
    private void calculaAnimes(){
        // [files][columnes]
        this.anima.set((int) posicion.x - mapa.getZoomBitmap()+1, (int) posicion.y - mapa.getZoomBitmap(),
                (int) posicion.x + mapa.getZoomBitmap()-1, (int) posicion.y + mapa.getZoomBitmap());
    }

    private void update(Jugadora jugadora) {
        //Log.d(TAG, "Update: moc una casella");
        /*if(haArribat())
            enEspera = true;
        else
            enEspera = false;*/
        if (!rectObjetivo.contains((int)posicion.x,(int)posicion.y)){//!posicion.equals(posObjetivo) && !enEspera) {
            act = new PointF();
            try {
                double min = 10000000;
                //int[] vecPos = {speed, -speed, 0, 0};
                //int[] vecPos2 = {speed+1, -speed-1, 0, 0};
                // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu

                for (int i = 0; i < 4; i++) {
                    p.set((int) posicion.x + vecPos[i], (int) posicion.y + vecPos[vecPos.length-1-i]);
                    pp.set((int) posicion.x + vecPos2[i], (int) posicion.y + vecPos2[vecPos2.length-1-i]);

                    int d = hiHaUnPoli(p,direccio, mapa.getListaPolicias());//gameView.listaIas);
                    if(d == 2 ){
                        //TODO si es miren, que s'evitin  (ara mateix funciona, fes-ho quan tinguis temps ;)
                        // 2: estan en horitzontal, han d'escapar un per baix i l'altre per dalt
                        // ferho aqui o al començament (millor idea ja que aqui es calcula 4 cops

                    }else if (d==3 ){
                        // 3: estan en vertical, han d'escapar un per la dreta i l'altre per l'esquerra

                    } else {
                        double distancia = calculaDistancia(p, posObjetivo);
                        if (distancia < min && estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                            direccio = i;
                            min = calculaDistancia(p, posObjetivo);
                            act.set(p);
                            act2.set(pp);
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                enEspera=true;
            }

            if(hiHaUnTransPoli(act2,direccio,mapa.getListaTranseuntes())==1) {
                mapa.decirATransQueSeEstaEncarando(this);
            }
            if(hiHaUnObjecte(act2, mapa.getListaObjetos()) == 1) {
                mapa.decirAObjetoQueLoEstoyDestruyendo(this);
            }
            if(hiHaLaJugadora(act2,direccio,jugadora)!=0){
                mapa.dañoVidaPolicia();
            }

            // si m'ho ha calculat bé, actualitzo posicio
            if((!enEspera && hiHaUnPoli(act2, direccio, mapa.getListaPolicias()) == 0 && hiHaLaJugadora(act2,  direccio, jugadora)==0
                    && hiHaUnTransPoli(act2,direccio,mapa.getListaTranseuntes()) == 0
                    && hiHaUnObjecte(act2, mapa.getListaObjetos()) == 0) || estoyCansadoDeEsperar){//gameView.hiHaUnIA(act2,direccio) == 0){// && !hihaIaInoEmPucMoure){ //&& !act.equals(getPosicion())) {

                posAntiga = new PointF(posicion.x, posicion.y);
                if(!act.equals(0,0))
                    posicion.set(act);
                calculaAnimes();
                currentFrame = ++currentFrame % BMP_COLUMNS;
                //estoyCansadoDeEsperar = false;
            }else {

                if(!estaDinsDeMalla(act, mapa.getMalla(), mapa.getZoomBitmap()))
                    Log.e(TAG,"No esta dins la malla");
                /*contEspera++;
                if(contEspera > 5) {
                    estoyCansadoDeEsperar = true;
                    contEspera = 0;
                }*/

            }

            if (enEspera)
                Log.d(TAG,"Ha arribat");

            //Log.d(TAG,"posX " + getPosicion().x);
            //Log.d(TAG,"posY " + getPosicion().y);

        } else { // ha arribat a la posició objectiu
            if (rectObjetivo.contains((int) posicion.x, (int) posicion.y))
                meQuieroMorir = true;
            /*if(!meVoy) {
                // se'n van a les taules
                posObjetivo.set(caminoAseguir[mapa.getCualEsMiCamino()]);
                mapa.setCualEsMiCamino(mapa.getCualEsMiCamino() + 1);
                calculaRecObjetivo();
                meVoy = true;
                if (mapa.getCualEsMiCamino() == caminoAseguir.length)
                    mapa.setCualEsMiCamino(0);
            } else {
                if(tiempoVotangoPasado >= tiempoVotando) {
                    posObjetivo.set(puertaAlInfierno);
                    calculaRecObjetivo();
                    if (rectObjetivo.contains((int) posicion.x, (int) posicion.y))
                        meQuieroMorir = true;
                } else
                    tiempoVotangoPasado++;
            }
            saberDireccio();*/
        }
    }

    protected Rect onDraw(Canvas canvas, Jugadora posJug) {
        //Log.d(TAG,"onDraw");
        update(posJug);
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
    private int hiHaUnTransPoli(PointF p, int direc, java.util.List<IATranseunte> listaIas){//}, int zoomBitmap) {
        for (IATranseunte ia : listaIas) {
            //a = definirRectangle(p,direc,zoomBitmap);
            if (ia.getAnima().contains((int) p.x, (int) p.y) && ia.isMeParoAdefender()) {// && !ia.getPosicion().equals(pos)) {
                //if(intersects(ia.getAnima(),a)){
                return 1;
            }
        }
        return 0;
    }
}
