package com.example.centermedic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.centermedic.R;
import com.example.centermedic.activity.Menu;
import com.example.centermedic.activity.UserRegister;
import com.example.centermedic.adapter.CitaListAdapter;
import com.example.centermedic.adapter.PacienteListAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.CitaDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.services.CitaService;
import com.example.centermedic.services.PacienteService;
import com.example.centermedic.utils.AlertUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentCita extends Fragment {

    TextView tvNombre, tvApePaterno, tvApeMaterno, tvDoctor, tvEspecialidad, tvHorario, tvFecha, tvConsultorio;
    CitaDTO citaDTO;
    Button btnCancel;
    public FragmentCita(CitaDTO citaDTO) {
        this.citaDTO = citaDTO;

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNombre = view.findViewById(R.id.tvNombres);
        tvApePaterno = view.findViewById(R.id.tvApePat);
        tvApeMaterno = view.findViewById(R.id.tvApeMat);
        tvDoctor = view.findViewById(R.id.tvMedico);
        tvEspecialidad = view.findViewById(R.id.tvEspecialidad);
        tvFecha = view.findViewById(R.id.tvFecha);
        tvHorario = view.findViewById(R.id.tvHora);
        tvConsultorio = view.findViewById(R.id.tvConsultorio);
        btnCancel = view.findViewById(R.id.btnCancel);

        handlePacienteSelection(citaDTO.getIdCita());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Menu.class);
                startActivity(intent);
            }
        });

    }
    // Función para manejar la selección del paciente
    private void handlePacienteSelection(Integer idCita) {

    //    AlertUtils.showAlert(requireContext(), "Alerta...", "Cita seleccionada: " + citaDTO.getIdCita() , false);
        Retrofit myRetrofit = MyApi.getInstance();
        CitaService myCitaService = myRetrofit.create(CitaService.class);
        myCitaService.obtenerRegistro(idCita).enqueue(new Callback<ResponseDTO<CitaDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<CitaDTO>> call, Response<ResponseDTO<CitaDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<CitaDTO> lstDatos = responseDTO.value;
                        CitaDTO cita = new CitaDTO();
                        cita = lstDatos.get(0);
                        tvNombre.setText(cita.getNombres().toString());
                        tvApePaterno.setText(cita.getApellidoPaterno().toString());
                        tvApeMaterno.setText(cita.getApellidoMaterno().toString());
                        tvDoctor.setText(cita.getDoctor().toString());
                        tvEspecialidad.setText(cita.getNomEspecialidad().toString());
                        tvFecha.setText(cita.getFecha().toString());
                        tvHorario.setText(cita.getHora().toString());
                        tvConsultorio.setText(cita.getConsultorio().toString());

                    }else{
                        AlertUtils.showAlert(requireContext(), "Alerta...", "1. Problemas al conectarse " + citaDTO.getIdCita() , false);

                    }

                } else {

                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<CitaDTO>> call, Throwable t) {
                AlertUtils.showAlert(requireContext(), "Alerta...", "2. Problemas al conectarse " + citaDTO.getIdCita() , false);
            }
        });
    }
}