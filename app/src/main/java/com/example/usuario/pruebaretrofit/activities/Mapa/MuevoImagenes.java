package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Bitmap;

/**
 * Created by annag on 11/01/2018.
 */

public abstract class MuevoImagenes {
    /** Esta clase no se utiliza actualmente **/
    // TODO: veure com faig aixo
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private Bitmap bmp;
    private GameView gameView;
    private int width; // de la imatge
    private int height;
    private static final int[] DIRECTION_TO_ANIMATION_MAP = {2, 1, 3, 0};

    private int getAnimationRow(int direccio) {
        //double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        //int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direccio];
    }

}
