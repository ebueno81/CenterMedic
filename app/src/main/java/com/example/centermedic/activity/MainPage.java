package com.example.centermedic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.centermedic.R;
import com.example.centermedic.api.MySingleton;
import com.example.centermedic.clases.CitaDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.fragment.FragmentCita;
import com.example.centermedic.fragment.FragmentCitaList;
import com.example.centermedic.fragment.FragmentMedicoList;
import com.example.centermedic.fragment.FragmentPacienteList;
import com.example.centermedic.fragment.FragmentPaciente;

public class MainPage extends AppCompatActivity {
    LinearLayout llHome, llDoctor, llPaciente, llHorario, llUser, llMnuCita;
    TextView tvUser1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);


        getSupportFragmentManager().beginTransaction().add(R.id.fmMain, new FragmentPacienteList()).commit();

        llHome = findViewById(R.id.llHome);
        llDoctor = findViewById(R.id.llDoctor);
        llPaciente = findViewById(R.id.llPaciente);
        llUser = findViewById(R.id.llUser);
        llMnuCita = findViewById(R.id.llMnuCita);

        tvUser1 = findViewById(R.id.tvUser1);
        MySingleton singleton = MySingleton.getInstance();
        tvUser1.setText("Hola, " + singleton.getValor());

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, Menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Para finalizar la actividad actual si lo deseas
            }
        });

        llMnuCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("ejecutarFuncion", true); // Pasar datos adicionales al Intent
                startActivity(intent);
            }
        });
        // Verificar si hay datos pasados a través del Intent
        if (getIntent().hasExtra("ejecutarFuncion")) {
            citasList();
        }

        if (getIntent().hasExtra("callDoctors")) {
            doctorList();
        }

        llDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorList();
            }
        });

        llPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pacienteList();
            }
        });

        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, UserRegister.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void replaceFragmentEdit(PacienteDTO paciente) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentPaciente(paciente))
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();
    }
    public void replaceFragmentNew() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentPaciente(null))
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();
    }
    public void replaceFragmentList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentPacienteList())
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();

    }

    public void pacienteList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentPacienteList())
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();

    }

    public void citasList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentCitaList())
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();

    }

    public void citasRegister(CitaDTO datos) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentCita(datos))
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();

    }
    public void doctorList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fmMain, new FragmentMedicoList())
                .addToBackStack(null) // Agregar a la pila de retroceso para permitir regresar al fragmento anterior
                .commit();

    }

}