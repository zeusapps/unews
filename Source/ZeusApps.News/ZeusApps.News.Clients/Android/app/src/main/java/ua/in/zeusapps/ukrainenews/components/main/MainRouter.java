package ua.in.zeusapps.ukrainenews.components.main;

import android.content.Intent;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpRouter;
import ua.in.zeusapps.ukrainenews.components.articles.ArticlesActivity;
import ua.in.zeusapps.ukrainenews.components.details.DetailsActivity;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public class MainRouter extends MvpRouter {

    @Inject
    public MainRouter() {
    }

    public void showArticles(Source source) {
        Intent intent = new Intent(getActivity(), ArticlesActivity.class);
        intent.putExtra(ArticlesActivity.SOURCE_ID_EXTRA, source.getId());

        startIntent(intent);
    }

    void showSettings() {
        //TODO implement showing settings
        //addToStack(new SettingsFragment(), R.id.activity_root_content);
    }

    public void showArticleDetails(Article article){
        Intent articleDetailsIntent = new Intent(getActivity(), DetailsActivity.class);
        articleDetailsIntent.putExtra(DetailsActivity.ARTICLE_ID_EXTRA, article.getId());

        startIntent(articleDetailsIntent);
    }
}
