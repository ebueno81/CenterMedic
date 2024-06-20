package com.example.centermedic.services;

import com.example.centermedic.clases.CitaDTO;
import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.implementacion.ICitaService;
import com.example.centermedic.implementacion.IPacienteService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CitaService {
    @POST("cita/guardar")
    Call<ICitaService> crearRegistro(@Body CitaDTO miPaciente);
    @PUT("cita/editar")
    Call<ICitaService> editarRegistro(@Body CitaDTO miPaciente);
    @GET("cita/listar")
    Call<ResponseDTO<CitaDTO>> listarRegistro(@Query("codigoUsuario") int codigoUsuario);
    @GET("cita/obtener")
    Call<ResponseDTO<CitaDTO>> obtenerRegistro(@Query("codigoCita") int codigoCita);
}
