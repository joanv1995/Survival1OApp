package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import static com.example.usuario.pruebaretrofit.activities.Mapa.MetodosParaTodos.*;

/**
 * Created by annag on 19/01/2018.
 */

public class IAMinijuego {
    /** Esta clase solo sirve para la clase MapaEscuela **/
    private final String TAG = this.getClass().getSimpleName();

    // Imatge bitMap
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private Bitmap bmp;

    //private GameView gameView;
    private Minijuego mapa;
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

    PointF puertaAlInfierno = new PointF(100,95);
    private int tiempoVotando = 10, tiempoVotangoPasado = 0;
    private boolean meVoy = false, meQuieroMorir= false;
    private boolean estoyCansadoDeEsperar = false;
    private int contEspera = 0;

    public IAMinijuego(Bitmap bmp, String idIa, PointF posicion, PointF posObjetivo, Minijuego mapa){
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

    private void calculaRecObjetivo(){
        rectObjetivo.set((int)posObjetivo.x-4,(int)posObjetivo.y-4,
                (int)posObjetivo.x+4,(int)posObjetivo.y+4);
    }
    private void calculaAnimes(){
        // [files][columnes]
        this.anima.set((int) posicion.x - mapa.getZoomBitmap()+1, (int) posicion.y - mapa.getZoomBitmap(),
                (int) posicion.x + mapa.getZoomBitmap()-1, (int) posicion.y + mapa.getZoomBitmap());
    }

    private void update(Jugadora jugadora, int zoomBitmap) {
        Log.d(TAG, "Update: moc una casella");
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

                    int d = 0;//hiHaUnIA(p,direccio, mapa.getListaIas(),zoomBitmap);//gameView.listaIas);
                    int dd = 0;//hiHaUnPoliAEscola(p,direccio, mapa.getListaPolicias(),zoomBitmap);//gameView.listaIas);
                    if(d == 2 || dd == 2){ // direction = 0 right, 1 left, 2 up, 3 down,
                        //TODO si es miren, que s'evitin  (ara mateix funciona, fes-ho quan tinguis temps ;)
                        // 2: estan en horitzontal, han d'escapar un per baix i l'altre per dalt
                        // ferho aqui o al començament (millor idea ja que aqui es calcula 4 cops
                        p.set((int) posicion.x, (int) posicion.y + speed*2);
                        pp.set((int) posicion.x, (int) posicion.y + speed*2 + 1);
                        if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                            direccio = 3;
                            act.set(p); //forço a que baixi cap a baix
                            act2.set(pp);
                            break;
                        } else {
                            p.set((int) posicion.x, (int) posicion.y - speed*2);
                            pp.set((int) posicion.x, (int) posicion.y - speed*2 - 1);
                            if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                    && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                                direccio = 2;
                                act.set(p); //forço a que baixi cap a baix
                                act2.set(pp);
                                break;
                            }
                        }
                    }else if (d==3 || dd == 3){
                        // 3: estan en vertical, han d'escapar un per la dreta i l'altre per l'esquerra
                        p.set((int) posicion.x + speed*2, (int) posicion.y);
                        pp.set((int) posicion.x + speed*2 + 1, (int) posicion.y);
                        if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                            direccio = 0;
                            act.set(p); //forço a que baixi cap a baix
                            act2.set(pp);
                            break;
                        } else {
                            p.set((int) posicion.x - speed*2, (int) posicion.y);
                            pp.set((int) posicion.x - speed*2 - 1, (int) posicion.y);
                            if (estaDinsDeMalla(p, mapa.getMalla(), mapa.getZoomBitmap())
                                    && esPotTrepitjar(p, mapa.getMalla(), mapa.getZoomBitmap()) && !p.equals(posAntiga)) {
                                direccio = 1;
                                act.set(p); //forço a que baixi cap a baix
                                act2.set(pp);
                                break;
                            }
                        }
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

            // si m'ho ha calculat bé, actualitzo posicio
            if((!enEspera /*&& hiHaUnIA(act2,direccio, mapa.getListaIas(),zoomBitmap) == 0
                    && hiHaUnPoliAEscola(act2, direccio, mapa.getListaPolicias(), zoomBitmap) == 0*/
                    && hiHaLaJugadora(act2,  direccio, jugadora)==0) || estoyCansadoDeEsperar){//gameView.hiHaUnIA(act2,direccio) == 0){// && !hihaIaInoEmPucMoure){ //&& !act.equals(getPosicion())) {
                posAntiga = new PointF(posicion.x, posicion.y);
                if(!act.equals(0,0))
                    posicion.set(act);
                calculaAnimes();
                currentFrame = ++currentFrame % BMP_COLUMNS;
                estoyCansadoDeEsperar = false;
            }else {
                if(!estaDinsDeMalla(act, mapa.getMalla(), mapa.getZoomBitmap()))
                    Log.e(TAG,"No esta dins la malla");
                contEspera++;
                if(contEspera > 35) {
                    estoyCansadoDeEsperar = true;
                    contEspera = 0;
                }

            }

            if (enEspera)
                Log.d(TAG,"Ha arribat");

            //Log.d(TAG,"posX " + getPosicion().x);
            //Log.d(TAG,"posY " + getPosicion().y);

        } else { // ha arribat a la posició objectiu
            //if (rectObjetivo.contains((int) posicion.x, (int) posicion.y))
            //meQuieroMorir = true;
            //if(!meVoy) {
            // se'n van a les taules

            //mapa.setCualEsMiCamino(mapa.getCualEsMiCamino() + 1);
            //calculaRecObjetivo();
            //meVoy = true;
            //if (mapa.getCualEsMiCamino() == caminoAseguir.length)
            //    mapa.setCualEsMiCamino(0);
            //} else {
            if(tiempoVotangoPasado >= tiempoVotando) {
                posObjetivo.set(puertaAlInfierno);
                posAntiga = new PointF();
                calculaRecObjetivo();
                if (rectObjetivo.contains((int) posicion.x, (int) posicion.y))
                    meQuieroMorir = true;
            } else {
                /// poLICIAS SE LLEVA URNA PROGRESS BAR O ALGO ASI
                //cancelandoUrna = true;

                tiempoVotangoPasado++;
            }
            //}
        }
    }

    protected Rect onDraw(Canvas canvas, Jugadora posJug, int zoomBitmap) {
        Log.d(TAG,"onDraw");
        update(posJug, zoomBitmap);
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


