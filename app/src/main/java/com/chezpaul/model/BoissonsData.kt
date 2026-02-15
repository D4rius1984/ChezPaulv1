package com.chezpaul.model

data class BoissonConfig(
    val nom: String,
    val categorie: CategorieBoisson,
    val isGroupe: Boolean,
    val isNonGroupe: Boolean,
    val isActivated: Boolean = true,
    val sousCategorie: String? = null
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
    BoissonConfig("Grillotine", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Mirabelle", CategorieBoisson.DIGESTIFS, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Bières
    BoissonConfig("Blonde 33cl", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanche 33cl", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Sans alcool 33cl", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rousse 33cl", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Demi", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Pinte", CategorieBoisson.BIERES, isGroupe = false, isNonGroupe = true, isActivated = true),

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

    // Vins Fontaine
    BoissonConfig("Morgon Pot", CategorieBoisson.VINS_FONTAINE, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Morgon Filette", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Morgon Verre", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR Pot", CategorieBoisson.VINS_FONTAINE, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR Filette", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("CDR Verre", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanc Pot", CategorieBoisson.VINS_FONTAINE, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanc Filette", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Blanc Verre", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rose Pot", CategorieBoisson.VINS_FONTAINE, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rose Filette", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Rose Verre", CategorieBoisson.VINS_FONTAINE, isGroupe = false, isNonGroupe = true, isActivated = true),

    // Vins Bouteilles - Rouge
    BoissonConfig("Cote Rotie", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Rouge"),
    BoissonConfig("Saint Joseph", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Rouge"),
    BoissonConfig("Crozes", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Rouge"),
    BoissonConfig("Régnié", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Rouge"),
    BoissonConfig("Village", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Rouge"),
    BoissonConfig("Bourgogne", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Rouge"),
    // Vins Bouteilles - Blanc
    BoissonConfig("Condrieu", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Blanc"),
    BoissonConfig("Saint-Peray", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Blanc"),
    BoissonConfig("Macon", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Blanc"),
    BoissonConfig("Chablis", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Blanc"),
    BoissonConfig("Gascogne", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Blanc"),
    // Vins Bouteilles - Bulles et Rosé
    BoissonConfig("Champagne", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Bulles et Rosé"),
    BoissonConfig("Montagnieu", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Bulles et Rosé"),
    BoissonConfig("Rosé", CategorieBoisson.VINS_BOUTEILLES, isGroupe = false, isNonGroupe = true, isActivated = true, sousCategorie = "Bulles et Rosé"),

    // Cafés
    BoissonConfig("Expresso", CategorieBoisson.CAFES, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Allongé", CategorieBoisson.CAFES, isGroupe = true, isNonGroupe = true, isActivated = true),
    BoissonConfig("Double", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Crème", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true, isActivated = true),
    BoissonConfig("Thé", CategorieBoisson.CAFES, isGroupe = false, isNonGroupe = true, isActivated = true)
)
