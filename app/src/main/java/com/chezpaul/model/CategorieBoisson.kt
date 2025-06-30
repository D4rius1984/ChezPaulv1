package com.chezpaul.model

enum class CategorieBoisson {
    APEROS,
    DIGESTIFS,
    BIERES,
    SOFTS,
    VINS,
    CAFES
}

val CategorieBoisson.displayName: String
    get() = when (this) {
        CategorieBoisson.APEROS -> "Apéros"
        CategorieBoisson.DIGESTIFS -> "Digestifs"
        CategorieBoisson.BIERES -> "Bières"
        CategorieBoisson.SOFTS -> "Softs"
        CategorieBoisson.VINS -> "Vins"
        CategorieBoisson.CAFES -> "Cafés"
    }
