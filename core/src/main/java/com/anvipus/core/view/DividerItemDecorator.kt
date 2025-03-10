package com.anvipus.core.view

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator(private val divider: Drawable?) : RecyclerView.ItemDecoration(){

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 2) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + (divider?.intrinsicHeight ?: 0)

            divider?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider?.draw(c)
        }
    }
}