package com.chezpaul.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Couleurs centralisées
object ChezPaulColors {
    val JauneMenu = Color(0xFFFFE066)
    val OrangeMenu = Color(0xFFEDA637)
    val FondPrincipal = Color(0xFF23190e)
    val FondCard = Color(0xFF292929)
    val TexteBlanc = Color.White
    val TexteGris = Color.Gray
    val TexteNoir = Color(0xFF222222)
    val RougeErreur = Color(0xFFFF6B6B)
    val RougeClair = Color(0xFFFFADAD)
    val DividerColor = Color(0x33FFFFFF)
}

// Typographie personnalisée
val ChezPaulTypography = Typography(
    // Titre principal (Accueil, Paramètres, etc.) - plus petit comme demandé
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),
    // Titres de sections
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp
    ),
    // Petits titres
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    // Corps de texte
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.4.sp
    ),
    // Labels
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = ChezPaulColors.JauneMenu,
    onPrimary = ChezPaulColors.TexteNoir,
    secondary = ChezPaulColors.OrangeMenu,
    onSecondary = ChezPaulColors.TexteNoir,
    background = ChezPaulColors.FondPrincipal,
    onBackground = ChezPaulColors.TexteBlanc,
    surface = ChezPaulColors.FondCard,
    onSurface = ChezPaulColors.TexteBlanc,
    error = ChezPaulColors.RougeErreur,
    onError = ChezPaulColors.TexteBlanc
)

private val LightColorScheme = lightColorScheme(
    primary = ChezPaulColors.JauneMenu,
    onPrimary = ChezPaulColors.TexteNoir,
    secondary = ChezPaulColors.OrangeMenu,
    onSecondary = ChezPaulColors.TexteNoir,
    background = ChezPaulColors.FondPrincipal,
    onBackground = ChezPaulColors.TexteBlanc,
    surface = ChezPaulColors.FondCard,
    onSurface = ChezPaulColors.TexteBlanc,
    error = ChezPaulColors.RougeErreur,
    onError = ChezPaulColors.TexteBlanc
)

@Composable
fun ChezPaulTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Pour l'instant, on force le thème sombre
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ChezPaulTypography,
        content = content
    )
}