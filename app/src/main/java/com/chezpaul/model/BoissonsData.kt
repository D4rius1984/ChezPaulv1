package com.chezpaul.model

data class BoissonConfig(
    val nom: String,
    val categorie: CategorieBoisson,
    val isGroupe: Boolean,
    val isNonGroupe: Boolean
)

val boissonsList = listOf(
    // Apéros
    BoissonConfig("Kir", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Communard", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Ricard", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Whisky", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Pastis", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true),

    // Digestifs
    BoissonConfig("Chartreuse jaune", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Chartreuse verte", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Genepi", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Genereuse", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Verveine", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Menthe", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Poire", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Mandarine", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Abricot", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Marc", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true),

    // Bières
    BoissonConfig("Blonde", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Blanche", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Sans alcool", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Ambree", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("IPA", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Speciale", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true),

    // Softs
    BoissonConfig("Sirop", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Limonade", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Ice Tea", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Coca", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Orange", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("ACE", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Fraise", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Badoit", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Evian", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true),

    // Vins
    BoissonConfig("Montagnieu BTL", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("CDR BTL", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Brouilly Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true),
    BoissonConfig("Brouilly Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Brouilly Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("CDR Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true),
    BoissonConfig("CDR Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("CDR Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Blanc Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true),
    BoissonConfig("Blanc Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Blanc Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Rose Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true),
    BoissonConfig("Rose Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Rose Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true),

    // Cafés
    BoissonConfig("Expresso", CategorieBoisson.CAFES, isGroupe = true, isNonGroupe = true),
    BoissonConfig("Allongé", CategorieBoisson.CAFES, isGroupe = true, isNonGroupe = true),
    BoissonConfig("Double", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Crème", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true),
    BoissonConfig("Thé", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true)
)
