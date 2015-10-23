package com.ryanddawkins.prephelper.data.storage;

import com.ryanddawkins.prephelper.data.pojo.Category;

/**
 * Created by ryan on 10/22/15.
 */
public interface CategoryStorageAdapter {

    void getCategoriesAsync(GetAllCallback<Category> categoryGetAllCallback);

}
