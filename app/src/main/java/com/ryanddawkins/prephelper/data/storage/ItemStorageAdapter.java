package com.ryanddawkins.prephelper.data.storage;

import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;

import java.util.List;

/**
 * Created by ryan on 10/11/15.
 */
public interface ItemStorageAdapter {

    List<Item> getItems(Prep prep);

    void getItemsAsync(final GetAllCallback<Item> callback, Prep prep);

    List<Item> getItems();

    void getItemsAsync(final GetAllCallback<Item> callback);

    void createItem(final Prep prep, final Item item);

}
