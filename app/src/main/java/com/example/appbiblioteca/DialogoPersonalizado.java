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

import com.example.appbiblioteca.DateUtils;

import java.util.Arrays;
import java.util.List;

public class DialogoPersonalizado extends DialogFragment implements   DialogoPersonalizadoFechaIni.FechaIni, DialogoPersonalizadoFechaFin.FechaFin  {
    private EditText etPrestado_a, etNotas, etTitulo, etAutor;
    private Spinner spinnerTipo,spinnerIdioma,spinnerFormato;
    private TextView tvFecha_lectura_ini, tvFecha_lectura_fin;
    private RatingBar ratingBar;
    private DatosL listener;

    // Variables temporales para guardar los datos iniciales
    private int id_inicial;
    private String categoria_inicial;
    private String titulo_inicial;
    private String autor_inicial;
    private String idioma_inicial;
    private int fecha_lectura_ini_inicial;
    private int fecha_lectura_fin_inicial;
    private String prestado_a_inicial;
    private Float valoracion_inicial;
    private String formato_inicial;
    private String notas_inicial;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflador = getActivity().getLayoutInflater();
        View vistaVentana = inflador.inflate(R.layout.dialogo_personalizado, null);

        etPrestado_a= vistaVentana.findViewById(R.id.et_Prestado_A);
        etNotas = vistaVentana.findViewById(R.id.et_Notas);
        etTitulo = vistaVentana.findViewById(R.id.etTitulo);
        etAutor = vistaVentana.findViewById(R.id.etAutor);
        spinnerTipo = vistaVentana.findViewById(R.id.spinnerTipo);
        spinnerIdioma = vistaVentana.findViewById(R.id.spinnerIdioma);
        spinnerFormato = vistaVentana.findViewById(R.id.spinnerFormato);
        tvFecha_lectura_ini = vistaVentana.findViewById(R.id.tvFecha_lectura_ini);
        tvFecha_lectura_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DialogoPersonalizadoFechaIni fechaIni = new DialogoPersonalizadoFechaIni();
                    fechaIni.show(getChildFragmentManager(), "Dialogo_Fecha_Ini");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al abrir el selector de fecha Ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvFecha_lectura_fin = vistaVentana.findViewById(R.id.tvFecha_lectura_fin);
        tvFecha_lectura_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DialogoPersonalizadoFechaFin fechaFin = new DialogoPersonalizadoFechaFin();
                    fechaFin.show(getChildFragmentManager(),"Dialogo_Fecha_Fin");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al abrir el selector de fecha Fin", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ratingBar = vistaVentana.findViewById(R.id.ratingBar2);

