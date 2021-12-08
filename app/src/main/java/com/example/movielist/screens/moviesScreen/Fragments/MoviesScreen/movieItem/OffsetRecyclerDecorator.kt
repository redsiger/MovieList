package com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.movieItem

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OffsetRecyclerDecorator(private val marginTop: Int = 0,
                              private val marginBottom: Int = 0,
                              private val marginLeft: Int = 0,
                              private val marginRight: Int = 0,
                              private val layoutManager: RecyclerView.LayoutManager
): RecyclerView.ItemDecoration() {

    constructor(margin: Int = 0, layoutManager: RecyclerView.LayoutManager) : this(
        marginTop = margin,
        marginBottom = margin,
        marginLeft = margin,
        marginRight = margin,
        layoutManager = layoutManager
)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.top = getPx(marginTop, view.context)
        outRect.bottom = getPx(marginBottom, view.context)
        outRect.left = getPx(marginLeft, view.context)
        outRect.right = getPx(marginRight, view.context)

        if (layoutManager is LinearLayoutManager && layoutManager !is GridLayoutManager) {
//            view.layoutParams.width = -2
            val position = parent.getChildAdapterPosition(view)
                .let { if (it == RecyclerView.NO_POSITION) return else it }
            if (position == 0) outRect.left = getPx(0, view.context)
        }

        if (layoutManager is GridLayoutManager) {
            view.layoutParams.width = -1
        }
    }

    private fun getPx(dp: Int, context: Context): Int {
        return Math.round(
            dp * context.resources.displayMetrics.density
        )
    }
}