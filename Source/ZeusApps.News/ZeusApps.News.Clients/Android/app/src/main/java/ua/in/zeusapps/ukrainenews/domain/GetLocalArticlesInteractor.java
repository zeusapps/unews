package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetLocalArticlesInteractor extends Interactor<List<Article>, Source> {

    private final IArticleRepository _articleRepository;

    @Inject
    public GetLocalArticlesInteractor(IArticleRepository articleRepository) {
        _articleRepository = articleRepository;
    }

    @Override
    protected Observable<List<Article>> buildObservable(Source source) {
        return _articleRepository.getBySource(source);
    }
}
