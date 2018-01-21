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
        this.direccio=0;
        calculaRecObjetivo();
        //saberDireccio();
        //calculaAnimes();
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
        this.posicion.x=this.posicion.x+speed;
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


