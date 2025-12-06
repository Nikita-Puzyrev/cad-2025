// Product.kt -> Member.kt
package com.fitnessclub.android.models

import java.util.Date

data class Member(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val registrationDate: String,
    val subscription: Subscription?
)

data class Subscription(
    val id: Long,
    val type: String,
    val price: Double,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean
)

data class Trainer(
    val id: Long,
    val name: String,
    val specialization: String,
    val experienceYears: Int
)

data class Workout(
    val id: Long,
    val name: String,
    val description: String,
    val duration: Int,
    val trainer: Trainer?,
    val scheduleTime: String
)