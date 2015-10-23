package com.ryanddawkins.prephelper.data.storage.parse;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.ryanddawkins.prephelper.data.pojo.Category;
import com.ryanddawkins.prephelper.data.storage.CategoryStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;

import java.util.List;

/**
 * Created by ryan on 10/22/15.
 */
public class ParseCategoryStorageAdapter implements CategoryStorageAdapter {

    public void getCategoriesAsync(final GetAllCallback<Category> categoryGetAllCallback) {

        ParseQuery<Category> categoryParseQuery = ParseQuery.getQuery(Category.class);

        categoryParseQuery.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> list, ParseException e) {
                categoryGetAllCallback.retrievedList(list);
            }
        });
    }

}
