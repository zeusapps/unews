package ua.in.zeusapps.ukrainenews.domain;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;

public class GetArticleInteractor extends SingleInteractor<ArticleRequestBundle, String> {

    private final ISourceRepository _sourceRepository;
    private final IArticleRepository _articleRepository;

    @Inject
    public GetArticleInteractor(
            ISourceRepository sourceRepository,
            IArticleRepository articleRepository) {
        _sourceRepository = sourceRepository;
        _articleRepository = articleRepository;
    }

    @Override
    protected Single<ArticleRequestBundle> build(String articleId) {
        return _articleRepository.getById(articleId).flatMap(article ->
                _sourceRepository.getByKey(article.getSourceId())
                        .map(source -> new ArticleRequestBundle(source, article, false)));
    }


}
