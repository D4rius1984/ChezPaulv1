package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Boisson
import com.chezpaul.model.CategorieBoisson
import com.chezpaul.model.Commande

@Composable
fun BoissonsScreen(
    commande: Commande?,
    onNext: (Commande) -> Unit
) {
    val aperos = listOf("Kir", "Communard", "Ricard")
    val digestifs = listOf("Chartreuse jaune", "Chartreuse verte", "Génépi", "Verveine", "Menthe", "Poire", "Mandarine", "Abricot", "Marc")
    val bieres = listOf("Blonde", "Blanche", "Sans Alcool", "Ambrée", "IPA", "Spéciale")
    val softs = listOf("Sirop", "Limonade", "Ice Tea", "Bière sans alcool", "Orange", "ACE", "Fraise", "Eau")
    val vinsDirects = listOf("Montagnieu BTL", "CDR BTC")
    val vinsSousCategories = mapOf(
        "Brouilly" to listOf("Pot", "Filette", "Verre"),
        "CDR" to listOf("Pot", "Filette", "Verre"),
        "Blanc" to listOf("Pot", "Filette", "Verre"),
        "Rosé" to listOf("Pot", "Filette", "Verre")
    )

    var boissonsSelectionnees by remember { mutableStateOf(commande?.boissons ?: emptyList()) }

    fun addOrRemoveBoisson(boisson: Boisson) {
        boissonsSelectionnees = if (boissonsSelectionnees.contains(boisson))
            boissonsSelectionnees - boisson
        else
            boissonsSelectionnees + boisson
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item { Text("Apéros", style = MaterialTheme.typography.titleMedium) }
        items(aperos) { nom ->
            Row {
                Checkbox(
                    checked = boissonsSelectionnees.any { it.nom == nom },
                    onCheckedChange = { addOrRemoveBoisson(Boisson(nom, CategorieBoisson.APEROS)) }
                )
                Text(nom, Modifier.padding(start = 4.dp))
            }
        }

        item { Spacer(Modifier.height(8.dp)); Text("Bières", style = MaterialTheme.typography.titleMedium) }
        items(bieres) { nom ->
            Row {
                Checkbox(
                    checked = boissonsSelectionnees.any { it.nom == nom },
                    onCheckedChange = { addOrRemoveBoisson(Boisson(nom, CategorieBoisson.BIERES)) }
                )
                Text(nom, Modifier.padding(start = 4.dp))
            }
        }

        // Similarly handle Softs, Digestifs, and Vins sections

        item {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    if (commande != null) {
                        onNext(commande.copy(boissons = boissonsSelectionnees))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Valider et voir le résumé")
            }
        }
    }
}
