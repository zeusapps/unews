package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import io.reactivex.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IDataService;

public class InitialLoadInteractor
    extends Interactor<Boolean, Void> {

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
    protected Observable<Boolean> buildObservable(Void parameter) {
         _dataService.getSources()
                .onErrorReturn(throwable -> _sourceRepository.getAll());
    }

    private Boolean checkSources(List<Source> remoteSources){
        if (remoteSources == null || remoteSources.size() == 0){
            return false;
        }

        List<Source> localSources = _sourceRepository.getAll();

        for (Source source: localSources) {
            if (!remoteSources.contains(source)){
                _sourceRepository.delete(source);
            }
        }

        for (Source source: remoteSources){
            if (!localSources.contains(source)){
                _sourceRepository.create(source);
            }
        }

        return true;
    }
}
