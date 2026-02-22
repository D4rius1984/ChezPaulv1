package com.chezpaul.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.*
import com.chezpaul.model.MenuConstants
import com.chezpaul.ui.components.formatPrix
import com.chezpaul.ui.theme.ChezPaulColors
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
    val jauneMenu = ChezPaulColors.JauneMenu
    val orangeMenu = ChezPaulColors.OrangeMenu
    val commandeViewModel: CommandeViewModel = viewModel()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Observer l'état de l'imprimante
    val isPrinterEnabled by printerViewModel.isPrinterEnabled
    val printerName by printerViewModel.printerName
    val isLoading by printerViewModel.isLoading

    var initDone by remember { mutableStateOf(commande != null) }

    var numeroTable by remember { mutableStateOf(commande?.numeroTable ?: "") }
    var couverts by remember { mutableStateOf(commande?.nombreCouverts?.toString() ?: "") }
    var isGroupe by remember { mutableStateOf(commande?.isGroupe ?: false) }
    var prixGroupeSelectionne by remember { mutableStateOf(commande?.prixMenuGroupe) }
    var hasMenuEnfant by remember { mutableStateOf((commande?.menusEnfants ?: 0) > 0) }
    var nbMenusEnfants by remember { mutableIntStateOf(commande?.menusEnfants ?: 0) }
    var shouldPrint by remember { mutableStateOf(false) } // Switch pour l'impression

    var selectedTab by remember { mutableIntStateOf(1) }
    val tabTitles = remember { listOf("Plats", "Boissons") }
    var expandedCategories by rememberSaveable { mutableStateOf(setOf<String>()) }
    var remarqueText by remember { mutableStateOf(commande?.remarque ?: "") }
    var showRemarqueDialog by remember { mutableStateOf(false) }
    var platsSelectionnes by remember { mutableStateOf<Map<String, Int>>(mutableMapOf()) }
    var boissonsSelectionnees by remember { mutableStateOf<Map<String, Int>>(mutableMapOf()) }
    var boissonSearchQuery by remember { mutableStateOf("") }

    // Initialiser la sélection si commande existante
    LaunchedEffect(commande) {
        if (commande != null) {
            numeroTable = commande.numeroTable
            couverts = commande.nombreCouverts.toString()
            isGroupe = commande.isGroupe
            prixGroupeSelectionne = commande.prixMenuGroupe
            hasMenuEnfant = commande.menusEnfants > 0
            nbMenusEnfants = commande.menusEnfants
            remarqueText = commande.remarque ?: ""
            platsSelectionnes = commande.plats.associate { plat -> plat.nom to plat.quantite }
            boissonsSelectionnees = commande.boissons.associate { boisson -> boisson.nom to boisson.quantite }
            initDone = true
        }
    }

    val hasRavigote by remember {
        derivedStateOf {
            platsSelectionnes.any { (nom, quantite) ->
                val plat = platsData.find { it.nom == nom }
                plat?.contientRavigote == true && quantite > 0
            }
        }
    }

    val totalPlatsHorsCervelle by remember {
        derivedStateOf {
            platsSelectionnes.entries
                .filter { (nom, _) ->
                    nom.lowercase() !in MenuConstants.PLATS_ILLIMITES
                }
                .sumOf { it.value }
        }
    }

    val nbCouvertsInt by remember {
        derivedStateOf { couverts.toIntOrNull() ?: 0 }
    }

    val peutAjouterPlat by remember {
        derivedStateOf { totalPlatsHorsCervelle < nbCouvertsInt }
    }

    val aDesPlats by remember {
        derivedStateOf { platsSelectionnes.any { it.value > 0 } }
    }
    val aDesBoissons by remember {
        derivedStateOf { boissonsSelectionnees.any { it.value > 0 } }
    }
    val platsRespectentRegle by remember {
        derivedStateOf { totalPlatsHorsCervelle == nbCouvertsInt }
    }

    val boutonValiderActif by remember {
        derivedStateOf {
            numeroTable.isNotBlank() && (
                (!aDesPlats && aDesBoissons) ||
                    (aDesPlats && platsRespectentRegle) ||
                    (aDesPlats && aDesBoissons && platsRespectentRegle)
                )
        }
    }

    val platsFiltres by remember {
        derivedStateOf {
            platsData.filter { plat ->
                val isActivated = platsActivationState[plat.nom] ?: true
                val isAvailableForCurrentType = if (isGroupe) plat.isGroupe else plat.isNonGroupe
                isActivated && isAvailableForCurrentType
            }
        }
    }
    val boissonsFiltres by remember {
        derivedStateOf {
            boissonsList.filter { boisson ->
                val isActivated = boissonsActivationState[boisson.nom] ?: true
                val isAvailableForCurrentType = if (isGroupe) boisson.isGroupe else boisson.isNonGroupe
                isActivated && isAvailableForCurrentType
            }
        }
    }
    val boissonsFiltresPourRecherche by remember {
        derivedStateOf {
            if (boissonSearchQuery.isBlank()) boissonsFiltres
            else boissonsFiltres.filter { it.nom.lowercase().contains(boissonSearchQuery.trim().lowercase()) }
        }
    }
    val effectiveExpandedCategories by remember {
        derivedStateOf {
            if (boissonSearchQuery.isBlank()) expandedCategories
            else boissonsFiltresPourRecherche.map { it.categorie.name }.toSet()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChezPaulColors.FondPrincipal)
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
                color = ChezPaulColors.FondCard,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = numeroTable,
                        onValueChange = {
                            if (it != numeroTable) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            numeroTable = it
                        },
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
                        onValueChange = {
                            if (it != couverts) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            couverts = it
                        },
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
                        Text("Table de Groupe", color = jauneMenu, modifier = Modifier.weight(1f))
                        Switch(
                            checked = isGroupe,
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isGroupe = it
                                if (!it) prixGroupeSelectionne = null
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = jauneMenu,
                                checkedTrackColor = jauneMenu.copy(alpha = 0.5f)
                            )
                        )
                    }

                    // Prix Groupe - chips de sélection
                    if (isGroupe) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Prix menu groupe :",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            MenuConstants.PRIX_GROUPE_OPTIONS.forEach { prix ->
                                val isSelected = prixGroupeSelectionne == prix
                                SuggestionChip(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        prixGroupeSelectionne = prix
                                    },
                                    label = {
                                        Text(
                                            prix.formatPrix(),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.Black else jauneMenu
                                        )
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = if (isSelected) jauneMenu else Color.Transparent
                                    ),
                                    border = SuggestionChipDefaults.suggestionChipBorder(
                                        enabled = true,
                                        borderColor = jauneMenu.copy(alpha = if (isSelected) 1f else 0.5f)
                                    )
                                )
                            }
                        }
                        if (prixGroupeSelectionne != null) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Boissons incluses dans le prix groupe",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Switch Menus enfants (masqué si groupe)
                    if (!isGroupe) {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Menus enfants (17€)", color = jauneMenu, modifier = Modifier.weight(1f))
                            Switch(
                                checked = hasMenuEnfant,
                                onCheckedChange = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    hasMenuEnfant = it
                                    if (!it) nbMenusEnfants = 0
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = jauneMenu,
                                    checkedTrackColor = jauneMenu.copy(alpha = 0.5f)
                                )
                            )
                        }
                        if (hasMenuEnfant) {
                            val maxEnfants = couverts.toIntOrNull() ?: 0
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = {
                                    if (nbMenusEnfants > 0) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        nbMenusEnfants--
                                    }
                                }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Moins", tint = jauneMenu)
                                }
                                Text(
                                    "$nbMenusEnfants",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                IconButton(onClick = {
                                    if (nbMenusEnfants < maxEnfants) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        nbMenusEnfants++
                                    }
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Plus", tint = jauneMenu)
                                }
                            }
                        }
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
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                shouldPrint = it
                            },
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
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            initDone = true
                        },
                        enabled = numeroTable.isNotBlank() && couverts.isNotBlank() && (!isGroupe || prixGroupeSelectionne != null),
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
                color = ChezPaulColors.FondCard,
                tonalElevation = 8.dp,
                modifier = Modifier.weight(1f)
            ) {
                Column(Modifier.padding(16.dp)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .fillMaxWidth()
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
                        // Badge Prix Groupe
                        if (isGroupe && prixGroupeSelectionne != null) {
                            Surface(
                                color = orangeMenu.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    "Groupe ${prixGroupeSelectionne!!.formatPrix()}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = orangeMenu,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                        // Badge Enfants
                        val effectiveEnfants = if (hasMenuEnfant) nbMenusEnfants else 0
                        if (effectiveEnfants > 0) {
                            Surface(
                                color = orangeMenu.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    "$effectiveEnfants enfant${if (effectiveEnfants > 1) "s" else ""}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = orangeMenu,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                        // Badge Remarque cliquable
                        Surface(
                            color = jauneMenu.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showRemarqueDialog = true
                            }
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
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (index == 0) boissonSearchQuery = ""
                                    selectedTab = index
                                },
                                text = { Text(title, color = if (selectedTab == index) orangeMenu else Color.White) }
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    LazyColumn {
                        if (selectedTab == 1) {
                            // Search bar
                            item(key = "boisson_search") {
                                OutlinedTextField(
                                    value = boissonSearchQuery,
                                    onValueChange = { boissonSearchQuery = it },
                                    label = { Text("Rechercher une boisson") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Search, contentDescription = null, tint = orangeMenu)
                                    },
                                    trailingIcon = {
                                        if (boissonSearchQuery.isNotEmpty()) {
                                            IconButton(onClick = { boissonSearchQuery = "" }) {
                                                Icon(Icons.Default.Clear, contentDescription = "Effacer", tint = Color.Gray)
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = orangeMenu,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedLabelColor = orangeMenu,
                                        unfocusedLabelColor = Color.Gray,
                                        cursorColor = orangeMenu
                                    )
                                )
                            }

                            if (boissonsFiltresPourRecherche.isEmpty() && boissonSearchQuery.isNotBlank()) {
                                item(key = "no_results") {
                                    Text(
                                        "Aucune boisson trouvée",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }

                            // Grouper les boissons par catégorie
                            val boissonsByCategory = boissonsFiltresPourRecherche.groupBy { it.categorie }

                            boissonsByCategory.forEach { (categorie, boissonsDeCategorie) ->
                                val catKey = categorie.name
                                val isExpanded = catKey in effectiveExpandedCategories
                                val selectedCount = boissonsDeCategorie.sumOf { boissonsSelectionnees[it.nom] ?: 0 }

                                // Header de catégorie (cliquable, avec chevron)
                                item(key = "header_$catKey") {
                                    val rotation by animateFloatAsState(
                                        targetValue = if (isExpanded) 180f else 0f,
                                        label = "chevron"
                                    )
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        color = ChezPaulColors.FondCard,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            expandedCategories = if (isExpanded) {
                                                expandedCategories - catKey
                                            } else {
                                                expandedCategories + catKey
                                            }
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = categorie.displayName,
                                                color = orangeMenu,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleSmall,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (selectedCount > 0) {
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = orangeMenu
                                                ) {
                                                    Text(
                                                        text = "$selectedCount",
                                                        color = ChezPaulColors.TexteNoir,
                                                        fontWeight = FontWeight.Bold,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Spacer(Modifier.width(8.dp))
                                            }
                                            Icon(
                                                Icons.Default.KeyboardArrowDown,
                                                contentDescription = if (isExpanded) "Réduire" else "Développer",
                                                tint = orangeMenu,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .rotate(rotation)
                                            )
                                        }
                                    }
                                }

                                // Contenu de la catégorie
                                if (isExpanded) {
                                    val parSousCategorie = boissonsDeCategorie.groupBy { it.sousCategorie }

                                    parSousCategorie.forEach { (sousCat, boissons) ->
                                        if (sousCat != null) {
                                            item(key = "subheader_${catKey}_$sousCat") {
                                                Text(
                                                    text = sousCat,
                                                    color = jauneMenu.copy(alpha = 0.7f),
                                                    fontWeight = FontWeight.SemiBold,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 2.dp)
                                                )
                                            }
                                        }

                                        items(boissons, key = { it.nom }) { boisson ->
                                            val count = boissonsSelectionnees[boisson.nom] ?: 0
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 8.dp)
                                                    .padding(vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(boisson.nom, color = Color.White, modifier = Modifier.weight(1f))
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    IconButton(onClick = {
                                                        if (count > 0) {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            val newMap = boissonsSelectionnees.toMutableMap()
                                                            newMap[boisson.nom] = count - 1
                                                            boissonsSelectionnees = newMap
                                                        }
                                                    }) {
                                                        Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                                    }
                                                    Text(count.toString(), color = orangeMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                                    IconButton(onClick = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
                            }
                        } else {
                            // Code existant pour les plats
                            items(platsFiltres, key = { it.nom }) { plat ->
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
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                val newMap = platsSelectionnes.toMutableMap()
                                                newMap[plat.nom] = count - 1
                                                platsSelectionnes = newMap
                                            }
                                        }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                        }
                                        Text(count.toString(), color = orangeMenu, modifier = Modifier.padding(horizontal = 8.dp))
                                        IconButton(
                                            enabled = peutAjouterPlat || plat.nom.lowercase() in MenuConstants.PLATS_ILLIMITES,
                                            onClick = {
                                                if (peutAjouterPlat || plat.nom.lowercase() in MenuConstants.PLATS_ILLIMITES) {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    val newMap = platsSelectionnes.toMutableMap()
                                                    newMap[plat.nom] = count + 1
                                                    platsSelectionnes = newMap
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Ajouter",
                                                tint = if (peutAjouterPlat || plat.nom.lowercase() in MenuConstants.PLATS_ILLIMITES) jauneMenu else Color.Gray
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
                        aDesPlats && totalPlatsHorsCervelle != nbCouvertsInt -> ChezPaulColors.RougeErreur
                        else -> jauneMenu
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
                                categorie = config?.categorie ?: CategorieBoisson.SOFTS,
                                prix = config?.prix ?: 0.0
                            )
                        },
                        remarque = remarqueText.takeIf { it.isNotBlank() },
                        isGroupe = isGroupe,
                        prixMenuGroupe = if (isGroupe) prixGroupeSelectionne else null,
                        menusEnfants = if (!isGroupe && hasMenuEnfant) nbMenusEnfants else 0
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
                val suggestions = remember { MenuConstants.SUGGESTIONS_REMARQUE }
                Column {
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
                    Spacer(Modifier.height(12.dp))
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        suggestions.forEach { mot ->
                            SuggestionChip(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    remarqueText = if (remarqueText.isBlank()) mot
                                    else "$remarqueText, $mot"
                                },
                                label = {
                                    Text(
                                        text = mot,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    enabled = true,
                                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
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
            containerColor = ChezPaulColors.FondCard,
            titleContentColor = jauneMenu,
            textContentColor = Color.White
        )
    }
}