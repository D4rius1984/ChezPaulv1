package com.chezpaul.model

import com.chezpaul.model.CategorieBoisson

data class Boisson(
    val nom: String,
    val quantite: Int,
    val categorie: CategorieBoisson
)
