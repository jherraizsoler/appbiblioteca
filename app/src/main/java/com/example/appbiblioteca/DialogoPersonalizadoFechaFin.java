package com.example.appbiblioteca;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class DialogoPersonalizadoFechaFin extends DialogFragment {
    CalendarView cal;

    //Importante revisarlo
    FechaFin fechaFinDate;
    String fechaStringFin = "Fecha Fin no seleccionada";
    @Override
    //Envia los datos a la principal
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogoPersonalizadoFechaFin.FechaFin) {
            fechaFinDate = (DialogoPersonalizadoFechaFin.FechaFin) parentFragment;
        } else if (getActivity() instanceof DialogoPersonalizadoFechaFin.FechaFin) {
            fechaFinDate = (DialogoPersonalizadoFechaFin.FechaFin) getActivity();
        } else {
            throw new RuntimeException("Parent fragment or activity must implement FechaFin");
        }
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflador = getActivity().getLayoutInflater();
        View vistaVentana = inflador.inflate(R.layout.dialogo_personalizadofechafin, null);
        //dialogo_personalizadofechafin
        cal = vistaVentana.findViewById(R.id.calendarView);


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                fechaStringFin = String.format("%02d/%02d/%04d", day, month + 1, year);
                Toast.makeText(getActivity(),fechaStringFin,Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("Selecciona una fecha, selecciona un dia y haz click: ");

        builder.setView(vistaVentana);



        builder.setNegativeButton("Mostrar fecha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fechaFinDate.fechaFin(fechaStringFin);
            }
        });
        return  builder.create();
    }

    //Interfaz Idioma
    public interface FechaFin
    {
        public  void fechaFin(String fecha);

    }
}
