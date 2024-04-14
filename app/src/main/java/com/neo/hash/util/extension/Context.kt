package com.neo.hash.util.extension

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openPlayStore() = runCatching {
    openUrl("market://details?id=$packageName")
}.getOrElse {
    openUrl("https://play.google.com/store/apps/details?id=$packageName")
}

fun Context.openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))