package com.example.dater.ui.components.TopFilterBar.animations.icon

import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dater.R

@Composable
fun TopFilterBarArrowToLensAnimation(
    modifier: Modifier,
    easing: Easing = EaseInOutExpo,
    showSearch: Boolean = false,
    onClickLens: () -> Unit,
    onClickArrow: () -> Unit
) {
    var isPlaying by remember {
        mutableStateOf(false)
    }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.arrow_to_search_animation_json))

    val _progress by remember(key1 = showSearch) {
        mutableStateOf(
            if (showSearch) {
                1f
            } else {
                0f
            }
        )
    }

    val progress = animateFloatAsState(
        targetValue = _progress,
        label = "",
        animationSpec = tween(400, easing = easing)
    )


    LottieAnimation(
        modifier = modifier
            .clickable {
                if (showSearch) {
                    onClickLens()
                } else {
                    onClickArrow()
                }
                isPlaying = true
            },
        composition = composition,
        progress = { progress.value }
    )

}

@Composable
fun TopFilterBarFilterToCross(
    modifier: Modifier,
    easing: Easing = EaseInOutExpo,
    showFilter: Boolean = false,
    onClickFilter: () -> Unit,
    onClickCross: () -> Unit
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.filter_to_cross_json))

    val _progress by remember(key1 = showFilter) {
        mutableStateOf(
            if (showFilter) {
                1f
            } else {
                0f
            }
        )
    }

    val progress = animateFloatAsState(
        targetValue = _progress,
        label = "",
        animationSpec = tween(400, easing = easing)
    )


    LottieAnimation(
        modifier = modifier
            .clickable {
               if(_progress == 1f){
                   onClickCross()
               } else {
                   onClickFilter()
               }
            },
        composition = composition,
        progress = { progress.value }
    )

}

@Composable
fun TopFilterBarFilterAscending(
    modifier: Modifier,
    easing: Easing = FastOutLinearInEasing,
    onClick: () -> Unit,
){
    var isPlaying by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.filter_ascending_animation_json))

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
                clickable {
                    isPlaying = true
                    onClick()
                          }
        ,
        composition = composition,
        progress = { progress.value }
    )

}

@Composable
fun TopFilterBarFilterDescending(
    modifier: Modifier,
    easing: Easing = FastOutLinearInEasing,
    onClick: () -> Unit,
){
    var isPlaying by remember {
        mutableStateOf(false)
    }

    // TODO() FIX NAME
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.filert_descending_animation_json))

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
        clickable {
            isPlaying = true
            onClick()
        }
        ,
        composition = composition,
        progress = { progress.value }
    )

}

