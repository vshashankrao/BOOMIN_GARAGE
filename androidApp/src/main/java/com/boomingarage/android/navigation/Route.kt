package com.boomingarage.android.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    // Auth
    @Serializable data object Onboarding : Route
    @Serializable data object Login : Route
    @Serializable data object SignUp : Route

    // Customer
    @Serializable data object Home : Route
    @Serializable data object Map : Route
    @Serializable data class GarageDetail(val garageId: String) : Route
    @Serializable data class Booking(val garageId: String) : Route
    @Serializable data class BookingConfirmation(val bookingId: String) : Route
    @Serializable data class CheckIn(val bookingId: String) : Route
    @Serializable data class Billing(val bookingId: String) : Route
    @Serializable data object BookingHistory : Route
    @Serializable data class WriteReview(val garageId: String, val bookingId: String) : Route
    @Serializable data object Profile : Route

    // Operator
    @Serializable data object OperatorDashboard : Route
    @Serializable data object ManageBookings : Route
    @Serializable data object BayManagement : Route
    @Serializable data object RevenueStats : Route
}
