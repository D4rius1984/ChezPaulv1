package com.chezpaul.model

enum class CategorieBoisson {
    APEROS, VINS, DIGESTIFS, BIERES, SOFTS
}

data class Boisson(
    val nom: String,
    val categorie: CategorieBoisson,
    val sousCategorie: String? = null,
    val format: String? = null
)
