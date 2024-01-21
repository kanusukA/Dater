package com.example.dater.Data.Journey.domain.model

import androidx.room.TypeConverter

class JourneyTypeConvertor {
    @TypeConverter
    fun fromListLongToString(list: List<Long>): String {
        var str = ""
        list.forEach { str += ("$it,") }
        return str
    }
    @TypeConverter
    fun toListIntFromString(string: String): List<Long> {
        val output = ArrayList<Long>()
        val split = string
            .replace("[","")
            .replace("]","")
            .replace(" ","")
            .split(",")
        split.forEach {
            try {
                output.add(it.toLong())
            }catch (_: Exception){
                println("(JourneyTypeConvertor) Journey - reminder list error - ")
            }
        }
        return output
    }
}