package com.ryanddawkins.prephelper.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ryan on 9/22/15.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_button)
    protected Button loginButton;

    @Bind(R.id.username)
    protected EditText usernameEditText;

    @Bind(R.id.password)
    protected EditText passwordEditText;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        this.addLayoutToContainer(R.layout.activity_login);

        // Sets heading
        this.setTitle(this.getString(R.string.login_heading));
    }

    @OnClick(R.id.login_button)
    public void loginButtonClicked() {
        Log.d("LoginActivity", "Clicked Button");
    }

}