package com.chezpaul.model

data class Commande(
    val numeroTable: String,
    val nombreCouverts: Int,
    val plats: List<Plat>,
    val boissons: List<Boisson>,
    val remarque: String? = null,
    val isGroupe: Boolean = false
)