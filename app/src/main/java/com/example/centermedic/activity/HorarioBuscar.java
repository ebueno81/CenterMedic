package com.example.centermedic.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centermedic.R;
import com.example.centermedic.adapter.EspecialidadAdapter;
import com.example.centermedic.adapter.HorarioAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.EspecialidadDTO;
import com.example.centermedic.clases.HorarioDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.dataholder.DataHolderEspecialidad;
import com.example.centermedic.dataholder.DataHolderHorario;
import com.example.centermedic.services.EspecialidadService;
import com.example.centermedic.services.HorarioService;
import com.example.centermedic.services.OnItemClickListenerHorario;
import com.example.centermedic.utils.DatePickerUtils;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HorarioBuscar extends AppCompatActivity implements OnItemClickListenerHorario {
    private EditText etStartDate, etEndDate;
    TextView tvEspecilidad2;

    Button btnBuscar;
    RecyclerView recycler;
    private ProgressBar progressBar;
    private int idEspecialidad=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_horario_buscar);

        tvEspecilidad2 = findViewById(R.id.tvEspecial2);

        Intent intent = getIntent();
        idEspecialidad = intent.getIntExtra("idEspecialidad",0);
        tvEspecilidad2.setText(intent.getStringExtra("Especialidad"));

        btnBuscar = findViewById(R.id.btnBuscar);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        recycler = findViewById(R.id.recycler_id);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar EditText con la fecha actual y configurar el DatePickerDialog
        DatePickerUtils.setEditTextCurrentDate(this, etStartDate);
        DatePickerUtils.setEditTextCurrentDate(this, etEndDate);

        cargarHorario(etStartDate.getText().toString(), etEndDate.getText().toString());

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtils.showDatePickerDialog(HorarioBuscar.this, etStartDate);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtils.showDatePickerDialog(HorarioBuscar.this, etEndDate);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarHorario(etStartDate.getText().toString(),etEndDate.getText().toString());
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onItemClick(HorarioDTO item) {
        DataHolderHorario.setHorarioDTO(item);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name","horarioBuscar");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
    public void cargarHorario(String fechaInicio, String fechaFinal){
        progressBar.setVisibility(View.VISIBLE);
        // Establece el LayoutManager
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Retrofit myRetrofit = MyApi.getInstance();
        HorarioService myService = myRetrofit.create(HorarioService.class);
        myService.listar(fechaInicio, fechaFinal, idEspecialidad).enqueue(new Callback<ResponseDTO<HorarioDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<HorarioDTO>> call, Response<ResponseDTO<HorarioDTO>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<HorarioDTO> lstDatos = responseDTO.value;
                        HorarioAdapter adapterDatos = new HorarioAdapter(lstDatos, HorarioBuscar.this);
                        recycler.setAdapter(adapterDatos);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"2. Usuario o clave incorrecta...",Toast.LENGTH_LONG);
                        recycler.setAdapter(null);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    recycler.setAdapter(null);
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<HorarioDTO>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"1. Hubo un problema con la conexion... Error: " + t.getCause(),Toast.LENGTH_LONG);
            }
        });
    }

}