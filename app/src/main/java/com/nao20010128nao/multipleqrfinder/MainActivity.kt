package com.nao20010128nao.multipleqrfinder

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.google.zxing.Result
import com.nao20010128nao.multipleqrfinder.databinding.ActivityMainBinding
import com.nao20010128nao.multipleqrfinder.databinding.ItemNoneBinding
import com.nao20010128nao.multipleqrfinder.databinding.ItemQrcodeBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.list.also {
            it.layoutManager = LinearLayoutManager(this).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }
            it.adapter = Adapter(this)
        }
        binding.open.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).also { it.type = "image/*" }, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("Q $requestCode R $resultCode")
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            1 -> {
                val uri = data!!.data
                val bmp = contentResolver.openInputStream(uri).use { BitmapFactory.decodeStream(it) }
                binding.source = bmp
                val result = bmp.readQrCodes()
                (binding.list.adapter as? Adapter)?.list = result
            }
        }
    }

    class Adapter(private val context: MainActivity) : RecyclerView.Adapter<NullBindingViewHolder>() {
        var list: List<Result> by Delegates.observable(emptyList()) { _, _, _ ->
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NullBindingViewHolder =
                when (viewType) {
                    0 -> NoneItemViewHolder(ItemNoneBinding.inflate(context.layoutInflater, parent, false))
                    else -> QRCodeItemViewHolder(ItemQrcodeBinding.inflate(context.layoutInflater, parent, false))
                }

        override fun getItemCount(): Int = when (list.size) {
            0 -> 1
            else -> list.size
        }

        override fun getItemViewType(position: Int): Int = when (list.size) {
            0 -> 0
            else -> 1
        }

        override fun onBindViewHolder(holder: NullBindingViewHolder?, position: Int) {
            if (holder?.binding is ItemNoneBinding) {
                return
            }
            (holder?.binding as? ItemQrcodeBinding)?.also {
                val image = list[position]
                val rect = image.toRectangle()
                it.result = image
                it.cropped = context.binding.source?.crop(rect)
            }
        }
    }
}
