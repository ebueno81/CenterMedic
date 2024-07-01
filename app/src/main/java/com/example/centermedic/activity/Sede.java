package com.example.centermedic.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.centermedic.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Sede extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    EditText etLatitud, etLongitud;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sede);

        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        LatLng mexico = new LatLng(-11.9678154,-76.9982021);
        mMap.addMarker(new MarkerOptions().position(mexico).title("San Juan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
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
}