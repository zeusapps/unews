package ua.in.zeusapps.ukrainenews.modules.root;

import ua.in.zeusapps.ukrainenews.common.RouterBase;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface RootRouter extends RouterBase {
    void showSources();

    void showArticles(Source source);
}
