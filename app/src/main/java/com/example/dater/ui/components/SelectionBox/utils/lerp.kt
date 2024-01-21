package com.example.dater.ui.components.SelectionBox.utils

fun lerp(start: Float, stop: Float, fraction:Float)=
    (start * (1 - fraction) + stop * fraction)