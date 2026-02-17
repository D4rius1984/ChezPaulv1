package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.ui.navigation.Screen

class BottomNavViewModel : ViewModel() {
    val selectedScreen = mutableStateOf<Screen>(Screen.Accueil)

    fun selectScreen(screen: Screen) {
        selectedScreen.value = screen
    }
}
