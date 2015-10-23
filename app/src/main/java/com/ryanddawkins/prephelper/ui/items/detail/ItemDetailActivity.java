package com.ryanddawkins.prephelper.ui.items.detail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;
import com.ryanddawkins.prephelper.data.pojo.Category;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.storage.CategoryStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.GetByIdCallback;
import com.ryanddawkins.prephelper.data.storage.ItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.parse.ParseCategoryStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.parse.ParseItemStorageAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ryan on 10/22/15.
 */
public class ItemDetailActivity extends BaseActivity implements GetAllCallback<Category>, GetByIdCallback<Item>, DatePickerDialog.OnDateSetListener {

    public static String ITEM = "item";

    @Nullable
    @Bind(R.id.itemName)
    protected EditText itemNameEditText;

    @Nullable
    @Bind(R.id.categorySpinner)
    protected Spinner categorySpinner;

    @Nullable
    @Bind(R.id.expirationDate)
    protected EditText expirationDateEditText;

    private CategoryStorageAdapter categoryStorageAdapter;
    private ItemStorageAdapter itemStorageAdapter;

    private HashMap<String, Integer> categoryIdHashmap;
    private List<Category> categoryList;
    private Item item;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        this.addLayoutToContainer(R.layout.activity_item_detail);

        this.setTitle("Edit Item");

        this.enableBackNav();

        Intent intent = this.getIntent();
        String itemId = intent.getStringExtra(ITEM);

        this.itemStorageAdapter = new ParseItemStorageAdapter();
        itemStorageAdapter.getItemAsync(this, itemId);

        this.categoryStorageAdapter = new ParseCategoryStorageAdapter();
        this.categoryStorageAdapter.getCategoriesAsync(this);

        this.categoryIdHashmap = new HashMap<String, Integer>();
    }

    @Nullable
    @OnClick(R.id.saveItemBtn)
    public void onSaveBtnClick() {

        // Category logic...
        int categoryPosition = (this.categorySpinner != null) ? this.categorySpinner.getSelectedItemPosition() : 0;
        Category category = null;
        if(categoryPosition != 0) {
            category = this.categoryList.get(categoryPosition - 1);
        }

        // Item Name
        String name = (this.itemNameEditText != null) ? this.itemNameEditText.getText().toString() : "";

        // Saving Values
        item.setName(name);
        item.setCategory(category);
        this.itemStorageAdapter.saveItem(item);
    }

    @Nullable
    @OnClick(R.id.expirationDate)
    public void onClick() {
        DatePickerFragment datePickerFragment = new DatePickerFragment(this);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        if(this.expirationDateEditText != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.expirationDateEditText.setText(formatter.format(calendar.getTime()));
        }
    }

    public void loadCategorySpinner() {

        String[] categoryStrings = new String[this.categoryList.size()+1];
        categoryStrings[0] = "Category";

        int i = 1;
        for(Category category : this.categoryList) {
            categoryStrings[i] = category.getName();

            // This is needed for setting the value when loading a category.
            this.categoryIdHashmap.put(category.getId(), i);

            i++;
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                categoryStrings
                );
        if(this.categorySpinner != null) {
            this.categorySpinner.setAdapter(spinnerArrayAdapter);

            if(this.item != null && this.item.getCategory() != null) {
                this.categorySpinner.setSelection(this.categoryIdHashmap.get(this.item.getCategory().getId()));
            }
        }
    }

    public void loadItem() {
        if(this.itemNameEditText != null) {
            this.itemNameEditText.setText(this.item.getName());
        }

        if(this.categorySpinner != null && this.item.getCategory() != null && this.categoryList != null && this.categoryList.size() > 0) {
            this.categorySpinner.setSelection(this.categoryIdHashmap.get(this.item.getCategory().getId()));
        }
    }

    @Override
    public void retrievedList(List<Category> list) {
        this.categoryList = list;
        loadCategorySpinner();
    }

    @Override
    public void gotById(Item object) {
        this.item = object;
        this.loadItem();
    }
}
