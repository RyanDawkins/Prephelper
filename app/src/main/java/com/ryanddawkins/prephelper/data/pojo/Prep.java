package com.ryanddawkins.prephelper.data.pojo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ryan on 9/27/15.
 */
@ParseClassName("prep")
public class Prep extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public Prep setName(String name) {
        put("name", name);
        return this;
    }

    public String getId() {
        return super.getObjectId();
    }

}
