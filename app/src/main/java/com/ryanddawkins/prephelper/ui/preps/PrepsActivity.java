package com.ryanddawkins.prephelper.ui.preps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ryanddawkins.prephelper.PrepHelperApp;
import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.base.BaseActivity;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.storage.GetAllCallback;
import com.ryanddawkins.prephelper.data.storage.PrepStorageAdapter;
import com.ryanddawkins.prephelper.ui.ItemCallback;
import com.ryanddawkins.prephelper.ui.items.ItemsActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ryan on 10/8/15.
 */
public class PrepsActivity extends BaseActivity implements ItemCallback<View>, GetAllCallback<Prep> {

    @Nullable
    @Bind(R.id.preps_list)
    protected RecyclerView prepsRecyclerView;

    @Nullable
    @Bind(R.id.add_preps_fab)
    protected FloatingActionButton addPrepFab;

    private PrepStorageAdapter prepStorageAdapter;
    private PrepsActivity self;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.self = this;

        // Adding layout to container
        FrameLayout container = this.addLayoutToContainer(R.layout.activity_preps);
        ButterKnife.bind(this, container);

        setTitle(getString(R.string.items_heading));

        this.prepStorageAdapter = PrepHelperApp.getInstance().getPrepStorageAdapter();

        if(this.prepsRecyclerView != null) {
            this.prepsRecyclerView.setAdapter(new PrepsAdapter(null, this));
            this.prepStorageAdapter.getPrepsAsync(this);
            this.prepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Log.w("PrepsActivity", "Preps Recyclerview is nt present.");
        }
    }

    public void updateListAdapter(List<Prep> preps) {
        if(this.prepsRecyclerView != null) {
            this.prepsRecyclerView.setAdapter(new PrepsAdapter(preps, this));
            this.prepsRecyclerView.getAdapter().notifyDataSetChanged();
            this.showToast("should show data");
        } else {
            Log.w("PrepsActivity", "recyclerview is null");
            this.showToast("not good mann..");
        }
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
        this.showToast("Received list..");
        this.updateListAdapter(list);
    }
}
