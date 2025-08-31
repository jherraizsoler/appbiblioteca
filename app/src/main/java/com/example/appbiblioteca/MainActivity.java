package com.example.appbiblioteca;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


/**Revisar esta url Pagina Official developer.android.com*/
// https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#java
// Este proyecto se ha realizado con la información del profesor de Moviles 2 DAM España Zaragoza 2024-2025
// Y se han insertado el cambio de segun la edad si es mayor de edad o menor tiene una imagen u otra
public class MainActivity extends AppCompatActivity  implements View.OnClickListener, DialogoPersonalizado.DatosL{
    RecyclerView vistaRecycler;
    ArrayList<DatosLibro> lista = new ArrayList<DatosLibro>();
    Adaptador adaptador;
    TextView tv_NumeroRegistro, tv_Orden;
    Button btnOrdenar, btnImportar, btnExportar, btnAltaLibro, btnAcercaDe;
    ActivityResultLauncher<Intent> lanzadorAlta;
    private Toolbar toolbar2;
    // Ejemplo de uso en una actividad o fragmento
    String rutaArchivo = "/storage/emulated/0/Download/libros_a_importar.csv"; // Ejemplo de ruta

    int posicionEdicion;
    // En MainActivity
    private int posicionSeleccionada;

    BDHelper usdbh;
    SQLiteDatabase dbw;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_NumeroRegistro = findViewById(R.id.tv_NumeroRegistro);
        tv_Orden = findViewById(R.id.tv_Orden);

        tv_NumeroRegistro.setText("");
        tv_Orden.setText("Normal");



        // Base de datos SQLITE
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new BDHelper(this, "BibliotecaBD.db", null, 1);

        dbw = usdbh.getWritableDatabase();

