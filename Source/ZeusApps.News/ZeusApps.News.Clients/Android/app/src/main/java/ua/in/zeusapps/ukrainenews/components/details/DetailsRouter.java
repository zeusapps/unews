package ua.in.zeusapps.ukrainenews.components.details;

import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpRouter;
import ua.in.zeusapps.ukrainenews.models.Article;

public class DetailsRouter
    extends MvpRouter {

    @Inject
    public DetailsRouter() {
    }

    public void close(){
        getActivity().finish();
    }

    public void viewInBrowser(Article article){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
        startIntent(intent);
    }
}
