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
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )

                    //Llama a la función de construcción de la lista
                    ListaProductos(productos = productos)
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
                val nuevoProducto = CrearProducto(
                    nombre = "Pera",
                    precio = 7.0,
                    stock = 30,
                    categoria = 2,
                    codigo = "C-300",
                    activo = 1
                )
                val respuestaPost = RetrofitClient.api.crearProducto(nuevoProducto)
                if (respuestaPost.isSuccessful) {
                    Log.d(
                        "API_TEST",
                        "Producto creado: ${respuestaPost.body()}"
                    )
                } else {
                    Log.e(
                        "API_TEST",
                        "Error HTTP POST: ${respuestaPost.code()}"
                    )
                }
            } catch (e: Exception) { // Ocurrió un problema al realizar la petición, por ejemplo falta de conexión o servidor inaccesible.
                Log.e(
                    "API_TEST",
                    "Error de conexión: ${e.message}",
                    e
                )
            }
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PruebasApiTheme {
        Greeting("Android")
    }
}