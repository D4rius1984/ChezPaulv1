package com.chezpaul.viewmodel

import android.content.Context
import android.net.wifi.WifiManager
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.*
import com.chezpaul.model.Commande

/**
 * Gère la connexion et l'impression vers une imprimante thermique ESC/POS via TCP (port 9100).
 * Supporte le scan réseau, le ping, et l'impression formatée des commandes.
 */
class PrinterViewModel : ViewModel() {
    var isPrinterEnabled = mutableStateOf(false)
    var printerIP = mutableStateOf("")
    var printerName = mutableStateOf("")
    var connectionStatus = mutableStateOf("Déconnecté")
    var debugText = mutableStateOf("")
    var lastPingResult = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isScanning = mutableStateOf(false)
    var foundPrinters = mutableStateOf<List<FoundPrinter>>(emptyList())
    var scanProgress = mutableStateOf(0)

    fun addPrinter(name: String, ip: String) {
        if (isValidIP(ip)) {
            printerName.value = name
            printerIP.value = ip
            connectionStatus.value = "Configuré"
            lastPingResult.value = ""
        } else {
            lastPingResult.value = "Adresse IP invalide"
        }
    }

    private fun isValidIP(ip: String): Boolean {
        return try {
            val parts = ip.split(".")
            if (parts.size != 4) return false
            parts.all { part ->
                val num = part.toIntOrNull()
                num != null && num in 0..255
            }
        } catch (e: Exception) {
            false
        }
    }

    /** Formate et envoie une commande à l'imprimante configurée (non bloquant). */
    fun printCommande(commande: Commande, context: Context) {
        if (!isPrinterEnabled.value || printerIP.value.isEmpty()) {
            Toast.makeText(context, "Imprimante non configurée", Toast.LENGTH_SHORT).show()
            return
        }

        val ticketText = formatTicket(commande)

        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    sendTextToPrinter(printerIP.value, ticketText)
                }

                if (result.success) {
                    Toast.makeText(context, "✅ Impression réussie !", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "❌ Impression impossible", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(context, "❌ Impression impossible", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading.value = false
            }
        }
    }

    // NOUVELLE MÉTHODE - Formatage du ticket
    private fun formatTicket(commande: Commande): String {
        val builder = StringBuilder()

        // Header
        builder.append("\n")
        builder.append("TABLE ${commande.numeroTable} - ${commande.nombreCouverts} COUVERTS")
        if (commande.isGroupe) {
            builder.append(" \"GROUPE\"")
        }
        builder.append("\n\n")

        // Plats
        if (commande.plats.isNotEmpty()) {
            builder.append("PLATS :\n")
            commande.plats.forEach { plat ->
                builder.append("${plat.quantite} ${plat.nom}")
                if (plat.contientRavigote) {
                    builder.append(" (RAVIGOTE)")
                }
                builder.append("\n")
            }
            builder.append("\n")
        }

        // Boissons
        if (commande.boissons.isNotEmpty()) {
            builder.append("BOISSONS :\n")
            commande.boissons.forEach { boisson ->
                builder.append("${boisson.quantite} ${boisson.nom}\n")
            }
            builder.append("\n")
        }

        // Remarque
        if (!commande.remarque.isNullOrBlank()) {
            builder.append("REMARQUE : ${commande.remarque}\n\n")
        }

        // Footer
        builder.append("****************MERCI LA CUISINE <3***************\n\n\n")

        return builder.toString()
    }

    fun pingPrinter(context: Context? = null) {
        if (printerIP.value.isEmpty()) {
            lastPingResult.value = "Aucune IP configurée"
            context?.let {
                Toast.makeText(it, "Aucune IP configurée", Toast.LENGTH_SHORT).show()
            }
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    pingHost(printerIP.value)
                }

                lastPingResult.value = result.message
                connectionStatus.value = if (result.success) "Connecté" else "Erreur de connexion"

                context?.let {
                    Toast.makeText(
                        it,
                        if (result.success) "Ping réussi !" else "Échec du ping: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                val errorMsg = "Erreur ping: ${e.message}"
                lastPingResult.value = errorMsg
                connectionStatus.value = "Erreur de connexion"
                context?.let {
                    Toast.makeText(it, errorMsg, Toast.LENGTH_LONG).show()
                }
            } finally {
                isLoading.value = false
            }
        }
    }

