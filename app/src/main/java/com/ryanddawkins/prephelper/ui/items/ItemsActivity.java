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
import com.ryanddawkins.prephelper.base.BaseActivity;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.GetByIdCallback;
import com.ryanddawkins.prephelper.data.storage.parse.ParseItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.parse.ParsePrepStorageAdapter;
import com.ryanddawkins.prephelper.ui.ItemCallback;
import com.ryanddawkins.prephelper.ui.preps.PrepsAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ryan on 10/11/15.
 */
public class ItemsActivity extends BaseActivity implements ItemCallback<View>, GetAllCallback<Item>, GetByIdCallback<Prep> {

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

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_items);
        ButterKnife.bind(this, container);

        setTitle(getString(R.string.preps_heading));

        this.self = this;
        this.itemStorageAdapter = new ParseItemStorageAdapter();

        // Code to get the prep
        Intent intent = this.getIntent();
        String prepId = intent.getStringExtra(PREP);

        this.itemsRecyclerView.setAdapter(new PrepsAdapter(null, this));
        this.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(prepId != null) {
            ParsePrepStorageAdapter parsePrepStorageAdapter = new ParsePrepStorageAdapter();
            parsePrepStorageAdapter.getPrepByIdAsync(this, prepId);
        } else {
            this.updateItems();
        }
    }

    public void updateItems() {
        if(this.prep != null) {
            this.itemStorageAdapter.getItemsAsync(this, this.prep);
        } else {
            this.itemStorageAdapter.getItemsAsync(this);
        }
    }

    public void updateListAdapter(List<Item> items) {
        if(this.itemsRecyclerView != null) {
            this.itemsRecyclerView.setAdapter(new ItemsAdapter(items, this));
            this.itemsRecyclerView.getAdapter().notifyDataSetChanged();
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
                        self.updateItems();
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
    public void retrievedList(List<Item> list) {
        this.updateListAdapter(list);
    }

    @Override
    public void gotById(Prep object) {
        this.prep = object;
        this.showToast(this.prep.getName());
        this.updateItems();
    }
}
