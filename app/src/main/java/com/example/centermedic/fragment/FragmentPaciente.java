package com.example.centermedic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.centermedic.R;
import com.example.centermedic.activity.Login;
import com.example.centermedic.activity.MainPage;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.api.MySingleton;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.implementacion.IPacienteService;
import com.example.centermedic.services.PacienteService;
import com.example.centermedic.utils.AlertUtils;
import com.example.centermedic.utils.DatePickerUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentPaciente extends Fragment {

    Button btnSave;
    EditText etName, etCorreo, etDni, etStartDate, etFono, etCelular, etApePaterno, etApeMaterno;

    PacienteDTO paciente;
    public FragmentPaciente(PacienteDTO pacienteDTO) {
        // Required empty public constructor
        this.paciente = pacienteDTO;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etStartDate = view.findViewById(R.id.etStartDate);
        DatePickerUtils.setEditTextCurrentDate(requireContext(), etStartDate);
        etCorreo = view.findViewById(R.id.etEmail);
        etName = view.findViewById(R.id.etName);
        etApePaterno = view.findViewById(R.id.etApePat);
        etApeMaterno = view.findViewById(R.id.etApeMat);
        etDni = view.findViewById(R.id.etDni1);
        etFono = view.findViewById(R.id.etTelefono1);
        etCelular = view.findViewById(R.id.etCelular1);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ((MainPage) requireActivity()).replaceFragmentList();
                try {
                    savePaciente();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if (paciente != null) {
            etName.setText(paciente.getNombres());
            etApePaterno.setText(paciente.getApellidoPaterno());
            etApeMaterno.setText(paciente.getApellidoMaterno());
            etDni.setText(paciente.getNroDocumento());
            etCelular.setText(paciente.getCelular());
            etFono.setText(paciente.getTelefono());
            etStartDate.setText(formatDateToString(paciente.getFechaNacimiento()));
            etCorreo.setText(paciente.getCorreo());

        }
    }

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String formatDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formatter.format(date);
    }
    private void savePaciente() throws ParseException {
        MySingleton singleton = MySingleton.getInstance();
        String dateString = etStartDate.getText().toString(); // String en formato dd/MM/yyyy
      //  Date fechaNacimiento = convertStringToDate(dateString);

   //     AlertUtils.showAlert(requireContext(), "Alerta...", "Fecha: " +  etStartDate.getText(), false);
        PacienteDTO paciente = new PacienteDTO();
        paciente.setIdPaciente(0);
        paciente.setNombres(etName.getText().toString());
        paciente.setApellidoPaterno(etApePaterno.getText().toString());
        paciente.setApellidoMaterno(etApeMaterno.getText().toString());
        paciente.setNroDocumento(etDni.getText().toString());
        paciente.setCorreo(etCorreo.getText().toString());
        paciente.setTelefono(etFono.getText().toString());
        paciente.setCelular(etCelular.getText().toString());
        paciente.setFechaNaci(dateString);
        paciente.setIdUsuario(singleton.getIdUsuario());
        paciente.setTipoDocumento(1);

        Retrofit myRetrofit = MyApi.getInstance();
        PacienteService myPacienteservice = myRetrofit.create(PacienteService.class);

        myPacienteservice.crearPaciente(paciente).enqueue(new Callback<IPacienteService>() {
            @Override
            public void onResponse(Call<IPacienteService> call, Response<IPacienteService> response) {
                if(response.isSuccessful()){
                    IPacienteService responseDTO = response.body();
                    PacienteDTO pacienteDTOS = responseDTO.value;

                    if(responseDTO.status){
                        ((MainPage) requireActivity()).replaceFragmentList();

                    }else{
                        AlertUtils.showAlert(requireContext(), "Alerta...", "1. Hubo un error de conexion ", false);
                        etApeMaterno.requestFocus();
                    }
                }else{
                    AlertUtils.showAlert(requireContext(), "Alerta...", "2. Hubo un respuesta de error ", false);
                }
            }

            @Override
            public void onFailure(Call<IPacienteService> call, Throwable t) {
                //Toast.makeText(view.c,"1. Existe un valor incorrecto...",Toast.LENGTH_LONG);
                AlertUtils.showAlert(requireContext(), "Alerta...", "3. Hubo un error de conexion " + t.getMessage(), false);
                etApeMaterno.requestFocus();
            }
        });
    }
}