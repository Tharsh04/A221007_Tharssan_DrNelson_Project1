package com.example.a221007_tharssan_drnelson_project1

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a221007_tharssan_drnelson_project1.data.FoodDonation
import com.example.a221007_tharssan_drnelson_project1.ui.theme.BottomNav

@Composable
fun AppNavigation(donorViewModel: DonorViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Temporary state to hold donation data until the bank transfer is confirmed
    var pendingDonation by remember { mutableStateOf<FoodDonation?>(null) }

    Scaffold(
        bottomBar = {
            // Only show BottomNav if the user is logged in and not on the Payment screen
            if (donorViewModel.currentUser != null && currentRoute != "payment") {
                BottomNav(
                    currentView = currentRoute ?: "home",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            // 1. Login
            composable("login") {
                LoginScreen(
                    onLoginClick = { email, pass ->
                        if (donorViewModel.login(email, pass)) {
                            navController.navigate("home") { popUpTo("login") { inclusive = true } }
                            true
                        } else false
                    },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }

            // 2. Register
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = { user ->
                        donorViewModel.register(user)
                        navController.navigate("login")
                    },
                    onBackToLogin = { navController.popBackStack() }
                )
            }

            // 3. Home
            composable("home") {
                HomeScreen(viewModel = donorViewModel, onNavigate = { route -> navController.navigate(route) })
            }

            // 4. History
            composable("history") {
                HistoryScreen(viewModel = donorViewModel)
            }

            // 5. Donate
            composable("donate-food") {
                DonateFoodScreen(
                    donorName = donorViewModel.currentUser?.name ?: "",
                    donorEmail = donorViewModel.currentUser?.email ?: "",
                    onDonate = { donation ->
                        pendingDonation = donation
                        navController.navigate("payment")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // 6. Payment & Receipt
            composable("payment") {
                PaymentScreen(
                    totalAmount = "RM ${pendingDonation?.totalItemCount?.times(10)}.00",
                    donationData = pendingDonation,
                    onComplete = {
                        pendingDonation?.let { donorViewModel.addDonation(it) }
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            // 7. Profile
            composable("profile") {
                ProfileSettingsScreen(
                    donorName = donorViewModel.currentUser?.name ?: "",
                    donorEmail = donorViewModel.currentUser?.email ?: "",
                    donorMatric = donorViewModel.currentUser?.matric ?: "",
                    onSaveProfile = { name, email, matric ->
                        donorViewModel.updateProfile(name, email, matric)
                    },
                    onLogout = {
                        donorViewModel.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}