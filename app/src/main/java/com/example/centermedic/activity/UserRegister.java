package com.example.centermedic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.centermedic.R;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.api.MySingleton;
import com.example.centermedic.implementacion.IUserService;
import com.example.centermedic.clases.UserDTO;
import com.example.centermedic.services.UserService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRegister extends AppCompatActivity {

    EditText etName,etEmail1, etUser,etKey,etConfirm;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_register);

        etName = findViewById(R.id.etName);
        etEmail1= findViewById(R.id.etEmail1);
        etKey = findViewById(R.id.etKey);
        etUser    = findViewById(R.id.etUser);
        etConfirm = findViewById(R.id.etConfirm);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etKey.getText().toString().equals(etConfirm.getText().toString())){
                    saveUser();
                }else{
                    Toast.makeText(getApplicationContext(),"1. Clave incorrecta...",Toast.LENGTH_LONG);
                  //  saveUser();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveUser(){
        UserDTO user = new UserDTO();
        user.setUsuario1(etUser.getText().toString());
        user.setClave(etKey.getText().toString());
        user.setNombres(etName.getText().toString());
        user.setCorreo(etEmail1.getText().toString());
        user.setIdPerfil(2);
        user.setEstado(1);
        Retrofit myRetrofit = MyApi.getInstance();
        UserService myUserservice = myRetrofit.create(UserService.class);

        myUserservice.saveLogin(user).enqueue(new Callback<IUserService>() {
            @Override
            public void onResponse(Call<IUserService> call, Response<IUserService> response) {
                if(response.isSuccessful()){
                    IUserService responseDTO = response.body();
                    UserDTO userDTOS = responseDTO.value;

                    if(responseDTO.status){
                        MySingleton singleton = MySingleton.getInstance();
                        singleton.setIdUsuario(userDTOS.idUsuario);
                        singleton.setValor(user.getUsuario1());


                        Intent i1 = new Intent(getApplicationContext(), Menu.class);
                        startActivity(i1);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"1. Usuario o clave incorrecta...",Toast.LENGTH_LONG);
                        etUser.requestFocus();
                    }
                }
            }

            @Override
            public void onFailure(Call<IUserService> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"1. Existe un valor incorrecto...",Toast.LENGTH_LONG);
                etUser.requestFocus();
            }
        });
    }
}