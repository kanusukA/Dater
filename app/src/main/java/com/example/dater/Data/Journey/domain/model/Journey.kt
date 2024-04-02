package com.example.dater.Data.Journey.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journeys")
data class Journey(

    val startDate: Long,
    val endDate: Long,
    val reminders: List<Long> = emptyList(),
    val title: String,
    val notification: Boolean = true,

    @PrimaryKey(autoGenerate = true) val id: Int = 0

)