package com.neo.hash.util.extension

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween

const val TRANSITION_DURATION = 700

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<*>.exitToLeftTransition
    get() = slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DURATION)
    )

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<*>.exitToRightTransition
    get() = slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION)
    )

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<*>.enterToRightTransition
    get() = slideIntoContainer(
        AnimatedContentScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION)
    )

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<*>.enterToLeftTransition
    get() = slideIntoContainer(
        AnimatedContentScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DURATION)
    )