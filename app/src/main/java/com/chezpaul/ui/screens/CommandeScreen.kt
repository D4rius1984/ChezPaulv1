package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chezpaul.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandeScreen(
    commande: Commande?,
    onNext: (Commande) -> Unit
) {
    var numeroTable by remember { mutableStateOf(commande?.numeroTable ?: "") }
    var salle by remember { mutableStateOf(commande?.salle ?: "") }
    var couverts by remember { mutableStateOf(commande?.nombreCouverts?.toString() ?: "") }
    var platState by remember { mutableStateOf(commande?.plat) }

    val plats = listOf(
        Plat("Langue de bœuf piquante", false),
        Plat("Langue de bœuf ravigote", true),
        Plat("Tête de veau", true),
        Plat("Quenelle", false),
        Plat("Tablier de sapeur", false),
        Plat("Andouillette", false),
        Plat("Cervelle de canut", false)
    )

    var ravigoteVisible by remember { mutableStateOf(false) }

    // Logic for showing Ravigote warning
    LaunchedEffect(platState) {
        ravigoteVisible = platState?.contientRavigote == true
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = numeroTable, onValueChange = { numeroTable = it }, label = { Text("Numéro de table") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = salle, onValueChange = { salle = it }, label = { Text("Salle") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = couverts,
            onValueChange = { couverts = it.filter { c -> c.isDigit() } },
            label = { Text("Nombre de couverts") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Display the Plat options
        Text("Plat principal :", style = MaterialTheme.typography.titleMedium)
        plats.forEach {
            Row(Modifier.fillMaxWidth()) {
                RadioButton(
                    selected = platState?.nom == it.nom,
                    onClick = { platState = it }
                )
                Text(it.nom, Modifier.padding(start = 4.dp))
            }
        }

        if (ravigoteVisible) {
            Text(
                "⚡ Ravigote à prévoir pour cette table !",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Validation button
        Button(
            onClick = {
                if (numeroTable.isNotBlank() && salle.isNotBlank() && couverts.isNotBlank() && platState != null) {
                    onNext(
                        Commande(
                            numeroTable = numeroTable,
                            salle = salle,
                            nombreCouverts = couverts.toInt(),
                            plat = platState!!,
                            boissons = emptyList()
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Valider et passer aux boissons")
        }
    }
}
