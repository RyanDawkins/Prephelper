package com.ryanddawkins.prephelper.ui.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseDrawerActivity;
import com.ryanddawkins.prephelper.data.GetObjectCallback;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.GetByIdCallback;
import com.ryanddawkins.prephelper.data.storage.parse.ParseItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.parse.ParsePrepStorageAdapter;
import com.ryanddawkins.prephelper.ui.ItemCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ryan on 10/11/15.
 */
public class ItemsActivity extends BaseDrawerActivity implements ItemCallback<View>, GetObjectCallback<Item>, GetAllCallback<Item>, GetByIdCallback<Prep> {

    public static String PREP = "prep";

    @Nullable
    @Bind(R.id.items_list)
    protected RecyclerView itemsRecyclerView;

    @Nullable
    @Bind(R.id.add_items_fab)
    protected FloatingActionButton addItemsFab;

    private ItemsActivity self;
    private ParseItemStorageAdapter itemStorageAdapter;
    private Prep prep;
    private ItemsAdapter itemsAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_items);
        ButterKnife.bind(this, container);

        this.self = this;
        this.itemStorageAdapter = new ParseItemStorageAdapter();

        // Code to get the prep
        Intent intent = this.getIntent();
        String prepId = intent.getStringExtra(PREP);

        this.itemsAdapter = new ItemsAdapter(new ArrayList<Item>(), this);
        if(this.itemsRecyclerView != null) {
            this.itemsRecyclerView.setAdapter(this.itemsAdapter);
            this.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if(prepId != null) {
            ParsePrepStorageAdapter parsePrepStorageAdapter = new ParsePrepStorageAdapter();
            parsePrepStorageAdapter.getPrepByIdAsync(this, prepId);
        } else {
            this.itemStorageAdapter.getItemsAsync(this);
        }
    }

    @Override public void onStart() {
        super.onStart();
        setTitle(getString(R.string.items_heading));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void updateItems() {
        if(this.prep != null) {
            this.itemStorageAdapter.getItemsAsync(this, this.prep);
        } else {
            this.itemStorageAdapter.getItemsAsync(this);
        }
    }

    @Nullable @OnClick(R.id.add_items_fab)
    public void addItemsClick() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Create new Item")
                .content("What's the name of your item?")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String itemName = input.toString();
                        Item item = new Item();
                        item.setName(itemName);
                        self.itemStorageAdapter.createItem(self.prep, item);
                        itemsAdapter.addItem(item);
                    }
                }).build();
        materialDialog.show();
    }


    @Override
    public void onItemClick(View view) {
        Item item = (Item) view.getTag(R.id.item);
        this.showToast(item.getName());
    }

    @Override
    public void onItemLongClick(View view) {
        // Nothing to do here...
        // Yet.
    }

    @Override
    public void gotById(Prep object) {
        this.prep = object;
        this.showToast(this.prep.getName());
        this.itemStorageAdapter.getItemsAsync(this, prep);
    }

    @Override
    public void gotObjectCallback(Item item) {
        this.itemsAdapter.addItem(item);
    }

    @Override
    public void retrievedList(List<Item> list) {
        this.itemsAdapter.replaceItems(list);
    }
}
