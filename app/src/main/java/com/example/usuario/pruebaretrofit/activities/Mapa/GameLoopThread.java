package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.graphics.Canvas;
import android.util.Log;

public class GameLoopThread extends Thread {

    private final String TAG = this.getClass().getSimpleName();

    static final long FPS = 30;

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
        long ticksPS = 3000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;

            startTime = System.currentTimeMillis();
            try {
                //  (synchronized) It first evaluates the expression foo (view.getHolder()).
                // The result must be an object reference.
                // Then it locks the object, performs the body of the synchronized block, and then it unlocks the object.

                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) { //si falla lo del try desbloqueixo el canvas per si de cas
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
// ticksPS es un marge de temps per executar els processos,
// (System.currentTimeMillis() - startTime) temps que triga en executar el process
//  Si es triga mes que el marge, per no posar un temps d'espera negatiu, es posa un fixe, pero no s'espera a que s'arribi a aquest extrem
// amb aquest metode s'igualen els temps enstre threads

// An application consists of one or more processes.
// A process, in the simplest terms, is an executing program.
// One or more threads run in the context of the process.
// A thread is the basic unit to which the operating system allocates processor time.
// A thread can execute any part of the process code, including parts currently being executed by another thread