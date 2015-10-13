package com.ryanddawkins.prephelper.data.pojo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ryan on 10/10/15.
 */
@ParseClassName("prepItem")
public class PrepItem extends ParseObject {

    public Prep getPrep() {
        return (Prep)get("prep");
    }

    public PrepItem setPrep(Prep prep) {
        put("prep", prep);
        return this;
    }

    public Item getItem() {
        return (Item)get("item");
    }

    public PrepItem setItem(Item item) {
        put("item", item);
        return this;
    }

}
