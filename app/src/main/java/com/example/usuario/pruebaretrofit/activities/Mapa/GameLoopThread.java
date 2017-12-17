package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Canvas;
import android.util.Log;

public class GameLoopThread extends Thread {

    private final String TAG = this.getClass().getSimpleName();

    static final long FPS = 10;

    private GameView view;
    private boolean running = false;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public void run() {
        Log.d(TAG, "Estic corrent");
        long ticksPS = 100 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                // It first evaluates the expression foo.
                // The result must be an object reference.
                // Then it locks the object, performs the body of the synchronized block, and then it unlocks the object.

                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime); Log.d(TAG," " + sleepTime);
                }else {
                    sleep(10); Log.d(TAG,"10");
                }
            } catch (Exception e) {
                // DO NOTHING
            }
        }
    }
}