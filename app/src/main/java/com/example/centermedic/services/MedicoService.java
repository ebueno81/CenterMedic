package com.example.centermedic.services;

import com.example.centermedic.clases.MedicoDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MedicoService {
    @GET("medico/listar")
    Call<ResponseDTO<MedicoDTO>> listar(@Query("codigo") int codigo, @Query("valor") String valor);
}
