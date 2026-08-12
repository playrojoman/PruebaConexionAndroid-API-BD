package com.example.pruebasapi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("producto/productos.php")
    suspend fun obtenerProductos(): Response<RespuestaApi<List<Producto>>>

    @POST("producto/crear.php")
    suspend fun crearProducto(@Body producto: CrearProducto): Response<RespuestaApi<ProductoCreado>>

}