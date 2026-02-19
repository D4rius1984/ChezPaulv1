package com.chezpaul.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.chezpaul.ui.components.*
import com.chezpaul.ui.theme.ChezPaulColors
import com.chezpaul.viewmodel.SettingsViewModel
import com.chezpaul.viewmodel.PrinterViewModel
import com.chezpaul.viewmodel.CommandeViewModel
import com.chezpaul.viewmodel.PlatViewModel
import com.chezpaul.viewmodel.BoissonViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    printerViewModel: PrinterViewModel,
    commandeViewModel: CommandeViewModel? = null,
    platViewModel: PlatViewModel? = null,
    boissonViewModel: BoissonViewModel? = null,
    onHistoryClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val isPrinterEnabled by printerViewModel.isPrinterEnabled
    val printerName by printerViewModel.printerName
    val printerIP by printerViewModel.printerIP
    val connectionStatus by printerViewModel.connectionStatus
    val lastPingResult by printerViewModel.lastPingResult
    val debugText by printerViewModel.debugText
    val isLoading by printerViewModel.isLoading
    val isScanning by printerViewModel.isScanning
    val foundPrinters by printerViewModel.foundPrinters
    val scanProgress by printerViewModel.scanProgress

    ChezPaulScreen(title = "Paramètres") {
        // Section Service
        ChezPaulCard {
            Text(
                "Service",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = ChezPaulColors.JauneMenu.copy(alpha = 0.3f))
                    ) {
                        settingsViewModel.closeService(
                            context = context,
                            commandeViewModel = commandeViewModel,
                            platViewModel = platViewModel,
                            boissonViewModel = boissonViewModel,
                            printerViewModel = printerViewModel
                        )
                    }
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    "Fin de service",
                    color = ChezPaulColors.TexteBlanc,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Clôture le service et réinitialise toutes les données",
                    color = ChezPaulColors.TexteGris,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = ChezPaulColors.JauneMenu.copy(alpha = 0.3f))
                    ) {
                        onHistoryClick()
                    }
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    "Historique des services",
                    color = ChezPaulColors.TexteBlanc,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Consulter et gérer les archives des services passés",
                    color = ChezPaulColors.TexteGris,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Prix du Menu
        ChezPaulCard {
            Text(
                "Prix du Menu",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Actuel : ${settingsViewModel.getMenuModeLabel()}",
                color = ChezPaulColors.TexteGris,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            val menuMode by settingsViewModel.menuModeOverride
            val options = listOf(
                Triple("AUTO", "Auto (selon l'heure)", "Midi 24€ / Soir-WE 32€"),
                Triple("FORCE_MIDI", "Forcer Midi (24€)", "Lun-Ven déjeuner"),
                Triple("FORCE_SOIR", "Forcer Soir/WE (32€)", "Soir, samedi, dimanche")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { (value, label, description) ->
                    val isSelected = menuMode == value
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) ChezPaulColors.JauneMenu.copy(alpha = 0.15f) else Color.Transparent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                settingsViewModel.menuModeOverride.value = value
                                commandeViewModel?.menuPrice?.value = settingsViewModel.getMenuPrice()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    settingsViewModel.menuModeOverride.value = value
                                    commandeViewModel?.menuPrice?.value = settingsViewModel.getMenuPrice()
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = ChezPaulColors.JauneMenu,
                                    unselectedColor = ChezPaulColors.TexteGris
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    label,
                                    color = if (isSelected) ChezPaulColors.JauneMenu else ChezPaulColors.TexteBlanc,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    description,
                                    color = ChezPaulColors.TexteGris,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Données
        if (commandeViewModel != null) {
            ChezPaulCard {
                Text(
                    "Données",
                    color = ChezPaulColors.JauneMenu,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                val importResult by commandeViewModel.importResult

                // Show import result toast
                LaunchedEffect(importResult) {
                    importResult?.let { result ->
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                        commandeViewModel.clearImportResult()
                    }
                }

                val csvPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { uri ->
                    uri?.let { commandeViewModel.importCsv(it) }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = ChezPaulColors.JauneMenu.copy(alpha = 0.3f))
                        ) {
                            csvPickerLauncher.launch(arrayOf("text/csv", "text/comma-separated-values", "*/*"))
                        }
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        "Importer CSV",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Restaurer un historique depuis un fichier CSV exporté",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Section Imprimante
        ChezPaulCard {
            Text(
                "Imprimante réseau",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Activation de l'imprimante
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Activer l'imprimante",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Connexion réseau vers imprimante thermique",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = isPrinterEnabled,
                    onCheckedChange = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        printerViewModel.isPrinterEnabled.value = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ChezPaulColors.JauneMenu,
                        uncheckedThumbColor = ChezPaulColors.TexteGris,
                        checkedTrackColor = ChezPaulColors.OrangeMenu.copy(alpha = 0.5f),
                        uncheckedTrackColor = ChezPaulColors.FondCard
                    )
                )
            }

            if (isPrinterEnabled) {
                Spacer(modifier = Modifier.height(16.dp))

                // Configuration de l'imprimante
                Column {
                    Text(
                        "Configuration",
                        color = ChezPaulColors.JauneMenu,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    var tempPrinterName by remember { mutableStateOf(printerName) }
                    var tempPrinterIP by remember { mutableStateOf(printerIP) }

                    OutlinedTextField(
                        value = tempPrinterName,
                        onValueChange = { tempPrinterName = it },
                        label = { Text("Nom de l'imprimante", color = ChezPaulColors.TexteGris) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChezPaulColors.JauneMenu,
                            unfocusedBorderColor = ChezPaulColors.TexteGris,
                            focusedTextColor = ChezPaulColors.TexteBlanc,
                            unfocusedTextColor = ChezPaulColors.TexteBlanc
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && !isScanning
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = tempPrinterIP,
                        onValueChange = { tempPrinterIP = it },
                        label = { Text("Adresse IP", color = ChezPaulColors.TexteGris) },
                        placeholder = { Text("192.168.1.100", color = ChezPaulColors.TexteGris) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChezPaulColors.JauneMenu,
                            unfocusedBorderColor = ChezPaulColors.TexteGris,
                            focusedTextColor = ChezPaulColors.TexteBlanc,
                            unfocusedTextColor = ChezPaulColors.TexteBlanc
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && !isScanning
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            printerViewModel.addPrinter(tempPrinterName, tempPrinterIP)
                        },
                        enabled = !isLoading && !isScanning && tempPrinterName.isNotEmpty() && tempPrinterIP.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChezPaulColors.JauneMenu,
                            contentColor = ChezPaulColors.FondCard
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Configurer l'imprimante")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bouton scan réseau
                    Button(
                        onClick = {
                            printerViewModel.scanNetworkForPrinters(context)
                        },
                        enabled = !isLoading && !isScanning,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChezPaulColors.OrangeMenu,
                            contentColor = ChezPaulColors.TexteBlanc
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (isScanning) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = ChezPaulColors.TexteBlanc,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Scan en cours... ${scanProgress}%")
                            } else {
                                Text("🔍 Scanner le réseau")
                            }
                        }
                    }

                    // Affichage des imprimantes trouvées
                    if (foundPrinters.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Imprimantes trouvées:",
                                color = ChezPaulColors.JauneMenu,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(
                                onClick = { printerViewModel.clearScanResults() }
                            ) {
                                Text(
                                    "Effacer",
                                    color = ChezPaulColors.TexteGris,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        foundPrinters.forEach { printer ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        printerViewModel.selectPrinter(printer)
                                        tempPrinterIP = printer.ip
                                        tempPrinterName = printer.name
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = ChezPaulColors.FondCard.copy(alpha = 0.7f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            printer.ip,
                                            color = ChezPaulColors.TexteBlanc,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "Port ${printer.port}",
                                            color = ChezPaulColors.TexteGris,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Text(
                                        "Temps de réponse: ${printer.responseTime}",
                                        color = ChezPaulColors.TexteGris,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }

                    if (printerIP.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Statut de connexion
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Statut:",
                                color = ChezPaulColors.TexteGris,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = ChezPaulColors.JauneMenu,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    if (isLoading) "Test en cours..." else connectionStatus,
                                    color = ChezPaulColors.JauneMenu,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Boutons de test
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { printerViewModel.pingPrinter(context) },
                                enabled = !isLoading && !isScanning,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ChezPaulColors.OrangeMenu,
                                    contentColor = ChezPaulColors.TexteBlanc
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Ping")
                            }

                            Button(
                                onClick = { printerViewModel.testPrinterConnection(context) },
                                enabled = !isLoading && !isScanning,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ChezPaulColors.JauneMenu,
                                    contentColor = ChezPaulColors.FondCard
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Test TCP")
                            }

                            Button(
                                onClick = { printerViewModel.resetPrinter() },
                                enabled = !isLoading && !isScanning,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ChezPaulColors.TexteGris,
                                    contentColor = ChezPaulColors.TexteBlanc
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reset")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Test d'envoi de texte
                        var tempDebugText by remember { mutableStateOf(debugText) }

                        OutlinedTextField(
                            value = tempDebugText,
                            onValueChange = { tempDebugText = it },
                            label = { Text("Texte de test", color = ChezPaulColors.TexteGris) },
                            placeholder = { Text("Test d'impression...", color = ChezPaulColors.TexteGris) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChezPaulColors.JauneMenu,
                                unfocusedBorderColor = ChezPaulColors.TexteGris,
                                focusedTextColor = ChezPaulColors.TexteBlanc,
                                unfocusedTextColor = ChezPaulColors.TexteBlanc
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading && !isScanning
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                printerViewModel.sendDebugText(tempDebugText, context)
                                printerViewModel.debugText.value = tempDebugText
                            },
                            enabled = !isLoading && !isScanning && tempDebugText.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ChezPaulColors.JauneMenu,
                                contentColor = ChezPaulColors.FondCard
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Envoyer vers imprimante")
                        }

                        if (lastPingResult.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Résultat: $lastPingResult",
                                color = ChezPaulColors.TexteGris,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}