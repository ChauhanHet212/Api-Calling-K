package com.example.apicalling.model

import java.io.Serializable

data class Show(
    val id: Int,
    val name: String,
    val language: String,
    val genres: List<String>,
    val status: String,
    val runtime: Int,
    val premiered: String,
    val ended: String,
    val officialSite: String,
    val rating: Rating,
    val image: Image,
    val summary: String
) : Serializable
