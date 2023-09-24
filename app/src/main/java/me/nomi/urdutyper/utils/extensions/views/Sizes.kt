package me.nomi.urdutyper.utils.extensions.views

import android.content.res.Resources

val Int.dp
    get() = Resources.getSystem().displayMetrics.density * this

val Int.sp
    get() = Resources.getSystem().displayMetrics.scaledDensity * this
