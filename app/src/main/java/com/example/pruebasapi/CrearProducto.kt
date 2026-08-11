package com.example.pruebasapi

data class CrearProducto(
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: Int,
    val codigo: String,
    val activo: Int
)
