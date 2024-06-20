package com.example.centermedic.services;

import com.example.centermedic.clases.EspecialidadDTO;
import com.example.centermedic.clases.HorarioDTO;
import com.example.centermedic.clases.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HorarioService {
    @GET("horario/listarrango")
    Call<ResponseDTO<HorarioDTO>> listar(@Query("fechaInicio") String fechaInicio, @Query("fechaFinal") String fechaFinal, @Query("idEspecialidad") int idEspecialidad);
}
