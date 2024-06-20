package com.example.centermedic.clases;

import java.util.List;

public class ResponseDTO<T> {
    public Boolean status;
    public String msg;
    public List<T> value;
   // public String token;
}
