package com.chezpaul.model

data class Commande(
    val numeroTable: String,
    val salle: String,
    val nombreCouverts: Int,
    val plat: Plat?,
    val boissons: List<Boisson>
)
