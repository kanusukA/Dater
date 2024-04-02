package com.example.dater.Data.Journey.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("journeySelection")
data class JourneyWidgetSelection(

    val primaryJourneyId: Int = -1,
    val secondaryJourneyId: Int = -1,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)
