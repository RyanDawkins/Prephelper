package com.ryanddawkins.prephelper.data.auth.parse;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.ryanddawkins.prephelper.data.auth.LoginAdapter;
import com.ryanddawkins.prephelper.data.auth.LoginCallback;
import com.ryanddawkins.prephelper.data.auth.User;

/**
 * Created by ryan on 10/1/15.
 */
public class ParseLoginAdapter implements LoginAdapter {

    @Override public boolean login(String email, String password) {
        try {
            ParseUser.logIn(email, password);
        } catch (ParseException e) {
            return false;
        }

        return ParseUser.getCurrentUser() != null;
    }

    @Override public void loginAsync(final LoginCallback mLoginCallback, String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(parseUser != null) {
                    mLoginCallback.loginSuccess();
                } else {
                    mLoginCallback.loginFailed();
                    Log.e("ParseLoginAdapter", e.getMessage());
                }
            }
        });
    }

    @Override public boolean isLoggedIn() {
        return ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().isAuthenticated();
    }

    @Override public void logout() {
        if(ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override public User getUser() {
        if(this.isLoggedIn()) {
            return new ParseUserAdapter(ParseUser.getCurrentUser());
        } else {
            return null;
        }
    }

}
