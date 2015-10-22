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
import com.ryanddawkins.prephelper.data.storage.SaveCallback;
import com.ryanddawkins.prephelper.data.storage.SaveException;

import java.util.List;

import timber.log.Timber;

/**
 * Created by ryan on 10/11/15.
 */
public class ParsePrepStorageAdapter implements PrepStorageAdapter {

    private ParseUser currentUser;

    public ParsePrepStorageAdapter() {
        this.currentUser = ParseUser.getCurrentUser();
    }

    public ParseUser getCurrentUser() {
        if(this.currentUser == null) {
            this.currentUser = ParseUser.getCurrentUser();
        }
        return this.currentUser;
    }

    @Override
    public List<Prep> getPreps() {

        ParseQuery<Prep> query = ParseQuery.getQuery(Prep.class);
        query.whereEqualTo("user", this.getCurrentUser());

        try {
            return query.find();
        } catch (ParseException e) {
            Timber.e(e.getMessage());
            throw new RetrievalException(e.getMessage());
        }
    }

    @Override public void getPrepsAsync(final GetAllCallback<Prep> callback) {
        ParseQuery<Prep> query = ParseQuery.getQuery(Prep.class);
        query.whereEqualTo("user", this.getCurrentUser());

        query.findInBackground(new FindCallback<Prep>() {
            @Override
            public void done(List<Prep> list, ParseException e) {
                if (e == null) {
                    callback.retrievedList(list);
                } else {
                    Timber.e(e.getMessage());
                    throw new RetrievalException(e.getMessage());
                }
            }
        });
    }

    @Override
    public void createPrep(Prep prep) {

        prep.put("user", this.getCurrentUser());

        // Restricting access to only this user.
        prep.setACL(new ParseACL(this.getCurrentUser()));


        try {
            prep.save();
        } catch (ParseException e) {
            Timber.e(e.getMessage());
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
            Timber.e(e.getMessage());
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

    @Override
    public void savePrep(Prep prep) {

        try {
            prep.save();
        } catch (ParseException e) {
            Timber.e(e.getMessage());
        }
    }

    public void savePrepAsync(Prep prep) {
        prep.saveInBackground();
    }

    public void savePrepAsync(Prep prep, final SaveCallback saveCallback) {
        prep.saveInBackground(new com.parse.SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    saveCallback.finishedSaving();
                } else {
                    Timber.e(e.getMessage());
                    throw new SaveException(e.getMessage());
                }
            }
        });
    }

}
