package com.example.appbiblioteca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE libros (_id integer primary key autoincrement," +
            " categoria text not null, titulo text not null, autor text not null, idioma text," +
            " fecha_lectura_ini int, fecha_lectura_fin int, prestado_a text, valoracion float," +
            " formato text, notas text);";

    public BDHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS libros");
        db.execSQL(sqlCreate);
    }
}
