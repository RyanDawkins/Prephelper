package com.ryanddawkins.prephelper.data.storage.parse;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.GetByIdCallback;
import com.ryanddawkins.prephelper.data.storage.PrepStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.RetrievalException;
import com.ryanddawkins.prephelper.data.storage.SaveException;

import java.util.List;

/**
 * Created by ryan on 10/11/15.
 */
public class ParsePrepStorageAdapter implements PrepStorageAdapter {

    private ParseUser currentUser;

    public ParsePrepStorageAdapter() {
        this.currentUser = ParseUser.getCurrentUser();
    }

    @Override
    public List<Prep> getPreps() {

        ParseQuery<Prep> query = ParseQuery.getQuery(Prep.class);
        query.whereEqualTo("user", this.currentUser);

        try {
            return query.find();
        } catch (ParseException e) {
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override public void getPrepsAsync(final GetAllCallback<Prep> callback) {
        ParseQuery<Prep> query = ParseQuery.getQuery(Prep.class);
        query.whereEqualTo("user", this.currentUser);

        query.findInBackground(new FindCallback<Prep>() {
            @Override
            public void done(List<Prep> list, ParseException e) {
                if (e == null) {
                    callback.retrievedList(list);
                } else {
                    throw new RetrievalException(e.getMessage());
                }
            }
        });
    }

    @Override
    public void createPrep(Prep prep) {

        prep.put("user", this.currentUser);

        // Restricting access to only this user.
        prep.setACL(new ParseACL(this.currentUser));


        try {
            prep.save();
        } catch (ParseException e) {
            throw new SaveException(e.getMessage());
        }

    }

    @Override
    public void deletePrep(Prep prep) {
        prep.deleteInBackground();
    }

    @Override
    public Prep getPrepById(String id) {
        ParseQuery<Prep> query = ParseQuery.getQuery(Prep.class);
        try {
            return query.get(id);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void getPrepByIdAsync(final GetByIdCallback<Prep> callback, String id) {
        ParseQuery<Prep> query = ParseQuery.getQuery(Prep.class);
        query.getInBackground(id, new GetCallback<Prep>() {
            @Override
            public void done(Prep prep, ParseException e) {
                if(e == null) {
                    callback.gotById(prep);
                } else {
                    throw new RetrievalException(e.getMessage());
                }
            }
        });
    }

}