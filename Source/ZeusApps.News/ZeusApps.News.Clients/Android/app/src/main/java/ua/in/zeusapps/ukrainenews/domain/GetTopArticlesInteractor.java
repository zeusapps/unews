package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Article;

public class GetTopArticlesInteractor
    extends SingleInteractor<List<Article>, Void>{

    private final IArticleRepository _repository;

    @Inject
    public GetTopArticlesInteractor(IArticleRepository repository) {
        _repository = repository;
    }

    @Override
    protected Single<List<Article>> build(Void parameter) {
        return _repository.getTopArticles();
    }
}
