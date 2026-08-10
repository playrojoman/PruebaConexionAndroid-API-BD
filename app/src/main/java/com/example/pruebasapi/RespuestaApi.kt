package com.example.pruebasapi

data class RespuestaApi <T> (
    val success: Boolean,
    val status: Int,
    val data: T? = null,
    val message: String? = null
)