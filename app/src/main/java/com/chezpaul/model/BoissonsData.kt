package com.chezpaul.model

data class BoissonConfig(
    val nom: String,
    val categorie: CategorieBoisson,
    val isGroupe: Boolean,
    val isNonGroupe: Boolean,
    val isActivated: Boolean = true  // Ajout de la propriété 'isActivated' avec valeur par défaut true
)

val boissonsList = listOf(
    // Apéros
    BoissonConfig("Kir", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Communard", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Ricard", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Whisky", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Pastis", CategorieBoisson.APEROS, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Digestifs
    BoissonConfig("Chartreuse jaune", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Chartreuse verte", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Genepi", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Genereuse", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Verveine", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Menthe", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Poire", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Mandarine", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Abricot", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Marc", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Bières
    BoissonConfig("Blonde", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanche", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Sans alcool", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Ambree", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("IPA", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Speciale", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Softs
    BoissonConfig("Sirop", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Limonade", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Ice Tea", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Coca", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Orange", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("ACE", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Fraise", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Badoit", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Evian", CategorieBoisson.SOFTS, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Vins
    BoissonConfig("Montagnieu BTL", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR BTL", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Brouilly Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Brouilly Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Brouilly Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanc Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanc Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanc Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rose Pot", CategorieBoisson.VINS, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rose Filette", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rose Verre", CategorieBoisson.VINS, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Cafés
    BoissonConfig("Expresso", CategorieBoisson.CAFES, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Allongé", CategorieBoisson.CAFES, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Double", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Crème", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Thé", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true, isActivated = true)
)
