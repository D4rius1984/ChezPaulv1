package com.chezpaul.model

data class PlatConfig(
    val nom: String,
    val abrv: String,
    val contientRavigote: Boolean,
    val isGroupe: Boolean,
    val isNonGroupe: Boolean,
    val isActivated: Boolean = true  // Ajout de la propriété 'isActivated' avec valeur par défaut true
)

val platsData = listOf(
    PlatConfig("Tablier", "Tablier", false, false, true, isActivated = true),
    PlatConfig("Tete de veau", "Tete de veau", true, true, true, isActivated = true),
    PlatConfig("Saucisson", "Saucisson", false, true, true, isActivated = true),
    PlatConfig("Civet", "Civet", false, true, true, isActivated = true),
    PlatConfig("Quenelle", "Quenelle", false, true, true, isActivated = true),
    PlatConfig("Langue de boeuf ravigote", "Ldb ravigote", true, true, true, isActivated = true),
    PlatConfig("Langue de boeuf piquante", "Ldb piquante", false, false, true, isActivated = true),
    PlatConfig("Andouillette", "Andouillette", false, true, true, isActivated = true),
    PlatConfig("Cote piquante", "Cote cochon", false, false, true, isActivated = true),
    PlatConfig("Cote vin", "Blanquette", false, false, true, isActivated = true),
    PlatConfig("Poulet", "Poulet", false, false, true, isActivated = true),
    PlatConfig("Boudin", "Boudin", false, false, true, isActivated = true),
    PlatConfig("Piece du B", "Piece du B", false, false, true, isActivated = true),
    PlatConfig("Végétarien", "Vegetarien", false, true, true, isActivated = true),
    PlatConfig("Cervelle", "Cervelle", false, true, true, isActivated = true),
    PlatConfig("St Marcelin", "St Marcelin", false, true, true, isActivated = true),
)
