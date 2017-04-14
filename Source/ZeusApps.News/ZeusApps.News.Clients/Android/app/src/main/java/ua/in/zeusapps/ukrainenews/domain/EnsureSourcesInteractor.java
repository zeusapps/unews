package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.ISourceRepository;

public class EnsureSourcesInteractor extends Interactor<Boolean, List<Source>> {

    private ISourceRepository _repository;

    @Inject
    public EnsureSourcesInteractor(ISourceRepository repository) {
        _repository = repository;
    }

    @Override
    protected Observable<Boolean> buildObservable(List<Source> remoteSources) {

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

        return Observable.just(true);
    }
}
