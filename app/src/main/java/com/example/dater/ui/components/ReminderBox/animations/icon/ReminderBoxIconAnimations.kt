package com.example.dater.ui.components.ReminderBox.animations.icon

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.airbnb.lottie.model.KeyPath
import com.example.dater.R

// Implement Animation in journey and reminder box && Make Journey Box reminder filter work
@Composable
fun ReminderAlertAnimation(
    modifier: Modifier,
    easing: Easing = FastOutLinearInEasing,
    onClick: () -> Unit,
){

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val keyPath = KeyPath("**","Stroke 1").keysToString()

    //TODO()  ADD DYNAMIC COLOR TO WIDGETS

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR
            , value =   MaterialTheme.colorScheme.primary.hashCode(),
            keyPath = arrayOf(KeyPath("**","Stroke 1").keysToString())
        )
    )

    var isPlaying by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.reminder_alert_animation_json)
    )

    val progress = animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = 1
    )

    LaunchedEffect(key1 = progress.value){
        if (progress.value == 1f){
            isPlaying = false
        }
    }

    LottieAnimation(
        modifier = modifier.
        clickable (
            interactionSource = interactionSource,
            indication = null
        ){
            isPlaying = true
            onClick()
        }
        ,
        dynamicProperties = dynamicProperties,
        composition = composition,
        progress = { progress.value }
    )

}

@Composable
fun ReminderBirthdayAnimation(
    modifier: Modifier,
    easing: Easing = FastOutLinearInEasing,
    onClick: () -> Unit,
){

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.reminder_birthday_animation_json))

    val progress = animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = 1
    )

    LaunchedEffect(key1 = progress.value){
        if (progress.value == 1f){
            isPlaying = false
        }
    }

    LottieAnimation(
        modifier = modifier.
        clickable (
            interactionSource = interactionSource,
            indication = null
        ){
            isPlaying = true
            onClick()
        }
        ,
        composition = composition,
        progress = { if(progress.isPlaying){ progress.value } else{1f} }
    )

}

@Composable
fun ReminderEventAnimation(
    modifier: Modifier,
    easing: Easing = FastOutLinearInEasing,
    onClick: () -> Unit,
){
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.reminder_events_animation_json))

    val progress = animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = 1
    )

    LaunchedEffect(key1 = progress.value){
        if (progress.value == 1f){
            isPlaying = false
        }
    }

    LottieAnimation(
        modifier = modifier.
        clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            isPlaying = true
            onClick()
        }
        ,
        composition = composition,
        progress = { if(progress.isPlaying){ progress.value } else{1f} }
    )

}