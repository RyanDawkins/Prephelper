package com.ryanddawkins.prephelper.data.auth.parse;

import com.parse.ParseUser;
import com.ryanddawkins.prephelper.data.auth.User;

/**
 * Created by ryan on 10/15/15.
 */
public class ParseUserAdapter implements User {

    private ParseUser parseUser;

    public ParseUserAdapter(ParseUser parseUser) {
        this.parseUser = parseUser;
    }

    @Override
    public String getUsername() {
        return this.parseUser.getUsername();
    }

    @Override
    public String getEmail() {
        return this.parseUser.getEmail();
    }

    @Override
    public String getDisplayName() {

        String displayName;

        if(this.getUsername() != null) {
            displayName = this.getUsername();
        }
        else {
            displayName = this.getEmail();
        }

        return displayName;

    }



}
