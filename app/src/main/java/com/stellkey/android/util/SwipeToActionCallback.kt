package com.stellkey.android.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R

abstract class SwipeToActionCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)!!
    private val approveIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_check_white)!!

    private val background = GradientDrawable()
    private val deleteBackgroundColor = Color.parseColor("#FFEA3131")
    private val approveBackgroundColor = Color.parseColor("#FF6222EE")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 100

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            when {
                dX < 0 -> {
                    // Swiping to the left
                    val iconMargin: Int = (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconTop: Int =
                        itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconBottom: Int = iconTop + deleteIcon.intrinsicHeight
                    val iconLeft: Int = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                    val iconRight: Int = itemView.right - iconMargin
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                    background.apply {
                        cornerRadii = floatArrayOf(0F, 0F, 100F, 100F, 100F, 100F, 0F, 0F)
                        colors = intArrayOf(deleteBackgroundColor, deleteBackgroundColor)
                        orientation = GradientDrawable.Orientation.TOP_BOTTOM
                        gradientType = GradientDrawable.LINEAR_GRADIENT
                        setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                }
                dX > 0 -> {
                    // Swiping to the right
                    val iconMargin: Int = (itemView.height - approveIcon.intrinsicHeight) / 2
                    val iconTop: Int =
                        itemView.top + (itemView.height - approveIcon.intrinsicHeight) / 2
                    val iconBottom: Int = iconTop + approveIcon.intrinsicHeight
                    val iconLeft: Int = itemView.left + iconMargin
                    val iconRight: Int = itemView.left + iconMargin + approveIcon.intrinsicWidth
                    approveIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                    background.apply {
                        cornerRadii = floatArrayOf(100F, 100F, 100F, 100F, 100F, 100F, 100F, 100F)
                        colors = intArrayOf(approveBackgroundColor, approveBackgroundColor)
                        orientation = GradientDrawable.Orientation.TOP_BOTTOM
                        gradientType = GradientDrawable.LINEAR_GRADIENT
                        setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.bottom
                        )
                    }
                }
                else -> {
                    // view is unSwiped
                    background.setBounds(0, 0, 0, 0)
                }
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        background.draw(c)
        deleteIcon.draw(c)
        approveIcon.draw(c)
    }

    /*override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        background.apply {
            cornerRadii = floatArrayOf(0F, 0F, 100F, 100F, 100F, 100F, 0F, 0F)
            colors = intArrayOf(backgroundColor, backgroundColor)
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            gradientType = GradientDrawable.LINEAR_GRADIENT
            setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            draw(c)
        }

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }*/

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}