package com.example.centermedic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.centermedic.R;
import com.example.centermedic.adapter.PacienteListAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.CitaDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.services.PacienteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentCita extends Fragment {

    Spinner spPaciente;

    CitaDTO citaDTO;
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
        spPaciente = view.findViewById(R.id.spPaciente);

        Retrofit myRetrofit = MyApi.getInstance();
        PacienteService myPacienteService = myRetrofit.create(PacienteService.class);
        myPacienteService.listarPaciente(0,"").enqueue(new Callback<ResponseDTO<PacienteDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<PacienteDTO>> call, Response<ResponseDTO<PacienteDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<PacienteDTO> lstPaciente = responseDTO.value;
                        PacienteListAdapter adapter = new PacienteListAdapter(requireContext(), lstPaciente);
                        spPaciente.setAdapter(adapter);
                        spPaciente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Obtener el objeto seleccionado
                                PacienteDTO selectedPaciente = (PacienteDTO) parent.getItemAtPosition(position);
                                // Obtener el idPaciente
                                Integer idPaciente = selectedPaciente.getIdPaciente();
                                // Llamar a una función con el idPaciente
                                handlePacienteSelection(idPaciente);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Manejar el caso cuando no se selecciona nada, si es necesario
                            }
                        });
                    }else{
                        Toast.makeText(view.getContext(),"2. Usuario o clave incorrecta...",Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<PacienteDTO>> call, Throwable t) {
                Toast.makeText(view.getContext(),"2. Usuario o clave incorrecta...",Toast.LENGTH_LONG);
            }
        });
    }
    // Función para manejar la selección del paciente
    private void handlePacienteSelection(Integer idPaciente) {
        // Implementar la lógica que necesitas con el idPaciente
        Toast.makeText(requireContext(), "Paciente seleccionado ID: " + idPaciente, Toast.LENGTH_SHORT).show();
        // Aquí puedes llamar a otra función o realizar la acción deseada con el idPaciente
    }
}