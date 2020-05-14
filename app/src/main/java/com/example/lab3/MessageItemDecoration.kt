package com.example.lab3

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MessageItemDecoration(
    private val marginVertical: Int,
    private val marginHorizontal: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        if (position == RecyclerView.NO_POSITION) {
            outRect.set(0, 0, 0, 0)
            return
        }
        outRect.left=marginHorizontal
        outRect.right=marginHorizontal
        outRect.top=if (position == 0) marginVertical*2 else marginVertical
        outRect.bottom=if (position == itemCount-1) marginVertical*2 else marginVertical
    }
}