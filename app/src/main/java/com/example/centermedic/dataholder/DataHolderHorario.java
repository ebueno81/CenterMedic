package com.example.centermedic.dataholder;

import com.example.centermedic.clases.HorarioDTO;

public class DataHolderHorario {
    private static HorarioDTO HorarioDTO;

    public static HorarioDTO getHorarioDTO() {
        return HorarioDTO;
    }

    public static void setHorarioDTO(HorarioDTO HorarioDTO) {
        DataHolderHorario.HorarioDTO = HorarioDTO;
    }
}