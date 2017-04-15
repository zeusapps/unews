package ua.in.zeusapps.ukrainenews.domain;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;

public class EnsureSourcesInteractor extends Interactor<Boolean, List<Source>> {

    private final ISourceRepository _repository;
    private final IDataService _dataService;

    @Inject
    public EnsureSourcesInteractor(
            ISourceRepository repository,
            IDataService dataService) {
        _repository = repository;
        _dataService = dataService;
    }

    @Override
    protected Observable<Boolean> buildObservable(List<Source> remoteSources) {
        return _dataService.getSources()
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Source>>>() {
                    @Override
                    public Observable<? extends List<Source>> call(Throwable throwable) {
                        return Observable.just(_repository.getAll());
                    }
                })
                .map(new Func1<List<Source>, Boolean>() {
                    @Override
                    public Boolean call(List<Source> sources) {
                        return checkSources(sources);
                    }
                });
    }

    @NonNull
    private Boolean checkSources(List<Source> remoteSources){
        if (remoteSources == null || remoteSources.size() == 0){
            return false;
        }


        List<Source> localSources = _repository.getAll();
        for (Source local: localSources) {
            if (!remoteSources.contains(local)){
                _repository.delete(local);
            }
        }

        for (Source remote: remoteSources){
            if (!localSources.contains(remote)){
                _repository.create(remote);
            }
        }

        return true;
    }
}
