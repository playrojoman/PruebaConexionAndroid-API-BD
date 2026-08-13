package com.example.pruebasapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pruebasapi.ui.theme.PruebasApiTheme

import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    // Lista de productos. Inicialmente está vacía.
    // Al cambiar su valor, Compose detecta el cambio y recompone
    // los componentes que leen este estado.
    private var productos by mutableStateOf<List<Producto>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebasApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //Llama a la función de construcción de la lista
                    //ListaProductos(productos = productos)
                    FormularioProducto(
                        modifier = Modifier.padding(innerPadding),
                        onCrearProducto = { producto ->
                            crearProducto(producto)
                        }
                    )
                }
            }
        }

        // Inicia una corrutina asociada al ciclo de vida del Activity.
        // Permite realizar operaciones suspendidas, como las peticiones de red,
        // sin bloquear el hilo principal.
        lifecycleScope.launch {
            try {

                //Obtener productos mediante get
                // Ejecuta la petición GET definida en ApiService y espera la respuesta de la API.
                val respuesta = RetrofitClient.api.obtenerProductos() //Generar la petición
                if (respuesta.isSuccessful) {
                    // Obtiene el cuerpo de la respuesta HTTP convertido por Retrofit/Gson al tipo RespuestaApi<List<Producto>>.
                    val datos = respuesta.body()
                    Log.d("API_TEST", "Respuesta: $datos")

                    // Si la respuesta contiene "data", se asigna la lista de productos.
                    // Si "data" es null, se utiliza una lista vacía para evitar valores nulos.
                    productos = datos?.data ?: emptyList()

                } else { // La API respondió, pero con un código HTTP de error, (ej. 400, 404 o 500.)
                    Log.e(
                        "API_TEST",
                        "Error HTTP: ${respuesta.code()}"
                    )
                }

                //Crear productos mediante post
//                val nuevoProducto = CrearProducto(
//                    nombre = "Pera",
//                    precio = 7.0,
//                    stock = 30,
//                    categoria = 2,
//                    codigo = "C-300",
//                    activo = 1
//                )
//                val respuestaPost = RetrofitClient.api.crearProducto(nuevoProducto)
//                if (respuestaPost.isSuccessful) {
//                    Log.d(
//                        "API_TEST",
//                        "Producto creado: ${respuestaPost.body()}"
//                    )
//                } else {
//                    Log.e(
//                        "API_TEST",
//                        "Error HTTP POST: ${respuestaPost.code()}"
//                    )
//                }
            } catch (e: Exception) { // Ocurrió un problema al realizar la petición, por ejemplo falta de conexión o servidor inaccesible.
                Log.e(
                    "API_TEST",
                    "Error de conexión: ${e.message}",
                    e
                )
            }
        }
    }

    //Método encargado de usar Retrofit para crear la conexión con la API
    private fun crearProducto(producto: CrearProducto) {

        lifecycleScope.launch {

            try {

                val respuesta = RetrofitClient.api.crearProducto(producto)

                if (respuesta.isSuccessful) {

                    Log.d(
                        "API_TEST",
                        "Producto creado: ${respuesta.body()}"
                    )

                } else {

                    Log.e(
                        "API_TEST",
                        "Error HTTP POST: ${respuesta.code()}"
                    )
                }

            } catch (e: Exception) {

                Log.e(
                    "API_TEST",
                    "Error de conexión POST: ${e.message}",
                    e
                )
            }
        }
    }
}

//Fin de la clase Main Activity
// Inicio de componentes

@Composable
fun FormularioProducto(modifier: Modifier = Modifier, onCrearProducto: (CrearProducto) -> Unit) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(16.dp)
    ) {

        //Titulo
        Text(
            text = "Crear producto",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Nombre
        CampoProducto(
            valor = nombre,
            alCambiar = { nombre = it },
            etiqueta = "Nombre"
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Precio
        CampoProducto(
            valor = precio,
            alCambiar = { precio = it },
            etiqueta = "Precio"
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Stock
        CampoProducto(
            valor = stock,
            alCambiar = { stock = it },
            etiqueta = "Stock"
        )
        Spacer(modifier = Modifier.height(8.dp))

        //categoria
        CampoProducto(
            valor = categoria,
            alCambiar = { categoria = it },
            etiqueta = "Categoría"
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Codigo
        CampoProducto(
            valor = codigo,
            alCambiar = { codigo = it },
            etiqueta = "Código"
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Botón de acción de registro
        Button(
            onClick = {
                val precioNumero = precio.toDoubleOrNull()
                val stockNumero = stock.toIntOrNull()
                val categoriaNumero = categoria.toIntOrNull()

                //Verificación de los datos antes de ingresarlos
                if (nombre.isNotBlank() &&
                    precioNumero != null &&
                    stockNumero != null &&
                    categoriaNumero != null &&
                    codigo.isNotBlank()
                ) {

                    val nuevoProducto = CrearProducto(
                        nombre = nombre,
                        precio = precioNumero,
                        stock = stockNumero,
                        categoria = categoriaNumero,
                        codigo = codigo,
                        activo = 1
                    )

                    onCrearProducto(nuevoProducto)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear producto")
        }
    }
}

// Composable encargado de mostrar la lista de productos.
@Composable
fun ListaProductos(productos: List<Producto>) {
    // Mostrar los productos
    LazyColumn {
        items(productos) { producto ->
            Text(text = "${producto.Nombre} - $${producto.Precio}")

        }
    }
}

//Componente generico para mostrar cada uno de los campos del formulario
//Gracias a que todos usan la misma estructura
@Composable
fun CampoProducto(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String
) {

    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

//Preview
@Preview(showBackground = true)
@Composable
fun Preview() {
    PruebasApiTheme {
        FormularioProducto(
            onCrearProducto = { producto ->
            }
        )
    }
}