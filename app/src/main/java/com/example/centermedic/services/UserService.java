package com.example.centermedic.services;

import com.example.centermedic.implementacion.IUserService;
import com.example.centermedic.clases.ResponseDTO;
import com.example.centermedic.clases.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("usuario/login")
    Call<ResponseDTO<UserDTO>> validarLogin(@Body UserDTO miUser);
    @POST("usuario/guardar")
    Call<IUserService> saveLogin(@Body UserDTO miUser);
}
