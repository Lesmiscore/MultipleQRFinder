package com.nao20010128nao.multipleqrfinder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import java.io.InputStream

fun InputStream.readQrCodes(): List<Result> = use {
    BitmapFactory.decodeStream(it).res {
        readQrCodes()
    }
}

fun Bitmap.readQrCodes(): List<Result> {
    val bitmap = BinaryBitmap(HybridBinarizer(toLuminanceSource()))
    val reader = QRCodeMultiReader()
    return try {
        reader.decodeMultiple(bitmap).asList()
    } catch (e: Exception) {
        emptyList()
    }
}

fun Bitmap.toLuminanceSource(): LuminanceSource {
    return forceConfig(Bitmap.Config.ARGB_8888).res {
        val intArray = IntArray(width * height)
        getPixels(intArray, 0, width, 0, 0, width, height)

        RGBLuminanceSource(width, height, intArray)
    }
}

fun Bitmap.forceConfig(config: Bitmap.Config): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(bmp)
    canvas.drawBitmap(this, 0f, 0f, Paint())
    return bmp
}
