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

import android.util.Log;
import androidx.lifecycle.lifecycleScope;
import kotlinx.coroutines.launch;

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebasApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.api.obtenerProductos()
                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()
                    Log.d("API_TEST", "Respuesta: $datos")
                } else {
                    Log.e(
                        "API_TEST",
                        "Error HTTP: ${respuesta.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "API_TEST",
                    "Error de conexión: ${e.message}",
                    e
                )
            }
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