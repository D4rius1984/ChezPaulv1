package com.chezpaul.model

data class Commande(
    val numeroTable: String,
    val nombreCouverts: Int,
    val plats: List<Plat>,
    val boissons: List<Boisson>,
    val remarque: String? = null,
    val isGroupe: Boolean = false,
    val prixMenuGroupe: Double? = null,
    val menusEnfants: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val modifiedAt: Long? = null,
)
