package com.example.appbiblioteca;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MiContenedor> implements View.OnClickListener {

    ArrayList<DatosLibro> lista;
    private MainActivity mainActivity;
    Context contexto;
    View.OnClickListener escuchador;
    int posicionEdicion;;


    public Adaptador(Context contexto, ArrayList<DatosLibro> lista, View.OnClickListener escuchador, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.lista = lista;
        this.contexto = contexto;
        this.escuchador = escuchador;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class MiContenedor extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor;
        TextView tvFecha_lectura_Fin;
        ImageView imagenLibro, imagenNotas, imagenFinalizado, imagenPrestado, imagenFormato;
        RatingBar ratingBar;

        public MiContenedor(View itemview) {
            super(itemview);
            tvTitulo=   itemview.findViewById(R.id.tvTitulo);
            tvAutor=  itemview.findViewById(R.id.tvAutor);
            imagenLibro =  itemview.findViewById(R.id.imagenLibro);
            imagenNotas =  itemview.findViewById(R.id.imagenNotas);
            imagenFinalizado =  itemview.findViewById(R.id.imagenFinalizado);
            imagenPrestado =  itemview.findViewById(R.id.imagenPrestado);
            imagenFormato = itemview.findViewById(R.id.imagenFormato);
            ratingBar = itemview.findViewById(R.id.ratingBar);
            tvFecha_lectura_Fin = itemview.findViewById(R.id.tvFecha_Lectura_Fin);
        }
    }

    public Adaptador(ArrayList<DatosLibro> dataSet) {
        lista = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MiContenedor onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vItem = inflater.inflate(R.layout.item_layout, parent,false);
        vItem.setOnClickListener(this);
        return new MiContenedor(vItem);

    }

    @Override
    public void onClick(View view) {
        if(escuchador != null)
            escuchador.onClick(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MiContenedor holder, int position) {

        // Obtener la posición de forma dinámica
        DatosLibro dato = lista.get(position);


        // Configurar los datos en las vistas del ítem
        holder.tvTitulo.setText(dato.getTitulo());
        holder.tvAutor.setText(dato.getAutor());
        String[] listaIdiomas = {"Aleman", "Catalan", "Chino", "Ingles", "Español", "Frances", "Italiano", "Vasco", "Senegal"};
        Integer[] listaImagenesLibros = {
                R.mipmap.ic_libroale_foreground, R.mipmap.ic_librocat_foreground, R.mipmap.ic_librochi_foreground, R.mipmap.ic_libroeng_foreground,
                R.mipmap.ic_libroesp_foreground, R.mipmap.ic_librofra_foreground, R.mipmap.ic_libroita_foreground,
                R.mipmap.ic_librovas_foreground, R.mipmap.ic_librogal_foreground,
        };
        for (int i = 0; i < listaIdiomas.length; i++){
            if (dato.getIdioma().equals(listaIdiomas[i])) {
                // Seleccionar la imagen que deseas mostrar (por ejemplo, la imagen en la posición 0 del ArrayList)
                int imagenSeleccionada = listaImagenesLibros[i];  // Esto obtiene el identificador de la imagen
                holder.imagenLibro.setImageResource(imagenSeleccionada);
            }
        }

        holder.ratingBar.setRating(dato.getValoracion());
        int fechaLong = dato.fecha_lectura_fin;
        String fechaStr = formatearFecha(fechaLong);
        holder.tvFecha_lectura_Fin.setText(fechaStr);


        String[] listaFormatos = {"book","audio","ebook"};
        Integer[] listaImagenesFormatos = {
                R.mipmap.ic_libro_foreground, R.mipmap.ic_audio_foreground, R.mipmap.ic_ebook_foreground
        };
        for (int i = 0; i < listaFormatos.length; i++){
            if (dato.getFormato().equals(listaFormatos[i])) {
                // Seleccionar la imagen que deseas mostrar (por ejemplo, la imagen en la posición 0 del ArrayList)
                int imagenSeleccionada = listaImagenesFormatos[i];  // Esto obtiene el identificador de la imagen
                holder.imagenFormato.setImageResource(imagenSeleccionada);
            }
        }

        if(dato.getNotas().length() > 0){
            holder.imagenNotas.setImageResource(R.mipmap.ic_checked_foreground);
        }else{
            holder.imagenNotas.setImageResource(R.drawable.baseline_radio_button_unchecked_24);
        }

        // Obtener la instancia de Calendar con la fecha actual
        Calendar calendar = Calendar.getInstance();

        // Obtener el día, mes y año
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;  // Los meses en Calendar empiezan en 0
        int anio = calendar.get(Calendar.YEAR);

        // Fecha actual
        String fechaa = formatearFechaString(Integer.parseInt(dia+""+mes+""+""+ anio)).replace("/", "");

        Log.d("FECHA_a", "La fecha A es: " + fechaa);
        int fechaActual =  Integer.parseInt(fechaa);


        Log.d("FECHA_ACTUAL", "La fecha actual es: " + fechaActual);

        // Llamada a la función para verificar si el libro está prestado
        boolean estaPrestado = estaPrestado(dato.fecha_lectura_ini, dato.fecha_lectura_fin, fechaActual);

        if (estaPrestado) {
            holder.imagenPrestado.setImageResource(R.mipmap.ic_checked_foreground);
        } else {
            holder.imagenPrestado.setImageResource(R.drawable.baseline_radio_button_unchecked_24);
        }

        boolean estaFinalizado = estaFinalizado(dato.fecha_lectura_fin,fechaActual);


        if (estaFinalizado) {
            holder.imagenFinalizado.setImageResource(R.mipmap.ic_checked_foreground);
        } else {
            holder.imagenFinalizado.setImageResource(R.drawable.baseline_radio_button_unchecked_24);
        }

        // Configurar el clic largo para mostrar el PopupMenu

        holder.itemView.setOnLongClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return false;

            PopupMenu popupMenu = new PopupMenu(contexto, v);
            popupMenu.getMenuInflater().inflate(R.menu.menucontextual, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.item_eliminar) {

                    mainActivity.borrarLibroBD(lista.get(adapterPosition).getId()); // Llama al método en MainActivity
                    lista.remove(adapterPosition);
                    mainActivity.tv_NumeroRegistro.setText(lista.size()+"");
                    notifyItemRemoved(adapterPosition);
                    return true;
                }
                return false;
            });

            popupMenu.show();
            return true;
        });
    }

    public String formatearFecha(int fecha) {
        String fechaStr = String.valueOf(fecha);
        if (fechaStr.length() >= 6) { // Verifica que la longitud sea suficiente
            int anio = Integer.parseInt(fechaStr.substring(fechaStr.length() - 4)); // Obtiene los últimos 4 dígitos (año)
            int mes = Integer.parseInt(fechaStr.substring(fechaStr.length() - 6, fechaStr.length() - 4)); // Obtiene los 2 dígitos del mes
            int dia = Integer.parseInt(fechaStr.substring(0, fechaStr.length() - 6)); // Obtiene los dígitos del día
            return String.format("%02d/%02d/%04d", dia, mes, anio);
        } else {
            return "Fecha inválida"; // Maneja fechas inválidas
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lista.size();
    }

    public boolean estaPrestado(int fechaLecturaInicial, int fechaLecturaFinal, int fechaActual) {
        // Convertir enteros a Calendar
        Calendar inicio = convertirEnteroACalendar(fechaLecturaInicial);
        Calendar fin = convertirEnteroACalendar(fechaLecturaFinal);
        Calendar actual = convertirEnteroACalendar(fechaActual);

        // Verificar si la fecha actual está dentro del rango del préstamo
        return (actual.after(inicio) || actual.equals(inicio)) &&
                (actual.before(fin) || actual.equals(fin));
    }

    public boolean estaFinalizado(int fechaLecturaFinal, int fechaActual) {
        // Convertir enteros a Calendar
        Calendar fin = convertirEnteroACalendar(fechaLecturaFinal);
        Calendar actual = convertirEnteroACalendar(fechaActual);

        // Verificar si la fecha actual es posterior a la fecha de finalización
        return actual.after(fin);
    }

    private Calendar convertirEnteroACalendar(int fecha) {
        Calendar calendar = Calendar.getInstance();
        int anio = fecha % 10000;
        int mes = (fecha / 10000) % 100 - 1; // Restamos 1 porque Calendar usa meses de 0 a 11
        int dia = fecha / 1000000;
        calendar.set(anio, mes, dia);
        return calendar;
    }

    public static String formatearFechaString(int fecha) {
        String fechaStr = String.valueOf(fecha);

        if (fechaStr.length() == 8) { // Formato ddMMyyyy
            return fechaStr.substring(0, 2) + "/" + fechaStr.substring(2, 4) + "/" + fechaStr.substring(4, 8);
        } else if (fechaStr.length() == 7) { // Formato ddMMyy (suponiendo años en 2000+)
            if(Integer.parseInt(fechaStr.substring(1,3)) > 0 && Integer.parseInt(fechaStr.substring(1,3)) <= 12){
                return 0+ "" +fechaStr.substring(0, 1) + "/" + fechaStr.substring(1, 3) + "/" + fechaStr.substring(3, 7);
            }else{
                return fechaStr.substring(0, 2) + "/0" + fechaStr.substring(2, 3) + "/" + fechaStr.substring(3, 7);
            }

        } else if (fechaStr.length() == 6) { // Formato ddMMyy (suponiendo años en 2000+)
            return fechaStr.substring(0, 1) + "/" + fechaStr.substring(1, 2) + "/" + fechaStr.substring(2, 6);
        } else {
            return "Formato inválido";
        }
    }

}
