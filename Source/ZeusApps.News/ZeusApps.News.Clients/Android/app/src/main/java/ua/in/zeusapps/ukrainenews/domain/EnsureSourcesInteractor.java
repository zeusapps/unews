package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Source;

public class EnsureSourcesInteractor extends SingleInteractor<Boolean, List<Source>> {

    private final ISourceRepository _repository;
    private final IDataService _dataService;

    @Inject
    EnsureSourcesInteractor(
            ISourceRepository repository,
            IDataService dataService) {
        _repository = repository;
        _dataService = dataService;
    }

    @Override
    protected Single<Boolean> build(List<Source> remoteSources) {
        return _dataService.getSources()
                .onErrorResumeNext(x ->
                    _repository.getAll()
                )
                .flatMap(_repository::checkSources)
                .map(sources -> true);
    }
}
