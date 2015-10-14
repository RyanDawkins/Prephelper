package com.ryanddawkins.prephelper.data.storage;

import com.ryanddawkins.prephelper.data.pojo.Prep;

import java.util.List;

/**
 * Created by ryan on 10/11/15.
 */
public interface PrepStorageAdapter {

    List<Prep> getPreps();

    void getPrepsAsync(final GetAllCallback<Prep> callback);

    void createPrep(Prep prep);

    void deletePrep(Prep prep);

    Prep getPrepById(String id);

    void getPrepByIdAsync(GetByIdCallback<Prep> callback, String id);

    void savePrep(Prep prep);

    void savePrepAsync(Prep prep);

    void savePrepAsync(Prep prep, SaveCallback saveCallback);

}
