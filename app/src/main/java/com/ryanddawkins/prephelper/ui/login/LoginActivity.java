package com.ryanddawkins.prephelper.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ryan on 9/22/15.
 */
public class LoginActivity extends BaseActivity {

    @Nullable
    @Bind(R.id.login_button)
    protected Button loginButton;

    @Nullable
    @Bind(R.id.email)
    protected EditText emailEditText;

    @Nullable
    @Bind(R.id.password)
    protected EditText passwordEditText;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_login);
        ButterKnife.bind(this, container);

        // Sets heading
        this.setTitle(this.getString(R.string.login_heading));
    }

    @Nullable @OnClick(R.id.login_button)
    public void loginButtonClicked() {

        String email = this.emailEditText.getText().toString();
        String password = this.emailEditText.getText().toString();

    }

}