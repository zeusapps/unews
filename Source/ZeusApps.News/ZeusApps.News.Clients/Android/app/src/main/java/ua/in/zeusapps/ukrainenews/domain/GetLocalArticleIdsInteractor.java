package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetLocalArticleIdsInteractor extends Interactor<List<String>, Source> {

    private final IArticleRepository _articleRepository;

    @Inject
    public GetLocalArticleIdsInteractor(IArticleRepository articleRepository) {
        _articleRepository = articleRepository;
    }

    @Override
    protected Observable<List<String>> buildObservable(Source source) {
        return _articleRepository.getIds(source);
    }
}
