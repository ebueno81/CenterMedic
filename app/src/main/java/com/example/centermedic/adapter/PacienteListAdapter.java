package com.example.centermedic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.centermedic.clases.PacienteDTO;

import java.util.List;

public class PacienteListAdapter extends ArrayAdapter<PacienteDTO> {
    private final Context context;
    private final List<PacienteDTO> pacientes;


    public PacienteListAdapter(Context context, List<PacienteDTO> pacientes) {
        super(context, android.R.layout.simple_spinner_item, pacientes);
        this.context = context;
        this.pacientes = pacientes;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Para el desplegable
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view = super.getView(position, convertView, parent);
        TextView text = (TextView) view.findViewById(android.R.id.text1);
        PacienteDTO paciente = pacientes.get(position);
        // Muestra solo el campo 'nombres'
        text.setText(paciente.getNombres() + " " + paciente.getApellidoPaterno() + " " + paciente.getApellidoMaterno());
        return view;
    }
}
