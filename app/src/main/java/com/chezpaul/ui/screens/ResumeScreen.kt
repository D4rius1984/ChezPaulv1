package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Commande

@Composable
fun ResumeScreen(
    commande: Commande?,
    onValide: () -> Unit,
    commandesList: List<Commande>
) {
    Column(Modifier.padding(16.dp)) {
        Text("Résumé de la commande", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        commande?.let {
            Text("Table : ${it.numeroTable}")
            Text("Salle : ${it.salle}")
            Text("Couverts : ${it.nombreCouverts}")
            Text("Plat : ${it.plat?.nom}")
            if (it.plat?.contientRavigote == true) {
                Text("⚡ Ravigote à prévoir", color = MaterialTheme.colorScheme.primary)
            }
            Text("Boissons :")
            it.boissons.forEach { boisson ->
                Text("- ${boisson.nom}")
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onValide,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Valider la commande")
            }
        } ?: Text("Aucune commande en cours")
        Spacer(Modifier.height(24.dp))
        Text("Commandes ouvertes :", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(commandesList) { cmd ->
                Column(Modifier.padding(vertical = 4.dp)) {
                    Text("Table ${cmd.numeroTable} (${cmd.salle}) - ${cmd.nombreCouverts} couverts")
                    Text("Plat : ${cmd.plat?.nom}")
                    if (cmd.plat?.contientRavigote == true) {
                        Text("⚡ Ravigote", color = MaterialTheme.colorScheme.primary)
                    }
                    if (cmd.boissons.isNotEmpty()) {
                        Text("Boissons : ${cmd.boissons.joinToString { it.nom }}")
                    }
                }
            }
        }
    }
}