    private suspend fun pingHost(host: String, timeout: Int = 5000): PingResult {
        return try {
            val address = InetAddress.getByName(host)
            val startTime = System.currentTimeMillis()
            val reachable = address.isReachable(timeout)
            val endTime = System.currentTimeMillis()
            val latency = endTime - startTime

            if (reachable) {
                PingResult(true, "Ping vers $host réussi (${latency}ms)")
            } else {
                PingResult(false, "Host $host non accessible")
            }
        } catch (e: UnknownHostException) {
            PingResult(false, "Host inconnu: $host")
        } catch (e: IOException) {
            PingResult(false, "Erreur réseau: ${e.message}")
        } catch (e: Exception) {
            PingResult(false, "Erreur: ${e.message}")
        }
    }

    fun sendDebugText(text: String, context: Context? = null) {
        if (printerIP.value.isEmpty()) {
            context?.let {
                Toast.makeText(it, "Aucune IP configurée", Toast.LENGTH_SHORT).show()
            }
            return
        }

        if (text.isEmpty()) {
            context?.let {
                Toast.makeText(it, "Aucun texte à envoyer", Toast.LENGTH_SHORT).show()
            }
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    sendTextToPrinter(printerIP.value, text)
                }

                lastPingResult.value = result.message

                context?.let {
                    Toast.makeText(
                        it,
                        if (result.success) "Texte envoyé avec succès !" else "Échec envoi: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                val errorMsg = "Erreur envoi: ${e.message}"
                lastPingResult.value = errorMsg
                context?.let {
                    Toast.makeText(it, errorMsg, Toast.LENGTH_LONG).show()
                }
            } finally {
                isLoading.value = false
            }
        }
    }

    private suspend fun sendTextToPrinter(ip: String, text: String, port: Int = 9100): PingResult {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 5000)

            val outputStream = socket.getOutputStream()

            // ESC/POS : Initialize printer
            outputStream.write(byteArrayOf(0x1B, 0x40))

            // Encoder le texte en ISO-8859-1 pour les accents français
            val textBytes = text.toByteArray(Charsets.ISO_8859_1)
            outputStream.write(textBytes)

            // Feed paper (3 lignes)
            outputStream.write("\n\n\n".toByteArray(Charsets.ISO_8859_1))

            // ESC/POS : Cut paper (partial cut)
            outputStream.write(byteArrayOf(0x1D, 0x56, 0x42, 0x00))

