package com.ryanddawkins.prephelper.ui.preps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ryanddawkins.prephelper.PrepHelperApp;
import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseDrawerActivity;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.PrepStorageAdapter;
import com.ryanddawkins.prephelper.ui.ItemCallback;
import com.ryanddawkins.prephelper.ui.items.ItemsActivity;
import com.ryanddawkins.prephelper.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by ryan on 10/8/15.
 */
public class PrepsActivity extends BaseDrawerActivity implements ItemCallback<View>, GetAllCallback<Prep> {

    @Nullable
    @Bind(R.id.preps_list)
    protected RecyclerView prepsRecyclerView;

    @Nullable
    @Bind(R.id.add_preps_fab)
    protected FloatingActionButton addPrepFab;

    private PrepStorageAdapter prepStorageAdapter;
    private PrepsActivity self;
    private List<Prep> prepsList;
    private PrepsAdapter prepsAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle(getString(R.string.preps_heading));

        this.self = this;

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_preps);
        //ButterKnife.bind(this, container);
    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override public void onResume() {
        super.onResume();

        if(!PrepHelperApp.getInstance().getLoginAdapter().isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            this.startActivity(intent);
            return;
        }

        this.prepStorageAdapter = PrepHelperApp.getInstance().getPrepStorageAdapter();

        if(this.prepsRecyclerView != null) {
            this.prepsAdapter = new PrepsAdapter(new ArrayList<Prep>(), this);
            this.prepsRecyclerView.setAdapter(this.prepsAdapter);
            this.prepStorageAdapter.getPrepsAsync(this);
            this.prepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Log.w("PrepsActivity", "Preps Recyclerview is nt present.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch(itemId) {
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateListAdapter(List<Prep> preps) {
        if(this.prepsRecyclerView != null) {
            this.prepsAdapter.replacePreps(preps);
            Timber.d("Preps length: "+preps.size());
            this.showToast("should show data");
        } else {
            Log.w("PrepsActivity", "recyclerview is null");
            this.showToast("not good mann..");
        }
    }

    public void onItemLongClick(View view) {

        Timber.d("Hopefully this happens");

        final Prep prep = (Prep) view.getTag(R.id.prep);

        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Create new Prep")
                .content("What's the name of your prep?")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("7 Day Snow Storm Supplies", prep.getName(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        prep.setName(input.toString());
                        prepStorageAdapter.savePrepAsync(prep);
                        prepsAdapter.addPrep(prep);
                    }
                }).build();
        materialDialog.show();
    }

    @Nullable @OnClick(R.id.add_preps_fab)
    public void addPrepsClicked() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Create new Prep")
                .content("What's the name of your prep?")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("7 Day Snow Storm Supplies", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        String prepName = input.toString();

                        Prep prep = new Prep();
                        prep.setName(prepName);

                        self.prepStorageAdapter.createPrep(prep);
                        self.prepStorageAdapter.getPrepsAsync(self);
                    }
                }).build();
        materialDialog.show();
    }

    @Override
    public void onItemClick(View view) {
        Prep prep = (Prep) view.getTag(R.id.prep);

        Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra(ItemsActivity.PREP, prep.getId());
        startActivity(intent);
    }

    @Override
    public void retrievedList(List<Prep> list) {
        this.prepsList = list;
        this.showToast("Received list..");
        this.updateListAdapter(list);
    }
}
