// PlatsData.kt
package com.chezpaul.model

data class PlatConfig(
    val nom: String,
    val abrv: String,
    val contientRavigote: Boolean,
    val isGroupe: Boolean,       // Plat spécifique pour groupe
    val isNonGroupe: Boolean     // Plat spécifique pour commande normale
)

val platsData = listOf(
    PlatConfig("Tablier", "Tablier", false, false, true),
    PlatConfig("Tete de veau", "Tete de veau", true, true, true),
    PlatConfig("Saucisson", "Saucisson", false, true, true),
    PlatConfig("Civet", "Civet", false, true, true),
    PlatConfig("Quenelle", "Quenelle", false, true, true),
    PlatConfig("Langue de boeuf ravigote", "Ldb ravigote", true, true, true),
    PlatConfig("Langue de boeuf piquante", "Ldb piquante", false, false, true),
    PlatConfig("Andouillette", "Andouillette", false, true, true),
    PlatConfig("Cote cochon", "Cote cochon", false, false, true),
    PlatConfig("Blanquette", "Blanquette", false, false, true),
    PlatConfig("Poulet", "Poulet", false, false, true),
    PlatConfig("Boudin", "Boudin", false, false, true),
    PlatConfig("Piece du B", "Piece du B", false, false, true),
    PlatConfig("Végétarien", "Végétarien", false, true, true),
    PlatConfig("Cervelle", "Cervelle", false, true, false),
    // Exemple de plat spécifique aux groupes
)
