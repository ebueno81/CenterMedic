package com.example.centermedic.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.centermedic.R;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.api.MySingleton;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.UserDTO;
import com.example.centermedic.services.UserService;
import com.example.centermedic.utils.AlertUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {
    AppCompatButton btnEnter;
EditText etUser;
private TextInputEditText etPassword;
private TextInputLayout textInputLayoutPassword;
    private ProgressBar progressBar;
LinearLayout lnNewUser1, lnNewUser2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        lnNewUser1 = findViewById(R.id.newUser1);
        lnNewUser2 = findViewById(R.id.newUser2);
        btnEnter = findViewById(R.id.btnEnter);
        etUser  = findViewById(R.id.etUser);

        progressBar = findViewById(R.id.progressBar);
        etPassword = findViewById(R.id.etPassword);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        lnNewUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(i1);
                finish();
            }
        });

        lnNewUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(i1);
                finish();
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // iniciarSession();
                btnEnter.setEnabled(false); // Desactivar el botón
              //  new IniciarSessionTask().execute(); // Ejecutar la tarea asíncrona
                iniciarSession();
                btnEnter.setEnabled(true);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    private void iniciarSession(){
        progressBar.setVisibility(View.VISIBLE);
        UserDTO user = new UserDTO();
        user.setUsuario1(etUser.getText().toString());
        user.setClave(etPassword.getText().toString());

        Retrofit myRetrofit = MyApi.getInstance();
        UserService myUserservice = myRetrofit.create(UserService.class);
        MySingleton singleton = MySingleton.getInstance();

        myUserservice.validarLogin(user).enqueue(new Callback<ResponseDTO<UserDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                if(response.isSuccessful()){
                    ResponseDTO responseDTO = response.body();
                    List<UserDTO> userDTOS = responseDTO.value;
                    if(responseDTO.status){
                        singleton.setValor(user.usuario1);
                        singleton.setIdUsuario(user.idUsuario);

                        Intent i1 = new Intent(getApplicationContext(), Menu.class);

                        startActivity(i1);
                        finish();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        AlertUtils.showAlert(Login.this, "Alerta...", "1. Usuario o clave incorrecta...", false);
                        etUser.requestFocus();
                        btnEnter.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtils.showAlert(Login.this, "Alerta...", "2. Hubo un error de conexion " + t.getMessage(), false);}

        });

    }
}