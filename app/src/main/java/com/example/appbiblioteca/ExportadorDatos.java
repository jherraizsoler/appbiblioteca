package com.example.appbiblioteca;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportadorDatos {

    private static final String TAG = "ExportadorDatos";

    public static void exportar(Context context, String rutaArchivo) {
        BDHelper dbHelper = new BDHelper(context, "BibliotecaBD.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM libros", null);
            FileWriter writer = new FileWriter(new File(rutaArchivo));

            // Escribir los nombres de las columnas
            writer.append("categoria,titulo,autor,idioma,fecha_lectura_ini,fecha_lectura_fin,prestado_a,valoracion,formato,notas\n");

            // Escribir los datos de las filas
            while (cursor.moveToNext()) {
                writer.append(cursor.getString(1)).append(","); // categoria
                writer.append(cursor.getString(2)).append(","); // titulo
                writer.append(cursor.getString(3)).append(","); // autor
                writer.append(cursor.getString(4)).append(","); // idioma
                writer.append(String.valueOf(cursor.getInt(5))).append(","); // fecha_lectura_ini
                writer.append(String.valueOf(cursor.getInt(6))).append(","); // fecha_lectura_fin
                writer.append(cursor.getString(7)).append(","); // prestado_a
                writer.append(String.valueOf(cursor.getFloat(8))).append(","); // valoracion
                writer.append(cursor.getString(9)).append(","); // formato
                writer.append(cursor.getString(10)).append("\n"); // notas
            }

            writer.flush();
            writer.close();
            cursor.close();
            Log.d(TAG, "Datos exportados exitosamente a " + rutaArchivo);

        } catch (IOException e) {
            Log.e(TAG, "Error al escribir el archivo: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}

