package com.chezpaul.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.platform.LocalFocusManager
import com.chezpaul.model.Commande
import com.chezpaul.model.Plat
import com.chezpaul.model.Boisson
import com.chezpaul.model.platsData
import com.chezpaul.model.boissonsList
import com.chezpaul.model.CategorieBoisson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandeScreen(
    commande: Commande?,
    onNext: (Commande) -> Unit
) {
    val jauneMenu = Color(0xFFFFE066)
    val orangeMenu = Color(0xFFEDA637)
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Plats", "Boissons")

    var numeroTable by remember { mutableStateOf(commande?.numeroTable ?: "") }
    var couverts by remember { mutableStateOf(commande?.nombreCouverts?.toString() ?: "") }
    var infosConfirmees by remember { mutableStateOf(commande != null) } // Auto-confirmer si c'est une modification

    // Remarque corrigée
    var remarqueText by remember { mutableStateOf(commande?.remarque ?: "") }
    var showRemarqueDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Initialisation des plats sélectionnés avec les valeurs existantes
    var platsSelectionnes by remember {
        mutableStateOf(
            if (commande != null) {
                // Convertir les plats de la commande en Map avec les abréviations comme clés
                val platsMap = mutableMapOf<String, Int>()
                commande.plats.forEach { plat ->
                    // Trouver l'abréviation correspondante au nom du plat
                    val platConfig = platsData.find { it.nom == plat.nom }
                    if (platConfig != null) {
                        platsMap[platConfig.abrv] = plat.quantite
                    }
                }
                platsMap.toMap()
            } else {
                mapOf<String, Int>()
            }
        )
    }

    // Initialisation des boissons sélectionnées avec les valeurs existantes
    var boissonsSelectionnees by remember {
        mutableStateOf(
            if (commande != null) {
                val boissonsMap = mutableMapOf<String, Int>()
                commande.boissons.forEach { boisson ->
                    boissonsMap[boisson.nom] = boisson.quantite
                }
                boissonsMap.toMap()
            } else {
                mapOf<String, Int>()
            }
        )
    }

    val ravigoteVisible = (platsSelectionnes["Tdv"] ?: 0) > 0 || (platsSelectionnes["Ldb ravigote"] ?: 0) > 0
    val totalPlats = platsSelectionnes.values.sum()
    val nbCouverts = couverts.toIntOrNull() ?: 0

    // Déterminer automatiquement si c'est un groupe en fonction des plats/boissons existants
    var isGroupe by remember {
        mutableStateOf(
            if (commande != null) {
                // Vérifier si la commande existante contient des plats/boissons de groupe
                val hasGroupePlats = commande.plats.any { plat ->
                    platsData.find { it.nom == plat.nom }?.isGroupe == true
                }
                val hasGroupeBoissons = commande.boissons.any { boisson ->
                    boissonsList.find { it.nom == boisson.nom }?.isGroupe == true
                }
                hasGroupePlats || hasGroupeBoissons
            } else {
                false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(16.dp)
    ) {
        Text(
            "Commande",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF292929),
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp)) {
                if (!infosConfirmees) {
                    OutlinedTextField(
                        value = numeroTable,
                        onValueChange = { numeroTable = it },
                        label = { Text("Numéro de table") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = orangeMenu,
                            unfocusedBorderColor = orangeMenu,
                            focusedLabelColor = orangeMenu,
                            cursorColor = orangeMenu
                        )
                    )
                    OutlinedTextField(
                        value = couverts,
                        onValueChange = { couverts = it.filter { c -> c.isDigit() } },
                        label = { Text("Nombre de couverts") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = orangeMenu,
                            unfocusedBorderColor = orangeMenu,
                            focusedLabelColor = orangeMenu,
                            cursorColor = orangeMenu
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Groupe",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Switch(
                            checked = isGroupe,
                            onCheckedChange = {
                                isGroupe = it
                                // Réinitialiser les sélections si on change le mode
                                if (commande == null) {
                                    platsSelectionnes = mapOf()
                                    boissonsSelectionnees = mapOf()
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = jauneMenu,
                                uncheckedThumbColor = Color.Gray,
                                checkedTrackColor = orangeMenu,
                                uncheckedTrackColor = Color(0xFF292929)
                            )
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (numeroTable.isNotBlank() && couverts.isNotBlank() && nbCouverts > 0) {
                                infosConfirmees = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = numeroTable.isNotBlank() && couverts.isNotBlank() && nbCouverts > 0,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = jauneMenu,
                            contentColor = Color(0xFF222222)
                        )
                    ) {
                        Text("Confirmer la table", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Affichage de la remarque
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = jauneMenu.copy(alpha = 0.13f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Table $numeroTable  $couverts cv",
                                style = MaterialTheme.typography.labelLarge,
                                color = jauneMenu,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        // Badge remarque, icône stylo seule (plus de texte)
                        Surface(
                            color = orangeMenu.copy(alpha = 0.23f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            IconButton(
                                onClick = { showRemarqueDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Remarque",
                                    tint = jauneMenu
                                )
                            }
                        }
                    }

                    // Boite de dialogue animée pour la remarque (champ auto-focus)
                    AnimatedVisibility(
                        visible = showRemarqueDialog,
                        enter = fadeIn(animationSpec = tween(250)) + scaleIn(initialScale = 0.8f, animationSpec = tween(250)),
                        exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 0.7f, animationSpec = tween(200))
                    ) {
                        AlertDialog(
                            onDismissRequest = {
                                showRemarqueDialog = false
                                focusManager.clearFocus()
                            },
                            title = { Text("Remarque pour la table", color = orangeMenu) },
                            text = {
                                OutlinedTextField(
                                    value = remarqueText,
                                    onValueChange = { remarqueText = it },
                                    label = { Text("Ajouter/modifier une remarque") },
                                    singleLine = false,
                                    modifier = Modifier.fillMaxWidth()
                                        .fillMaxWidth()
                                        .background(Color(0xFF292929), RoundedCornerShape(8.dp)), // Fond gris foncé
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = orangeMenu,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedLabelColor = orangeMenu,
                                        unfocusedLabelColor = Color.Gray,
                                        cursorColor = orangeMenu,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showRemarqueDialog = false
                                        focusManager.clearFocus()
                                    }
                                ) { Text("OK", color = orangeMenu) }
                            },
                            containerColor = Color(0xFF292929),
                            tonalElevation = 10.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
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
                        tabTitles.forEachIndexed { idx, title ->
                            Tab(
                                selected = selectedTab == idx,
                                onClick = { selectedTab = idx },
                                text = { Text(title, color = if (selectedTab == idx) orangeMenu else Color.White) }
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))

                    if (selectedTab == 0) {
                        Text("Plats :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(6.dp))

                        // Filtrage des plats en fonction de isGroupe
                        val platsFiltres = platsData.filter {
                            if (isGroupe) it.isGroupe else it.isNonGroupe
                        }

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .fillMaxWidth()
                        ) {
                            itemsIndexed(platsFiltres) { _, config ->
                                val nomPlat = config.abrv
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(nomPlat, color = Color.White)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = {
                                                val currentQty = platsSelectionnes[nomPlat] ?: 0
                                                if (currentQty > 0) {
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().apply {
                                                        put(nomPlat, currentQty - 1)
                                                    }.toMap()
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(
                                            (platsSelectionnes[nomPlat] ?: 0).toString(),
                                            color = jauneMenu,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        IconButton(
                                            onClick = {
                                                if (totalPlats < nbCouverts) {
                                                    val currentQty = platsSelectionnes[nomPlat] ?: 0
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().apply {
                                                        put(nomPlat, currentQty + 1)
                                                    }.toMap()
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = jauneMenu)
                                        }
                                    }
                                }
                            }
                        }
                        if (totalPlats < nbCouverts) {
                            Text(
                                "Sélectionnez encore ${nbCouverts - totalPlats} plat(s) !",
                                color = Color(0xFFFFADAD),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        if (totalPlats > nbCouverts) {
                            Text(
                                "Trop de plats ! Diminuez la sélection.",
                                color = Color(0xFFFFADAD),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        if (ravigoteVisible) {
                            Text(
                                "⚡ Ravigote à prévoir pour cette table !",
                                style = MaterialTheme.typography.labelLarge,
                                color = jauneMenu,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val platsCommandes = platsFiltres.mapNotNull { config ->
                                    val qte = platsSelectionnes[config.abrv] ?: 0
                                    if (qte > 0) Plat(config.nom, qte, config.contientRavigote) else null
                                }
                                val boissonsCommandes = boissonsList.filter {
                                    if (isGroupe) it.isGroupe else it.isNonGroupe
                                }.mapNotNull { config ->
                                    val qte = boissonsSelectionnees[config.nom] ?: 0
                                    if (qte > 0) Boisson(config.nom, qte, config.categorie) else null
                                }
                                onNext(
                                    Commande(
                                        numeroTable = numeroTable,
                                        nombreCouverts = nbCouverts,
                                        plats = platsCommandes,
                                        boissons = boissonsCommandes,
                                        remarque = remarqueText,
                                        isGroupe = isGroupe
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            enabled = totalPlats == nbCouverts && nbCouverts > 0,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = jauneMenu,
                                contentColor = Color(0xFF222222)
                            )
                        ) {
                            Text("Valider la sélection", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Section Boissons - filtrage par catégorie ET par isGroupe
                        Text("Boissons :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(6.dp))

                        // Filtrage des boissons en fonction de isGroupe (comme pour les plats)
                        val boissonsFiltrees = boissonsList.filter {
                            if (isGroupe) it.isGroupe else it.isNonGroupe
                        }

                        val categoriesOrdre = listOf(
                            CategorieBoisson.APEROS,
                            CategorieBoisson.BIERES,
                            CategorieBoisson.SOFTS,
                            CategorieBoisson.DIGESTIFS,
                            CategorieBoisson.VINS,
                            CategorieBoisson.CAFES
                        )
                        val boissonsParCategorie = categoriesOrdre.associateWith { cat ->
                            boissonsFiltrees.filter { it.categorie == cat }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .fillMaxWidth()
                        ) {
                            categoriesOrdre.forEach { categorie ->
                                val boissons = boissonsParCategorie[categorie] ?: emptyList()
                                if (boissons.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = when (categorie) {
                                                CategorieBoisson.APEROS -> "Apéros"
                                                CategorieBoisson.BIERES -> "Bières"
                                                CategorieBoisson.SOFTS -> "Softs"
                                                CategorieBoisson.DIGESTIFS -> "Digestifs"
                                                CategorieBoisson.VINS -> "Vins"
                                                CategorieBoisson.CAFES -> "Cafés"
                                            },
                                            style = MaterialTheme.typography.titleSmall,
                                            color = jauneMenu,
                                            modifier = Modifier.padding(vertical = 6.dp)
                                        )
                                    }
                                    itemsIndexed(boissons) { _, config ->
                                        val nomBoisson = config.nom
                                        val quantite = boissonsSelectionnees[nomBoisson] ?: 0
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(nomBoisson, color = Color.White)
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                IconButton(
                                                    onClick = {
                                                        if (quantite > 0) {
                                                            boissonsSelectionnees = boissonsSelectionnees.toMutableMap().apply {
                                                                put(nomBoisson, quantite - 1)
                                                            }.toMap()
                                                        }
                                                    }
                                                ) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                                }
                                                Text(quantite.toString(), color = jauneMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                                IconButton(
                                                    onClick = {
                                                        boissonsSelectionnees = boissonsSelectionnees.toMutableMap().apply {
                                                            put(nomBoisson, quantite + 1)
                                                        }.toMap()
                                                    }
                                                ) {
                                                    Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = jauneMenu)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val platsCommandes = platsData.filter {
                                    if (isGroupe) it.isGroupe else it.isNonGroupe
                                }.mapNotNull { config ->
                                    val qte = platsSelectionnes[config.abrv] ?: 0
                                    if (qte > 0) Plat(config.nom, qte, config.contientRavigote) else null
                                }
                                val boissonsCommandes = boissonsFiltrees.mapNotNull { config ->
                                    val qte = boissonsSelectionnees[config.nom] ?: 0
                                    if (qte > 0) Boisson(config.nom, qte, config.categorie) else null
                                }
                                onNext(
                                    Commande(
                                        numeroTable = numeroTable,
                                        nombreCouverts = nbCouverts,
                                        plats = platsCommandes,
                                        boissons = boissonsCommandes,
                                        remarque = remarqueText,
                                        isGroupe = isGroupe
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = jauneMenu,
                                contentColor = Color(0xFF222222)
                            )
                        ) {
                            Text("Valider la sélection", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}