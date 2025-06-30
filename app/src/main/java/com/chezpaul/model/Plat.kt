package com.chezpaul.model

data class Plat(
    val nom: String,
    val quantite: Int = 1,
    val contientRavigote: Boolean = false,
    val isActivated: Boolean = true  // Ajout de la propriété 'isActivated' avec valeur par défaut true
)
