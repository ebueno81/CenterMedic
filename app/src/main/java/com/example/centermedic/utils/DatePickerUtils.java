package com.example.centermedic.utils;

// DatePickerUtils.java
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerUtils {
    public static String getCurrentDate() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static void setEditTextCurrentDate(Context context, EditText editText) {
        // Establecer la fecha actual en el EditText
        editText.setText(getCurrentDate());

        // Configurar el clic para mostrar el DatePickerDialog
        editText.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(context, editText);
            }
        });
    }
    public static void showDatePickerDialog(Context context, final EditText editText) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear y mostrar el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Ajustar el mes (de 0 a 11) a un formato de 1 a 12
                        int displayMonth = monthOfYear + 1;
                        // Mostrar la fecha seleccionada en el EditText
                        String day1 = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        String month1 = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);
                        editText.setText(day1 + "/" + month1 + "/" + year);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}

