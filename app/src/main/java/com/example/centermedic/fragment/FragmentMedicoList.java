package com.example.centermedic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centermedic.R;
import com.example.centermedic.activity.MainPage;
import com.example.centermedic.activity.Menu;
import com.example.centermedic.adapter.MedicoListarAdapter;
import com.example.centermedic.adapter.PacienteAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.MedicoDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.services.MedicoService;
import com.example.centermedic.services.PacienteService;
import com.example.centermedic.utils.KeyboardUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentMedicoList extends Fragment {
    ImageButton ibNuevo;
    EditText etBuscar;
    private Handler handler = new Handler();
    private Runnable runnable;
    RecyclerView recycler;
   // private RecyclerView.Adapter adapterList;

    public FragmentMedicoList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medico_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibNuevo = view.findViewById(R.id.ibNuevo);
        ibNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ((MainPage) requireActivity()).replaceFragmentNew();
                Intent i1 = new Intent(getContext(), Menu.class);

                startActivity(i1);
            }
        });

        recycler = view.findViewById(R.id.recycler_id);
        cargarMedico("");

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
                        cargarMedico(s.toString());
                        KeyboardUtils.esconderTeclado(requireActivity());
                    }
                };
                handler.postDelayed(runnable, 2000); // 2000 milisegundos = 3 segundos
            }
        });
    }

    private void cargarMedico(String valor){
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        Retrofit myRetrofit = MyApi.getInstance();
        MedicoService myService = myRetrofit.create(MedicoService.class);
        myService.listar(0, valor).enqueue(new Callback<ResponseDTO<MedicoDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<MedicoDTO>> call, Response<ResponseDTO<MedicoDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<MedicoDTO> lstDatos = responseDTO.value;
                        MedicoListarAdapter adapterDatos = new MedicoListarAdapter(lstDatos);
                        recycler.setAdapter(adapterDatos);
                    }else{
                        Toast.makeText(getContext(),"2. Usuario o clave incorrecta...",Toast.LENGTH_LONG);

                    }

                } else {

                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<MedicoDTO>> call, Throwable t) {
                Toast.makeText(getContext(),"1. Hubo un problema con la conexoion... Error: " + t.getCause(),Toast.LENGTH_LONG);
            }
        });

    }
}
