package com.example.usuario.pruebaretrofit.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.activities.Mapa.MapaActivity;
import com.example.usuario.pruebaretrofit.model.Usuario2;
import com.example.usuario.pruebaretrofit.service.RestClient;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    String playerName;
    EditText password;
    String pass;
    Button login;
    Button signup;
    Retrofit retrofit;
    Usuario2 user;
    Button button;

    private static final String URL_BASE = "http://147.83.7.206:8088/1O-survival/game/"; ///nuestra api virtual



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userName = (EditText) findViewById(R.id.usuarioname);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(it);
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setTypeface(null, Typeface.NORMAL);
                userName.setTextColor(Color.parseColor("#000000"));//negro
                userName.setText("");

            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password.setTypeface(null, Typeface.NORMAL);
                password.setTextColor(Color.parseColor("#000000"));
                password.setText("");
                password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            }
        });
        login.setBackgroundResource(R.drawable.buttonstatelogin);
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               playerName = userName.getText().toString();
               pass = password.getText().toString();
               //user = new Usuario2("Joan Valverde","admin","joanv1995@gmail.com");
               connectApiService();



           }
       });

        /*button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapa = new Intent(LoginActivity.this, MapaActivity.class);
                startActivity(mapa);
            }
        });*/

    }



    public void connectApiService() {
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setIndeterminate(true);
        pd.setTitle("1O - Survival");
        pd.setMessage("Cargando usuario");
        pd.show();
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RestClient service = retrofit.create(RestClient.class);

        user = new Usuario2(playerName,pass);


        Call<Usuario2> call = service.esUser(user);
        try {

            Callback<Usuario2> cb = new Callback<Usuario2>() {
                @Override
                public void onResponse(Call<Usuario2> call, Response<Usuario2> response) {
                    user = (Usuario2) response.body();

                    //user = new Usuario2(response.body().getNombre(), response.body().getPassword(), response.body().getCorreo(), response.body().getPuntFinal(), response.body().getResponse());

                    EntryUserInterface(user.getResponse(),pd);

                    //if(!p.getPassword().equals(pass))
                    //Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                    //else


                }

                @Override
                public void onFailure(Call<Usuario2> call, Throwable t) {
                    //Log.e("players", t.toString());
                }
            };
            Log.i("players", "abans enqueue");
            call.enqueue(cb);

        } catch (Exception er) {
            Log.e("error", er.getMessage());
        }
    }
        public void EntryUserInterface(int response, ProgressDialog pd){
            //if(user!=null)
            //{
            //    Toast.makeText(this,"Usuario "+user.getNombre()+"ha sido logueado correctamente", Toast.LENGTH_SHORT).show();

              //  Intent in = new Intent(this, PerfilActivity.class);
               //in.putExtra("jugador",user);

              //startActivity(in);
            //}
           // else{
            if(response ==0) {
                stopProgress(pd);
                Toast.makeText(this,"Usuario "+ user.getNombre()+" ha sido logueado correctamente", Toast.LENGTH_LONG).show();
                Intent in = new Intent(LoginActivity.this,PerfilActivity.class);
                in.putExtra("jugador", user);
                startActivity(in);
            }
            if (response == -3){
                stopProgress(pd);
                Toast.makeText(this,"Usuario no existe o credenciales mal introducidas, intente de nuevo.", Toast.LENGTH_LONG).show();
                userName.setText("");
                password.setText("");
            }


            //}

    }
    private void stopProgress(final ProgressDialog pd){
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    pd.dismiss();
                    t.cancel();
                }
            },1500);

    }


}

 class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }
        public char charAt(int index) {
            return '*'; // This is the important part
        }
        public int length() {
            return mSource.length(); // Return default
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}

