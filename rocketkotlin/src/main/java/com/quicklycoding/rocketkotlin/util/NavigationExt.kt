package com.quicklycoding.rocketkotlin.util

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.quicklycoding.rocketkotlin.R


/*
*
* Navigate with Animation
* */
fun NavController.navigateWithAnim(
    destination: Int, args: Bundle? = null, navOptions: NavOptions? = getNavOptionsWithFadeAnim()
) {
    navigate(destination, args, navOptions)
}


/*
*
* Start Destination with some conditions
* */
fun NavController.startDestination(destination: Int) {
    Log.d("NavigationExt", "startDestination: $destination")

    if (destination == currentDestination?.id) return
    Log.d("NavigationExt", "startDestination: $currentDestination")

    if (isDestinationExists(destination)) {
        popBackStack(destination, false)
    } else {
        navigate(destination, null, getNavOptionsWithFadeAnim())
    }
}


/*
*
* Show Destination if already have in back stack
* */
fun NavController.isDestinationExists(destination: Int) = try {
    destination == getBackStackEntry(destination).destination.id
} catch (e: IllegalArgumentException) {
    false
}

/*
*
*Setup Navigation Animation
* */
fun getNavOptionsWithFadeAnim() = NavOptions.Builder().apply {
    setEnterAnim(R.anim.fade_in)
    setExitAnim(R.anim.fade_out)
    setPopEnterAnim(R.anim.fade_in)
    setPopExitAnim(R.anim.fade_out)
}.build()

fun getNavOptionsWithSlideAnim() = NavOptions.Builder().apply {
    setEnterAnim(R.anim.slide_in_right)
    setExitAnim(R.anim.slide_out_left)
    setPopEnterAnim(R.anim.slide_in_left)
    setPopExitAnim(R.anim.slide_out_right)
}.build()

