package org.wfp.cats;

import android.app.Application;

import org.wfp.cats.model.MyObjectBox;

import io.objectbox.BoxStore;

public class CatsApplication extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
