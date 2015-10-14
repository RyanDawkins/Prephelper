package com.ryanddawkins.prephelper.data.storage.parse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ryanddawkins.prephelper.data.GetObjectCallback;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.pojo.PrepItem;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.ItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.RetrievalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 10/11/15.
 */
public class ParseItemStorageAdapter implements ItemStorageAdapter {

    private ParseUser currentUser;

    public ParseItemStorageAdapter() {
        this.currentUser = ParseUser.getCurrentUser();
    }

    @Override
    public List<Item> getItems(Prep prep) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("user", this.currentUser);
        query.whereEqualTo("prep", prep);

        try {
            return query.find();
        } catch (ParseException e) {
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override
    public void getItemsAsync(final GetObjectCallback<Item> callback, Prep prep) {
        ParseQuery<PrepItem> query = ParseQuery.getQuery(PrepItem.class);
        query.whereEqualTo("user", this.currentUser);
        query.whereEqualTo("prep", prep);

        Log.d("getItemsAsync", "User Object ID: " + this.currentUser.getObjectId());
        Log.d("getItemsAsync", "User Prep ID: "+prep.getObjectId());
        Log.d("getItemsAsync", "Prep Name: "+prep.getName());

        query.findInBackground(new FindCallback<PrepItem>() {
            @Override
            public void done(List<PrepItem> list, ParseException e) {
                if (e == null) {
                    Log.d("getItemsAsync", "List size: "+list.size());
                    getItemsFromPrepItems(callback, list);
                } else {
                    throw new RetrievalException(e.getMessage());
                }
            }
        });
    }

    @Override
    public List<Item> getItems() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("user", this.currentUser);

        try {
            return query.find();
        } catch (ParseException e) {
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override
    public void getItemsAsync(final GetAllCallback<Item> callback) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("user", this.currentUser);

        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> list, ParseException e) {
                if (e == null) {
                    callback.retrievedList(list);
                } else {
                    throw new RetrievalException(e.getMessage());
                }
            }
        });
    }

    @Override
    public void createItem(final Prep prep, final Item item) {

        ParseACL parseACL = new ParseACL(this.currentUser);

        item.put("user", this.currentUser);
        item.setACL(parseACL);

        PrepItem prepItem = new PrepItem();
        prepItem.put("user", this.currentUser);
        prepItem.setItem(item);
        prepItem.setPrep(prep);
        prepItem.setACL(parseACL);
        prepItem.saveInBackground();
    }

    private List<Item> getItemsFromPrepItems(final GetObjectCallback<Item> callback, List<PrepItem> prepItems) {

        final ArrayList<Item> items = new ArrayList<Item>(prepItems.size());

        for(PrepItem prepItem : prepItems) {
            Item item = prepItem.getItem();
            item.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    Item i = (Item) parseObject;
                    items.add(i);
                    callback.gotObjectCallback(i);
                }
            });
        }

        return items;
    }
}
