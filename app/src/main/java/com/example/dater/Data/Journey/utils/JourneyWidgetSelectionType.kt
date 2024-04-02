package com.example.dater.Data.Journey.utils

import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection


fun getJourneyWidgetType(journeyWidgetSelection: JourneyWidgetSelection,journey: Journey):JourneyWidgetType{

    if(journeyWidgetSelection.primaryJourneyId == journey.id){
        return JourneyWidgetType.PrimarySelection
    }
    else if (journeyWidgetSelection.secondaryJourneyId == journey.id){
        return  JourneyWidgetType.SecondarySelection
    }else {
        return JourneyWidgetType.None
    }
}



sealed class JourneyWidgetType{
    object PrimarySelection: JourneyWidgetType()
    object SecondarySelection: JourneyWidgetType()

    object None: JourneyWidgetType()
}