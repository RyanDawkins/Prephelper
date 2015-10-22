package com.ryanddawkins.prephelper.ui.preps;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanddawkins.prephelper.R;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.ui.ItemCallback;

import java.util.List;

/**
 * Created by ryan on 10/10/15.
 */
public class PrepsAdapter extends RecyclerView.Adapter<PrepHolder> {

    private List<Prep> preps;
    private ItemCallback<View> itemCallback;

    public PrepsAdapter(@NonNull List<Prep> preps, @NonNull ItemCallback<View> itemCallback) {
        this.preps = preps;
        this.itemCallback = itemCallback;
    }

    @Override
    public PrepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prep, parent, false);
        return new PrepHolder(view, this.itemCallback);
    }

    @Override
    public void onBindViewHolder(PrepHolder holder, int position) {
        Prep prep = this.preps.get(position);
        holder.setNameViewText(prep.getName());
        holder.setItemViewTag(R.id.prep, prep);
    }

    public void addPrep(Prep i) {
        this.preps.add(i);
        notifyItemChanged(preps.size() - 1);
    }

    public void addList(List<Prep> preps) {
        this.preps.addAll(preps);
        notifyDataSetChanged();
    }

    public void replacePreps(List<Prep> preps) {
        this.preps = preps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.preps != null ? this.preps.size() : 0;
    }
}
