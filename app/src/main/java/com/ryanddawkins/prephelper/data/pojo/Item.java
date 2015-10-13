package com.ryanddawkins.prephelper.data.pojo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ryan on 9/27/15.
 */
@ParseClassName("item")
public class Item extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public Item setName(String name) {
        put("name", name);
        return this;
    }

    public Category getCategory() {
        return (Category)get("category");
    }

    public Item setCategory(Category category) {
        put("category", category);
        return this;
    }
}
