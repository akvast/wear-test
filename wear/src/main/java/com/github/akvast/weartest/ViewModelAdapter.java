package com.github.akvast.weartest;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ViewModelAdapter extends RecyclerView.Adapter<ViewModelAdapter.ViewHolder> {

    private final Map<Integer, Object> mGlobalObjects = new Hashtable<>();

    private final Map<Class, CellInfo> mCellInfoMap = new HashMap<>();

    protected final List<Object> mItems = new LinkedList<>();

    private int mBeginUpdateItemsSize = 0;

    protected void addGlobalItem(int bindingId, Object object) {
        mGlobalObjects.put(bindingId, object);
    }

    protected void registerCell(@NonNull Class objectClass, @LayoutRes int layoutId, int bindingId) {
        CellInfo cellInfo = new CellInfo();
        cellInfo.mLayoutId = layoutId;
        cellInfo.mBindingId = bindingId;
        mCellInfoMap.put(objectClass, cellInfo);
    }

    public void reload() {
        reload(null);
    }

    public abstract void reload(@Nullable SwipeRefreshLayout refreshLayout);

    protected void loadMore() {

    }

    protected void beginUpdates() {
        mBeginUpdateItemsSize = mItems.size();
    }

    protected void endUpdates() {
        int changed = Math.min(mBeginUpdateItemsSize, mItems.size());
        int diff = Math.max(mBeginUpdateItemsSize, mItems.size()) - changed;

        if (diff == 0 && changed > 1) {
            notifyDataSetChanged();
            return;
        }

        if (changed != 0) notifyItemRangeChanged(0, changed);

        if (diff > 0) {
            if (mBeginUpdateItemsSize > mItems.size()) {
                notifyItemRangeRemoved(changed, diff);
            } else {
                notifyItemRangeInserted(changed, diff);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mCellInfoMap.get(mItems.get(position).getClass()).mLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        ViewDataBinding binding = holder.getBinding();
        for (Map.Entry<Integer, Object> entry : mGlobalObjects.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CellInfo cellInfo = mCellInfoMap.get(mItems.get(position).getClass());

        if (cellInfo.mBindingId != 0) {
            ViewDataBinding binding = holder.getBinding();
            binding.setVariable(cellInfo.mBindingId, mItems.get(position));
        }

        if (position == mItems.size() - 2) loadMore();
    }

    private static class CellInfo {
        @LayoutRes
        private int mLayoutId;
        private int mBindingId;
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding mBinding;

        ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        ViewDataBinding getBinding() {
            return mBinding;
        }

    }

}