package com.munywele.sms.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms")
data class SmsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val sender: String,
    val body: String,
    val amount: Double,
    val timestamp: Long
)
