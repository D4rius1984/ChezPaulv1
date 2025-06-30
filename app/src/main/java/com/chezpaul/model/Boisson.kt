package com.chezpaul.model

data class Boisson(
    val nom: String,
    val quantite: Int = 1,
    val categorie: CategorieBoisson
)
