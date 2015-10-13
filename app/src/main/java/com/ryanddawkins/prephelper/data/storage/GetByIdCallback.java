package com.ryanddawkins.prephelper.data.storage;

/**
 * Created by ryan on 10/11/15.
 */
public interface GetByIdCallback<T> {

    void gotById(T object);

}