        // Toolbar menu
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        lanzadorAlta = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult resultado) {

                if(resultado.getResultCode() == Activity.RESULT_OK){

                    Intent data = resultado.getData();

                    if (data == null) {
                        Toast.makeText(MainActivity.this, "Error: No se recibieron datos de AltaActivity", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String categoria = resultado.getData().getStringExtra("categoria");
                    String titulo = resultado.getData().getStringExtra("titulo");
                    String autor = resultado.getData().getStringExtra("autor");
                    String idioma = resultado.getData().getStringExtra("idioma");
                    int fecha_lectura_ini = resultado.getData().getIntExtra("fecha_lectura_ini",0);
                    int fecha_lectura_fin   = resultado.getData().getIntExtra("fecha_lectura_fin",0);
                    String prestado_a = resultado.getData().getStringExtra("prestado_a");
                    Float valoracion = resultado.getData().getFloatExtra("valoracion",0);
                    String formato = resultado.getData().getStringExtra("formato");
                    String notas = resultado.getData().getStringExtra("notas");




                    if( InsertarLibroBD(categoria,titulo,autor,idioma,fecha_lectura_ini,fecha_lectura_fin,prestado_a,valoracion,formato,notas) != false){
                        Toast.makeText(MainActivity.this, "Insertado en BD", Toast.LENGTH_SHORT).show();
                        int id = consultarIDlibro(categoria,titulo,autor,idioma,fecha_lectura_ini,fecha_lectura_fin,prestado_a,valoracion,formato,notas);
                        if (id > 0){
                            lista.add(new DatosLibro(id,categoria,titulo,autor,idioma,fecha_lectura_ini,fecha_lectura_fin,prestado_a,valoracion,formato,notas));
                            if (lista.size() == 0){
                                adaptador.notifyItemInserted(lista.size());
                                tv_NumeroRegistro.setText(lista.size()+"");
                            }else{
                                adaptador.notifyItemInserted(lista.size() -1);
                                tv_NumeroRegistro.setText(lista.size()+"");
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Error: No se ha encontrado en id del Libro ", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Error al insertraLibro en BD", Toast.LENGTH_SHORT).show();
                    };



                }
            }
        });


        toolbar2 = findViewById(R.id.custom_toolbar);
        btnOrdenar = toolbar2.findViewById(R.id.btn_OrdenarPor);
        btnImportar = toolbar2.findViewById(R.id.btn_ImportarDatos);
        btnExportar = toolbar2.findViewById(R.id.btn_ExportarDatos);
        btnAltaLibro = toolbar2.findViewById(R.id.btn_AltaLibro);
        btnAcercaDe = toolbar2.findViewById(R.id.btn_AcercaDe);

        btnOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón Ordenar
                DialogoPersonalizadoOrdenarPor ordenar = new DialogoPersonalizadoOrdenarPor();
                ordenar.show(getSupportFragmentManager(),"OrdenarPor");
                ordenar.setDialogoOrdenarPorListener(new DialogoPersonalizadoOrdenarPor.InterfazOrdenar() {
                    @Override
                    public void sentenciaOrdenar(String comando) {
                        // Abrimos la base de datos en modo lectura/escritura
                        dbw = usdbh.getWritableDatabase();

                        // Definimos los campos que queremos obtener en el SELECT
                        String[] campos = new String[]{
                                "_id", "categoria", "titulo", "autor", "idioma",
                                "fecha_lectura_ini", "fecha_lectura_fin", "prestado_a",
                                "valoracion", "formato", "notas"
                        };

                        // Definir la columna para el ORDER BY según el comando recibido
                        String orden = "_id ASC"; // Orden por defecto (Normal)
                        if (comando.equals("Titulo")) {
                            orden = "titulo ASC";
                        } else if (comando.equals("Autor")) {
                            orden = "autor ASC";
                        } else if (comando.equals("Valoraciones")) {
                            orden = "valoracion DESC"; // Ordenado de mayor a menor valoración
                        }else if(comando.equals("FechaFin")){
                            orden = "fecha_lectura_fin ASC"; // Ordenado por fecha
                        }

                        // Realizamos la consulta ordenada
                        Cursor c2 = dbw.query("libros", campos, null, null, null, null, orden);

                        // Validamos si hay resultados
                        if (c2 == null || c2.getCount() == 0) {
                            Toast.makeText(MainActivity.this, "No hay ningún libro registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            lista.clear(); // Limpiar la lista antes de agregar nuevos elementos
                            while (c2.moveToNext()) {
                                DatosLibro libro = new DatosLibro(
                                        c2.getInt(0),
                                        c2.getString(1),
                                        c2.getString(2),
                                        c2.getString(3),
                                        c2.getString(4),
                                        c2.getInt(5),
                                        c2.getInt(6),
                                        c2.getString(7),
                                        c2.getFloat(8),
                                        c2.getString(9),
                                        c2.getString(10)
                                );
                                lista.add(libro);
                            }
                            adaptador.notifyDataSetChanged(); // Notificar cambios al adaptador
                            tv_NumeroRegistro.setText(lista.size() + "");
                            tv_Orden.setText(orden);
                        }
                    }
                });
                if (toolbar2.getVisibility() == View.VISIBLE) {
                    toolbar2.setVisibility(View.GONE);
                }

            }
        });


        btnAltaLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón Alta Libro
                Intent intent = new Intent(MainActivity.this, AltaActivity.class);
                lanzadorAlta.launch(intent);
                if (toolbar2.getVisibility() == View.VISIBLE) {
                    toolbar2.setVisibility(View.GONE);
                }

            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón Acerca De
                DialogoPersonalizadoAcercaDe AcercaDe = new DialogoPersonalizadoAcercaDe();
                AcercaDe.show(getSupportFragmentManager(),"Dialogo Acerca de...");
                AcercaDe.setCancelable(true);
                if (toolbar2.getVisibility() == View.VISIBLE) {
                    toolbar2.setVisibility(View.GONE);
                }

            }
        });

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarDatosTabla();
                ImportadorDatos.importar(MainActivity.this, rutaArchivo);
                refrescarTabla();
                if (toolbar2.getVisibility() == View.VISIBLE) {
                    toolbar2.setVisibility(View.GONE);
                }
            }
        });

        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExportadorDatos.exportar(MainActivity.this, rutaArchivo);
                if (toolbar2.getVisibility() == View.VISIBLE) {
                    toolbar2.setVisibility(View.GONE);
                }
            }
        });


        vistaRecycler = findViewById(R.id.recyclerView);
        adaptador = new Adaptador(this, lista, this,MainActivity.this);
        vistaRecycler.setLayoutManager(new LinearLayoutManager(this));
        vistaRecycler.setAdapter(adaptador);

        registerForContextMenu(vistaRecycler);
        //instanciarLista();
        refrescarTabla();

    }

    public int consultarIDlibro(String categoria, String titulo, String autor, String idioma,
                            int fecha_lectura_ini, int fecha_lectura_fin,
                            String prestado_a, Float valoracion, String formato, String notas) {
    int id = -1;

    // Abrimos la base de datos en modo lectura
    SQLiteDatabase db = usdbh.getReadableDatabase();

    // Consulta segura con parámetros
    String sqlConsultar = "SELECT _id FROM libros WHERE categoria = ? AND titulo = ? AND autor = ? " +
            "AND idioma = ? AND fecha_lectura_ini = ? AND fecha_lectura_fin = ? " +
            "AND prestado_a = ? AND valoracion = ? AND formato = ? AND notas = ?";

    // Parámetros en un array (convertimos fechas y valoraciones a String si es necesario)
    String[] parametros = {
            categoria, titulo, autor, idioma,
            String.valueOf(fecha_lectura_ini), String.valueOf(fecha_lectura_fin),
            prestado_a, String.valueOf(valoracion), formato, notas
    };

    Cursor c = db.rawQuery(sqlConsultar, parametros);

    // Si el cursor tiene datos, obtener el ID
    if (c.moveToFirst()) {
        id = c.getInt(0);
    }

    // Cerramos el cursor y la base de datos
    c.close();
    db.close();

    return id;
}

    public boolean InsertarLibroBD(String categoria, String titulo, String autor, String idioma, int fecha_lectura_ini,
                                int fecha_lectura_fin, String prestado_a, Float valoracion, String formato, String notas){

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        dbw = usdbh.getWritableDatabase();

        if(categoria.strip().length() < 2 || categoria.strip().length() > 50){
            Toast.makeText(this, "ERROR: El tipo no se puede dejar vacio o necesita de minimo 5 caracteres hasta 30 inclusive.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(titulo.strip().length() < 5 || titulo.strip().length() > 100){
                Toast.makeText(this, "ERROR: El titulo no se puede dejar vacio o necesita de minimo 10 caracter hasta 50 inclusive.", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                if(autor.strip().length() < 3 || autor.strip().length() > 25){
                    Toast.makeText(this, "ERROR: El autor no se puede dejar vacio o necesita de minimo 3 caracteres hasta 20 inclusive.", Toast.LENGTH_SHORT).show();
                    return false;
                }else{
                    if(idioma.strip().length() < 2 || idioma.strip().length() > 20){
                        Toast.makeText(this, "ERROR: El idioma no se puede dejar vacio, necesita de minimo 5 caracteres hasta 15 inclusive.", Toast.LENGTH_SHORT).show();
                        return false;
                    }else{
                        if(String.valueOf(fecha_lectura_ini).length() < 7  || String.valueOf(fecha_lectura_ini).length() > 8){
                            Toast.makeText(this, "ERROR: La fecha de lectura inicial  no se puede dejar vacia, necesita de minimo y maximo de 8 caracteres.", Toast.LENGTH_SHORT).show();
                            return false;
                        }else{
                            if(String.valueOf(fecha_lectura_fin).length() < 7  || String.valueOf(fecha_lectura_fin).length() > 8){
                                Toast.makeText(this, "ERROR: La fecha de lectura final  no se puede dejar vacia, necesita de minimo y maximo de 8 caracteres.", Toast.LENGTH_SHORT).show();
                                return false;
                            }else{
                                if(prestado_a.strip().length() < 0 || prestado_a.strip().length() > 25){
                                    Toast.makeText(this, "ERROR: El prestado_a no se puede dejar vacio, necesita de minimo 3 caracteres hasta 25 inclusive.", Toast.LENGTH_SHORT).show();
                                    return false;
                                }else{
                                    if(valoracion < 0 || valoracion > 10){
                                        Toast.makeText(this, "ERROR: La valoración(Estrellas) no puede ser menor de 0 o mayor de 10", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }else{
                                        if(formato.strip().length() < 2 || formato.strip().length() > 20){
                                            Toast.makeText(this, "ERROR: El formato no se puede dejar vacio o necesita de minimo 5 caracteres hasta 20 inclusive.", Toast.LENGTH_SHORT).show();
                                            return false;
                                        }else{
                                            if(notas.strip().length() > 50){
                                                Toast.makeText(this, "ERROR: Las notas no se pueden dejar vacias o necesita de minimo 5 caracteres hasta 50 inclusive.", Toast.LENGTH_SHORT).show();
                                                return false;
                                            }else{
                                                try {
                                                    //String sqlInsertar = "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) VALUES('" + categoria + "','" + titulo + "','" + autor + "','" + idioma + "'," + fecha_lectura_ini + "," + fecha_lectura_fin + ",'" + prestado_a + "'," + valoracion + ",'" + formato + "','" + notas + "')";
                                                    //String sqlInsertar = "INSERT INTO libros (categoria,titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) VALUES('" +  categoria +"','" + titulo + "','" + autor + "','" + idioma + "', fecha_lectura_ini , fecha_lectura_fin ,'" + prestado_a + "', valoracion, '" + formato + "', '" + notas + "')";
                                                    //dbw.execSQL(sqlInsertar);

                                                    ContentValues cv = new ContentValues();
                                                    cv.put("categoria", categoria);
                                                    cv.put("titulo",titulo);
                                                    cv.put("autor",autor);
                                                    cv.put("idioma",idioma);
                                                    cv.put("fecha_lectura_ini",fecha_lectura_ini);
                                                    cv.put("fecha_lectura_fin",fecha_lectura_fin);
                                                    cv.put("prestado_a",prestado_a);
                                                    cv.put("valoracion",valoracion);
                                                    Toast.makeText(this, "ValoracionMainActivityAlta: "+ valoracion, Toast.LENGTH_SHORT).show();
                                                    cv.put("formato",formato);
                                                    cv.put("notas",notas);

                                                    long resultado = dbw.insert("libros", null, cv);

                                                    if (resultado == -1) {
                                                        Log.e("DB_ERROR", "Error al insertar libro en la base de datos");
                                                        return false;
                                                    }else{
                                                        Toast.makeText(this, "Se ha insertado correctamente el libro \n" + " con titulo: " + titulo  + " y autor " + autor, Toast.LENGTH_SHORT).show();
                                                        return true;
                                                    }
                                                }catch (SQLException e){
                                                    Toast.makeText(this, "Error SQL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    Log.println(Log.DEBUG,"Insertar SQLlite",e.getMessage());
                                                }catch (Exception ex) {
                                                        Log.e("DB_EXCEPTION", "Excepción al insertar en BD: " + ex.getMessage());
                                                        return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                // Dispositivo sin botón de menú físico (overflow menu)
                getMenuInflater().inflate(R.menu.main_menu, menu);
                MenuItem opcionOpciones = findViewById(R.id.itemOpciones);
                MenuItem opcionSalir = findViewById(R.id.Salir);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.Salir) {
            // Lógica para Salir
            finishAffinity();
            System.exit(0);
            return true;
        }else  if(id == R.id.itemOpciones){
            if (toolbar2.getVisibility() == View.GONE) {
                toolbar2.setVisibility(View.VISIBLE);
            }else if(toolbar2.getVisibility() == View.VISIBLE){
                toolbar2.setVisibility(View.GONE);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    // Menu__contextual ------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflador = getMenuInflater();

        if (v.getId() == android.R.id.content) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        } else {
            if (v.getId() == R.id.recyclerView) {
                //posicionEdicion = vistaRecycler.getChildAdapterPosition(v);
                inflador.inflate(R.menu.menucontextual, menu);
            } else if (v.getId() == R.id.main) {
                inflador.inflate(R.menu.main_menu, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_eliminar) {
            return true;
        }
        return super.onContextItemSelected(item);


    }

    @Override
    public void onClick(View view) {

        // Primero obtén la posición del item seleccionado
        posicionEdicion = vistaRecycler.getChildAdapterPosition(view);

        // Verifica que la posición sea válida antes de continuar
        if (posicionEdicion == RecyclerView.NO_POSITION) {
            return; // Evita errores si no se seleccionó correctamente
        }

        DialogoPersonalizado dialog1 = new DialogoPersonalizado();
        DatosLibro datos = lista.get(posicionEdicion);
        dialog1.setDatos(datos.getId(), datos.getCategoria(), datos.getTitulo(),datos.getAutor(),datos.getIdioma(),datos.getFecha_lectura_ini(),datos.getFecha_lectura_fin(), datos.getPrestado_a(),datos.getValoracion(),datos.getFormato(),datos.getNotas());
        dialog1.setDialogoPersonalizadoListener(new DialogoPersonalizado.DatosL() {
            @Override
            public void datosLibro(int id, String categoria, String titulo, String autor, String idioma, int fecha_lectura_ini,
                                   int fecha_lectura_fin, String prestado_a, Float valoracion, String formato, String notas) {
                DatosLibro nuevoDato = new DatosLibro(id, categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato,  notas);
                actualizarLibro(nuevoDato);
                lista.set(posicionEdicion, nuevoDato);
                adaptador.notifyItemChanged(posicionEdicion);
            }
        });
        dialog1.show(getSupportFragmentManager(), "dialogo_modificar2");
        dialog1.setCancelable(false);

        posicionEdicion = vistaRecycler.getChildAdapterPosition(view);
    }

    @Override
    public void datosLibro(int id, String categoria, String titulo, String autor, String idioma, int fecha_lectura_ini,
                           int fecha_lectura_fin, String prestado_a, Float valoracion, String formato, String notas) {
        if (posicionEdicion != RecyclerView.NO_POSITION && posicionEdicion < lista.size()) {
            DatosLibro nuevoDato = new DatosLibro(id, categoria,titulo,autor,idioma,fecha_lectura_ini,fecha_lectura_fin,prestado_a,valoracion,formato,notas);
            actualizarLibro(nuevoDato);
            lista.set(posicionEdicion, nuevoDato);
            adaptador.notifyItemChanged(posicionEdicion);
        }
    }

    public void actualizarLibro(DatosLibro libro) {
        dbw = usdbh.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("categoria", libro.getCategoria());
        valores.put("titulo", libro.getTitulo());
        valores.put("autor", libro.getAutor());
        valores.put("idioma", libro.getIdioma());
        valores.put("fecha_lectura_ini", libro.getFecha_lectura_ini());
        valores.put("fecha_lectura_fin", libro.getFecha_lectura_fin());
        valores.put("prestado_a", libro.getPrestado_a());
        valores.put("valoracion", libro.getValoracion());
        valores.put("formato", libro.getFormato());
        valores.put("notas", libro.getNotas());

        int filasAfectadas = dbw.update("libros", valores, "_id = ?", new String[]{String.valueOf(libro.getId())});

        if (filasAfectadas > 0) {
            Toast.makeText(this, "Libro actualizado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar el libro", Toast.LENGTH_SHORT).show();
        }
    }

    public void borrarLibroBD(int id) {
        dbw = usdbh.getWritableDatabase();

        // Condición para eliminar
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int filasEliminadas = dbw.delete("libros", selection, selectionArgs);

        // Si filasEliminadas es mayor que 0, se ha eliminado correctamente
        if (filasEliminadas > 0) {
            Toast.makeText(this, "Libro eliminado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
        }
    }

    public void refrescarTabla(){
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        dbw = usdbh.getWritableDatabase();

        /** Parte tvResultado Mostrar info de todos los usuario Select*/

        String []campos = new String[] {"_id","categoria","titulo","autor","idioma","fecha_lectura_ini","fecha_lectura_fin","prestado_a","valoracion","formato","notas"};
        // Metodo Consultar

        //String sqlConsultar2 = "Select  * from Usuarios";
        //Cursor c2 = dbw.rawQuery(sqlConsultar2, null);

        Cursor c2 = dbw.query("libros",campos,null,null,null,null,null);


        if (c2.getCount() == 0 || c2 == null ){
            Toast.makeText(this, "No hay ningun Usuario registrado", Toast.LENGTH_SHORT).show();
        }else{
            lista.clear(); // Limpiar la lista antes de agregar nuevos elementos
            while (c2.moveToNext()){
                DatosLibro libro = new DatosLibro(
                        c2.getInt(0),
                        c2.getString(1),
                        c2.getString(2),
                        c2.getString(3),
                        c2.getString(4),
                        c2.getInt(5),
                        c2.getInt(6),
                        c2.getString(7),
                        c2.getFloat(8),
                        c2.getString(9),
                        c2.getString(10)
                );

                lista.add(libro);
                if (lista != null && !lista.isEmpty()) {
                    int position = lista.size() - 1;
                    adaptador.notifyItemInserted(position);
                }else{
                    adaptador.notifyItemInserted(0);
                }

            }
            tv_NumeroRegistro.setText(lista.size()+"");
        }


    }

    public void borrarDatosTabla(){
        dbw = usdbh.getWritableDatabase();

        // Ejecutar la instrucción SQL para borrar todos los registros
        dbw.execSQL("DELETE FROM libros");

        Toast.makeText(this, "Todos los libros han sido eliminados", Toast.LENGTH_SHORT).show();
    }

}

