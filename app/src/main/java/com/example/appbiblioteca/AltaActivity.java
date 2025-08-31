package com.example.appbiblioteca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbiblioteca.DateUtils;

import java.util.Arrays;
import java.util.List;

public class AltaActivity extends AppCompatActivity implements  DialogoPersonalizadoFechaIni.FechaIni, DialogoPersonalizadoFechaFin.FechaFin{

    private EditText etPrestado_a, etNotas, etTitulo, etAutor;
    private Spinner spinnerTipo,spinnerIdioma,spinnerFormato;
    private TextView tvFecha_lectura_ini, tvFecha_lectura_fin;
    private RatingBar ratingBar;
    private Button btnAlta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);

        etPrestado_a= findViewById(R.id.et_Prestado_A);
        etNotas = findViewById(R.id.et_Notas2);
        etTitulo = findViewById(R.id.etTitulo2);
        etAutor = findViewById(R.id.etAutor2);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerIdioma = findViewById(R.id.spinnerIdioma);
        spinnerFormato = findViewById(R.id.spinnerFormato);
        tvFecha_lectura_ini = findViewById(R.id.tvFecha_lectura_ini);
        tvFecha_lectura_fin = findViewById(R.id.tvFecha_lectura_fin);

        tvFecha_lectura_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoPersonalizadoFechaIni fechaIni = new DialogoPersonalizadoFechaIni();
                fechaIni.show(getSupportFragmentManager(),"Dialogo_Fecha_Ini");
            }
        });
        tvFecha_lectura_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoPersonalizadoFechaFin fechaFin = new DialogoPersonalizadoFechaFin();
                fechaFin.show(getSupportFragmentManager(),"Dialogo_Fecha_Fin");
            }
        });

        ratingBar = findViewById(R.id.ratingBar2);

        // Establecer los valores iniciales si existen
        // Poner valores al spinner Tipo
        List<String> listaItemsSpinnerTipo = Arrays.asList("Ficción", "No Ficción", "Ciencia Ficción y Fantasía", "Terror y Suspenso", "Romance",
                "Autoayuda y Desarrollo Personal", "Negocios y Finanzas", "Educación y Aprendizaje", "Cómics y Novelas Gráficas", "Infantil y Juvenil"
        );
        ArrayAdapter<String> adapterSpinnerTipo = new ArrayAdapter<>(AltaActivity.this, android.R.layout.simple_spinner_item, listaItemsSpinnerTipo);
        adapterSpinnerTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterSpinnerTipo);


        // Poner valores al spinner Idioma
        List<String> listaItemsSpinnerIdioma = Arrays.asList("Aleman","Catalan","Chino","Ingles","Español","Frances","Italiano","Vasco","Senegal"
        );
        ArrayAdapter<String> adapterSpinnerIdioma = new ArrayAdapter<>(AltaActivity.this, android.R.layout.simple_spinner_item, listaItemsSpinnerIdioma);
        adapterSpinnerIdioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapterSpinnerIdioma);




        // Poner valores al spinner Formato
        List<String> listaItemsSpinnerFormato= Arrays.asList("book","audio","ebook"
        );
        ArrayAdapter<String> adapterSpinnerFormato = new ArrayAdapter<>(AltaActivity.this, android.R.layout.simple_spinner_item, listaItemsSpinnerFormato);
        adapterSpinnerFormato.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormato.setAdapter(adapterSpinnerFormato);


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


        btnAlta = findViewById(R.id.btnAlta);

        btnAlta.setOnClickListener(v -> {
            try {
                String categoria =  spinnerTipo.getSelectedItem().toString();
                String titulo = etTitulo.getText().toString();
                String autor = etAutor.getText().toString();
                String idioma = spinnerIdioma.getSelectedItem().toString();

                String fechaIniString = tvFecha_lectura_ini.getText().toString().trim();
                String fechaFinString = tvFecha_lectura_fin.getText().toString().trim();

                if (fechaIniString.isEmpty() || fechaFinString.isEmpty()) {
                    Toast.makeText(this, "Error: Debes seleccionar ambas fechas", Toast.LENGTH_SHORT).show();
                    return;
                }

                int fecha_lectura_ini = DateUtils.formatearFechaStringAEntero(fechaIniString);
                int fecha_lectura_fin = DateUtils.formatearFechaStringAEntero(fechaFinString);

                String prestado_a = etPrestado_a.getText().toString();
                Float valoracion = ratingBar.getRating();
                Toast.makeText(this, "ValoracionAlta: " + valoracion,Toast.LENGTH_SHORT).show();
                String formato = spinnerFormato.getSelectedItem().toString();
                String notas = etNotas.getText().toString();


                // Devuelve los datos a la actividad principal
                Intent resultado = new Intent();

                resultado.putExtra("categoria", categoria);
                resultado.putExtra("titulo",titulo);
                resultado.putExtra("autor",autor);
                resultado.putExtra("idioma",idioma);
                resultado.putExtra("fecha_lectura_ini",fecha_lectura_ini);
                resultado.putExtra("fecha_lectura_fin",fecha_lectura_fin);
                resultado.putExtra("prestado_a",prestado_a);
                resultado.putExtra("valoracion",valoracion);
                resultado.putExtra("formato",formato);
                resultado.putExtra("notas",notas);

                setResult(Activity.RESULT_OK, resultado);
                finish(); // Finaliza la actividad
            }catch (NumberFormatException e){
                Toast.makeText(this, "Error Number Format Exeption en AltaActivity", Toast.LENGTH_SHORT).show();
            }
        });
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
}
