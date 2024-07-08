package com.example.centermedic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centermedic.R;
import com.example.centermedic.adapter.SedeAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.SedeDTO;
import com.example.centermedic.services.OnItemClickListenerSede;
import com.example.centermedic.services.SedeService;
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            cargarSedes(0);
        } else {
            Toast.makeText(this, "Error al cargar el mapa", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        LatLng defaultLocation = new LatLng(Double.parseDouble(etLatitud.getText().toString()), Double.parseDouble(etLongitud.getText().toString()));
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("San Juan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        etLatitud.setText(String.valueOf(latLng.latitude));
        etLongitud.setText(String.valueOf(latLng.longitude));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        etLatitud.setText(String.valueOf(latLng.latitude));
        etLongitud.setText(String.valueOf(latLng.longitude));
    }

    public void cargarSedes(Integer codigo) {
        progressBar.setVisibility(View.VISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Retrofit myRetrofit = MyApi.getInstance();
        SedeService myService = myRetrofit.create(SedeService.class);
        myService.listarSede(codigo).enqueue(new Callback<ResponseDTO<SedeDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<SedeDTO>> call, Response<ResponseDTO<SedeDTO>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ResponseDTO<SedeDTO> responseDTO = response.body();
                    if (responseDTO.status) {
                        List<SedeDTO> lstDatos = responseDTO.value;
                        SedeAdapter adapterDatos = new SedeAdapter(lstDatos, Sede.this);
                        recycler.setAdapter(adapterDatos);
                    } else {
                        Toast.makeText(getApplicationContext(), "No existen registros.", Toast.LENGTH_LONG).show();
                        recycler.setAdapter(null);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<SedeDTO>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Hubo un problema con la conexión. Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(SedeDTO item) {
        etLatitud.setText(item.getLatitud());
        etLongitud.setText(item.getLongitud());

        LatLng selectedLocation = new LatLng(Double.parseDouble(item.getLatitud()), Double.parseDouble(item.getLongitud()));
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(selectedLocation).title(item.getNombre()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15));
    }
}
