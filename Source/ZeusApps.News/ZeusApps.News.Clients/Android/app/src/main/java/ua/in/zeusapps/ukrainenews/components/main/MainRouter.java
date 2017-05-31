package ua.in.zeusapps.ukrainenews.components.main;

import android.content.Intent;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.MvpRouter;
import ua.in.zeusapps.ukrainenews.components.details.DetailsActivity;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.main.fragments.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.settings.SettingsFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.sources.SourcesFragment;

public class MainRouter extends MvpRouter {

    @Inject
    public MainRouter() {
    }

    public void showSources() {
        addClearStack(new SourcesFragment(), R.id.activity_root_content);
    }

    public void showArticles(Source source) {
        addToStack(ArticleFragment.newInstance(source), R.id.activity_root_content);
    }

    public void showSettings() {
        addToStack(new SettingsFragment(), R.id.activity_root_content);
    }

    public void showArticleDetails(Article article, Source source){
        Intent articleDetailsIntent = new Intent(getActivity(), DetailsActivity.class);
        articleDetailsIntent.putExtra(DetailsActivity.ARTICLE_ID_EXTRA, article.getId());
        articleDetailsIntent.putExtra(DetailsActivity.SOURCE_EXTRA, source);

        startIntent(articleDetailsIntent);
    }
}
