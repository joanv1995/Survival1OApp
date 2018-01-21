package com.example.usuario.pruebaretrofit.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.model.Usuario2;
import com.example.usuario.pruebaretrofit.service.RestClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
Button sign;
Button volver;
String name;
String pass;
String passRep;
String email;
Usuario2 user;
Usuario2 userReturned;

EditText nombre;
EditText pass1;
EditText pass2;
EditText mail;
    private static final String URL_BASE = "http://147.83.7.206:8088/1O-survival/game/"; ///nuestra api virtual
    private Retrofit retrofit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nombre = (EditText) findViewById(R.id.namee);
        pass1 = (EditText) findViewById(R.id.pass11);
        pass2 = (EditText) findViewById(R.id.pass22);
        mail = (EditText) findViewById(R.id.mail);
        sign= (Button) findViewById(R.id.signup);
        volver = (Button) findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nombre.getText().toString();
                pass = pass1.getText().toString();
                passRep = pass2.getText().toString();
                email = mail.getText().toString();
                if(pass.equals(passRep)) {
                    connectService();
                }
        else{
                Log.i("user","Falló");
                Toast.makeText(getApplicationContext(),"El campo 'Contraseña' y 'Repetir contraseña' deben de ser iguales",Toast.LENGTH_LONG).show();

            }
            }
        });

    }
    public void connectService(){
        final ProgressDialog pd;
        pd = new ProgressDialog(SignUpActivity.this);
        pd.setMax(100);
        pd.setMessage("Connectant...");
        //pd.setTitle("");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        pd.show();
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RestClient service = retrofit.create(RestClient.class);


            user = new Usuario2(name, pass, email);
            Call<Usuario2> call = service.signupUser(user);
            try {
                call.enqueue(new Callback<Usuario2>() {
                    @Override
                    public void onResponse(Call<Usuario2> call, Response<Usuario2> response) {
                        pd.dismiss();

                        userReturned = new Usuario2(response.body().getNombre(), response.body().getPassword(), response.body().getCorreo(), response.body().getPuntFinal(), response.body().getResponse());
                        //userReturned = response.body();

                        signUpAnswer(userReturned.getResponse());



                    }

                    @Override
                    public void onFailure(Call<Usuario2> call, Throwable t) {
                        t.getMessage();

                    }
                });
            }

            catch (Exception e){
                throw e;
            }

    }
    public void signUpAnswer(int response){

        if(response == 0) {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (response == -1) {
            Toast.makeText(this, "Usuario ya registrado, pruebe con otras credenciales.", Toast.LENGTH_SHORT).show();
            nombre.setText("");
            pass1.setText("");
            pass2.setText("");
            mail.setText("");
        }




            /*switch (i){

                case 1:
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                    break;



            }
            finish();*/



    }

}
