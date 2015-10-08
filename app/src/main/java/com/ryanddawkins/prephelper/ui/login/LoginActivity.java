package com.ryanddawkins.prephelper.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ryanddawkins.prephelper.PrepHelperApp;
import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;
import com.ryanddawkins.prephelper.data.auth.LoginAdapter;
import com.ryanddawkins.prephelper.data.auth.LoginCallback;
import com.ryanddawkins.prephelper.data.auth.SignupAdapter;
import com.ryanddawkins.prephelper.ui.preps.PrepsActivity;
import com.ryanddawkins.prephelper.ui.signup.SignUpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ryan on 9/22/15.
 */
public class LoginActivity extends BaseActivity implements LoginCallback {

    @Nullable
    @Bind(R.id.login_button)
    protected Button loginButton;

    @Nullable
    @Bind(R.id.signup_button)
    protected Button signup_button;

    @Nullable
    @Bind(R.id.email)
    protected EditText emailEditText;

    @Nullable
    @Bind(R.id.password)
    protected EditText passwordEditText;

    private PrepHelperApp mApplication;
    private LoginAdapter mLoginAdapter;
    private SignupAdapter mSignupAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_login);
        ButterKnife.bind(this, container);

        this.mApplication = PrepHelperApp.getInstance();

        this.mLoginAdapter = this.mApplication.getLoginAdapter();
        this.mLoginAdapter.logout();

        // Sets heading
        this.setTitle(this.getString(R.string.login_heading));

        if(this.mLoginAdapter.isLoggedIn()) {
            this.navigateToPrepsActivity();
        }
    }

    public void onStart() {
        super.onStart();
        this.navigateToPrepsActivity();
    }

    private void navigateToPrepsActivity() {
        Intent intent = new Intent(this, PrepsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intent);
    }

    @Nullable @OnClick(R.id.login_button)
    public void loginButtonClicked() {

        String email = this.emailEditText.getText().toString();
        String password = this.passwordEditText.getText().toString();

        this.mLoginAdapter.loginAsync(this, email, password);
    }

    @Nullable @OnClick(R.id.signup_button)
    public void signupButtonClicked() {
        Intent intent = new Intent(this, SignUpActivity.class);
        this.startActivity(intent);
    }

    @Override public void loginSuccess() {
        Log.d("LoginActivity", "Login Success!");
        this.navigateToPrepsActivity();
    }

    @Override public void loginFailed() {
        Log.d("LoginActivity", "Login failed..");
    }

}