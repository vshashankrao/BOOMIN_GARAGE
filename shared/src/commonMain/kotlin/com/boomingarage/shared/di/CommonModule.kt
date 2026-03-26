package com.boomingarage.shared.di

import com.boomingarage.shared.data.repository.*
import com.boomingarage.shared.domain.repository.*
import com.boomingarage.shared.domain.usecase.auth.*
import com.boomingarage.shared.domain.usecase.booking.*
import com.boomingarage.shared.domain.usecase.checkin.*
import com.boomingarage.shared.domain.usecase.garage.*
import com.boomingarage.shared.domain.usecase.payment.*
import com.boomingarage.shared.domain.usecase.review.*
import com.boomingarage.shared.domain.usecase.stats.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    // Repositories
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::GarageRepositoryImpl) bind GarageRepository::class
    singleOf(::BookingRepositoryImpl) bind BookingRepository::class
    singleOf(::CheckInRepositoryImpl) bind CheckInRepository::class
    singleOf(::ReviewRepositoryImpl) bind ReviewRepository::class
    singleOf(::PaymentRepositoryImpl) bind PaymentRepository::class
    singleOf(::StatsRepositoryImpl) bind StatsRepository::class

    // Auth Use Cases
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }

    // Garage Use Cases
    factory { SearchGaragesUseCase(get()) }
    factory { GetGarageDetailsUseCase(get(), get()) }
    factory { GetAvailableBaysUseCase(get()) }

    // Booking Use Cases
    factory { CreateBookingUseCase(get(), get()) }
    factory { CancelBookingUseCase(get()) }
    factory { GetUserBookingsUseCase(get()) }
    factory { GetGarageBookingsUseCase(get()) }

    // CheckIn Use Cases
    factory { StartCheckInUseCase(get(), get()) }
    factory { EndCheckInUseCase(get(), get()) }

    // Review Use Cases
    factory { SubmitReviewUseCase(get()) }

    // Payment Use Cases
    factory { ProcessPaymentUseCase(get()) }

    // Stats Use Cases
    factory { GetDashboardStatsUseCase(get()) }
}
