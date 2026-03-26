package com.boomingarage.android.di

import com.boomingarage.android.ui.auth.AuthViewModel
import com.boomingarage.android.ui.customer.billing.BillingViewModel
import com.boomingarage.android.ui.customer.booking.BookingViewModel
import com.boomingarage.android.ui.customer.checkin.CheckInViewModel
import com.boomingarage.android.ui.customer.details.GarageDetailViewModel
import com.boomingarage.android.ui.customer.history.BookingHistoryViewModel
import com.boomingarage.android.ui.customer.home.HomeViewModel
import com.boomingarage.android.ui.customer.map.MapViewModel
import com.boomingarage.android.ui.customer.review.WriteReviewViewModel
import com.boomingarage.android.ui.operator.bookings.ManageBookingsViewModel
import com.boomingarage.android.ui.operator.bays.BayManagementViewModel
import com.boomingarage.android.ui.operator.dashboard.OperatorDashboardViewModel
import com.boomingarage.android.ui.operator.stats.RevenueStatsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MapViewModel(get()) }
    viewModel { params -> GarageDetailViewModel(params.get(), get()) }
    viewModel { params -> BookingViewModel(params.get(), get(), get()) }
    viewModel { params -> CheckInViewModel(params.get(), get(), get(), get()) }
    viewModel { params -> BillingViewModel(params.get(), get()) }
    viewModel { BookingHistoryViewModel(get(), get()) }
    viewModel { params -> WriteReviewViewModel(params.get(), params.get(), get(), get()) }

    // Operator ViewModels
    viewModel { OperatorDashboardViewModel(get(), get(), get()) }
    viewModel { ManageBookingsViewModel(get(), get(), get()) }
    viewModel { BayManagementViewModel(get(), get()) }
    viewModel { RevenueStatsViewModel(get(), get()) }
}
