package com.example.centermedic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centermedic.R;
import com.example.centermedic.adapter.EspecialidadAdapter;
import com.example.centermedic.adapter.SedeAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.EspecialidadDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.SedeDTO;
import com.example.centermedic.dataholder.DataHolderEspecialidad;
import com.example.centermedic.dataholder.DataHolderSede;
import com.example.centermedic.services.EspecialidadService;
import com.example.centermedic.services.OnItemClickListenerEspecialidad;
import com.example.centermedic.services.OnItemClickListenerSede;
import com.example.centermedic.services.SedeService;
import com.example.centermedic.utils.AlertUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Sede extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnItemClickListenerSede {

    EditText etLatitud, etLongitud;
    private ProgressBar progressBar;
    public GoogleMap mMap;
    RecyclerView recycler;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sede);

        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        recycler = findViewById(R.id.recycler_id);
        progressBar = findViewById(R.id.progressBar);
        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        finish();
            }
        });

        etLatitud.setText("-11.9678154");
        etLongitud.setText("-76.9982021");
     //   SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            cargarSedes(0);
          //  AlertUtils.showAlert(Sede.this, "Alerta...", "Fragmento inicializado ", false);
        } else {
            Toast.makeText(this, "Error al cargar el mapa", Toast.LENGTH_SHORT).show();
        };

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng mexico = new LatLng(Double.parseDouble(etLatitud.getText().toString()),Double.parseDouble(etLongitud.getText().toString()));
        mMap.addMarker(new MarkerOptions().position(mexico).title("San Juan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));

       // AlertUtils.showAlert(Sede.this, "Alerta...", "2. Si se agrego el mapa ", false);

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        etLatitud.setText(("" + latLng.latitude));
        etLongitud.setText(("" + latLng.longitude));

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        etLatitud.setText(("" + latLng.latitude));
        etLongitud.setText(("" + latLng.longitude));

    }

    public void cargarSedes(Integer codigo){
        progressBar.setVisibility(View.VISIBLE);
        // Establece el LayoutManager
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Retrofit myRetrofit = MyApi.getInstance();
        SedeService myService = myRetrofit.create(SedeService.class);
        myService.listarSede(codigo).enqueue(new Callback<ResponseDTO<SedeDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<SedeDTO>> call, Response<ResponseDTO<SedeDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<SedeDTO> lstDatos = responseDTO.value;
                        SedeAdapter adapterDatos = new SedeAdapter(lstDatos, Sede.this);
                        recycler.setAdapter(adapterDatos);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"2. No existen registros..",Toast.LENGTH_LONG);
                        recycler.setAdapter(null);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<SedeDTO>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"1. Hubo un problema con la conexion... Error: " + t.getCause(),Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onItemClick(SedeDTO item) {

        etLongitud.setText(item.getLongitud());
        etLatitud.setText(item.getLatitud());

        //   mMap = this.mMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng mexico = new LatLng(Double.parseDouble(etLongitud.getText().toString()),Double.parseDouble(etLatitud.getText().toString()));
        mMap.addMarker(new MarkerOptions().position(mexico).title(item.getNombre()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }
}