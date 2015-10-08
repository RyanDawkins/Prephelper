package com.ryanddawkins.prephelper.ui.preps;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by ryan on 10/8/15.
 */
public class PrepsActivity extends BaseActivity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_preps);
        ButterKnife.bind(this, container);

        setTitle(getString(R.string.hello_world));

    }

}
