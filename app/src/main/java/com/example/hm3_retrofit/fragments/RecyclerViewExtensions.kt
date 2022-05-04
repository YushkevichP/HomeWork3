package com.example.hm3_retrofit.fragments

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.addSpaceDecoration(bottomSpace: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            val itemCount = parent.adapter?.itemCount ?: return // количество эл-ов
            val position =
                parent.getChildAdapterPosition(view) // позиция для которой нужно отрисовка
            if (position != (itemCount - 1)) {
                outRect.bottom = bottomSpace
            }
        }
    })
}

