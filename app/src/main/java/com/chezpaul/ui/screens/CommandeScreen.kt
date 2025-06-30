package com.chezpaul.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.*
import com.chezpaul.viewmodel.CommandeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CommandeScreen(
    commande: Commande? = null,
    onNext: (Commande) -> Unit = {},
    platsActivationState: Map<String, Boolean>, // Ajout pour l'état des plats
    boissonsActivationState: Map<String, Boolean> // Ajout pour l'état des boissons
) {
    val jauneMenu = Color(0xFFFFE066)
    val orangeMenu = Color(0xFFEDA637)
    val commandeViewModel: CommandeViewModel = viewModel()


    var initDone by remember { mutableStateOf(commande != null) }

    var numeroTable by remember { mutableStateOf(commande?.numeroTable ?: "") }
    var couverts by remember { mutableStateOf(commande?.nombreCouverts?.toString() ?: "") }
    var isGroupe by remember { mutableStateOf(commande?.isGroupe ?: false) }

    var selectedTab by remember { mutableIntStateOf(1) }
    val tabTitles = listOf("Plats", "Boissons")
    var remarqueText by remember { mutableStateOf(commande?.remarque ?: "") }
    var showRemarqueDialog by remember { mutableStateOf(false) }
    var platsSelectionnes by remember { mutableStateOf<Map<String, Int>>(mutableMapOf()) }
    var boissonsSelectionnees by remember { mutableStateOf<Map<String, Int>>(mutableMapOf()) }

    // Initialiser la sélection si commande existante
    LaunchedEffect(Unit) {
        commande?.let {
            platsSelectionnes = it.plats.associate { plat -> plat.nom to plat.quantite }
            boissonsSelectionnees = it.boissons.associate { boisson -> boisson.nom to boisson.quantite }
        }
    }

    // Etat pour badge Ravigote
    val hasRavigote = platsSelectionnes.any { (nom, quantite) ->
        val plat = platsData.find { it.nom == nom }
        plat?.contientRavigote == true && quantite > 0
    }

    // Calcul du total des plats hors cervelle
    val totalPlatsHorsCervelle = platsSelectionnes.entries
        .filter { (nom, _) -> nom.lowercase() != "cervelle" }
        .sumOf { it.value }

    val nbCouvertsInt = couverts.toIntOrNull() ?: 0

    val peutAjouterPlat = totalPlatsHorsCervelle < nbCouvertsInt
    val boutonValiderActif = totalPlatsHorsCervelle == nbCouvertsInt && numeroTable.isNotBlank()

    // Filtrer les plats et boissons selon l'activation
    val platsFiltres = platsData.filter { plat -> platsActivationState[plat.nom] ?: true }
    val boissonsFiltres = boissonsList.filter { boisson -> boissonsActivationState[boisson.nom] ?: true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(12.dp)
    ) {
        Text(
            "Commande",
            style = MaterialTheme.typography.headlineSmall,
            color = jauneMenu,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (!initDone) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF292929),
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = numeroTable,
                        onValueChange = { numeroTable = it },
                        label = { Text("Numéro de table") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = jauneMenu,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = jauneMenu,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = jauneMenu
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = couverts,
                        onValueChange = { couverts = it },
                        label = { Text("Nombre de couverts") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = jauneMenu,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = jauneMenu,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = jauneMenu
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Groupe", color = jauneMenu, modifier = Modifier.weight(1f))
                        Switch(
                            checked = isGroupe,
                            onCheckedChange = { isGroupe = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = jauneMenu,
                                checkedTrackColor = jauneMenu.copy(alpha = 0.5f)
                            )
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { initDone = true },
                        enabled = numeroTable.isNotBlank() && couverts.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = jauneMenu,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Confirmer la table", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF292929),
                tonalElevation = 8.dp,
                modifier = Modifier.weight(1f)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Surface(
                            color = jauneMenu.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Table $numeroTable",
                                style = MaterialTheme.typography.labelLarge,
                                color = jauneMenu,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            color = jauneMenu.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "$couverts cv",
                                style = MaterialTheme.typography.labelLarge,
                                color = jauneMenu,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = orangeMenu,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = orangeMenu
                            )
                        }
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, color = if (selectedTab == index) orangeMenu else Color.White) }
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    LazyColumn {
                        if (selectedTab == 1) {
                            items(boissonsFiltres) { boisson ->
                                val count = boissonsSelectionnees[boisson.nom] ?: 0
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(boisson.nom, color = Color.White, modifier = Modifier.weight(1f))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            if (count > 0) {
                                                boissonsSelectionnees = boissonsSelectionnees.toMutableMap().also {
                                                    it[boisson.nom] = count - 1
                                                }
                                            }
                                        }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(count.toString(), color = orangeMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                        IconButton(onClick = {
                                            boissonsSelectionnees = boissonsSelectionnees.toMutableMap().also {
                                                it[boisson.nom] = count + 1
                                            }
                                        }) {
                                            Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = jauneMenu)
                                        }
                                    }
                                }
                            }
                        } else {
                            items(platsFiltres) { plat ->
                                val count = platsSelectionnes[plat.nom] ?: 0
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(plat.nom, color = Color.White, modifier = Modifier.weight(1f))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            if (count > 0) {
                                                platsSelectionnes = platsSelectionnes.toMutableMap().also {
                                                    it[plat.nom] = count - 1
                                                }
                                            }
                                        }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(count.toString(), color = orangeMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                        IconButton(
                                            enabled = peutAjouterPlat,
                                            onClick = {
                                                if (peutAjouterPlat) {
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().also {
                                                        it[plat.nom] = count + 1
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Ajouter",
                                                tint = if (peutAjouterPlat) jauneMenu else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val newCommande = Commande(
                        numeroTable = numeroTable,
                        nombreCouverts = nbCouvertsInt,
                        plats = platsSelectionnes.map { (nom, quantite) ->
                            val platConfig = platsData.find { it.nom == nom }
                            Plat(
                                nom = nom,
                                quantite = quantite,
                                contientRavigote = platConfig?.contientRavigote ?: false
                            )
                        },
                        boissons = boissonsSelectionnees.map { (nom, quantite) ->
                            val config = boissonsList.find { it.nom == nom }
                            Boisson(
                                nom = nom,
                                quantite = quantite,
                                categorie = config?.categorie ?: CategorieBoisson.SOFTS
                            )
                        },
                        remarque = remarqueText,
                        isGroupe = isGroupe
                    )
                    commandeViewModel.validerCommande(newCommande)
                    onNext(newCommande)
                },
                enabled = boutonValiderActif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (boutonValiderActif) jauneMenu else Color.Gray,
                    contentColor = Color.Black
                )
            ) {
                Text("Valider la sélection", fontWeight = FontWeight.Bold)
            }
        }
    }
}
