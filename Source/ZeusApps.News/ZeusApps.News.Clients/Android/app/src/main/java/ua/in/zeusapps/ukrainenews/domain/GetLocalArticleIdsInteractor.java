package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetLocalArticleIdsInteractor extends SingleInteractor<List<String>, Source> {

    private final IArticleRepository _articleRepository;

    @Inject
    GetLocalArticleIdsInteractor(IArticleRepository articleRepository) {
        _articleRepository = articleRepository;
    }

    @Override
    protected Single<List<String>> build(Source source) {
        return _articleRepository.getIds(source);
    }
}