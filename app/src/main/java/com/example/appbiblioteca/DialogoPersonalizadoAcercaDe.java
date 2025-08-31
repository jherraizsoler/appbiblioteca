package com.example.appbiblioteca;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DialogoPersonalizadoAcercaDe extends DialogFragment  {


    // Variables temporales para guardar los datos iniciales


    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflador = getActivity().getLayoutInflater();
        View vistaVentana = inflador.inflate(R.layout.dialogo_personalizadoacercade, null);

        builder.setView(vistaVentana); // <-- IMPORTANTE: Establecer la vista aquí
        builder.setPositiveButton("Volver", (dialogInterface, i) -> {

        });
        return builder.create();
    }
}
