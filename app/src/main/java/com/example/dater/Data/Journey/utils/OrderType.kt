package com.example.dater.Data.Journey.utils

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}