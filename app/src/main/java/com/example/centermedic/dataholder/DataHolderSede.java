package com.example.centermedic.dataholder;

import com.example.centermedic.clases.SedeDTO;

public class DataHolderSede {
    private static SedeDTO sedeDTO;

    public static SedeDTO getSedeDTO() {
        return sedeDTO;
    }

    public static void setSedeDTO(SedeDTO sedeDTO) {
        DataHolderSede.sedeDTO = sedeDTO;
    }
}
