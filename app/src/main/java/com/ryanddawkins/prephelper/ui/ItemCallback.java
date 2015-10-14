package com.ryanddawkins.prephelper.ui;

/**
 * Created by ryan on 10/10/15.
 */
public interface ItemCallback<T> {
    void onItemClick(T t);

    void onItemLongClick(T t);
}
