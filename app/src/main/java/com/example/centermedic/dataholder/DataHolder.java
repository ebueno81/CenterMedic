package com.example.centermedic.dataholder;

import com.example.centermedic.clases.PacienteDTO;

public class DataHolder {
    private static PacienteDTO pacienteDTO;

    public static PacienteDTO getPacienteDTO() {
        return pacienteDTO;
    }

    public static void setPacienteDTO(PacienteDTO dto) {
        pacienteDTO = dto;
    }
}