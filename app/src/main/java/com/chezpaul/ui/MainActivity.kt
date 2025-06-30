package com.chezpaul.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.chezpaul.ui.theme.ChezPaulTheme
import com.chezpaul.viewmodel.BottomNavViewModel

class MainActivity : ComponentActivity() {
    // Initialisation du ViewModel pour la navigation
    private val bottomNavViewModel: BottomNavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChezPaulTheme {
                // Passez le ViewModel à votre composant principal
                ChezPaulApp(viewModel = bottomNavViewModel)
            }
        }
    }
}
