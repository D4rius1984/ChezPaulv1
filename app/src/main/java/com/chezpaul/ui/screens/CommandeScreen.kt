package com.chezpaul.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Print
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.*
import com.chezpaul.viewmodel.CommandeViewModel
import com.chezpaul.viewmodel.PrinterViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CommandeScreen(
    commande: Commande? = null,
    onNext: (Commande) -> Unit = {},
    platsActivationState: Map<String, Boolean>,
    boissonsActivationState: Map<String, Boolean>,
    printerViewModel: PrinterViewModel // Ajout du PrinterViewModel
) {
    val jauneMenu = Color(0xFFFFE066)
    val orangeMenu = Color(0xFFEDA637)
    val commandeViewModel: CommandeViewModel = viewModel()
    val context = LocalContext.current

    // Observer l'état de l'imprimante
    val isPrinterEnabled by printerViewModel.isPrinterEnabled
    val printerName by printerViewModel.printerName
    val isLoading by printerViewModel.isLoading

    var initDone by remember { mutableStateOf(commande != null) }

    var numeroTable by remember { mutableStateOf(commande?.numeroTable ?: "") }
    var couverts by remember { mutableStateOf(commande?.nombreCouverts?.toString() ?: "") }
    var isGroupe by remember { mutableStateOf(commande?.isGroupe ?: false) }
    var shouldPrint by remember { mutableStateOf(false) } // Switch pour l'impression

    var selectedTab by remember { mutableIntStateOf(1) }
    val tabTitles = listOf("Plats", "Boissons")
    var remarqueText by remember { mutableStateOf(commande?.remarque ?: "") }
    var showRemarqueDialog by remember { mutableStateOf(false) }
    var platsSelectionnes by remember { mutableStateOf<Map<String, Int>>(mutableMapOf()) }
    var boissonsSelectionnees by remember { mutableStateOf<Map<String, Int>>(mutableMapOf()) }

    // Initialiser la sélection si commande existante
    LaunchedEffect(commande) {
        if (commande != null) {
            numeroTable = commande.numeroTable
            couverts = commande.nombreCouverts.toString()
            isGroupe = commande.isGroupe
            remarqueText = commande.remarque ?: ""
            platsSelectionnes = commande.plats.associate { plat -> plat.nom to plat.quantite }
            boissonsSelectionnees = commande.boissons.associate { boisson -> boisson.nom to boisson.quantite }
            initDone = true
        }
    }

    // Etat pour badge Ravigote
    val hasRavigote = platsSelectionnes.any { (nom, quantite) ->
        val plat = platsData.find { it.nom == nom }
        plat?.contientRavigote == true && quantite > 0
    }

    // Calcul du total des plats hors cervelle et st marcelin
    val totalPlatsHorsCervelle = platsSelectionnes.entries
        .filter { (nom, _) ->
            nom.lowercase() != "cervelle" && nom.lowercase() != "st marcelin"
        }
        .sumOf { it.value }

    val nbCouvertsInt = couverts.toIntOrNull() ?: 0

    val peutAjouterPlat = totalPlatsHorsCervelle < nbCouvertsInt

    // Logique de validation : soit plats respectent la règle, soit juste des boissons, soit les deux
    val aDesPlats = platsSelectionnes.any { it.value > 0 }
    val aDesBoissons = boissonsSelectionnees.any { it.value > 0 }
    val platsRespectentRegle = totalPlatsHorsCervelle == nbCouvertsInt

    val boutonValiderActif = numeroTable.isNotBlank() && (
            (!aDesPlats && aDesBoissons) ||  // Seulement boissons = OK
                    (aDesPlats && platsRespectentRegle) ||  // Plats respectent la règle = OK
                    (aDesPlats && aDesBoissons && platsRespectentRegle)  // Les deux = OK
            )

    // Filtrer les plats et boissons selon l'activation ET le type de commande (groupe/non-groupe)
    val platsFiltres = platsData.filter { plat ->
        val isActivated = platsActivationState[plat.nom] ?: true
        val isAvailableForCurrentType = if (isGroupe) plat.isGroupe else plat.isNonGroupe
        isActivated && isAvailableForCurrentType
    }
    val boissonsFiltres = boissonsList.filter { boisson ->
        val isActivated = boissonsActivationState[boisson.nom] ?: true
        val isAvailableForCurrentType = if (isGroupe) boisson.isGroupe else boisson.isNonGroupe
        isActivated && isAvailableForCurrentType
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(12.dp)
    ) {
        Text(
            if (commande != null) "Modifier commande" else "Commande",
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

                    // Switch Groupe
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

                    Spacer(Modifier.height(8.dp))

                    // Switch Impression - NOUVEAU
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Print,
                                    contentDescription = "Impression",
                                    tint = if (isPrinterEnabled && printerName.isNotEmpty()) jauneMenu else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Imprimer en cuisine",
                                    color = if (isPrinterEnabled && printerName.isNotEmpty()) jauneMenu else Color.Gray
                                )
                            }
                            if (isPrinterEnabled && printerName.isNotEmpty()) {
                                Text(
                                    printerName,
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    "Aucune imprimante configurée",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Switch(
                            checked = shouldPrint,
                            onCheckedChange = { shouldPrint = it },
                            enabled = isPrinterEnabled && printerName.isNotEmpty(),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = jauneMenu,
                                checkedTrackColor = jauneMenu.copy(alpha = 0.5f),
                                disabledCheckedThumbColor = Color.Gray,
                                disabledUncheckedThumbColor = Color.Gray
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
                        Spacer(Modifier.width(8.dp))
                        // Badge Remarque cliquable
                        Surface(
                            color = jauneMenu.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable { showRemarqueDialog = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Ajouter remarque",
                                    tint = jauneMenu,
                                    modifier = Modifier.size(16.dp)
                                )
                                if (remarqueText.isNotBlank()) {
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        remarqueText.take(8) + if (remarqueText.length > 8) "..." else "",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = jauneMenu,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        // Badge Impression si activé
                        if (shouldPrint && isPrinterEnabled && printerName.isNotEmpty()) {
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                color = orangeMenu.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Print,
                                        contentDescription = "Impression activée",
                                        tint = orangeMenu,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "Print",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = orangeMenu,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        // Badge Ravigote si des plats avec ravigote sont sélectionnés
                        if (hasRavigote) {
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                color = orangeMenu.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    "⚡ Ravigote !",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = orangeMenu,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
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
                            // Grouper les boissons par catégorie
                            val boissonsByCategory = boissonsFiltres.groupBy { it.categorie }

                            boissonsByCategory.forEach { (categorie, boissonsDeCategorie) ->
                                // Header de catégorie
                                item {
                                    Text(
                                        text = categorie.displayName,
                                        color = orangeMenu,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }

                                // Grouper par sous-catégorie si présente
                                val parSousCategorie = boissonsDeCategorie.groupBy { it.sousCategorie }

                                parSousCategorie.forEach { (sousCat, boissons) ->
                                    // Afficher le sous-header si une sous-catégorie existe
                                    if (sousCat != null) {
                                        item {
                                            Text(
                                                text = sousCat,
                                                color = jauneMenu.copy(alpha = 0.7f),
                                                fontWeight = FontWeight.SemiBold,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 2.dp)
                                            )
                                        }
                                    }

                                    items(boissons) { boisson ->
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
                                                        val newMap = boissonsSelectionnees.toMutableMap()
                                                        newMap[boisson.nom] = count - 1
                                                        boissonsSelectionnees = newMap
                                                    }
                                                }) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                                }
                                                Text(count.toString(), color = orangeMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                                IconButton(onClick = {
                                                    val newMap = boissonsSelectionnees.toMutableMap()
                                                    newMap[boisson.nom] = count + 1
                                                    boissonsSelectionnees = newMap
                                                }) {
                                                    Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = jauneMenu)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Code existant pour les plats
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
                                                val newMap = platsSelectionnes.toMutableMap()
                                                newMap[plat.nom] = count - 1
                                                platsSelectionnes = newMap
                                            }
                                        }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(count.toString(), color = orangeMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                        IconButton(
                                            enabled = peutAjouterPlat || plat.nom.lowercase() == "cervelle" || plat.nom.lowercase() == "st marcelin",
                                            onClick = {
                                                if (peutAjouterPlat || plat.nom.lowercase() == "cervelle" || plat.nom.lowercase() == "st marcelin") {
                                                    val newMap = platsSelectionnes.toMutableMap()
                                                    newMap[plat.nom] = count + 1
                                                    platsSelectionnes = newMap
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Ajouter",
                                                tint = if (peutAjouterPlat || plat.nom.lowercase() == "cervelle" || plat.nom.lowercase() == "st marcelin") jauneMenu else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Texte d'aide pour les règles de validation
            if (initDone) {
                Text(
                    text = when {
                        !aDesPlats && !aDesBoissons -> "Sélectionnez au moins des plats ou des boissons"
                        aDesPlats && totalPlatsHorsCervelle != nbCouvertsInt ->
                            "Règle: 1 plat = 1 couvert (${totalPlatsHorsCervelle}/${nbCouvertsInt}). Cervelle et St Marcelin illimités."
                        !aDesPlats && aDesBoissons -> "Boissons seules: validation possible ✓"
                        else -> "Prêt à valider ✓"
                    },
                    color = when {
                        !aDesPlats && !aDesBoissons -> Color.Gray
                        aDesPlats && totalPlatsHorsCervelle != nbCouvertsInt -> Color(0xFFFF6B6B)
                        else -> jauneMenu
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    val newCommande = Commande(
                        numeroTable = numeroTable,
                        nombreCouverts = nbCouvertsInt,
                        plats = platsSelectionnes.filter { it.value > 0 }.map { (nom, quantite) ->
                            val platConfig = platsData.find { it.nom == nom }
                            Plat(
                                nom = nom,
                                quantite = quantite,
                                contientRavigote = platConfig?.contientRavigote ?: false
                            )
                        },
                        boissons = boissonsSelectionnees.filter { it.value > 0 }.map { (nom, quantite) ->
                            val config = boissonsList.find { it.nom == nom }
                            Boisson(
                                nom = nom,
                                quantite = quantite,
                                categorie = config?.categorie ?: CategorieBoisson.SOFTS
                            )
                        },
                        remarque = remarqueText.takeIf { it.isNotBlank() },
                        isGroupe = isGroupe
                    )

                    // Impression si demandée (non bloquant !)
                    if (shouldPrint && isPrinterEnabled && printerName.isNotEmpty()) {
                        printerViewModel.printCommande(newCommande, context)
                    }

                    commandeViewModel.validerCommande(newCommande)
                    onNext(newCommande)
                },
                enabled = boutonValiderActif && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (boutonValiderActif) jauneMenu else Color.Gray,
                    contentColor = Color.Black
                )
            ) {
                if (isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Impression...", fontWeight = FontWeight.Bold)
                    }
                } else {
                    val buttonText = when {
                        !aDesPlats && aDesBoissons -> "Valider (boissons uniquement)"
                        aDesPlats && aDesBoissons -> "Valider (plats + boissons)"
                        aDesPlats && !aDesBoissons -> "Valider (plats uniquement)"
                        else -> "Valider la sélection"
                    }
                    Text(buttonText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Dialog pour les remarques
    if (showRemarqueDialog) {
        AlertDialog(
            onDismissRequest = { showRemarqueDialog = false },
            title = {
                Text("Remarque pour la table $numeroTable", color = jauneMenu)
            },
            text = {
                OutlinedTextField(
                    value = remarqueText,
                    onValueChange = { remarqueText = it },
                    label = { Text("Votre remarque") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = jauneMenu,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = jauneMenu,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = jauneMenu
                    ),
                    maxLines = 3
                )
            },
            confirmButton = {
                Button(
                    onClick = { showRemarqueDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = jauneMenu,
                        contentColor = Color.Black
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemarqueDialog = false }) {
                    Text("Annuler", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF292929),
            titleContentColor = jauneMenu,
            textContentColor = Color.White
        )
    }
}