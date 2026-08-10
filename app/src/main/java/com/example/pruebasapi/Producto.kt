package com.example.pruebasapi

data class Producto(
    val id_Producto: Int,
    val Nombre: String,
    val Precio: Double,
    val Stock: Int,
    val id_Categoria: Int,
    val Codigo: String,
    val Activo: Int
)