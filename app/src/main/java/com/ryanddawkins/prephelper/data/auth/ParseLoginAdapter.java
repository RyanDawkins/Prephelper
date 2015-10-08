package com.ryanddawkins.prephelper.data.auth;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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

        ParseUser user = ParseUser.getCurrentUser();

        return user != null && user.isAuthenticated();
    }

    @Override public void logout() {
        ParseUser.getCurrentUser().logOut();
    }
}
