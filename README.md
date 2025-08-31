# 📚 App Biblioteca

Una aplicación de Android para gestionar y organizar tu biblioteca personal de libros.  
Registra tus lecturas, lleva un control de los libros que prestas y guarda tus valoraciones y notas.

---

## ✨ Características Principales

- **Registro de Libros**: Añade nuevos libros con detalles como título, autor, género, idioma y formato.  
- **Gestión de Lectura**: Registra las fechas de inicio y finalización de tus lecturas.  
- **Valoración y Notas**: Califica tus libros y añade notas personales.  
- **Gestión de Préstamos**: Mantén un registro de los libros que has prestado y a quién.  

---

## 📂 Estructura del Proyecto

El proyecto está estructurado de la siguiente manera:

app/
└── src/
├── main/java/ # Código fuente de la aplicación en Java
├── main/res/ # Recursos: layouts XML, imágenes y strings
└── test/java/ # Tests unitarios

yaml
Copiar código

---

## 🧪 Test Unitarios

Para garantizar la calidad y fiabilidad del código, hemos implementado tests unitarios que verifican el comportamiento de las funciones clave del proyecto.  

La clase **DateUtils**, que se encarga de formatear fechas, es un ejemplo perfecto de una clase que debe ser probada rigurosamente.

### ▶️ ¿Cómo ejecutar los tests?

1. Abre la terminal en la raíz de tu proyecto.  
2. Ejecuta el siguiente comando para correr los tests de la clase `DateUtilsTest`:

```bash
./gradlew testDebugUnitTest --tests "com.example.appbiblioteca.DateUtilsTest"
```
Este comando ejecutará los tests de forma aislada y te dará un resumen en la consola.

### 📊 ¿Cómo ver el informe de resultados?

Se genera un informe HTML en la siguiente ruta:

app/build/reports/tests/testDebugUnitTest/index.html

yaml
Copiar código

Abre este archivo en tu navegador para ver un informe detallado (tests pasados ✅ en verde y fallidos ❌ en rojo).

---

📜 Licencia
Todos los derechos y permisos los tiene su autor @jherraizsoler Jorge Herraiz Soler  
Este proyecto solo puede ser modificado o distribuido por su autor, se puede testear pero no distribuir o comercializar sin autorización del autor.
