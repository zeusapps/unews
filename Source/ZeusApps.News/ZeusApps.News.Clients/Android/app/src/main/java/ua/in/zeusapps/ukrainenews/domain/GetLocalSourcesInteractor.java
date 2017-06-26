package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetLocalSourcesInteractor extends SingleInteractor<List<Source>, Void> {

    private ISourceRepository _repository;

    @Inject
    GetLocalSourcesInteractor(ISourceRepository repository) {
        _repository = repository;
    }

    @Override
    protected Single<List<Source>> build(Void parameter) {
        return _repository.getAll();
    }
}