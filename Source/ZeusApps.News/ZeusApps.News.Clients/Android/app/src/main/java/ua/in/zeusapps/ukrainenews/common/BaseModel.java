package ua.in.zeusapps.ukrainenews.common;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BaseModel {

    private final Scheduler _observeOn;
    private final Scheduler _subscribeOn;
    private Retrofit _retrofit;

    protected BaseModel() {
        _observeOn = AndroidSchedulers.mainThread();
        _subscribeOn = Schedulers.io();
    }

    protected <T> Observable<T> wrapObservable(Observable<T> observable){
        return observable
                .observeOn(_observeOn)
                .subscribeOn(_subscribeOn);
    }
}
