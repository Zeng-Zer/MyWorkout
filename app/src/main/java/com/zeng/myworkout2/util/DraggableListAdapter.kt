package com.zeng.myworkout2.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class DraggableListAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>)
    : ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    private var longPressEnabled = false

    val callback = object : SimpleCallback(0, 0) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return this@DraggableListAdapter.onMove(recyclerView, viewHolder, target)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            this@DraggableListAdapter.onSwiped(viewHolder, direction)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return longPressEnabled
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            this@DraggableListAdapter.clearView(recyclerView, viewHolder)
        }

    }

    fun disableSwipe() {
        callback.setDefaultSwipeDirs(0)
    }

    fun enableSwipe() {
        callback.setDefaultSwipeDirs(LEFT.or(RIGHT))
    }

    fun disableDrag() {
        longPressEnabled = false
        callback.setDefaultDragDirs(0)
    }

    fun enableDrag() {
        longPressEnabled = true
        callback.setDefaultDragDirs(UP.or(DOWN))
    }

    open fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    open fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    open fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
    }


}