package com.example.centermedic.utils;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
public class AlertUtils {
    public static void showAlert(Context context, String title, String message, boolean showCancelButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        // Botón de OK
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        // Condicional: Botón de Cancelar
        if (showCancelButton) {
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Nuevo método para mostrar un diálogo con Sí y No
    public static void showYesNoDialog(Context context, String title, String message, YesNoListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Sí", (dialog, which) -> {
            listener.onYes();
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            listener.onNo();
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Interfaz para manejar la respuesta Sí o No
    public interface YesNoListener {
        void onYes();
        void onNo();
    }
}
