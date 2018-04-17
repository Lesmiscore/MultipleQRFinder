package com.nao20010128nao.multipleqrfinder

import android.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.zxing.Result
import com.nao20010128nao.multipleqrfinder.databinding.ItemNoneBinding
import com.nao20010128nao.multipleqrfinder.databinding.ItemQrcodeBinding


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

class BindingViewHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
typealias NoneItemViewHolder = BindingViewHolder<ItemNoneBinding>
typealias QRCodeItemViewHolder = BindingViewHolder<ItemQrcodeBinding>
typealias NullBindingViewHolder = BindingViewHolder<*>

fun RectF.expand(factor: Double): RectF {
    val w = ((right - left) * (factor - 1)).toFloat()
    val h = ((bottom - top) * (factor - 1)).toFloat()
    return RectF(left + w, top + h, right + w, bottom + h)
}

fun Bitmap.crop(rectF: RectF): Bitmap = Bitmap.createBitmap(
        this,
        rectF.left.toInt(),
        rectF.top.toInt(),
        (rectF.right - rectF.left).toInt(),
        (rectF.bottom - rectF.top).toInt()
)
