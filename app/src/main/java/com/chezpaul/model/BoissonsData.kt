package com.chezpaul.model

data class BoissonConfig(
    val nom: String,
    val categorie: CategorieBoisson,
    val isGroupe: Boolean,       // Boisson spécifique pour groupe
    val isNonGroupe: Boolean     // Bois spécifique pour commande normale
)

val boissonsList = listOf(
    // Apéros
    BoissonConfig("Kir", CategorieBoisson.APEROS, false, true),
    BoissonConfig("Communard", CategorieBoisson.APEROS, false, true),
    BoissonConfig("Ricard", CategorieBoisson.APEROS, false, true),
    BoissonConfig("Whisky", CategorieBoisson.APEROS, false, true),
    BoissonConfig("Pastis", CategorieBoisson.APEROS, false, true),
    // Digestifs
    BoissonConfig("Chartreuse jaune", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Chartreuse verte", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Genepi", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Genereuse", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Verveine", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Menthe", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Poire", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Mandarine", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Abricot", CategorieBoisson.DIGESTIFS, false, true),
    BoissonConfig("Marc", CategorieBoisson.DIGESTIFS, false, true),
    // Bières
    BoissonConfig("Blonde", CategorieBoisson.BIERES, false, true),
    BoissonConfig("Blanche", CategorieBoisson.BIERES, false, true),
    BoissonConfig("Sans alcool", CategorieBoisson.BIERES, false, true),
    BoissonConfig("Ambree", CategorieBoisson.BIERES, false, true),
    BoissonConfig("IPA", CategorieBoisson.BIERES, false, true),
    BoissonConfig("Speciale", CategorieBoisson.BIERES, false, true),
    // Softs
    BoissonConfig("Sirop", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Limonade", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Ice Tea", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Coca", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Orange", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("ACE", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Fraise", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Badoit", CategorieBoisson.SOFTS, false, true),
    BoissonConfig("Evian", CategorieBoisson.SOFTS, false, true),
    // Vins directs
    BoissonConfig("Montagnieu BTL", CategorieBoisson.VINS, false, true),
    BoissonConfig("CDR BTL", CategorieBoisson.VINS, false, true),
    // Vins sous-catégories
    BoissonConfig("Brouilly Pot", CategorieBoisson.VINS, true, true),
    BoissonConfig("Brouilly Filette", CategorieBoisson.VINS, false, true),
    BoissonConfig("Brouilly Verre", CategorieBoisson.VINS, false, true),
    BoissonConfig("CDR Pot", CategorieBoisson.VINS, true, true),
    BoissonConfig("CDR Filette", CategorieBoisson.VINS, false, true),
    BoissonConfig("CDR Verre", CategorieBoisson.VINS, false, true),
    BoissonConfig("Blanc Pot", CategorieBoisson.VINS, true, true),
    BoissonConfig("Blanc Filette", CategorieBoisson.VINS, false, true),
    BoissonConfig("Blanc Verre", CategorieBoisson.VINS, false, true),
    BoissonConfig("Rose Pot", CategorieBoisson.VINS, true, true),
    BoissonConfig("Rose Filette", CategorieBoisson.VINS, false, true),
    BoissonConfig("Rose Verre", CategorieBoisson.VINS, false, true),
    // Nouveaux cafés
    BoissonConfig("Expresso", CategorieBoisson.CAFES, true, true),
    BoissonConfig("Allongé", CategorieBoisson.CAFES, true, true),
    BoissonConfig("Double", CategorieBoisson.CAFES, false, true),
    BoissonConfig("Crème", CategorieBoisson.CAFES, false, true),
    BoissonConfig("Thé", CategorieBoisson.CAFES, false, true)
)
