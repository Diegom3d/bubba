package com.bubba.express.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bubba.express.screens.home.HomeScreen
import com.bubba.express.screens.home.HomeViewModel
import com.bubba.express.screens.home.ProductDetailScreen
import com.bubba.express.screens.login.LoginScreen
import com.bubba.express.screens.order.OrderConfirmationScreen
import com.bubba.express.screens.order.OrderEvent
import com.bubba.express.screens.order.OrderSummaryScreen
import com.bubba.express.screens.order.OrderViewModel
import com.bubba.express.screens.register.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    // ViewModels compartidos para TODO el NavHost
    val homeViewModel: HomeViewModel = hiltViewModel()
    val orderViewModel: OrderViewModel = hiltViewModel()

    // Observamos el estado del carrito
    val orderState = orderViewModel.state.collectAsState().value

    // Cada vez que cambie el carrito, actualizamos el contador en el HomeViewModel
    LaunchedEffect(orderState.cartItems.size) {
        homeViewModel.updateCartCount(orderState.cartItems.size)
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ---------- LOGIN ----------
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // ---------- REGISTER ----------
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // ---------- HOME ----------
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                onProductClick = { productId ->
                    navController.navigate("product/$productId")
                },
                onCartClick = {
                    navController.navigate("cart")
                },
                onProfileClick = {
                    // TODO: ir a perfil
                },
                onHistoryClick = {
                    // TODO: ir a historial
                },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // ---------- PRODUCT DETAIL ----------
        composable(
            route = "product/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val productId = backStackEntry.arguments?.getInt("productId")

            // Usamos el MISMO HomeViewModel (NO crear otro)
            val homeState = homeViewModel.state.collectAsState().value
            val product = homeState.products.firstOrNull { it.id == productId }

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    onBack = { navController.popBackStack() },
                    onAddToCart = { _customization ->
                        // Agregamos al carrito
                        orderViewModel.onEvent(OrderEvent.AddToCart(product))
                        // El contador se actualiza automáticamente por el LaunchedEffect de arriba
                        navController.popBackStack()
                    }
                )
            } else {
                // Si no se encontró el producto por alguna razón, volvemos al home
                navController.popBackStack()
            }
        }

        // ---------- CART / ORDER SUMMARY ----------
        composable("cart") {
            // De momento no tenemos usuario real; puedes cambiar el ID cuando conectes login
            val fakeUserId = 1

            OrderSummaryScreen(
                viewModel = orderViewModel,
                usuarioId = fakeUserId,
                onBack = { navController.popBackStack() },
                onOrderConfirmed = {
                    navController.navigate("orderConfirmation")
                }
            )
        }

        // ---------- ORDER CONFIRMATION ----------
        composable("orderConfirmation") {
            OrderConfirmationScreen(
                viewModel = orderViewModel,
                onNewOrder = {
                    // Limpia carrito y regresa al home
                    navController.popBackStack(
                        route = "home",
                        inclusive = false
                    )
                }
            )
        }
    }
}
