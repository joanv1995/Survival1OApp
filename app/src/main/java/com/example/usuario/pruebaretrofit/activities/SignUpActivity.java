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
import com.example.usuario.pruebaretrofit.model.Usuario;
import com.example.usuario.pruebaretrofit.service.RestClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
Button sign;
String name;
String pass;
String passRep;
String email;
Usuario user;

EditText nombre;
EditText pass1;
EditText pass2;
EditText mail;
    private static final String URL_BASE = "http://10.193.222.188:8080/1O-survival/game/"; ///nuestra api virtual
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


            user = new Usuario(name, pass, email);
            Call<Integer> call = service.signupUser(user);
            try {
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        pd.dismiss();
                        signUpAnswer(response.body());
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        t.getMessage();

                    }
                });
            }

            catch (Exception e){
                throw e;
            }

    }
    public void signUpAnswer(Integer i){

            switch (i){

                case 1:
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                    break;



            }
            finish();

            }

}
