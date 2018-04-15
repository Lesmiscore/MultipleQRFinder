package com.nao20010128nao.multipleqrfinder

import android.graphics.Bitmap


inline fun <T> Bitmap.res(aa: Bitmap.(Bitmap) -> T): T {
    try {
        return aa(this, this)
    } finally {
        recycle()
    }
}
