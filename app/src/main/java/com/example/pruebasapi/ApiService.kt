package com.example.pruebasapi

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("producto/obtener.php")
    suspend fun obtenerProductos(): Response<RespuestaApi<List<Producto>>>

}