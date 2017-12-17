package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.usuario.pruebaretrofit.R;

public class MapaActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i(TAG,"onCreate");
        setContentView(new GameView(this));
        //setContentView(R.layout.activity_mapa);
    }
}
