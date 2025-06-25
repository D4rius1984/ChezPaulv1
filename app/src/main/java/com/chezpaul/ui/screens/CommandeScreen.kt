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
    var infosConfirmees by remember { mutableStateOf(false) }

    // PLATS DATA
    data class PlatConfig(val nom: String, val abrv: String, val contientRavigote: Boolean)
    val platsData = listOf(
        PlatConfig("Tablier", "Tablier", false),
        PlatConfig("Tete de veau", "Tdv", true),
        PlatConfig("Saucisson", "Saucisson", false),
        PlatConfig("Civet", "Civet", false),
        PlatConfig("Quenelle", "Quenelle", false),
        PlatConfig("Langue de boeuf ravigote", "Ldb ravigote", true),
        PlatConfig("Langue de boeuf piquante", "Ldb piquante", false),
        PlatConfig("Andouillette", "Andouillette", false),
        PlatConfig("Cote cochon", "Cote cochon", false),
        PlatConfig("Blanquette", "Blanquette", false),
        PlatConfig("Poulet", "Poulet", false),
        PlatConfig("Boudin", "Boudin", false),
        PlatConfig("Piece du B", "Piece du B", false)
    )
    var platsSelectionnes by remember {
        mutableStateOf(
            buildMap {
                platsData.forEach { put(it.abrv, 0) }
                commande?.plats?.forEach { plat ->
                    platsData.find { it.nom == plat.nom }?.let { put(it.abrv, plat.quantite) }
                }
            }.toMutableMap()
        )
    }

    // BOISSONS DATA
    data class BoissonConfig(val nom: String, val categorie: CategorieBoisson)
    val boissonsList = listOf(
        // Apéros
        BoissonConfig("Kir", CategorieBoisson.APEROS),
        BoissonConfig("Communard", CategorieBoisson.APEROS),
        BoissonConfig("Ricard", CategorieBoisson.APEROS),
        // Digestifs
        BoissonConfig("Chartreuse jaune", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Chartreuse verte", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Genepi", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Verveine", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Menthe", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Poire", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Mandarine", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Abricot", CategorieBoisson.DIGESTIFS),
        BoissonConfig("Marc", CategorieBoisson.DIGESTIFS),
        // Bières
        BoissonConfig("Blonde", CategorieBoisson.BIERES),
        BoissonConfig("Blanche", CategorieBoisson.BIERES),
        BoissonConfig("Sans alcool", CategorieBoisson.BIERES),
        BoissonConfig("Ambree", CategorieBoisson.BIERES),
        BoissonConfig("IPA", CategorieBoisson.BIERES),
        BoissonConfig("Speciale", CategorieBoisson.BIERES),
        // Softs
        BoissonConfig("Sirop", CategorieBoisson.SOFTS),
        BoissonConfig("Limonade", CategorieBoisson.SOFTS),
        BoissonConfig("Ice Tea", CategorieBoisson.SOFTS),
        BoissonConfig("Biere sans alcool", CategorieBoisson.SOFTS),
        BoissonConfig("Orange", CategorieBoisson.SOFTS),
        BoissonConfig("ACE", CategorieBoisson.SOFTS),
        BoissonConfig("Fraise", CategorieBoisson.SOFTS),
        BoissonConfig("Eau", CategorieBoisson.SOFTS),
        // Vins directs
        BoissonConfig("Montagnieu BTL", CategorieBoisson.VINS),
        BoissonConfig("CDR BTL", CategorieBoisson.VINS),
        // Vins sous-catégories
        BoissonConfig("Brouilly Pot", CategorieBoisson.VINS),
        BoissonConfig("Brouilly Filette", CategorieBoisson.VINS),
        BoissonConfig("Brouilly Verre", CategorieBoisson.VINS),
        BoissonConfig("CDR Pot", CategorieBoisson.VINS),
        BoissonConfig("CDR Filette", CategorieBoisson.VINS),
        BoissonConfig("CDR Verre", CategorieBoisson.VINS),
        BoissonConfig("Blanc Pot", CategorieBoisson.VINS),
        BoissonConfig("Blanc Filette", CategorieBoisson.VINS),
        BoissonConfig("Blanc Verre", CategorieBoisson.VINS),
        BoissonConfig("Rose Pot", CategorieBoisson.VINS),
        BoissonConfig("Rose Filette", CategorieBoisson.VINS),
        BoissonConfig("Rose Verre", CategorieBoisson.VINS)
    )
    var boissonsSelectionnees by remember {
        mutableStateOf(
            buildMap {
                boissonsList.forEach { put(it.nom, 0) }
                commande?.boissons?.forEach { boisson ->
                    put(boisson.nom, boisson.quantite)
                }
            }.toMutableMap()
        )
    }

    val ravigoteVisible = (platsSelectionnes["Tdv"] ?: 0) > 0 || (platsSelectionnes["Ldb ravigote"] ?: 0) > 0
    val totalPlats = platsSelectionnes.values.sum()
    val nbCouverts = couverts.toIntOrNull() ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(16.dp)
    ) {
        Text(
            "Commande",
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
                                "Table $numeroTable  ${couverts} cv",
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
                                                if ((platsSelectionnes[nomPlat] ?: 0) > 0) {
                                                    platsSelectionnes = platsSelectionnes.toMutableMap().also {
                                                        it[nomPlat] = (it[nomPlat] ?: 0) - 1
                                                    }
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
                                val platsCommandes = platsData.mapNotNull { config ->
                                    val qte = platsSelectionnes[config.abrv] ?: 0
                                    if (qte > 0) Plat(config.nom, qte, config.contientRavigote) else null
                                }
                                val boissonsCommandes = boissonsList.mapNotNull { config ->
                                    val qte = boissonsSelectionnees[config.nom] ?: 0
                                    if (qte > 0) Boisson(config.nom, qte, config.categorie) else null
                                }
                                onNext(
                                    Commande(
                                        numeroTable = numeroTable,
                                        nombreCouverts = nbCouverts,
                                        plats = platsCommandes,
                                        boissons = boissonsCommandes
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
                        Text("Boissons :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(6.dp))
                        val categoriesOrdre = listOf(
                            CategorieBoisson.APEROS,
                            CategorieBoisson.BIERES,
                            CategorieBoisson.SOFTS,
                            CategorieBoisson.DIGESTIFS,
                            CategorieBoisson.VINS
                        )
                        val boissonsParCategorie = categoriesOrdre.associateWith { cat ->
                            boissonsList.filter { it.categorie == cat }
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
                                                            boissonsSelectionnees = boissonsSelectionnees.toMutableMap().also {
                                                                it[nomBoisson] = (it[nomBoisson] ?: 0) - 1
                                                            }
                                                        }
                                                    }
                                                ) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Retirer", tint = jauneMenu)
                                                }
                                                Text(quantite.toString(), color = jauneMenu, modifier = Modifier.padding(horizontal = 8.dp))
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
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val platsCommandes = platsData.mapNotNull { config ->
                                    val qte = platsSelectionnes[config.abrv] ?: 0
                                    if (qte > 0) Plat(config.nom, qte, config.contientRavigote) else null
                                }
                                val boissonsCommandes = boissonsList.mapNotNull { config ->
                                    val qte = boissonsSelectionnees[config.nom] ?: 0
                                    if (qte > 0) Boisson(config.nom, qte, config.categorie) else null
                                }
                                onNext(
                                    Commande(
                                        numeroTable = numeroTable,
                                        nombreCouverts = nbCouverts,
                                        plats = platsCommandes,
                                        boissons = boissonsCommandes
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
