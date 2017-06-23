package ua.in.zeusapps.ukrainenews.domain;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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
                .onErrorResumeNext(x -> {
                    return Observable.just(_repository.getAll());
                })
                .map(this::checkSources);
    }

    @NonNull
    private Boolean checkSources(List<Source> remoteSources){
        if (remoteSources == null || remoteSources.size() == 0){
            return false;
        }

        List<Source> localSources = _repository.getAll();

        localSources
                .stream()
                .filter(local -> !remoteSources.contains(local))
                .forEach(_repository::delete);

        remoteSources
                .stream()
                .filter(remote -> !localSources.contains(remote))
                .forEach(_repository::create);

        return true;
    }
}
