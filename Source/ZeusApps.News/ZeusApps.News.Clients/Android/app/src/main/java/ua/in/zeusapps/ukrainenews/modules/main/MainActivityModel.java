package ua.in.zeusapps.ukrainenews.modules.main;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.common.Constants;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

public class MainActivityModel extends BaseModel implements MainActivityMVP.Model {
    private ISourceService _service;

    public MainActivityModel(){
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.REMOTE_URL)
                .build();
        _service = retrofit.create(ISourceService.class);
    }

    @Override
    public Observable<List<Source>> getSources() {
        return _service
                .getSources()
                .observeOn(observeOn)
                .subscribeOn(subscribeOn);
    }
}