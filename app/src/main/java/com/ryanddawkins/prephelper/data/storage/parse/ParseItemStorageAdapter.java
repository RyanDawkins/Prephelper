package com.ryanddawkins.prephelper.data.storage.parse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ryanddawkins.prephelper.data.GetObjectCallback;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.pojo.PrepItem;
import com.ryanddawkins.prephelper.data.storage.AddedItemToPrepCallback;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.GetByIdCallback;
import com.ryanddawkins.prephelper.data.storage.ItemStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.RetrievalException;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ryan on 10/11/15.
 */
public class ParseItemStorageAdapter implements ItemStorageAdapter {

    private ParseUser currentUser;

    public ParseItemStorageAdapter() {
        this.currentUser = ParseUser.getCurrentUser();
    }

    public ParseUser getCurrentUser() {
        if(this.currentUser == null) {
            this.currentUser = ParseUser.getCurrentUser();
        }
        return this.currentUser;
    }

    @Override
    public List<Item> getItems(Prep prep) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("user", this.getCurrentUser());
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
        query.whereEqualTo("user", this.getCurrentUser());
        query.whereEqualTo("prep", prep);

        Log.d("getItemsAsync", "User Object ID: " + this.getCurrentUser().getObjectId());
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
        query.whereEqualTo("user", this.getCurrentUser());

        try {
            return query.find();
        } catch (ParseException e) {
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override
    public void getItemsAsync(final GetAllCallback<Item> callback) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("user", this.getCurrentUser());

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

        ParseACL parseACL = new ParseACL(this.getCurrentUser());

        item.put("user", this.getCurrentUser());
        item.setACL(parseACL);

        PrepItem prepItem = new PrepItem();
        prepItem.put("user", this.getCurrentUser());
        prepItem.setItem(item);
        if(prep != null) {
            prepItem.setPrep(prep);
        }
        prepItem.setACL(parseACL);
        prepItem.saveInBackground();
    }

    @Override
    public void saveItem(Item item) {
        item.saveInBackground();
    }

    @Override
    public void getItemsNotInPrepAsync(final GetObjectCallback<Item> callback, Prep prep) {

        ParseQuery<PrepItem> prepItemParseQuery = ParseQuery.getQuery(PrepItem.class);
        prepItemParseQuery.whereEqualTo("user", this.getCurrentUser());
        prepItemParseQuery.whereEqualTo("prep", prep);

        prepItemParseQuery.findInBackground(new FindCallback<PrepItem>() {
            @Override
            public void done(List<PrepItem> list, ParseException e) {
                if(e != null) {
                    throw new RetrievalException(e.getMessage());
                }

                ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);
                itemParseQuery.whereEqualTo("user", getCurrentUser());

                ArrayList<String> items = new ArrayList<String>();
                for(PrepItem prepItem : list) {
                    items.add(prepItem.getItem().getObjectId());
                }
                itemParseQuery.whereNotContainedIn("objectId", items);

                itemParseQuery.findInBackground(new FindCallback<Item>() {
                    @Override
                    public void done(List<Item> list, ParseException e) {
                        if (e == null) {
                            Log.d("getItemsAsync", "List size: "+list.size());
                            for(Item item : list) {
                                callback.gotObjectCallback(item);
                            }
                        } else {
                            throw new RetrievalException(e.getMessage());
                        }
                    }
                });
            }
        });

    }

    @Override
    public void addToPrepAsync(final AddedItemToPrepCallback callback, Prep prep, Item item) {

        PrepItem prepItem = new PrepItem();
        prepItem.setPrep(prep);
        prepItem.setItem(item);
        prepItem.put("user", this.getCurrentUser());

        ParseACL parseACL = new ParseACL(this.getCurrentUser());
        prepItem.setACL(parseACL);

        prepItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.addItemToPrep();
                if(e != null) {
                    Timber.e(e.getMessage());
                }
            }
        });

    }

    @Override
    public void getItemAsync(final GetByIdCallback<Item> callback, String itemId) {
        ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);
        itemParseQuery.getInBackground(itemId, new GetCallback<Item>() {
            @Override
            public void done(Item item, ParseException e) {
                callback.gotById(item);
            }
        });
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
