package com.chezpaul.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.chezpaul.viewmodel.BoissonViewModel
import com.chezpaul.viewmodel.PlatViewModel
import com.chezpaul.model.BoissonConfig
import com.chezpaul.model.PlatConfig

@Composable
fun MenuModificationScreen(
    platViewModel: PlatViewModel,
    boissonViewModel: BoissonViewModel,
    onValidate: () -> Unit = {}
) {
    val jauneMenu = Color(0xFFFFE066)
    val orangeMenu = Color(0xFFEDA637)

    // Onglets et leur état sélectionné
    var selectedTab by remember { mutableStateOf(0) } // 0 = Plats, 1 = Boissons

    // Observer les données et états depuis les ViewModels
    val plats by platViewModel.plats.observeAsState(emptyList())
    val boissons by boissonViewModel.boissons.observeAsState(emptyList())
    val platsActivationState by platViewModel.platsActivationState.observeAsState(emptyMap())
    val boissonsActivationState by boissonViewModel.boissonsActivationState.observeAsState(emptyMap())

    val context = LocalContext.current

    // Fonction pour afficher la liste avec des cases à cocher pour Plats et Boissons
    @Composable
    fun generateItemList(
        isPlats: Boolean,
        items: List<Any>,
        activationState: Map<String, Boolean>,
        toggleActivation: (String, Boolean) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(items) { item ->
                // Casting explicite pour PlatConfig ou BoissonConfig
                val itemName = if (isPlats) {
                    (item as PlatConfig).nom
                } else {
                    (item as BoissonConfig).nom
                }

                // Si l'élément n'a pas d'état, on assume qu'il est activé par défaut
                val isActivated = activationState[itemName] ?: true

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = itemName,
                        modifier = Modifier.weight(1f),
                        color = Color.White
                    )
                    Checkbox(
                        checked = isActivated,
                        onCheckedChange = { checked ->
                            toggleActivation(itemName, checked)
                        },
                        colors = CheckboxDefaults.colors(checkedColor = jauneMenu)
                    )
                }
            }
        }
    }

    // Affichage des onglets
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(Color(0xFF23190e))
    ) {
        Text(
            "Modification du Menu",
            style = MaterialTheme.typography.headlineSmall,
            color = jauneMenu,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Plats") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Boissons") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card layout for Plat and Boisson selections
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF292929),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(Modifier.padding(16.dp)) {
                // Affichage des éléments selon l'onglet sélectionné
                if (selectedTab == 0) {
                    generateItemList(
                        isPlats = true,
                        items = plats,
                        activationState = platsActivationState,
                        toggleActivation = { platNom, isActivated ->
                            platViewModel.togglePlatActivation(platNom, isActivated)
                        }
                    )
                } else {
                    generateItemList(
                        isPlats = false,
                        items = boissons,
                        activationState = boissonsActivationState,
                        toggleActivation = { boissonNom, isActivated ->
                            boissonViewModel.toggleBoissonActivation(boissonNom, isActivated)
                        }
                    )
                }
            }
        }

        // Button to validate the selection, placed at the bottom
        Button(
            onClick = {
                Toast.makeText(context, "Modifications sauvegardées", Toast.LENGTH_SHORT).show()
                onValidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = jauneMenu)
        ) {
            Text("Valider les modifications", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}