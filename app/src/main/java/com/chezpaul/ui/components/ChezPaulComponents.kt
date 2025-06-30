package com.chezpaul.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.ui.theme.ChezPaulColors

// Écran de base avec fond et titre
@Composable
fun ChezPaulScreen(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChezPaulColors.FondPrincipal)
            .padding(16.dp)
    ) {
        // Titre avec style uniforme
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = ChezPaulColors.JauneMenu,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Contenu
        content()
    }
}

// Card standard pour toutes les sections
@Composable
fun ChezPaulCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ChezPaulColors.FondCard),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}

// Surface standard pour les badges et petits éléments
@Composable
fun ChezPaulBadge(
    text: String,
    backgroundColor: Color = ChezPaulColors.JauneMenu,
    textColor: Color = ChezPaulColors.TexteNoir,
    modifier: Modifier = Modifier
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Bouton principal
@Composable
fun ChezPaulButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ChezPaulColors.JauneMenu,
            contentColor = ChezPaulColors.TexteNoir
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

// Divider personnalisé
@Composable
fun ChezPaulDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 0.7.dp,
        color = ChezPaulColors.DividerColor
    )
}