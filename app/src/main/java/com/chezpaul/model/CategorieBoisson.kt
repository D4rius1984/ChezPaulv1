package com.chezpaul.model

enum class CategorieBoisson {
    APEROS,
    DIGESTIFS,
    BIERES,
    SOFTS,
    VINS_FONTAINE,
    VINS_BOUTEILLES,
    CAFES,
}

/** Ordre d'affichage des catégories dans l'outil de commande. */
val categoriesOrdre = listOf(
    CategorieBoisson.APEROS,
    CategorieBoisson.SOFTS,
    CategorieBoisson.BIERES,
    CategorieBoisson.VINS_FONTAINE,
    CategorieBoisson.VINS_BOUTEILLES,
    CategorieBoisson.CAFES,
    CategorieBoisson.DIGESTIFS,
)

val CategorieBoisson.displayName: String
    get() =
        when (this) {
            CategorieBoisson.APEROS -> "Apéros"
            CategorieBoisson.DIGESTIFS -> "Digestifs"
            CategorieBoisson.BIERES -> "Bières"
            CategorieBoisson.SOFTS -> "Softs"
            CategorieBoisson.VINS_FONTAINE -> "Vins Fontaine"
            CategorieBoisson.VINS_BOUTEILLES -> "Vins Bouteilles"
            CategorieBoisson.CAFES -> "Cafés"
        }
