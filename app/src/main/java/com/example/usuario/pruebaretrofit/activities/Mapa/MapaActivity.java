package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.model.Usuario2;

public class MapaActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private SurfaceView surfaceView;
    private Usuario2 player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i(TAG,"onCreate");
       // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //        | View.SYSTEM_UI_FLAG_FULLSCREEN;
        Intent in = getIntent();
        player = (Usuario2)in.getSerializableExtra("user");
        int uiOptions2 = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        //View decor = getWindow().getDecorView();
        //decor.setSystemUiVisibility(uiOptions);
        View gameView = new GameView(this, player);
        gameView.setSystemUiVisibility(uiOptions2);
        setContentView(gameView);
        //setContentView(R.layout.activity_mapa);
        //super.onCreate(savedInstanceState);
        int a=0;

    }
}
