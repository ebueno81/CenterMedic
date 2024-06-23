package com.example.centermedic.services;

import com.example.centermedic.clases.PacienteDTO;
import com.example.centermedic.implementacion.IUserService;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("usuario/login")
    Call<ResponseDTO<UserDTO>> validarLogin(@Body UserDTO miUser);
    @POST("usuario/guardar")
    Call<IUserService> saveLogin(@Body UserDTO miUser);
    @GET("usuario/correo")
    Call<ResponseDTO> envioCorreo(@Query("idUsuario") int idUsuario);
}
