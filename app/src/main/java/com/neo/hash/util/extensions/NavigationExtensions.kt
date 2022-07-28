package com.neo.hash.util.extensions

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

infix fun NavHostController.isCurrent(
    stack: NavBackStackEntry
) = currentDestination?.id == stack.destination.id