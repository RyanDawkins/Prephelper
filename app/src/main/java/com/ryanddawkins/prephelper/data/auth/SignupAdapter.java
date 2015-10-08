package com.ryanddawkins.prephelper.data.auth;

/**
 * Created by ryan on 10/6/15.
 */
public interface SignupAdapter {

    boolean signup(String email, String password);

    void signupAsync(SignupCallback mSignupCallback, String email, String password);

}
