package com.ryanddawkins.prephelper.ui.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;
import com.ryanddawkins.prephelper.data.auth.parse.ParseSignupAdapter;
import com.ryanddawkins.prephelper.data.auth.SignupAdapter;
import com.ryanddawkins.prephelper.data.auth.SignupCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ryan on 10/6/15.
 */
public class SignUpActivity extends BaseActivity implements SignupCallback {

    @Nullable
    @Bind(R.id.email)
    protected EditText emailEditText;

    @Nullable
    @Bind(R.id.password)
    protected EditText passwordEditText;

    @Nullable
    @Bind(R.id.password_confirm)
    protected EditText passwordConfirmEditText;

    private SignupAdapter mSignupAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_signup);
        ButterKnife.bind(this, container);

        setTitle(getString(R.string.signup_heading));

        this.mSignupAdapter = new ParseSignupAdapter();
    }

    @Nullable @OnClick(R.id.create_account_button)
    public void signupButtonClick() {

        String email = this.emailEditText.getText().toString();
        String password = this.passwordEditText.getText().toString();
        String passwordConfirm = this.passwordConfirmEditText.getText().toString();

        if(!password.equals(passwordConfirm)) {
            this.showToast(getString(R.string.passwords_do_not_match), true);
            return;
        }

        this.mSignupAdapter.signupAsync(this, email, password);

    }

    @Override public void signupSuccess() {
        this.showToast("Successful Sign Up!");
        this.finish();
    }

    @Override public void signupFailed() {
        this.showToast("Failed login :(");
    }
}
