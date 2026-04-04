package com.mobileinvoice.ocr;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileinvoice.ocr.InvoiceAdapter;

/* loaded from: classes7.dex */
public class ItemMoveCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperContract contract;

    public interface ItemTouchHelperContract {
        void onRowClear(RecyclerView.ViewHolder viewHolder);

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(RecyclerView.ViewHolder viewHolder);
    }

    public ItemMoveCallback(ItemTouchHelperContract contract) {
        this.contract = contract;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(3, 0);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        this.contract.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != 0 && (viewHolder instanceof InvoiceAdapter.InvoiceViewHolder)) {
            this.contract.onRowSelected(viewHolder);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof InvoiceAdapter.InvoiceViewHolder) {
            this.contract.onRowClear(viewHolder);
        }
    }
}
