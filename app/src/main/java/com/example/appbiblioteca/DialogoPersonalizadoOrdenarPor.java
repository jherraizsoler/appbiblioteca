package com.example.appbiblioteca;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.List;

public class DialogoPersonalizadoOrdenarPor extends DialogFragment  {


    // Variables temporales para guardar los datos iniciales


    Spinner spinnerOrdenarPor;

    private InterfazOrdenar listener;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflador = getActivity().getLayoutInflater();
        View vistaVentana = inflador.inflate(R.layout.dialogo_personalizadoordenarpor, null);


        spinnerOrdenarPor = vistaVentana.findViewById(R.id.spinnerOrdenarPor);

        // Poner valores al spinner Formato
        List<String> listaItemsSpinnerOrdenarPor = Arrays.asList("Normal","Titulo","Autor","Valoraciones","FechaFin"
        );
        ArrayAdapter<String> adapterSpinnerOrdenarPor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listaItemsSpinnerOrdenarPor);
        adapterSpinnerOrdenarPor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdenarPor.setAdapter(adapterSpinnerOrdenarPor);

        builder.setView(vistaVentana); // <-- IMPORTANTE: Establecer la vista aquí
        builder.setPositiveButton("Ordenar por", (dialogInterface, i) -> {
            String ordenarPor = spinnerOrdenarPor.getSelectedItem().toString();

            if (listener != null) {
                listener.sentenciaOrdenar(ordenarPor);
            }
        });

        return builder.create();
    }

    // Método setter para asignar el listener
    public void setDialogoOrdenarPorListener(InterfazOrdenar listener) {
        this.listener = listener;
    }

    // Interfaz para comunicar datos a la actividad principal
    public interface InterfazOrdenar {
        void sentenciaOrdenar( String comando);
    }
}
