package com.ryanddawkins.prephelper.data.storage;

import java.util.List;

/**
 * Created by ryan on 10/11/15.
 */
public interface GetAllCallback<T> {

    void retrievedList(List<T> list);

}
