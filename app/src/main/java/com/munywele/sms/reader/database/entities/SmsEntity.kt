package com.munywele.sms.reader.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms")
data class SmsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val address: String,
    val body: String,
    val date: Long
)
