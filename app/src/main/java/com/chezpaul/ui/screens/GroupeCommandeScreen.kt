package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.chezpaul.model.Commande
import com.chezpaul.model.Plat
import com.chezpaul.model.Boisson
import com.chezpaul.model.CategorieBoisson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupeCommandeScreen(
    commande: Commande? = null,
    onCommandeValidee: (Commande) -> Unit
) {
    val jauneMenu = Color(0xFFFFE066)
    val orangeMenu = Color(0xFFEDA637)
    val tabTitles = listOf("Plats", "Boissons")

    // Ajout pour résumé intermédiaire
    var showResume by remember { mutableStateOf(false) }
    var commandeAValider by remember { mutableStateOf<Commande?>(null) }

    data class PlatConfig(val nom: String, val contientRavigote: Boolean, val isCervelle: Boolean = false)
    val platsData = listOf(
        PlatConfig("Tête de veau sauce ravigote", true),
        PlatConfig("Saucisson chaud", false),
        PlatConfig("Quenelle de brochet", false),
        PlatConfig("Civet de joue de porc", false),
        PlatConfig("Andouillette", false),
        PlatConfig("Langue de bœuf sauce ravigote", true),
        PlatConfig("Cervelle", false, isCervelle = true),
        PlatConfig("Special", false)
    )
    val boissonsList = listOf("Vin", "Café")

    var nomGroupe by remember { mutableStateOf(commande?.nomGroupe ?: "") }
    var tables by remember { mutableStateOf(commande?.numeroTable ?: "") }
    var couverts by remember { mutableStateOf(commande?.nombreCouverts?.toString() ?: "") }
    var infosConfirmees by remember { mutableStateOf(commande != null) }
    var selectedTab by remember { mutableStateOf(0) }

    var platsSelectionnes by remember {
        mutableStateOf(
            buildMap {
                platsData.forEach { put(it.nom, 0) }
                commande?.plats?.forEach { plat ->
                    put(plat.nom, plat.quantite)
                }
            }.toMutableMap()
        )
    }
    var boissonsSelectionnees by remember {
        mutableStateOf(
            buildMap {
                boissonsList.forEach { put(it, 0) }
                commande?.boissons?.forEach { boisson ->
                    if (boissonsList.contains(boisson.nom)) put(boisson.nom, boisson.quantite)
                }
            }.toMutableMap()
        )
    }

    val nbCouvertsInt = couverts.toIntOrNull() ?: 0
    val totalPlatsHorsCervelle = platsSelectionnes.filterKeys { k ->
        !platsData.find { it.nom == k }!!.isCervelle
    }.values.sum()
    val ravigoteVisible = (platsSelectionnes["Tête de veau sauce ravigote"] ?: 0) > 0 ||
            (platsSelectionnes["Langue de bœuf sauce ravigote"] ?: 0) > 0

    // ========== FLOW AVEC RESUME ==========
    if (showResume && commandeAValider != null) {
        // Affiche un résumé similaire à ResumeScreen, mais simplifié
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF23190e))
                .padding(16.dp)
        ) {
            Text(
                "Résumé de la commande groupe",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = jauneMenu,
                modifier = Modifier.padding(start = 24.dp, top = 36.dp, bottom = 12.dp)
            )
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = jauneMenu,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                "GROUPE",
                                color = Color(0xFF222222),
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            buildString {
                                append("Tables : ${commandeAValider!!.numeroTable}")
                                append(" (${commandeAValider!!.nomGroupe})")
                            },
                            color = jauneMenu,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "Couverts : ${commandeAValider!!.nombreCouverts}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("Plats :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                    if (commandeAValider!!.plats.isNotEmpty()) {
                        commandeAValider!!.plats.forEach { plat ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("- ${plat.nom} x${plat.quantite}", color = Color.White)
                                if (plat.contientRavigote) {
                                    Text(" ⚡ Ravigote", color = jauneMenu)
                                }
                            }
                        }
                    } else {
                        Text("Aucun plat", color = Color.Gray)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Boissons :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                    if (commandeAValider!!.boissons.isNotEmpty()) {
                        commandeAValider!!.boissons.forEach { boisson ->
                            Text("- ${boisson.nom} x${boisson.quantite}", color = Color.White)
                        }
                    } else {
                        Text("Aucune boisson", color = Color.Gray)
                    }
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = {
                            onCommandeValidee(commandeAValider!!)
                            showResume = false
                            commandeAValider = null
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
                        Text("Valider la commande", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return // Pour ne pas afficher le reste
    }
    // ========== FIN FLOW RESUME ==========

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(16.dp)
    ) {
        Text(
            "Commande Groupe",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = jauneMenu,
            modifier = Modifier.padding(start = 24.dp, top = 36.dp, bottom = 12.dp)
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
                        value = nomGroupe,
                        onValueChange = { nomGroupe = it },
                        label = { Text("Nom du groupe") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = orangeMenu,
                            unfocusedBorderColor = orangeMenu,
                            focusedLabelColor = orangeMenu,
                            cursorColor = orangeMenu
                        )
                    )
                    OutlinedTextField(
                        value = tables,
                        onValueChange = { tables = it },
                        label = { Text("Numéro(s) de table") },
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
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (
                                nomGroupe.isNotBlank() &&
                                tables.isNotBlank() &&
                                couverts.isNotBlank() && nbCouvertsInt > 0
                            ) {
                                infosConfirmees = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = nomGroupe.isNotBlank() &&
                                tables.isNotBlank() &&
                                couverts.isNotBlank() && nbCouvertsInt > 0,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = jauneMenu,
                            contentColor = Color(0xFF222222)
                        )
                    ) {
                        Text("Confirmer la table", fontWeight = FontWeight.Bold)
                    }
                } else {
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
                                "Tables $tables - $nbCouvertsInt cv",
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
                            TabRowDefaults.Indicator(
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
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .fillMaxWidth()
                        ) {
                            itemsIndexed(platsData) { _, config ->
                                val nomPlat = config.nom
                                val quantite = platsSelectionnes[nomPlat] ?: 0
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
                                                if (quantite > 0) {
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().also {
                                                        it[nomPlat] = (it[nomPlat] ?: 0) - 1
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(
                                            quantite.toString(),
                                            color = jauneMenu,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        IconButton(
                                            onClick = {
                                                if (config.isCervelle) {
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().also {
                                                        it[nomPlat] = (it[nomPlat] ?: 0) + 1
                                                    }
                                                } else if (totalPlatsHorsCervelle < nbCouvertsInt) {
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().also {
                                                        it[nomPlat] = (it[nomPlat] ?: 0) + 1
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = jauneMenu)
                                        }
                                    }
                                }
                            }
                        }
                        if (totalPlatsHorsCervelle < nbCouvertsInt) {
                            Text(
                                "Sélectionnez encore ${nbCouvertsInt - totalPlatsHorsCervelle} plat(s) !",
                                color = Color(0xFFFFADAD),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        if (totalPlatsHorsCervelle > nbCouvertsInt) {
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
                                val platsCommandes = platsData.mapNotNull { config ->
                                    val qte = platsSelectionnes[config.nom] ?: 0
                                    if (qte > 0) Plat(config.nom, qte, config.contientRavigote) else null
                                }
                                val boissonsCommandes = boissonsList.mapNotNull { nom ->
                                    val qte = boissonsSelectionnees[nom] ?: 0
                                    if (qte > 0) Boisson(nom, qte, CategorieBoisson.VINS)
                                    else null
                                }
                                // CHANGEMENT PRINCIPAL :
                                commandeAValider = Commande(
                                    numeroTable = tables,
                                    nombreCouverts = nbCouvertsInt,
                                    plats = platsCommandes,
                                    boissons = boissonsCommandes,
                                    isGroupe = true,
                                    nomGroupe = nomGroupe
                                )
                                showResume = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            enabled = totalPlatsHorsCervelle == nbCouvertsInt && nbCouvertsInt > 0,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = jauneMenu,
                                contentColor = Color(0xFF222222)
                            )
                        ) {
                            Text("Valider la sélection", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text("Boissons :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .fillMaxWidth()
                        ) {
                            itemsIndexed(boissonsList) { _, nomBoisson ->
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
                                                    boissonsSelectionnees = boissonsSelectionnees.toMutableMap().also {
                                                        it[nomBoisson] = (it[nomBoisson] ?: 0) - 1
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(
                                            quantite.toString(),
                                            color = jauneMenu,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        IconButton(
                                            onClick = {
                                                boissonsSelectionnees = boissonsSelectionnees.toMutableMap().also {
                                                    it[nomBoisson] = (it[nomBoisson] ?: 0) + 1
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = jauneMenu)
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val platsCommandes = platsData.mapNotNull { config ->
                                    val qte = platsSelectionnes[config.nom] ?: 0
                                    if (qte > 0) Plat(config.nom, qte, config.contientRavigote) else null
                                }
                                val boissonsCommandes = boissonsList.mapNotNull { nom ->
                                    val qte = boissonsSelectionnees[nom] ?: 0
                                    if (qte > 0) Boisson(nom, qte, CategorieBoisson.VINS) else null
                                }
                                // CHANGEMENT PRINCIPAL :
                                commandeAValider = Commande(
                                    numeroTable = tables,
                                    nombreCouverts = nbCouvertsInt,
                                    plats = platsCommandes,
                                    boissons = boissonsCommandes,
                                    isGroupe = true,
                                    nomGroupe = nomGroupe
                                )
                                showResume = true
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