        // Establecer los valores iniciales si existen
        // Poner valores al spinner Tipo
        List<String> listaItemsSpinnerTipo = Arrays.asList("Ficción", "No Ficción", "Ciencia Ficción y Fantasía", "Terror y Suspenso", "Romance",
                "Autoayuda y Desarrollo Personal", "Negocios y Finanzas", "Educación y Aprendizaje", "Cómics y Novelas Gráficas", "Infantil y Juvenil"
        );
        ArrayAdapter<String> adapterSpinnerTipo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listaItemsSpinnerTipo);
        adapterSpinnerTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterSpinnerTipo);

        // Obtener la posición del ítem en el adaptador
        int posicionSpinnerTipo = adapterSpinnerTipo.getPosition(categoria_inicial);
        // Establecer la selección en el Spinner
        if (posicionSpinnerTipo >= 0) { // Verifica que el ítem exista en el adaptador
            spinnerTipo.setSelection(posicionSpinnerTipo);
        } else {
            Toast.makeText(getActivity(), "No se encontrado ningun elemento que este en la Categoria", Toast.LENGTH_SHORT).show();
        }

        // Poner valores al spinner Idioma
        List<String> listaItemsSpinnerIdioma = Arrays.asList("Aleman", "Catalan", "Chino", "Ingles", "Español", "Frances", "Italiano", "Vasco", "Senegal"
        );
        ArrayAdapter<String> adapterSpinnerIdioma = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listaItemsSpinnerIdioma);
        adapterSpinnerIdioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapterSpinnerIdioma);

        // Obtener la posición del ítem en el adaptador
        int posicionSpinnerIdioma = adapterSpinnerIdioma.getPosition(idioma_inicial);
        // Establecer la selección en el Spinner
        if (posicionSpinnerIdioma >= 0) { // Verifica que el ítem exista en el adaptador
            spinnerIdioma.setSelection(posicionSpinnerIdioma);
        } else {
            Toast.makeText(getActivity(), "No se encontrado ningun elemento  que coincida con los idiomas", Toast.LENGTH_SHORT).show();
        }


        // Poner valores al spinner Formato
        List<String> listaItemsSpinnerFormato= Arrays.asList("book","audio","ebook"
        );
        ArrayAdapter<String> adapterSpinnerFormato = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listaItemsSpinnerFormato);
        adapterSpinnerFormato.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormato.setAdapter(adapterSpinnerFormato);

        // Obtener la posición del ítem en el adaptador
        int posicionSpinnerFormato = adapterSpinnerFormato.getPosition(formato_inicial);
        // Establecer la selección en el Spinner
        if (posicionSpinnerFormato >= 0) { // Verifica que el ítem exista en el adaptador
            spinnerFormato.setSelection(posicionSpinnerFormato);
        } else {
            Toast.makeText(getActivity(), "No se encontrado ningun elemento  que coincida con los formatos", Toast.LENGTH_SHORT).show();
        }

        // Escuchar cambios en el rating
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Asegurarse de que el rating está en incrementos de 0.5
                float roundedRating = Math.round(rating * 2) / 2.0f;
                if (rating != roundedRating) {
                    ratingBar.setRating(roundedRating);
                    return;
                }
            }
        });


        /***************************************************************************************************************************************/
        etPrestado_a.setText(prestado_a_inicial);
        etNotas.setText(notas_inicial);
        etTitulo.setText(titulo_inicial);
        etAutor.setText(autor_inicial);
        //String fechaIni = fecha_lectura_ini_inicial+"".toString().substring(0,1)+ "/" + fecha_lectura_ini_inicial+"".toString().substring(2,3) + "/" + fecha_lectura_ini_inicial+"".toString().substring(4,7);

        try {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            tvFecha_lectura_ini.setText(DateUtils.formatearFechaEnteroAString(fecha_lectura_ini_inicial));
            tvFecha_lectura_fin.setText(DateUtils.formatearFechaEnteroAString(fecha_lectura_fin_inicial));
        }catch (NumberFormatException e){
            Toast.makeText(getActivity(), "Error fechas Update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //String fechaFin = fecha_lectura_fin_inicial+"".toString().substring(0,1)+ "/" + fecha_lectura_fin_inicial+"".toString().substring(2,3) + "/" + fecha_lectura_fin_inicial+"".toString().substring(4,7);

        Toast.makeText(getActivity(), "ValoracionDialogo: " + valoracion_inicial, Toast.LENGTH_SHORT).show();
        ratingBar.setRating(valoracion_inicial);


        builder.setTitle("Modificar datos Libro");
        builder.setView(vistaVentana);

        builder.setPositiveButton("Guardar", (dialogInterface, i) -> {

            try{
                String categoria =  spinnerTipo.getSelectedItem().toString();
                String titulo = etTitulo.getText().toString();
                String autor = etAutor.getText().toString();
                String idioma = spinnerIdioma.getSelectedItem().toString();
                String fechaIniString = tvFecha_lectura_ini.getText().toString().trim();
                String fechaFinString = tvFecha_lectura_fin.getText().toString().trim();

                int fecha_lectura_ini = DateUtils.formatearFechaStringAEntero(fechaIniString);
                int fecha_lectura_fin = DateUtils.formatearFechaStringAEntero(fechaFinString);

                if (fechaIniString.isEmpty() || fechaFinString.isEmpty()) {
                    Toast.makeText(getActivity(), "Error: Debes seleccionar ambas fechas", Toast.LENGTH_SHORT).show();
                    return;
                }
                String prestado_a = etPrestado_a.getText().toString();
                Float valoracion = ratingBar.getRating();
                Toast.makeText(getActivity(),""+ valoracion,Toast.LENGTH_SHORT).show();
                String formato = spinnerFormato.getSelectedItem().toString();
                String notas = etNotas.getText().toString();

                if (listener != null) {
                    listener.datosLibro(id_inicial, categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas);
                }

            }catch (NumberFormatException e){
                Toast.makeText(getActivity(), "Error Fechas", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) ->
                Toast.makeText(getActivity(), "Cancelar", Toast.LENGTH_SHORT).show());

        return builder.create();
    }

    // Método modificado para guardar los datos temporalmente
    public void setDatos( int id, String categoria, String titulo, String autor, String idioma, int fecha_lectura_ini,
                          int fecha_lectura_fin, String prestado_a, Float valoracion, String formato, String notas) {
        this.id_inicial = id;
        this.categoria_inicial = categoria;
        this.titulo_inicial = titulo;
        this.autor_inicial = autor;
        this.idioma_inicial = idioma;
        this.fecha_lectura_ini_inicial =  fecha_lectura_ini;
        this.fecha_lectura_fin_inicial = fecha_lectura_fin;
        this.prestado_a_inicial = prestado_a;
        this.valoracion_inicial = valoracion;
        this.formato_inicial = formato;
        this.notas_inicial = notas;
    }

    // Método setter para asignar el listener
    public void setDialogoPersonalizadoListener(DatosL listener) {
        this.listener = listener;
    }

    // Los métodos de la interfaz no necesitan ser refactorizados.
    @Override
    public void fechaIni(String fechaIni) {
        tvFecha_lectura_ini.setText(fechaIni);
    }

    @Override
    public void fechaFin(String fechaFin) {
        tvFecha_lectura_fin.setText(fechaFin);
    }

    // Interfaz para comunicar datos a la actividad principal
    public interface DatosL {
        void datosLibro( int id, String categoria, String titulo, String autor, String idioma, int fecha_lectura_ini,
                         int fecha_lectura_fin, String prestado_a, Float valoracion, String formato, String notas);
    }
}