            outputStream.flush()
            socket.close()
            PingResult(true, "Texte envoyé vers $ip:$port")

        } catch (e: ConnectException) {
            PingResult(false, "Impossible de se connecter à $ip:$port")
        } catch (e: SocketTimeoutException) {
            PingResult(false, "Timeout de connexion vers $ip:$port")
        } catch (e: IOException) {
            PingResult(false, "Erreur IO: ${e.message}")
        } catch (e: Exception) {
            PingResult(false, "Erreur: ${e.message}")
        }
    }

    fun testPrinterConnection(context: Context? = null) {
        if (printerIP.value.isEmpty()) {
            context?.let {
                Toast.makeText(it, "Aucune IP configurée", Toast.LENGTH_SHORT).show()
            }
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    testConnection(printerIP.value, 9100)
                }

                lastPingResult.value = result.message
                connectionStatus.value = if (result.success) "Connecté" else "Erreur de connexion"

                context?.let {
                    Toast.makeText(
                        it,
                        if (result.success) "Connexion imprimante OK !" else "Échec connexion: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                val errorMsg = "Erreur test: ${e.message}"
                lastPingResult.value = errorMsg
                connectionStatus.value = "Erreur de connexion"
                context?.let {
                    Toast.makeText(it, errorMsg, Toast.LENGTH_LONG).show()
                }
            } finally {
                isLoading.value = false
            }
        }
    }

    private suspend fun testConnection(ip: String, port: Int = 9100): PingResult {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 3000)
            socket.close()
            PingResult(true, "Connexion TCP réussie vers $ip:$port")
        } catch (e: ConnectException) {
            PingResult(false, "Service non disponible sur $ip:$port")
        } catch (e: SocketTimeoutException) {
            PingResult(false, "Timeout connexion vers $ip:$port")
        } catch (e: Exception) {
            PingResult(false, "Erreur connexion: ${e.message}")
        }
    }

    fun resetPrinter() {
        isPrinterEnabled.value = false
        printerIP.value = ""
        printerName.value = ""
        connectionStatus.value = "Déconnecté"
        debugText.value = ""
        lastPingResult.value = ""
        isLoading.value = false
        isScanning.value = false
        foundPrinters.value = emptyList()
        scanProgress.value = 0
    }

    fun scanNetworkForPrinters(context: Context) {
        if (isScanning.value) return

        isScanning.value = true
        foundPrinters.value = emptyList()
        scanProgress.value = 0

        viewModelScope.launch {
            try {
                val networkRange = withContext(Dispatchers.IO) {
                    getLocalNetworkRange(context)
                }

                if (networkRange.isEmpty()) {
                    Toast.makeText(context, "Impossible de détecter le réseau local", Toast.LENGTH_SHORT).show()
                    isScanning.value = false
                    return@launch
                }

                Toast.makeText(context, "Scan du réseau $networkRange en cours...", Toast.LENGTH_SHORT).show()

                val printers = withContext(Dispatchers.IO) {
                    scanNetworkRange(networkRange)
                }

                foundPrinters.value = printers

                if (printers.isNotEmpty()) {
                    Toast.makeText(context, "${printers.size} imprimante(s) trouvée(s)", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Aucune imprimante trouvée sur le réseau", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Erreur scan: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isScanning.value = false
                scanProgress.value = 0
            }
        }
    }

    private suspend fun getLocalNetworkRange(context: Context): String {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress

            // Convertir l'IP en format lisible
            val ip = String.format(
                "%d.%d.%d.%d",
                ipAddress and 0xff,
                ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )

            // Extraire les 3 premiers octets pour le range réseau
            val parts = ip.split(".")
            if (parts.size == 4) {
                "${parts[0]}.${parts[1]}.${parts[2]}"
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    private suspend fun scanNetworkRange(networkBase: String): List<FoundPrinter> {
        val printers = mutableListOf<FoundPrinter>()
        val totalHosts = 254

        // Scanner les IPs de 1 à 254
        val jobs = (1..254).map { hostNumber ->
            viewModelScope.async(Dispatchers.IO) {
                val ip = "$networkBase.$hostNumber"
                scanProgress.value = ((hostNumber.toFloat() / totalHosts) * 100).toInt()

                try {
                    // Test de connexion rapide sur port 9100 (standard imprimante)
                    val socket = Socket()
                    socket.connect(InetSocketAddress(ip, 9100), 1000) // Timeout court
                    socket.close()

                    // Si connexion réussie, c'est probablement une imprimante
                    FoundPrinter(
                        ip = ip,
                        name = "Imprimante détectée",
                        port = 9100,
                        responseTime = "< 1s"
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }

        // Attendre tous les résultats et filtrer les non-null
        val results = jobs.awaitAll()
        printers.addAll(results.filterNotNull())

        return printers
    }

    fun selectPrinter(printer: FoundPrinter) {
        printerIP.value = printer.ip
        printerName.value = printer.name
        connectionStatus.value = "Sélectionné"
        lastPingResult.value = "Imprimante sélectionnée depuis le scan"
    }

    fun clearScanResults() {
        foundPrinters.value = emptyList()
    }

    data class PingResult(
        val success: Boolean,
        val message: String
    )

    data class FoundPrinter(
        val ip: String,
        val name: String,
        val port: Int,
        val responseTime: String
    )
}