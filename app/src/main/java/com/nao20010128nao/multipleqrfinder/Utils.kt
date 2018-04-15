package com.nao20010128nao.multipleqrfinder

import android.graphics.Bitmap
import android.graphics.RectF
import com.google.zxing.Result


inline fun <T> Bitmap.res(aa: Bitmap.(Bitmap) -> T): T {
    try {
        return aa(this, this)
    } finally {
        recycle()
    }
}

fun Result.toRectangle(): RectF {
    require(resultPoints.isNotEmpty())
    val x = resultPoints.map { it.x }
    val y = resultPoints.map { it.y }
    val l = x.min()!!
    val t = y.min()!!
    val r = x.max()!!
    val b = y.max()!!
    return RectF(l, t, r, b)
}
