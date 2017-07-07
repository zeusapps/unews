package ua.in.zeusapps.ukrainenews.components.articles;

import android.content.Intent;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpRouter;
import ua.in.zeusapps.ukrainenews.components.details.DetailsActivity;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public class ArticlesRouter extends MvpRouter {
    @Inject
    public ArticlesRouter() {
    }

    public void showArticleDetails(Article article, Source source){
        Intent articleDetailsIntent = new Intent(getActivity(), DetailsActivity.class);
        articleDetailsIntent.putExtra(DetailsActivity.ARTICLE_ID_EXTRA, article.getId());
        articleDetailsIntent.putExtra(DetailsActivity.SOURCE_ID_EXTRA, source.getId());

        startIntent(articleDetailsIntent);
    }
}
