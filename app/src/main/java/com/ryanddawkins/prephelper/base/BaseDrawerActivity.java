package com.ryanddawkins.prephelper.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.TextView;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.ui.items.ItemsActivity;
import com.ryanddawkins.prephelper.ui.preps.PrepsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by ryan on 10/13/15.
 */
public abstract class BaseDrawerActivity extends BaseActivity {

    protected DrawerLayout mDrawerLayout;

    @Nullable
    @Bind(R.id.nav_view)
    protected NavigationView mNavigationView;

    @Nullable
    @Bind(R.id.userName)
    protected TextView userName;

    @Nullable
    @Bind(R.id.userLocation)
    protected TextView userLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.setDebug(true);

        ButterKnife.bind(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        enableHamburgerMenu();

        if (mDrawerLayout != null && mNavigationView != null) {
            setupDrawerContent();
        } else {
            Timber.w("mDrawerLayout is null or mNavigationView is null");
        }
    }

    protected void setupDrawerContent() {

        final BaseDrawerActivity self = this;

        ButterKnife.bind(mNavigationView);
        if(mNavigationView == null) {
            Timber.w("mNavigationView is null");
            return;
        }
        Timber.d("mNavigationView not null");
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent i = null;

                        Timber.d("Item id " + menuItem.getItemId());
                        Timber.d("" + menuItem.getTitle());

                        switch (menuItem.getItemId()) {
                            case R.id.action_preps:
                                i = new Intent(self, PrepsActivity.class);
                                break;
                            case R.id.action_items:
                                i = new Intent(self, ItemsActivity.class);
                                break;
                        }
                        if (mDrawerLayout != null) {
                            mDrawerLayout.closeDrawers();
                        }

                        if (i != null) {
                            menuItem.setChecked(true);
                            startActivity(i);
                            finish();
                        }
                        return true;
                    }
                });
    }

    protected void enableHamburgerMenu() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Timber.d("Item id "+item.getItemId());
        Timber.d(""+item.getTitle());

        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout != null) {
                    Timber.d("mDrawerLayout is NOT null");
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @LayoutRes
    @Override
    public int getLayoutResource() {
        return R.layout.activity_base_drawer;
    }

    @LayoutRes
    @Override public int getRootView() {
        return R.id.drawer_layout;
    }

}