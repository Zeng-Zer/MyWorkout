package com.zeng.myworkout2.util

import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

abstract class DraggableAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    var longPressEnabled = true

    val callback = object : SimpleCallback(UP.or(DOWN), LEFT.or(RIGHT)) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            this@DraggableAdapter.onMove(recyclerView, viewHolder, target)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            this@DraggableAdapter.onSwiped(viewHolder, direction)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return longPressEnabled
        }

    }

    fun disableSwipe() {
        callback.setDefaultSwipeDirs(0)
    }

    fun enableSwipe() {
        callback.setDefaultSwipeDirs(LEFT.or(RIGHT))
    }

    fun disableDrag() {
        callback.setDefaultDragDirs(0)
    }

    fun enableDrag() {
        callback.setDefaultDragDirs(UP.or(DOWN))
    }

    fun enableLongPressDrag() {
        longPressEnabled = true
    }

    fun disableLongPressDrag() {
        longPressEnabled = false
    }

    abstract fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)

    abstract fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)


}