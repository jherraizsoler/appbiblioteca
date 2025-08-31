package com.example.appbiblioteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ImportadorDatos {

    private static final String TAG = "ImportadorDatos";
    private static final String[] COLUMNAS = {"categoria", "titulo", "autor", "idioma", "fecha_lectura_ini", "fecha_lectura_fin", "prestado_a", "valoracion", "formato", "notas"};

    public static void importar(Context context, String rutaArchivo) {
        BDHelper dbHelper = new BDHelper(context, "BibliotecaBD.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (BufferedReader br = new BufferedReader(new FileReader(new File(rutaArchivo)))) {
            String linea;
            db.beginTransaction(); // Iniciar una transacción para optimizar la importación
            int registrosImportados = 0;

            // Leer la primera línea para los encabezados.
            if ((linea = br.readLine()) != null) {
                // Si la primera línea es un encabezado, la ignoramos.
                // Si la primera línea es un dato, la importamos.
                // Usar una verificación simple para determinar si es un encabezado, por ejemplo, si la primera columna es "categoria"
                if (!linea.toLowerCase().trim().startsWith(COLUMNAS[0])) {
                    importarLinea(db, linea);
                    registrosImportados++;
                }
            }

            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) { // Verificar si la línea no está vacía o solo contiene espacios
                    importarLinea(db, linea);
                    registrosImportados++;
                }
            }

            db.setTransactionSuccessful(); // Marcar la transacción como exitosa
            Log.d(TAG, "Datos importados exitosamente a la tabla 'libros'. Registros: " + registrosImportados);

        } catch (IOException e) {
            Log.e(TAG, "Error al leer el archivo: " + e.getMessage());
        } finally {
            if (db.inTransaction()) {
                db.endTransaction(); // Finalizar la transacción
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private static void importarLinea(SQLiteDatabase db, String linea) {
        String[] datos = linea.split(",", -1); // El -1 asegura que se incluyan las cadenas vacías
        ContentValues valores = new ContentValues();

        if (datos.length != COLUMNAS.length) {
            Log.w(TAG, "Saltando línea con formato incorrecto: " + linea);
            return;
        }

        for (int i = 0; i < COLUMNAS.length; i++) {
            valores.put(COLUMNAS[i], datos[i].trim());
        }
        db.insert("libros", null, valores);
    }
}
