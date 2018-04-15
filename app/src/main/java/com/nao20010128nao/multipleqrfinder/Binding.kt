package com.nao20010128nao.multipleqrfinder

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.widget.ImageView

@BindingAdapter("android:src")
fun ImageView.setImageBitmapBND(bmp: Bitmap) {
    setImageBitmap(bmp)
}
