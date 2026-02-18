package com.chezpaul.model

data class Plat(
    val nom: String,
    val quantite: Int = 1,
    val contientRavigote: Boolean = false,
)
