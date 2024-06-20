package com.example.centermedic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.centermedic.R;
import com.example.centermedic.activity.Cita;
import com.example.centermedic.activity.MainPage;
import com.example.centermedic.activity.Menu;
import com.example.centermedic.adapter.CitaListAdapter;
import com.example.centermedic.adapter.PacienteAdapter;
import com.example.centermedic.api.MyApi;
import com.example.centermedic.clases.CitaDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.services.OnItemClickListener;
import com.example.centermedic.services.OnItemClickListenerCita;
import com.example.centermedic.services.CitaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentCitaList extends Fragment implements OnItemClickListenerCita {

    ImageButton ibNuevo;
    RecyclerView recycler;
   // private RecyclerView.Adapter adapterCitaList;
    public FragmentCitaList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cita_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ibNuevo = view.findViewById(R.id.ibNuevo);
        ibNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(requireActivity(), Cita.class);
                startActivity(i1);
                //((MainPage) requireActivity()).citasRegister(null);
            }
        });

        recycler = view.findViewById(R.id.recycler_id);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));

        Retrofit myRetrofit = MyApi.getInstance();
        CitaService myCitaService = myRetrofit.create(CitaService.class);
        myCitaService.listarRegistro(0).enqueue(new Callback<ResponseDTO<CitaDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<CitaDTO>> call, Response<ResponseDTO<CitaDTO>> response) {
                if (response.isSuccessful()) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.status){
                        List<CitaDTO> lstDatos = responseDTO.value;
                        CitaListAdapter adapterDatos = new CitaListAdapter(lstDatos, FragmentCitaList.this);
                        recycler.setAdapter(adapterDatos);
                    }else{
                        Toast.makeText(view.getContext(),"2. Usuario o clave incorrecta...",Toast.LENGTH_LONG);

                    }

                } else {

                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<CitaDTO>> call, Throwable t) {
                Toast.makeText(view.getContext(),"1. Hubo un problema con la conexoion... Error: " + t.getCause(),Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onItemClick(CitaDTO item) {
        ((MainPage) requireActivity()).citasRegister(item);
    }
}