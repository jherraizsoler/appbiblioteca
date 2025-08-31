package com.example.appbiblioteca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * Convierte una fecha en formato entero (ddMMyyyy) a un formato de cadena (dd/MM/yyyy).
     * @param fecha El entero de la fecha en formato ddMMyyyy.
     * @return La fecha formateada como String (dd/MM/yyyy) o "Formato inválido" si falla.
     */
    public static String formatearFechaEnteroAString(int fecha) {
        String fechaStr = String.valueOf(fecha);
        try {
            if (fechaStr.length() == 8) { // Formato ddMMyyyy
                return fechaStr.substring(0, 2) + "/" + fechaStr.substring(2, 4) + "/" + fechaStr.substring(4, 8);
            } else if (fechaStr.length() == 7) { // Formato dMMyyyy
                return "0" + fechaStr.substring(0, 1) + "/" + fechaStr.substring(1, 3) + "/" + fechaStr.substring(3, 7);
            } else if (fechaStr.length() == 6) { // Formato ddMMyy
                return fechaStr.substring(0, 2) + "/" + fechaStr.substring(2, 4) + "/20" + fechaStr.substring(4, 6);
            }
        } catch (Exception e) {
            // El catch ahora no hace nada para evitar la dependencia de Log
        }
        return "Fecha inválida";
    }

    /**
     * Convierte una fecha en formato de cadena (d/M/yyyy, dd/M/yyyy, d/MM/yyyy, dd/MM/yyyy) a un formato de cadena (dd/MM/yyyy).
     * @param fecha El string de la fecha.
     * @return La fecha formateada como String (dd/MM/yyyy) o "Fecha inválida" si falla.
     */
    public static String formatearFechaStringAString(String fecha) {
        SimpleDateFormat sdfEntrada = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        SimpleDateFormat sdfSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdfEntrada.parse(fecha);
            return sdfSalida.format(date);
        } catch (ParseException e) {
            return "Fecha inválida";
        }
    }

    /**
     * Convierte una fecha en formato de cadena (dd/MM/yyyy) a un entero (ddMMyyyy).
     * @param fecha La fecha como String (dd/MM/yyyy).
     * @return La fecha como entero (ddMMyyyy).
     */
    public static int formatearFechaStringAEntero(String fecha) {
        String fechaSinBarras = fecha.replace("/", "");
        try {
            return Integer.parseInt(fechaSinBarras);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Convierte un entero de fecha (ddMMyyyy) a un objeto Calendar.
     * @param fecha El entero de la fecha.
     * @return Un objeto Calendar.
     */
    public static Calendar convertirEnteroACalendar(int fecha) {
        Calendar calendar = Calendar.getInstance();
        try {
            int anio = fecha % 10000;
            int mes = (fecha / 10000) % 100 - 1; // Restamos 1 porque Calendar usa meses de 0 a 11
            int dia = fecha / 1000000;
            calendar.set(anio, mes, dia);
        } catch (Exception e) {
            // El catch ahora no hace nada para evitar la dependencia de Log
        }
        return calendar;
    }
}
