package ua.in.zeusapps.ukrainenews.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Constants;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.REMOTE_URL)
                .build();

        ISourceService sourceService = retrofit.create(ISourceService.class);

        sourceService.getSources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Source>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(MainActivity.class.getSimpleName(), "Download Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(MainActivity.class.getSimpleName(), e.getMessage(), e);
                    }

                    @Override
                    public void onNext(List<Source> sources) {
                        for (Source src : sources) {
                            Log.e(MainActivity.class.getSimpleName(), src.getTitle());
                        }
                    }
                });
    }
}
