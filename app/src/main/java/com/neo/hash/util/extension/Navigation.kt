package com.neo.hash.util.extension

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

infix fun NavHostController.isCurrent(
    stack: NavBackStackEntry
) = currentDestination?.id == stack.destination.id