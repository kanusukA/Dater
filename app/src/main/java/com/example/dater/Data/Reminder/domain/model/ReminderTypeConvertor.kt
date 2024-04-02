package com.example.dater.Data.Reminder.domain.model

import androidx.room.TypeConverter
import com.example.dater.Data.Reminder.utils.ReminderType

class ReminderTypeConvertor {

    @TypeConverter
    fun fromReminderTypeToInt(reminderType: ReminderType): Int{
        return when(reminderType){
            ReminderType.Alert -> 1
            ReminderType.Birthday -> 2
            ReminderType.Event -> 3
            ReminderType.All -> {4}
        }
    }

    @TypeConverter
    fun toReminderTypeFromInt(int: Int): ReminderType{
        return when(int){
            1 -> ReminderType.Alert
            2 -> ReminderType.Birthday
            3 -> ReminderType.Event
            else -> {
                ReminderType.All
            }
        }
    }

    @TypeConverter
    fun fromReminderDateTypeToInt(reminderDateType: ReminderDateType): Int{
        return when (reminderDateType){
            ReminderDateType.EmptyDate -> 0
            ReminderDateType.SelectedDate -> 1
            ReminderDateType.SelectedDays -> 2
        }
    }

    @TypeConverter
    fun toReminderDateTypeFromInt(int: Int): ReminderDateType{
        return when (int){
            0 -> ReminderDateType.EmptyDate
            2 -> ReminderDateType.SelectedDays
            1 -> ReminderDateType.SelectedDate
            else -> {
                ReminderDateType.SelectedDate
            }
        }
    }

    @TypeConverter
    fun fromListIntToString(list: List<Int>): String{
        var string = ""
        list.forEach { string += it.toString() }
        return string
    }

    @TypeConverter
    fun toStringFromListInt(string: String): List<Int>{

        val list = mutableListOf<Int>(0,0,0,0,0,0,0)
        string.forEachIndexed {int,char ->
            if(char.digitToInt() == 1){
                list[int] = 1
            }
        }

        return list
    }
}