package com.example.centermedic.dataholder;

import com.example.centermedic.clases.EspecialidadDTO;
import com.example.centermedic.clases.PacienteDTO;

public class DataHolderEspecialidad {
    private static EspecialidadDTO especialidadDTO;

    public static EspecialidadDTO getEspecialidadDTO() {
        return especialidadDTO;
    }

    public static void setEspecialidadDTO(EspecialidadDTO especialidadDTO) {
        DataHolderEspecialidad.especialidadDTO = especialidadDTO;
    }
}