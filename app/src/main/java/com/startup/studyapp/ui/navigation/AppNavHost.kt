package com.startup.studyapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.startup.studyapp.data.repository.StudyRepository
import com.startup.studyapp.ui.screens.FlashcardsScreen
import com.startup.studyapp.ui.screens.HistoryScreen
import com.startup.studyapp.ui.screens.HomeScreen
import com.startup.studyapp.ui.screens.OnboardingScreen
import com.startup.studyapp.ui.screens.SettingsScreen
import com.startup.studyapp.ui.screens.SummaryScreen
import com.startup.studyapp.ui.screens.TutorScreen
import com.startup.studyapp.ui.viewmodel.AppViewModel
import com.startup.studyapp.ui.viewmodel.AppViewModelFactory

private data class NavItem(val route: String, val label: String, val icon: @Composable () -> Unit)

@Composable
fun AppNavHost(repository: StudyRepository) {
    val navController = rememberNavController()
    val vm: AppViewModel = viewModel(factory = AppViewModelFactory(repository))
    val onboardingDone by vm.onboardingDone.collectAsStateWithLifecycle()

    val mainItems = listOf(
        NavItem("home", "Home", { Icon(Icons.Outlined.Home, contentDescription = null) }),
        NavItem("tutor", "Tutor", { Icon(Icons.Outlined.SmartToy, contentDescription = null) }),
        NavItem("resumo", "Resumo", { Icon(Icons.Outlined.MenuBook, contentDescription = null) }),
        NavItem("flashcards", "Cards", { Icon(Icons.Outlined.Style, contentDescription = null) }),
        NavItem("historico", "Histórico", { Icon(Icons.Outlined.DateRange, contentDescription = null) }),
        NavItem("config", "Ajustes", { Icon(Icons.Outlined.Settings, contentDescription = null) })
    )

    val startDestination = if (onboardingDone) "home" else "onboarding"
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = mainItems.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    mainItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(item.label) },
                            icon = item.icon
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("onboarding") {
                OnboardingScreen(vm) {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }
            composable("home") { HomeScreen() }
            composable("tutor") { TutorScreen(vm) }
            composable("resumo") { SummaryScreen(vm) }
            composable("flashcards") { FlashcardsScreen(vm) }
            composable("historico") { HistoryScreen(vm) }
            composable("config") { SettingsScreen(vm) }
        }
    }
}
