package com.example.centermedic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centermedic.R;
import com.example.centermedic.adapter.PacienteAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.dataholder.DataHolder;
import com.example.centermedic.services.OnItemClickListener;
import com.example.centermedic.services.PacienteService;
import com.example.centermedic.utils.KeyboardUtils;
import com.example.centermedic.datahelper.SearchDatabaseHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PacienteBuscar extends AppCompatActivity implements OnItemClickListener {
    EditText etBuscar;
    RecyclerView recycler;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private Runnable runnable;
    private SearchDatabaseHelper dbHelper;
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paciente_buscar);

        progressBar = findViewById(R.id.progressBar);
        etBuscar = findViewById(R.id.etBuscar);
        recycler = findViewById(R.id.recycler_id);
        ivCancel = findViewById(R.id.ivCancel);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbHelper = new SearchDatabaseHelper(this);

        // Recuperar el último valor de búsqueda al iniciar la actividad
        String lastSearch = dbHelper.getLastSearchText();
        etBuscar.setText(lastSearch);

        cargarPaciente(lastSearch);
        //cargarPaciente("");

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Esta función se llama antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Esta función se llama mientras el texto está cambiando
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Guardar el texto de búsqueda en la base de datos
                String searchText = s.toString().trim(); // Asegúrate de limpiar el texto si es necesario
                dbHelper.insertSearchText(searchText);
                dbHelper.updateSearchText(searchText);

                // Crear un nuevo runnable para ejecutar después de 2 segundos
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        cargarPaciente(s.toString());
                        KeyboardUtils.esconderTeclado(PacienteBuscar.this);
                    }
                };
                handler.postDelayed(runnable, 2000); // 2000 milisegundos = 3 segundos
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onItemClick(PacienteDTO item) {
        Intent returnIntent = new Intent();
        DataHolder.setPacienteDTO(item);
        returnIntent.putExtra("name","pacienteBuscar");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void cargarPaciente(String nombrePaciente){
        progressBar.setVisibility(View.VISIBLE);
        // Establece el LayoutManager
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Retrofit myRetrofit = MyApi.getInstance();
        PacienteService myPacienteService = myRetrofit.create(PacienteService.class);
        myPacienteService.listarPaciente(0,nombrePaciente).enqueue(new Callback<ResponseDTO<PacienteDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<PacienteDTO>> call, Response<ResponseDTO<PacienteDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<PacienteDTO> lstPaciente = responseDTO.value;
                        PacienteAdapter adapterDatos = new PacienteAdapter(lstPaciente, PacienteBuscar.this);
                        recycler.setAdapter(adapterDatos);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        recycler.setAdapter(null);
                        KeyboardUtils.esconderTeclado(PacienteBuscar.this);
                     //   progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"2. No existen registros...",Toast.LENGTH_LONG);
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<PacienteDTO>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"1. Hubo un problema con la conexion... Error: " + t.getCause(),Toast.LENGTH_LONG);
            }
        });
    }

}