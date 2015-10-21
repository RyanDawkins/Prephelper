package com.ryanddawkins.prephelper.ui.items;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.ui.ItemCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ryan on 10/10/15.
 */
public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private View itemView;
    private ItemCallback<View> itemCallback;

    @Nullable @Bind(R.id.name)
    protected TextView nameTextView;

    public ItemHolder(View itemView, ItemCallback<View> itemCallback) {
        super(itemView);

        this.itemCallback = itemCallback;

        ButterKnife.bind(this, itemView);

        this.itemView = itemView;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public ItemHolder(View itemView) {
        this(itemView, null);
    }

    @Override
    public void onClick(View v) {
        this.itemCallback.onItemClick(v);
    }

    @Override
    public boolean onLongClick(View v) {
        this.itemCallback.onItemLongClick(v);
        return true;
    }

    public void setNameViewText(String text) {
        this.nameTextView.setText(text);
    }

    public void setItemViewTag(int id, Item item) {
        if(this.itemView != null) {
            this.itemView.setTag(id, item);
        }
    }
}
