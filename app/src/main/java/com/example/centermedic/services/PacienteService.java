package com.example.centermedic.services;

import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.UserDTO;
import com.example.centermedic.implementacion.IPacienteService;
import com.example.centermedic.implementacion.IUserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PacienteService {
    @POST("paciente/guardar")
    Call<IPacienteService> crearPaciente(@Body PacienteDTO miPaciente);
    @PUT("paciente/editar")
    Call<IPacienteService> editarPaciente(@Body PacienteDTO miPaciente);
    @GET("paciente/listar")
    Call<ResponseDTO<PacienteDTO>> listarPaciente(@Query("idUsuario") int idUsuario, @Query("valor") String valor);
}
