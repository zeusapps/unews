package ua.in.zeusapps.ukrainenews.domain;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.ArticleResponse;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;
import ua.in.zeusapps.ukrainenews.services.IDataService;

public class GetArticlesInteractor extends Interactor<ArticleResponse, ArticleRequestBundle>{

    private final static int PAGE_COUNT = 20;
    private final ISourceRepository _sourceRepository;
    private final IArticleRepository _articleRepository;
    private final IDataService _dataService;
    private final Formatter _formatter;

    @Inject
    public GetArticlesInteractor(
            ISourceRepository sourceRepository,
            IArticleRepository articleRepository,
            IDataService dataService,
            Formatter formatter) {
        _sourceRepository = sourceRepository;
        _articleRepository = articleRepository;
        _dataService = dataService;
        _formatter = formatter;
    }

    @Override
    protected Observable<ArticleResponse> buildObservable(final ArticleRequestBundle bundle) {

        String publishedString = _formatter.toStringDate(bundle.getArticle().getPublished());

        return _dataService.getNewerArticles(
                bundle.getSource().getKey(),
                PAGE_COUNT,
                publishedString,
                bundle.getIsAfter())
                .map(new Func1<List<Article>, ArticleResponse>() {
                    @Override
                    public ArticleResponse call(List<Article> articles) {

                        boolean isRefresh = false;
                        if (!bundle.getIsAfter()){
                            updateSourceTimestamp(bundle.getSource());
                            if (articles.size() == PAGE_COUNT){
                                _articleRepository.removeBySource(bundle.getSource());
                                isRefresh = true;
                            }
                        }

                        for (Article article: articles) {
                            _articleRepository.create(article);
                        }
                        return new ArticleResponse(articles, isRefresh);
                    }
                });
    }

    private void updateSourceTimestamp(Source source){
        source.setTimestamp(new Date());
        _sourceRepository.update(source);
    }
}
