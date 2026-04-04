package com.mobileinvoice.ocr;

import android.graphics.Canvas;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileinvoice.ocr.RouteStopAdapter;

/* loaded from: classes7.dex */
public class RouteItemTouchHelper extends ItemTouchHelper.Callback {
    private final RouteStopAdapter adapter;
    private boolean itemMoved = false;
    private final OnItemMovedListener listener;

    public interface OnItemMovedListener {
        void onItemMoved(int fromPosition, int toPosition);
    }

    public RouteItemTouchHelper(RouteStopAdapter adapter, OnItemMovedListener listener) {
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int position;
        if (viewHolder instanceof RouteStopAdapter.HeaderViewHolder) {
            return makeMovementFlags(0, 0);
        }
        if ((viewHolder instanceof RouteStopAdapter.StopViewHolder) && (position = viewHolder.getAdapterPosition()) != -1 && this.adapter.isCompletedItem(position)) {
            return makeMovementFlags(0, 0);
        }
        return makeMovementFlags(3, 0);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition == -1 || toPosition == -1) {
            return false;
        }
        boolean moved = this.adapter.onItemMove(fromPosition, toPosition);
        if (moved) {
            this.itemMoved = true;
        }
        return moved;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != 0 && viewHolder != null) {
            viewHolder.itemView.setAlpha(0.7f);
            viewHolder.itemView.setScaleX(1.05f);
            viewHolder.itemView.setScaleY(1.05f);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
        if (this.itemMoved && this.listener != null) {
            this.listener.onItemMoved(-1, -1);
            this.itemMoved = false;
        }
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
