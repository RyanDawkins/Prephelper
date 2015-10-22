package com.ryanddawkins.prephelper.ui.items;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.ui.ItemCallback;

import java.util.List;

/**
 * Created by ryan on 10/10/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemHolder> {

    private List<Item> items;
    private ItemCallback<Integer> itemCallback;

    public ItemsAdapter(@NonNull List<Item> items, @NonNull ItemCallback<Integer> itemCallback) {
        this.items = items;
        this.itemCallback = itemCallback;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ItemHolder(view, this.itemCallback);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = this.items.get(position);
        holder.setPosition(position);
        holder.setNameViewText(item.getName());
        holder.setItemViewTag(R.id.item, item);
    }

    public void addItem(Item i) {
        this.items.add(i);
        notifyItemChanged(items.size() - 1);
    }

    public void addList(List<Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void replaceItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.items != null ? this.items.size() : 0;
    }
}
