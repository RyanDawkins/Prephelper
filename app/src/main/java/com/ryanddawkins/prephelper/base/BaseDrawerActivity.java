package com.ryanddawkins.prephelper.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.ui.items.ItemsActivity;
import com.ryanddawkins.prephelper.ui.preps.PrepsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ryan on 10/13/15.
 */
public abstract class BaseDrawerActivity extends BaseActivity {

    @Bind(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    protected NavigationView mNavigationView;

    //@Bind(R.id.userPicture)
    //CircleImageView mUserPicture;

    @Bind(R.id.userName)
    TextView userName;

    @Bind(R.id.userLocation)
    TextView userLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.setDebug(true);

        ButterKnife.bind(this);
        enableHamburgerMenu();
        setupDrawerContent();
    }

    private void setupDrawerContent() {

        final BaseDrawerActivity self = this;

        ButterKnife.bind(this, mNavigationView);
        if(mNavigationView == null) {
            return;
        }
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent i = null;
                        switch (menuItem.getItemId()) {
                            case R.id.action_preps:
                                i = new Intent(self, PrepsActivity.class);
                                break;
                            case R.id.action_items:
                                i = new Intent(self, ItemsActivity.class);
                                break;
                        }
                        if(mDrawerLayout != null) {
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

    //@OnClick(R.id.userPicture)
    public void onUserPictureClick(View v) {
        //IntentUtils.openUserProfile(this, mActiveUser.getUsername(), mUserPicture);
    }

    protected void enableHamburgerMenu() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout != null) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_base_drawer;
    }
}