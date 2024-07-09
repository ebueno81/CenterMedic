package com.example.centermedic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.centermedic.R;
import com.example.centermedic.activity.Login;
import com.example.centermedic.activity.MainPage;
import com.example.centermedic.activity.Menu;
import com.example.centermedic.activity.PacienteBuscar;
import com.example.centermedic.adapter.PacienteAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.datahelper.SearchDatabaseHelper;
import com.example.centermedic.services.OnItemClickListener;
import com.example.centermedic.services.PacienteService;
import com.example.centermedic.utils.AlertUtils;
import com.example.centermedic.utils.KeyboardUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentPacienteList extends Fragment implements OnItemClickListener {

    ImageButton ibNuevo, ibCancel;
    EditText etBuscar;
    RecyclerView recycler;
    private Handler handler = new Handler();
    private Runnable runnable;


  //  private RecyclerView.Adapter adapterPacienteList;
    private ProgressBar progressBar;

    public FragmentPacienteList() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paciente_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);

        ibCancel = view.findViewById(R.id.ibCancel);
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(requireActivity(), Menu.class);
                startActivity(i1);
            }
        });

        ibNuevo = view.findViewById(R.id.ibNuevo);
        ibNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage) requireActivity()).replaceFragmentNew();
            }
        });

        recycler = view.findViewById(R.id.recycler_id);



        cargarPaciente("");

        etBuscar = view.findViewById(R.id.etBuscar);
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

                // Crear un nuevo runnable para ejecutar después de 3 segundos
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        cargarPaciente(s.toString());
                        KeyboardUtils.esconderTeclado(requireActivity());
                    }
                };
                handler.postDelayed(runnable, 2000); // 2000 milisegundos = 3 segundos
            }
        });
    }

    @Override
    public void onItemClick(PacienteDTO item) {
        ((MainPage) requireActivity()).replaceFragmentEdit(item);
    }

    private void cargarPaciente(String nombrePaciente){
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        progressBar.setVisibility(View.VISIBLE);
        Retrofit myRetrofit = MyApi.getInstance();
        PacienteService myPacienteService = myRetrofit.create(PacienteService.class);
        myPacienteService.listarPaciente(0,nombrePaciente).enqueue(new Callback<ResponseDTO<PacienteDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<PacienteDTO>> call, Response<ResponseDTO<PacienteDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<PacienteDTO> lstPaciente = responseDTO.value;
                        PacienteAdapter adapterDatos = new PacienteAdapter(lstPaciente, FragmentPacienteList.this);
                        recycler.setAdapter(adapterDatos);
                        progressBar.setVisibility(View.GONE);
                    }else{
                      //  AlertUtils.showAlert(requireContext(), "Alerta...", "1. No se encontro ninguna coincidencia...", false);
                        recycler.setAdapter(null);
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    recycler.setAdapter(null);
                    progressBar.setVisibility(View.GONE);
                    //AlertUtils.showAlert(requireContext(), "Alerta...", "2. No hubo una conexion satisfactoria...", false);
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<PacienteDTO>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtils.showAlert(requireContext(), "Alerta...", "3. Hubo un error de conexion " + t.getMessage(), false);
            }
        });
    }
}