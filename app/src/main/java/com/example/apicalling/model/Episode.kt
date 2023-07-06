package com.example.apicalling.model

data class Episode(
    val name: String,
    val season: Int,
    val number: Int,
    val runtime: Int,
    val rating: Rating,
    val image: Image,
    val summary: String
)
