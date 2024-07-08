package com.example.centermedic.services;

import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.SedeDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SedeService {

    @GET("sede/listar")
    Call<ResponseDTO<SedeDTO>> listarSede(@Query("codigo") Integer codigo);
    @GET("sede/obtener")
    Call<ResponseDTO<SedeDTO>> obtenerSede(@Query("codigo") Integer codigo);

}
