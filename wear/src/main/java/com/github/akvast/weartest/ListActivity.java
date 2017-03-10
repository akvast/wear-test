package com.github.akvast.weartest;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.github.akvast.weartest.databinding.ActivityListBinding;

public final class ListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        ListAdapter adapter = new ListAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setCenterEdgeItems(true);

        adapter.reload(null);
    }

    private class ListAdapter extends ViewModelAdapter {

        public ListAdapter() {
            registerCell(Item.class, R.layout.cell_item, BR.vm);
        }

        @Override
        public void reload(@Nullable SwipeRefreshLayout refreshLayout) {
            beginUpdates();
            mItems.add(new Item("First item"));
            mItems.add(new Item("Second item"));
            mItems.add(new Item("Third item"));
            mItems.add(new Item("Fourth item"));
            mItems.add(new Item("Fifth item"));
            endUpdates();
        }

    }

    public static class Item {

        private final String mName;

        private Item(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

    }

}
