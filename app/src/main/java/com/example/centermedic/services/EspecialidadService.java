package com.example.centermedic.services;

import com.example.centermedic.clases.EspecialidadDTO;
import com.example.centermedic.clases.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EspecialidadService {
    @GET("especialidad/listar")
    Call<ResponseDTO<EspecialidadDTO>> listar(@Query("codigo") int codigo, @Query("valor") String valor);
}
