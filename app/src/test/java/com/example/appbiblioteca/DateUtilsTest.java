package com.example.appbiblioteca;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.appbiblioteca.DateUtils;

public class DateUtilsTest {

    @Test
    public void formatearFechaEnteroAString_formatoValido8Digitos() {
        // Prueba para un formato válido de 8 dígitos (ddMMyyyy)
        String resultado = DateUtils.formatearFechaEnteroAString(25122024);
        assertEquals("25/12/2024", resultado);
    }

    @Test
    public void formatearFechaEnteroAString_formatoValido7Digitos() {
        // Prueba para un formato válido de 7 dígitos (dMMyyyy)
        String resultado = DateUtils.formatearFechaEnteroAString(5022023);
        assertEquals("05/02/2023", resultado);
    }

    @Test
    public void formatearFechaEnteroAString_formatoInvalido() {
        // Prueba para un formato de entrada inválido
        String resultado = DateUtils.formatearFechaEnteroAString(12345);
        assertEquals("Fecha inválida", resultado);
    }

    @Test
    public void formatearFechaStringAString_formatoCompleto() {
        // Prueba para un formato de entrada completo (dd/MM/yyyy)
        String resultado = DateUtils.formatearFechaStringAString("25/12/2024");
        assertEquals("25/12/2024", resultado);
    }

    @Test
    public void formatearFechaStringAString_formatoParcial() {
        // Prueba para un formato de entrada con un solo dígito en día y/o mes
        String resultado = DateUtils.formatearFechaStringAString("5/2/2023");
        assertEquals("05/02/2023", resultado);
    }

    @Test
    public void formatearFechaStringAString_formatoInvalido() {
        // Prueba para un formato de entrada inválido
        String resultado = DateUtils.formatearFechaStringAString("2024-12-25");
        assertEquals("Fecha inválida", resultado);
    }

    @Test
    public void formatearFechaStringAEntero_formatoValido() {
        // Prueba para un formato de entrada válido
        int resultado = DateUtils.formatearFechaStringAEntero("25/12/2024");
        assertEquals(25122024, resultado);
    }

    @Test
    public void formatearFechaStringAEntero_formatoInvalido() {
        // Prueba para un formato de entrada que no es un número
        int resultado = DateUtils.formatearFechaStringAEntero("abc/def/ghi");
        assertEquals(0, resultado);
    }
}
