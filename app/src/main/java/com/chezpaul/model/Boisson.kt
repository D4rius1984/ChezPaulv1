package com.chezpaul.model

data class Boisson(
    val nom: String,
    val quantite: Int = 1,
    val categorie: CategorieBoisson,
    val isActivated: Boolean = true  // Ajout de la propriété 'isActivated' avec valeur par défaut true
)
