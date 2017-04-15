package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.ISourceRepository;

public class GetLocalSourcesInteractor extends Interactor<List<Source>, Void>{

    private ISourceRepository _repository;

    @Inject
    public GetLocalSourcesInteractor(ISourceRepository repository) {
        _repository = repository;
    }

    @Override
    protected Observable<List<Source>> buildObservable(Void parameter) {
        return Observable.just(_repository.getAll());
    }
}