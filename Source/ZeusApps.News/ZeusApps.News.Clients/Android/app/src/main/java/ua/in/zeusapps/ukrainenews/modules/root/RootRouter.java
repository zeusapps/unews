package ua.in.zeusapps.ukrainenews.modules.root;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.MvpRouter;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.settings.SettingsFragment;
import ua.in.zeusapps.ukrainenews.modules.sources.SourcesFragment;

public class RootRouter extends MvpRouter {

    @Inject
    public RootRouter() {
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
}
