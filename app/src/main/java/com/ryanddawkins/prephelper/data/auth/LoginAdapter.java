package com.ryanddawkins.prephelper.data.auth;

/**
 * Created by ryan on 10/1/15.
 */
public interface LoginAdapter {

    boolean login(String email, String password);

    void loginAsync(LoginCallback mLoginCallback, String email, String password);

    boolean isLoggedIn();

    void logout();

    User getUser();

}
