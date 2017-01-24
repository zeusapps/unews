package ua.in.zeusapps.ukrainenews.common;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BaseModel {

    protected Scheduler observeOn;
    protected Scheduler subscribeOn;

    protected BaseModel() {
        observeOn = AndroidSchedulers.mainThread();
        subscribeOn = Schedulers.io();
    }
}
