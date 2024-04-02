package com.example.dater.Data.utils


import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

class DateHandler (){

    private val calender = Calendar.getInstance()

    constructor(long: Long): this(){
        calender.timeInMillis = long
    }

    constructor(date: Int, month: Int, year: Int): this(){
        calender.set(year, (month - 1), date)
    }

    constructor(date: Int, month: Int, year: Int, hourOfDay: Int, min: Int, sec: Int): this(){
        calender.set(year, month, date,hourOfDay,min,sec)
    }

    fun getLocalDateTime(): LocalDateTime{
        return LocalDateTime.now()
    }

    fun getText(
        monthFormat: Int = Calendar.SHORT_FORMAT,
        dateLength: TextLength = TextLength.YEAR,
        capitalize: Boolean = true
    ): String{
        val day = calender.get(Calendar.DATE)
        /*TODO ADD USERS LOCAL*/
        val month = calender.getDisplayName(Calendar.MONTH,monthFormat,Locale.ENGLISH)
        val year = calender.get(Calendar.YEAR)

        val stringDay = if(day < 10){"0$day"} else {"$day"}

        return if(capitalize){
            when(dateLength){

                TextLength.DAY -> stringDay
                TextLength.MONTH -> stringDay + month.uppercase()
                TextLength.YEAR -> stringDay + "${month.uppercase()}$year"
            }
        }else{
            when(dateLength){
                TextLength.DAY -> stringDay
                TextLength.MONTH -> stringDay + month
                TextLength.YEAR -> stringDay + "$month$year"
            }

        }

    }

    fun getFullText(): String{
        val day = calender.get(Calendar.DATE)
        val month = calender.get(Calendar.MONTH)
        val year = calender.get(Calendar.YEAR)
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val min = calender.get(Calendar.MINUTE)
        val sec = calender.get(Calendar.SECOND)

        return "$day $month $year $hour $min $sec"
    }

    fun getTime():DateHandlerTime {

        val min = calender.get(Calendar.MINUTE)
        val sec = calender.get(Calendar.SECOND)
        val hourOfDay = calender.get(Calendar.HOUR_OF_DAY)
        return DateHandlerTime(hourOfDay, min, sec)
    }

    fun setTime(hour: Int, min: Int, sec: Int) {
        calender.set(Calendar.HOUR_OF_DAY,hour)
        calender.set(Calendar.MINUTE,min)
        calender.set(Calendar.SECOND,sec)
    }

    fun addTO(date: Int = 0, month: Int = 0, year: Int = 0, hour: Int = 0, min: Int = 0, sec: Int = 0){
        calender.add(Calendar.DATE,date)
        calender.add(Calendar.MONTH, month)
        calender.add(Calendar.YEAR, year)
        calender.add(Calendar.SECOND,sec)
        calender.add(Calendar.MINUTE,min)
        calender.add(Calendar.HOUR,hour)
    }

    fun getCompletionPercentage(start: Long, end: Long): Float{
        return  getDaysLeft(end) / ((start - end) / 86400000L).toFloat()
    }

    fun getDaysLeft(to: Long): Int{
        return ((to - calender.timeInMillis) / 86400000L).toInt()
    }

    fun getTheDay(): Int{
        return calender.get(Calendar.DAY_OF_WEEK)
    }

    fun getDayFromInt(int: Int): String{
        return when (int){
            0 -> "Sunday"
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            else -> {"Error"}
        }
    }


    fun getLong(): Long{
        return calender.timeInMillis
    }
    fun getDate(): Int{
        return calender.get(Calendar.DATE)
    }
    fun getMonth(): Int{
        return calender.get(Calendar.MONTH)
    }
    fun getYear(): Int{
        return calender.get(Calendar.YEAR)
    }
}

// Week Starts from 0 - 6
// 0 -> Sunday | 6 -> Saturday
fun getNextReminderWeekDay(selectedDays: List<Int>): Int {
    val day = DateHandler().getTheDay() - 1

    if (day == 0 || selectedDays.size == 1) {
        return selectedDays.indexOfFirst { it == 1 }
    } else {
        for (i in (day+1)..6) {
            if (selectedDays[i] == 1) {
                return i
            }
        }
        for (i in 0..day) {
            if (selectedDays[i] == 1) {
                return i
            }
        }
        return 0
    }
}


data class DateHandlerTime(
    val hour: Int,
    val min: Int,
    val sec: Int
)

enum class TextLength{
    DAY,
    MONTH,
    YEAR
}