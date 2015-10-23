package com.ryanddawkins.prephelper.data.pojo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ryan on 9/27/15.
 */
@ParseClassName("Category")
public class Category extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public Category setName(String name) {
        put("name", name);
        return this;
    }

    public String getId() {
        return this.getObjectId();
    }

}
