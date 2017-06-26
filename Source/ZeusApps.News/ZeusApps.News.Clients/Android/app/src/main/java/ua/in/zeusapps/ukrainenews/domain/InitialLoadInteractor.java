package ua.in.zeusapps.ukrainenews.domain;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;

public class InitialLoadInteractor
        extends SingleInteractor<Boolean, Void> {

    private final ISourceRepository _sourceRepository;
    private final IArticleRepository _articleRepository;
    private final IDataService _dataService;

    public InitialLoadInteractor(
            ISourceRepository sourceRepository,
            IArticleRepository articleRepository,
            IDataService dataService) {
        _sourceRepository = sourceRepository;
        _articleRepository = articleRepository;
        _dataService = dataService;
    }

    @Override
    protected Single<Boolean> build(Void parameter) {
        return _dataService
                .getSources()
                .onErrorResumeNext(throwable -> _sourceRepository.getAll())
                .map(x -> true);
    }
}