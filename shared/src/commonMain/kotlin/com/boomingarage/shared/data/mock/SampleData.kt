package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.*

object SampleData {

    val operator = User(
        id = "op-001",
        email = "operator@boomingarage.com",
        displayName = "Mike's Auto Shop",
        phone = "415-555-0100",
        role = UserRole.OPERATOR,
        isEmailVerified = true,
        createdAt = 1700000000000L
    )

    val customer = User(
        id = "cust-001",
        email = "demo@boomingarage.com",
        displayName = "Demo User",
        phone = "415-555-0199",
        role = UserRole.CUSTOMER,
        carInfo = CarInfo(
            make = "Honda",
            model = "Civic",
            year = 2019,
            color = "Silver",
            licensePlate = "7ABC123"
        ),
        isEmailVerified = true,
        createdAt = 1700000000000L
    )

    val garages = listOf(
        Garage(
            id = "garage-001",
            name = "Boomin Garage - SOMA",
            address = "123 Folsom St, San Francisco, CA 94105",
            latitude = 37.7849,
            longitude = -122.3994,
            operatorId = "op-001",
            photoUrls = emptyList(),
            amenities = listOf("Hydraulic Lift", "Air Compressor", "Tool Rental", "WiFi", "Restroom"),
            hourlyRate = 45.0,
            operatingHours = mapOf(
                "MONDAY" to DayHours("08:00", "20:00"),
                "TUESDAY" to DayHours("08:00", "20:00"),
                "WEDNESDAY" to DayHours("08:00", "20:00"),
                "THURSDAY" to DayHours("08:00", "20:00"),
                "FRIDAY" to DayHours("08:00", "20:00"),
                "SATURDAY" to DayHours("09:00", "18:00"),
                "SUNDAY" to DayHours("10:00", "16:00")
            ),
            bayCount = 4,
            averageRating = 4.7,
            totalReviews = 28,
            description = "Full-service DIY garage in the heart of SOMA. Professional-grade tools, hydraulic lifts, and expert guidance available."
        ),
        Garage(
            id = "garage-002",
            name = "Boomin Garage - Mission",
            address = "456 Valencia St, San Francisco, CA 94103",
            latitude = 37.7647,
            longitude = -122.4214,
            operatorId = "op-002",
            photoUrls = emptyList(),
            amenities = listOf("Hydraulic Lift", "Air Compressor", "Parts Store", "Diagnostic Scanner"),
            hourlyRate = 40.0,
            operatingHours = mapOf(
                "MONDAY" to DayHours("07:00", "21:00"),
                "TUESDAY" to DayHours("07:00", "21:00"),
                "WEDNESDAY" to DayHours("07:00", "21:00"),
                "THURSDAY" to DayHours("07:00", "21:00"),
                "FRIDAY" to DayHours("07:00", "21:00"),
                "SATURDAY" to DayHours("08:00", "19:00"),
                "SUNDAY" to DayHours("09:00", "17:00")
            ),
            bayCount = 6,
            averageRating = 4.5,
            totalReviews = 42,
            description = "Spacious 6-bay garage with on-site parts store. Great for bigger projects."
        ),
        Garage(
            id = "garage-003",
            name = "Boomin Garage - Oakland",
            address = "789 Broadway, Oakland, CA 94607",
            latitude = 37.7985,
            longitude = -122.2711,
            operatorId = "op-003",
            photoUrls = emptyList(),
            amenities = listOf("Hydraulic Lift", "Tire Machine", "Alignment Rack", "WiFi"),
            hourlyRate = 35.0,
            operatingHours = mapOf(
                "MONDAY" to DayHours("08:00", "20:00"),
                "TUESDAY" to DayHours("08:00", "20:00"),
                "WEDNESDAY" to DayHours("08:00", "20:00"),
                "THURSDAY" to DayHours("08:00", "20:00"),
                "FRIDAY" to DayHours("08:00", "20:00"),
                "SATURDAY" to DayHours("09:00", "18:00"),
                "SUNDAY" to DayHours(openTime = "", closeTime = "", isClosed = true)
            ),
            bayCount = 3,
            averageRating = 4.8,
            totalReviews = 15,
            description = "Affordable bays with specialty alignment and tire equipment."
        )
    )

    fun baysForGarage(garageId: String): List<Bay> = when (garageId) {
        "garage-001" -> listOf(
            Bay("bay-001", garageId, "Bay A", 1, true, listOf("Hydraulic Lift", "Drain"), 45.0),
            Bay("bay-002", garageId, "Bay B", 2, true, listOf("Hydraulic Lift"), 45.0),
            Bay("bay-003", garageId, "Bay C", 3, false, listOf("Floor Jack Only"), 35.0),
            Bay("bay-004", garageId, "Bay D", 4, true, listOf("Hydraulic Lift", "Alignment"), 55.0)
        )
        "garage-002" -> (1..6).map { n ->
            Bay("bay-2-$n", garageId, "Bay $n", n, n != 3, listOf("Hydraulic Lift"), 40.0)
        }
        "garage-003" -> (1..3).map { n ->
            Bay("bay-3-$n", garageId, "Bay $n", n, true, listOf("Hydraulic Lift"), 35.0)
        }
        else -> emptyList()
    }

    val reviews = listOf(
        Review("rev-001", "garage-001", "cust-002", "Alex M.", "book-old-1", 5, "Amazing space! Had everything I needed for a brake job. Will be back.", 1709000000000L),
        Review("rev-002", "garage-001", "cust-003", "Sarah K.", "book-old-2", 4, "Great facility, a bit pricey but the lift makes it worth it.", 1708500000000L),
        Review("rev-003", "garage-001", "cust-004", "Jordan P.", "book-old-3", 5, "Finally a place where I can wrench on my car without my landlord complaining!", 1708000000000L),
        Review("rev-004", "garage-002", "cust-005", "Chris L.", "book-old-4", 4, "Good spot. The parts store on-site saved me a trip to AutoZone.", 1707500000000L),
        Review("rev-005", "garage-002", "cust-006", "David W.", "book-old-5", 5, "Super clean bays and friendly staff. 10/10", 1707000000000L),
        Review("rev-006", "garage-003", "cust-007", "Priya R.", "book-old-6", 5, "Best deal in the East Bay. Alignment rack is a game changer.", 1706500000000L)
    )
}
