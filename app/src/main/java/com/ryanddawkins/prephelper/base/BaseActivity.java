package com.ryanddawkins.prephelper.base;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ryanddawkins.prephelper.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * FragmentManager that this activity uses
     */
    protected FragmentManager mFragmentManager = null;

    @Nullable
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Nullable
    @Bind(R.id.container)
    protected FrameLayout mContainerLayout;

    // Wheter this Activity has a Back arrow or not
    protected boolean mIsBackNav = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this); // Bind the views of this Activity

        if(mToolbar != null) {
            // Sets the SupportActionBar
            setSupportActionBar(mToolbar);
        }

        if(mFragmentManager == null) {
            mFragmentManager = getFragmentManager(); // Gets a reference to the FragmentManager
        }
    }

    /**
     * This method will take care of displaying a Back arrow to the previous activity in the stack
     */
    protected void enableBackNav() {
        if(getSupportActionBar() != null) {
            mIsBackNav = true;
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    /**
     * Sets the status bar color if the device is on Lollipop or more
     * @param color
     *      The color to be used
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor(@ColorRes int color) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    /**
     * Sets the title of the current toolbar
     * @param title
     *      The title for the toolbar
     */
    public void setTitle(@NonNull String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home && mIsBackNav) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this); // Unbind the views of this Activity to avoid Memory leaks
    }

    /**
     * This method should be Overriden if the Activity needs to use a different XML layout
     * @return
     *      The integer with the ID of the layout resource
     */
    @LayoutRes
    protected int getLayoutResource() {
        return R.layout.activity_base;
    }

    @LayoutRes
    protected int getRootView() {
        return R.id.main_content;
    }

    /**
     * Adds a fragment to the Container of this activity otherwise will throw a exception
     * The tag of a fragment can be used in some situations. The most common used cases are:
     *
     * 1. Retrieving a fragment from the BackStack
     * 2. Using the FragmentManager to retrieve retained fragments
     *
     * @param fragment
     *      The Fragment to be added
     * @param tag
     *      The string to be used as a Tag for this Fragment.
     */
    protected void addFragmentToContainer(@NonNull Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag).commit();
    }

    protected FrameLayout addLayoutToContainer(@LayoutRes int layout) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup group = (ViewGroup) inflater.inflate(layout, null, false);
        if(mContainerLayout != null) {
            mContainerLayout.addView(group);
            ButterKnife.bind(this, mContainerLayout);
            Timber.d("container layout not null");
        } else {
            Timber.d("container layout is null");
        }
        return mContainerLayout;
    }



    protected void showToast(String message) {
        this.showToast(message, false);
    }

    protected void showToast(String message, boolean isLong) {

        @LayoutRes
        int rootView = getRootView();
        View view = this.findViewById(rootView);

        if(view == null) {
            Timber.e("view is null, cannot show toast");
            Timber.d("View id: '"+getLayoutResource()+"'");
            return;
        }

        Snackbar snackbar = Snackbar.make(view, message, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}