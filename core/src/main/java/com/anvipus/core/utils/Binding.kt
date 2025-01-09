package com.anvipus.core.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("app:load")
fun load(img: ShapeableImageView, url: String?) {
    if (url?.isNotEmpty() == true) img.load(url)
}