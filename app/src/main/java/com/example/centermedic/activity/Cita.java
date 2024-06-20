package com.example.centermedic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.centermedic.R;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.api.MySingleton;
import com.example.centermedic.clases.CitaDTO;
import com.example.centermedic.clases.EspecialidadDTO;
import com.example.centermedic.clases.HorarioDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.UserDTO;
import com.example.centermedic.dataholder.DataHolder;
import com.example.centermedic.dataholder.DataHolderEspecialidad;
import com.example.centermedic.dataholder.DataHolderHorario;
import com.example.centermedic.implementacion.ICitaService;
import com.example.centermedic.implementacion.IUserService;
import com.example.centermedic.services.CitaService;
import com.example.centermedic.services.UserService;
import com.example.centermedic.utils.AlertUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Cita extends AppCompatActivity {
    HorarioDTO _horario = new HorarioDTO();
    PacienteDTO _paciente = new PacienteDTO();
    private int idEspecialidad=0;
    LinearLayout llPaciente, llHorario, llEspecial;
    TextView tvPaciente3,tvEspecialidad3, tvHorario3, tvDoctor2, tvConsultorio2;
    AppCompatButton btnSave;
    // Define un ActivityResultLauncher
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cita);

        // Llamar a la función para mostrar la alerta con "OK" y "Cancelar"
       // AlertUtils.showAlert(this, "Alerta personalizada", "Este es un mensaje de alerta con OK y Cancelar.", true);

        // Registra el ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Verifica si hay datos de retorno
                            Intent data = result.getData();
                            if (data != null) {
                                // Recuperar el string adicional
                                String additionalParam = data.getStringExtra("name");
                                if (additionalParam != null) {
                                    if (additionalParam.equals("pacienteBuscar")){
                                        _paciente = DataHolder.getPacienteDTO();
                                        tvPaciente3.setText(_paciente.getNombres() + " " + _paciente.getApellidoPaterno() + " " + _paciente.getApellidoMaterno());
                                    }
                                    if (additionalParam.equals("especialidadBuscar")){
                                        EspecialidadDTO resultado = DataHolderEspecialidad.getEspecialidadDTO();
                                        tvEspecialidad3.setText(resultado.getNombre());
                                        tvHorario3.setText("");
                                        tvDoctor2.setText("");
                                        tvConsultorio2.setText("");
                                        idEspecialidad=resultado.getIdEspecialidad();
                                    }
                                    if (additionalParam.equals("horarioBuscar")){
                                        _horario = DataHolderHorario.getHorarioDTO();
                                        tvHorario3.setText(_horario.getFecha() + " " + _horario.getHora());
                                        tvDoctor2.setText(_horario.getDoctor());
                                        tvConsultorio2.setText(_horario.getConsultorio());
                                    }
                                }
                            }
                        }
                    }
                });

        llPaciente = findViewById(R.id.llPaciente);
        llEspecial = findViewById(R.id.llEspecial11);
        llHorario = findViewById(R.id.llHorario11);
        tvPaciente3 = findViewById(R.id.tvPaciente3);
        tvEspecialidad3 = findViewById(R.id.tvEspecialidad3);
        tvHorario3 = findViewById(R.id.tvHorario3);
        tvDoctor2 = findViewById(R.id.tvDoctor2);
        tvConsultorio2 = findViewById(R.id.tvConsultorio2);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarDatos()){
                    AlertUtils.showYesNoDialog(Cita.this, "Confirmación", "¿Estás seguro de que deseas continuar?", new AlertUtils.YesNoListener() {
                        @Override
                        public void onYes() {
                            saveRecords();
                            AlertUtils.showAlert(Cita.this,"Cita","Registro se grabo correctamente",false);
                        }

                        @Override
                        public void onNo() {
                            // Lógica para la opción No
                            // Por ejemplo, mostrar otro mensaje o cancelar una acción
                           // AlertUtils.showAlert(Cita.this, "Elegiste No", "Has cancelado la acción.", false);
                        }
                    });
                }
            }
        });

        llPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cita.this, PacienteBuscar.class);
                activityResultLauncher.launch(intent);
            }
        });

        llEspecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cita.this, EspecialidadBuscar.class);
                activityResultLauncher.launch(intent);
            }
        });

        llHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idEspecialidad==0){
                    AlertUtils.showAlert(Cita.this, "Alerta...", "Falta selecionar la especialidad...", false);
                }else{
                    Intent intent = new Intent(Cita.this, HorarioBuscar.class);
                    intent.putExtra("idEspecialidad", idEspecialidad);
                    intent.putExtra("Especialidad", tvEspecialidad3.getText());

                    activityResultLauncher.launch(intent);
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validarDatos(){
        if(idEspecialidad>0){
            if(_horario.getIdHorario() >0){
                if(_paciente.getIdPaciente() != null){
                    return true;
                }else{
                    AlertUtils.showAlert(this, "Alerta...", "Falta selecionar paciente...", false);
                    return false;
                }
            }else{
                AlertUtils.showAlert(this, "Alerta...", "Falta selecionar horario...", false);
                return false;
            }
        }else{
            AlertUtils.showAlert(this, "Alerta...", "Falta selecionar especialidad...", false);
            return false;
        }
    }
    private void saveRecords(){

        CitaDTO citaDTO = new CitaDTO();
        citaDTO.setIdCita(0);
        citaDTO.setEstadoCita(0);
        citaDTO.setIdHorario(_horario.getIdHorario());
        citaDTO.setIdPaciente(_paciente.getIdPaciente());
        citaDTO.setIdUsuario(1);

        Retrofit myRetrofit = MyApi.getInstance();
        CitaService myCitaService = myRetrofit.create(CitaService.class);

        myCitaService.crearRegistro(citaDTO).enqueue(new Callback<ICitaService>() {
            @Override
            public void onResponse(Call<ICitaService> call, Response<ICitaService> response) {
                if(response.isSuccessful()){
                    ICitaService responseDTO = response.body();
                    if(responseDTO.status){
                        Intent i1 = new Intent(getApplicationContext(), Menu.class);
                        startActivity(i1);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"1. Hubo un error al momento de grabar...",Toast.LENGTH_LONG);

                    }
                }
            }

            @Override
            public void onFailure(Call<ICitaService> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"1. Existe un valor incorrecto...",Toast.LENGTH_LONG);
            }
        });

    }
}