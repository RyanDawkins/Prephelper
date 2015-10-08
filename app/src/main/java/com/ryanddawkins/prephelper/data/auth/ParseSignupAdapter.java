package com.ryanddawkins.prephelper.data.auth;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by ryan on 10/6/15.
 */
public class ParseSignupAdapter implements SignupAdapter {


    @Override public boolean signup(String email, String password) {

        ParseUser user = this.setupUser(email, password);

        try {
            user.signUp();
            return true;
        } catch(ParseException e) {
            return false;
        }

    }

    @Override public void signupAsync(final SignupCallback mSignupCallback, String email, String password) {

        ParseUser user = this.setupUser(email, password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    mSignupCallback.signupSuccess();
                } else {
                    mSignupCallback.signupFailed();
                    Log.e("ParseSignupAdapter", e.getMessage());
                }
            }
        });

    }

    private ParseUser setupUser(String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }

}
