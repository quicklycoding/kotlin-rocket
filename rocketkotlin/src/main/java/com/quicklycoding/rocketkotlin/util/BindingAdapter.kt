package com.quicklycoding.rocketkotlin.util

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


@BindingAdapter("startMarquee")
fun startMarquee(textView: TextView, _isSelected: Boolean) {
    with(textView) {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        isSelected = _isSelected
        isSingleLine = true
    }
}

@BindingAdapter("setLayoutManager")
fun setLayoutManager(recyclerView: RecyclerView, orientation: Int) {
    recyclerView.layoutManager =
        LinearLayoutManager(recyclerView.context, orientation, false)
}

@BindingAdapter("loadImageFromURL")
fun loadImageFromURL(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view).load(url).into(view)
        view.imageTintList = null
    }
}

@BindingAdapter("imageResource")
fun imageResource(view: ImageView, resource: Int) {
    view.setImageResource(resource)
}

