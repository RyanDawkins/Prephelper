package com.ryanddawkins.prephelper.data.storage.parse;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.pojo.PrepItem;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.ItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.RetrievalException;

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
        ParseQuery<Item> query = this.getBaseQuery();
        query.whereEqualTo("prep", prep);

        try {
            return query.find();
        } catch (ParseException e) {
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override
    public void getItemsAsync(final GetAllCallback<Item> callback, Prep prep) {
        ParseQuery<Item> query = this.getBaseQuery();
        query.whereEqualTo("prep", prep);

        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> list, ParseException e) {
                if(e == null) {
                    callback.retrievedList(list);
                } else {
                    throw new RetrievalException(e.getMessage());
                }
            }
        });
    }

    @Override
    public List<Item> getItems() {
        ParseQuery<Item> query = this.getBaseQuery();

        try {
            return query.find();
        } catch (ParseException e) {
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override
    public void getItemsAsync(final GetAllCallback<Item> callback) {
        ParseQuery<Item> query = this.getBaseQuery();

        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> list, ParseException e) {
                if(e == null) {
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
        prepItem.setItem(item);
        prepItem.setPrep(prep);
        prepItem.setACL(parseACL);
        prepItem.saveInBackground();
    }

    private ParseQuery<Item> getBaseQuery() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("user", this.currentUser);
        return query;
    }
}
