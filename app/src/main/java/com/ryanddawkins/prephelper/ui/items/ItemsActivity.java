package com.ryanddawkins.prephelper.ui.items;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseDrawerActivity;
import com.ryanddawkins.prephelper.data.GetObjectCallback;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.storage.AddedItemToPrepCallback;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.GetByIdCallback;
import com.ryanddawkins.prephelper.data.storage.parse.ParseItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.parse.ParsePrepStorageAdapter;
import com.ryanddawkins.prephelper.ui.ItemCallback;
import com.ryanddawkins.prephelper.ui.items.detail.ItemDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by ryan on 10/11/15.
 */
public class ItemsActivity extends BaseDrawerActivity implements ItemCallback<Integer>, GetObjectCallback<Item>, GetAllCallback<Item>, GetByIdCallback<Prep>, AddedItemToPrepCallback {

    public static String PREP = "prep";
    public static String ADD_NEW_ITEMS = "addNewItemsToPrep";

    @Nullable
    @Bind(R.id.items_list)
    protected RecyclerView itemsRecyclerView;

    @Nullable
    @Bind(R.id.add_items_fab)
    protected FloatingActionButton addItemsFab;

    // UI Related Elements
    private ItemsAdapter itemsAdapter;
    private ItemsActivity self;
    private ParseItemStorageAdapter itemStorageAdapter;
    private Menu mMenu;

    // Data Elements
    private List<Item> items;
    private Prep prep;

    // State Elements
    private boolean selectMode;
    private boolean addNewItemsToPrep;
    private HashMap<Integer, Boolean> selectedMap;
    private int itemsBeingAdded;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_items);

        this.self = this;
        this.itemStorageAdapter = new ParseItemStorageAdapter();

        // Code to get the prep
        Intent intent = this.getIntent();
        String prepId = intent.getStringExtra(PREP);

        // Check if this is expecting to add new items to a prep
        this.addNewItemsToPrep = intent.getBooleanExtra(ADD_NEW_ITEMS, false);

        this.itemsAdapter = new ItemsAdapter(new ArrayList<Item>(), this);
        if(this.itemsRecyclerView != null) {
            this.itemsRecyclerView.setAdapter(this.itemsAdapter);
            this.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        this.selectedMap = new HashMap<Integer, Boolean>();
        if(prepId != null) {
            this.enableBackNav();
            ParsePrepStorageAdapter parsePrepStorageAdapter = new ParsePrepStorageAdapter();
            parsePrepStorageAdapter.getPrepByIdAsync(this, prepId);
        } else {
            this.itemStorageAdapter.getItemsAsync(this);
        }
    }

    @Override public void onStart() {
        super.onStart();
        setTitle(getString(R.string.items_heading));

        if(this.prep != null) {
            this.itemsAdapter.replaceItems(new ArrayList<Item>());
            this.itemStorageAdapter.getItemsAsync(this, prep);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.selectedMap = null;
    }

    public void updateItems() {
        if(this.prep != null) {
            this.itemStorageAdapter.getItemsAsync(this, this.prep);
        } else {
            this.itemStorageAdapter.getItemsAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;

        this.updateMenu(R.menu.menu_main);

        return true;
    }

    public void updateMenu(@LayoutRes int menuId) {
        this.mMenu.clear();
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(menuId, this.mMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch(itemId) {
            case R.id.action_cancel:
                this.finish();
                break;

            case R.id.action_add_existing_item:
                Intent intent = new Intent(this, ItemsActivity.class);
                intent.putExtra(ADD_NEW_ITEMS, true);
                intent.putExtra(PREP, this.prep.getId());
                this.startActivity(intent);
                break;

            case R.id.action_add_to_prep:
                this.addSelectedItemsToExistingPrep();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addSelectedItemsToExistingPrep() {
        Set<Integer> keys = this.selectedMap.keySet();
        this.itemsBeingAdded = keys.size();
        for(int key : keys) {
            if(this.selectedMap.get(key)) {
                this.itemStorageAdapter.addToPrepAsync(this, this.prep, this.items.get(key));
            } else {
                this.itemsBeingAdded--;
            }
        }
        this.finish();
    }

    public void deselectViews() {
        Set<Integer> keys = this.selectedMap.keySet();
        for(int key : keys) {
            if(this.selectedMap.get(key)) {
                this.selectItem(true, key);
            }
        }
        this.selectedMap.clear();
    }

    public void selectItem(boolean wasSelected, int position) {

        Item item = this.items.get(position);
        this.selectedMap.put(position, !wasSelected);

        ItemHolder itemHolder = (ItemHolder) this.itemsRecyclerView.findViewHolderForAdapterPosition(position);

        if(wasSelected) {
            itemHolder.setTextColor(R.color.black);
            itemHolder.setBackgroundColor(Color.TRANSPARENT);
        } else {
            itemHolder.setBackgroundColorRes(R.color.accent_color);
            itemHolder.setTextColor(R.color.grey_white_1000);
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
    public void onItemClick(Integer position) {
        Item item = this.items.get(position);
        if(this.selectMode) {
            boolean wasSelected = false;
            if(this.selectedMap.containsKey(position)) {
                wasSelected = (boolean) this.selectedMap.get(position);
            }
            this.selectItem(wasSelected, position);
        } else {

            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra(ItemDetailActivity.ITEM, item.getId());
            this.startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(Integer position) {
//        if(!this.selectMode && this.prep == null) {
//
//            this.updateMenu(R.menu.menu_items_select_mode);
//            this.selectMode = true;
//            this.selectItem(false, position);
//        }
//        else {
//            Timber.d("this.selectMode: '"+this.selectMode+"'");
//            Timber.d("Prep == null : '"+(this.prep == null)+"'");
//        }
    }

    @Override
    public void gotById(Prep object) {
        this.prep = object;
        this.items = new ArrayList<Item>();
        this.showToast(this.prep.getName());
        if(this.addNewItemsToPrep) {
            Timber.d("looking for items not in prep");
            this.itemStorageAdapter.getItemsNotInPrepAsync(this, prep);
            this.selectMode = true;
            this.updateMenu(R.menu.menu_items_add_to_prep);
        } else {
            this.itemStorageAdapter.getItemsAsync(this, prep);
            this.updateMenu(R.menu.menu_items_add_new_items_to_prep);
        }
    }

    @Override
    public void gotObjectCallback(Item item) {
        this.itemsAdapter.addItem(item);
        this.items.add(item);
    }

    @Override
    public void retrievedList(List<Item> list) {
        this.items = list;
        this.itemsAdapter.replaceItems(list);
    }

    @Override
    public void addItemToPrep() {
        this.itemsBeingAdded--;
        if(this.itemsBeingAdded == 0) {
            this.finish();
        }
    }
}
