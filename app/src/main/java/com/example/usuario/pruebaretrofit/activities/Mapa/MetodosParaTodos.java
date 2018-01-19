package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static java.lang.Math.sqrt;

/**
 * Created by annag on 16/01/2018.
 */

public class MetodosParaTodos {
    public static String[][] llegirMapaTxt(String nomTxt, Context context) {
        String line = "";
        int cont = 1;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(nomTxt + ".txt")));

            line = bufferedReader.readLine();
            while (bufferedReader.readLine() != null) {
                cont++;
            }
            bufferedReader.close();
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            return null;
        }
        String[][] malla = new String[cont][line.length()];
        try {
            // Per omplir la malla
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(nomTxt + ".txt")));
            cont = 0;
            while ((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    malla[cont][i] = String.valueOf(line.charAt(i));
                }
                cont++;
            }
            bufferedReader.close();

        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            return null;
        }
        return malla;

    }

    protected static boolean estaDinsDeMalla(PointF p, String[][] malla, int zoomBitmap){
        return p.x <= malla[0].length-zoomBitmap-1 && p.x >= zoomBitmap-1
                && p.y <= (malla.length-zoomBitmap-1) && p.y >= zoomBitmap-1;
    }

    protected static boolean esPotTrepitjar(PointF p, String[][] malla, int zoomBitmap){
        // si per les celes proximes no toca la imatge del bitmap amb taules o merdes
        int[] vec = {zoomBitmap-1, -(zoomBitmap-1), 0, 0};
        String pp = "";
        // de les quatre celes del voltant, miro es la que esta mes aprop de l'objectiu
        try {
            for (int i = 0; i < 4; i++) {
                pp = malla[(int) p.y + vec[vec.length-1-i]][(int) p.x + vec[i]];
                if(!pp.contains("-") && !pp.contains("K") && !pp.contains("P"))
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        // Si la cela p hi ha cami:
        return malla[(int)p.y][(int)p.x].contains("-") || malla[(int)p.y][(int)p.x].contains("K") || malla[(int)p.y][(int)p.x].contains("P");
    }

    protected static Rect definirRectangle(PointF p, int direc, int zoomBitmap){
        Rect a = new Rect(); //TODO suposu que funciona be, pero revisaho per si de cas
        int[][] matrix ={{0, -1, -1, -1},
                {-1, -1, -1, 0},
                {1, 0, 1, 1},
                {1, 1, 0, 1}};
        a.set((int) p.x + matrix[0][direc] * (zoomBitmap),
                (int) p.y + matrix[1][direc] * (zoomBitmap),
                (int) p.x + matrix[2][direc] * (zoomBitmap),
                (int) p.y + matrix[3][direc] * (zoomBitmap));
        return a;
    }

    protected static int hiHaUnTransEnRectangle(PointF p, int direc, java.util.List<IATranseunte> listaIas, int zoomBitmap){//}, int zoomBitmap) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        Rect a;
        for (IATranseunte ia : listaIas) {
            a = definirRectangle(p,direc,zoomBitmap);
            //if (ia.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
            if(intersects(ia.getAnima(),a)){
                return listaIas.indexOf(ia);
            }
        }
        return -1;
    }

    protected static int hiHaUnIA(PointF p, int direc, java.util.List<IA> listaIas, int zoomBitmap){//}, int zoomBitmap) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        int cont = 0;
        Rect a;
        for (IA ia : listaIas) {
            //a = definirRectangle(p,direc,zoomBitmap);
            if (ia.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
            //if(intersects(ia.getAnima(),a)){
                if (Math.abs(direc - ia.getDireccio()) == 1) {
                    if (!((direc == 2 || ia.getDireccio() == 2) &&
                            (direc == 1 || ia.getDireccio() == 1))) {
                        if ((direc == 0 || ia.getDireccio() == 0) &&
                                (direc == 1 || ia.getDireccio() == 1)) {
                            return 2;
                        } else
                            return 3;
                    }
                }
                cont++;
            }
        }
        return cont > 1 ? 1 : 0;
    }

    protected static int hiHaUnTrans(PointF p, int direc, java.util.List<IATranseunte> listaIas){//}, int zoomBitmap) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        int cont = 0;
        Rect a;
        for (IATranseunte ia : listaIas) {
            //a = definirRectangle(p,direc,zoomBitmap);
            if (ia.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
                //if(intersects(ia.getAnima(),a)){
                if (Math.abs(direc - ia.getDireccio()) == 1) {
                    if (!((direc == 2 || ia.getDireccio() == 2) &&
                            (direc == 1 || ia.getDireccio() == 1))) {
                        if ((direc == 0 || ia.getDireccio() == 0) &&
                                (direc == 1 || ia.getDireccio() == 1)) {
                            return 2;
                        } else
                            return 3;
                    }
                }
                cont++;
            }
        }
        return cont > 1 ? 1 : 0;
    }

    protected static int hiHaUnPoli(PointF p, int direc, java.util.List<IAPolicias> listaPolicies) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        int cont = 0;
        for (IAPolicias ia : listaPolicies) {
            if (ia.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
                if (Math.abs(direc - ia.getDireccio()) == 1) {
                    if (!((direc == 2 || ia.getDireccio() == 2) &&
                            (direc == 1 || ia.getDireccio() == 1))) {
                        if ((direc == 0 || ia.getDireccio() == 0) &&
                                (direc == 1 || ia.getDireccio() == 1)) {
                            return 2;
                        } else
                            return 3;
                    }
                }
                cont++;
            }
        }
        return cont > 1 ? 1 : 0;
    }

    protected static int hiHaUnPoliAEscola(PointF p, int direc, java.util.List<IAPoliciaEscuela> listaPolicies, int zoomBitmap) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        // direction = 0 right, 1 left, 2 up, 3 down,
        int cont = 0;
        Rect a;
        for (IAPoliciaEscuela ia : listaPolicies) {
            //a = definirRectangle(p,direc,zoomBitmap);
            if (ia.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
            //if(intersects(ia.getAnima(),a)){
                if (Math.abs(direc - ia.getDireccio()) == 1) {
                    if (!((direc == 2 || ia.getDireccio() == 2) &&
                            (direc == 1 || ia.getDireccio() == 1))) {
                        if ((direc == 0 || ia.getDireccio() == 0) &&
                                (direc == 1 || ia.getDireccio() == 1)) {
                            return 2; //retornar "2" + "index ia" per saber quin es l'altre ia per canviarli la posicio
                        } else
                            return 3;
                    }
                }
                cont++;
            }
        }
        return cont > 1 ? 1 : 0;
    }

    protected static int hiHaLaJugadora(PointF p, int direc, Jugadora jugadora) {
        // 0: no hi ha, 1: hi ha, 2: esta de cara costat, 3 esta e cara vertical
        // 0-1 2-3
        int cont = 0;
        if (jugadora.getAnima().contains((int) p.x, (int) p.y)) {// && !ia.getPosicion().equals(pos)) {
            if (Math.abs(direc - jugadora.getDireccio()) == 1) {
                if (!((direc == 2 || jugadora.getDireccio() == 2) &&
                        (direc == 1 || jugadora.getDireccio() == 1))) {
                    if ((direc == 0 || jugadora.getDireccio() == 0) &&
                            (direc == 1 || jugadora.getDireccio() == 1)) {
                        return 2;
                    } else
                        return 3;
                }
            }
            return 1;
        }
        return 0;
    }

    public static boolean intersects(Rect rect, Rect otherRect) {
        return  rect.left <= otherRect.right && otherRect.left <= rect.right
                && rect.top <= otherRect.bottom && otherRect.top <= rect.bottom;
    }

    protected static int buscarIAperPosicio(PointF p, java.util.List<IA> listaIas) {
        for (IA ia : listaIas) {
            if (ia.getPosicion().equals(p))
                return listaIas.indexOf(ia);
        }
        return -1;
    }

    protected static double calculaDistancia(PointF p1, PointF p2){
        return  sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y));
    }

    protected static int midaCanvas(int canvasT, int length) {
        String TAG = "Hola";
        boolean trobat = false;
        int ampladaCanvas = 0;

        if (canvasT % length != 0) {
            for (int i = canvasT; i > length; i--) {
                if (i % length == 0) {
                    ampladaCanvas = i;
                    trobat = true;
                    break;// TODO; mirar esto porque no redondeo hacia abajo

                }
            }
        } else
            ampladaCanvas = canvasT;
        if (!trobat)
            Log.e(TAG, "No s'ha pogut adaptar el mapa");
        return ampladaCanvas;
    }

    protected static Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
}
