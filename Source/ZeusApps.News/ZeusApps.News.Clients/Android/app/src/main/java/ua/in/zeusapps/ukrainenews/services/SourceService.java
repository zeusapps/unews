package ua.in.zeusapps.ukrainenews.services;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.in.zeusapps.ukrainenews.common.Constants;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourceService implements ISourceService {

    private ISourceService _service;
    private Scheduler observeOn;
    private Scheduler subscribeOn;

    public SourceService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.REMOTE_URL)
                .build();
        _service = retrofit.create(ISourceService.class);
        observeOn = AndroidSchedulers.mainThread();
        subscribeOn = Schedulers.io();
    }

    @Override
    public Observable<List<Source>> getSources() {
        return _service
                .getSources()
                .observeOn(observeOn)
                .subscribeOn(subscribeOn);
    }
}
