package com.boomingarage.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.boomingarage.android.ui.auth.AuthViewModel
import com.boomingarage.android.ui.auth.LoginScreen
import com.boomingarage.android.ui.auth.OnboardingScreen
import com.boomingarage.android.ui.auth.SignUpScreen
import com.boomingarage.android.ui.customer.billing.BillingScreen
import com.boomingarage.android.ui.customer.booking.BookingConfirmationScreen
import com.boomingarage.android.ui.customer.booking.BookingScreen
import com.boomingarage.android.ui.customer.checkin.CheckInScreen
import com.boomingarage.android.ui.customer.details.GarageDetailScreen
import com.boomingarage.android.ui.customer.history.BookingHistoryScreen
import com.boomingarage.android.ui.customer.home.HomeScreen
import com.boomingarage.android.ui.customer.map.MapScreen
import com.boomingarage.android.ui.customer.review.WriteReviewScreen
import com.boomingarage.android.ui.operator.bays.BayManagementScreen
import com.boomingarage.android.ui.operator.bookings.ManageBookingsScreen
import com.boomingarage.android.ui.operator.dashboard.OperatorDashboardScreen
import com.boomingarage.android.ui.operator.stats.RevenueStatsScreen
import com.boomingarage.shared.domain.model.UserRole
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isAuthenticated = authState.user != null
    val isOperator = authState.user?.role == UserRole.OPERATOR

    val showBottomBar = isAuthenticated && currentDestination?.let { dest ->
        customerNavItems.any { dest.hasRoute(it.route::class) } ||
        operatorNavItems.any { dest.hasRoute(it.route::class) }
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                val items = if (isOperator) operatorNavItems else customerNavItems
                BottomNavBar(
                    items = items,
                    currentRoute = currentDestination?.route,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) {
                if (isOperator) Route.OperatorDashboard else Route.Home
            } else {
                Route.Onboarding
            },
            modifier = Modifier.padding(paddingValues)
        ) {
            // Auth
            composable<Route.Onboarding> {
                OnboardingScreen(
                    onGetStarted = { navController.navigate(Route.Login) }
                )
            }
            composable<Route.Login> {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        val dest = if (authState.user?.role == UserRole.OPERATOR) Route.OperatorDashboard else Route.Home
                        navController.navigate(dest) {
                            popUpTo(Route.Onboarding) { inclusive = true }
                        }
                    },
                    onSignUpClick = { navController.navigate(Route.SignUp) }
                )
            }
            composable<Route.SignUp> {
                SignUpScreen(
                    viewModel = authViewModel,
                    onSignUpSuccess = {
                        val dest = if (authState.user?.role == UserRole.OPERATOR) Route.OperatorDashboard else Route.Home
                        navController.navigate(dest) {
                            popUpTo(Route.Onboarding) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Customer
            composable<Route.Home> {
                HomeScreen(
                    onGarageClick = { garageId -> navController.navigate(Route.GarageDetail(garageId)) }
                )
            }
            composable<Route.Map> {
                MapScreen(
                    onGarageClick = { garageId -> navController.navigate(Route.GarageDetail(garageId)) }
                )
            }
            composable<Route.GarageDetail> { backStackEntry ->
                val detail = backStackEntry.toRoute<Route.GarageDetail>()
                GarageDetailScreen(
                    garageId = detail.garageId,
                    onBookClick = { navController.navigate(Route.Booking(detail.garageId)) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<Route.Booking> { backStackEntry ->
                val booking = backStackEntry.toRoute<Route.Booking>()
                BookingScreen(
                    garageId = booking.garageId,
                    onBookingConfirmed = { bookingId ->
                        navController.navigate(Route.BookingConfirmation(bookingId)) {
                            popUpTo(Route.Home)
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<Route.BookingConfirmation> { backStackEntry ->
                val confirmation = backStackEntry.toRoute<Route.BookingConfirmation>()
                BookingConfirmationScreen(
                    bookingId = confirmation.bookingId,
                    onDone = {
                        navController.navigate(Route.BookingHistory) {
                            popUpTo(Route.Home)
                        }
                    }
                )
            }
            composable<Route.CheckIn> { backStackEntry ->
                val checkin = backStackEntry.toRoute<Route.CheckIn>()
                CheckInScreen(
                    bookingId = checkin.bookingId,
                    onSessionEnded = { navController.navigate(Route.Billing(checkin.bookingId)) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<Route.Billing> { backStackEntry ->
                val billing = backStackEntry.toRoute<Route.Billing>()
                BillingScreen(
                    bookingId = billing.bookingId,
                    onPaymentComplete = {
                        navController.navigate(Route.BookingHistory) {
                            popUpTo(Route.Home)
                        }
                    },
                    onWriteReview = { garageId ->
                        navController.navigate(Route.WriteReview(garageId, billing.bookingId))
                    }
                )
            }
            composable<Route.BookingHistory> {
                BookingHistoryScreen(
                    onBookingClick = { bookingId -> navController.navigate(Route.CheckIn(bookingId)) },
                    onWriteReview = { garageId, bookingId ->
                        navController.navigate(Route.WriteReview(garageId, bookingId))
                    }
                )
            }
            composable<Route.WriteReview> { backStackEntry ->
                val review = backStackEntry.toRoute<Route.WriteReview>()
                WriteReviewScreen(
                    garageId = review.garageId,
                    bookingId = review.bookingId,
                    onReviewSubmitted = { navController.popBackStack() },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<Route.Profile> {
                // Simple profile placeholder
                com.boomingarage.android.ui.auth.ProfileScreen(
                    authViewModel = authViewModel,
                    onSignOut = {
                        navController.navigate(Route.Onboarding) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Operator
            composable<Route.OperatorDashboard> {
                OperatorDashboardScreen()
            }
            composable<Route.ManageBookings> {
                ManageBookingsScreen()
            }
            composable<Route.BayManagement> {
                BayManagementScreen()
            }
            composable<Route.RevenueStats> {
                RevenueStatsScreen()
            }
        }
    }
}
