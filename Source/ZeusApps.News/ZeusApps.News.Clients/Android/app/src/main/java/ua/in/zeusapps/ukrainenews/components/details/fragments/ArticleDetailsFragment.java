package ua.in.zeusapps.ukrainenews.components.details.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article_details)
public class ArticleDetailsFragment
    extends MvpFragment
    implements ArticleDetailsView, View.OnClickListener {

    private static final String ARTICLE_ID_EXTRA = "article_id";
    private static final String SOURCE_EXTRA = "source";
    private static final String MIME_TYPE = "text/html";

    private Source _source;
    private Article _article;

    @InjectPresenter
    ArticleDetailsPresenter presenter;
    @Inject
    Formatter formatter;
    @Inject
    IArticleRepository articleRepository;
    @BindView(R.id.fragment_article_view_image)
    ImageView articleImage;
    @BindView(R.id.fragment_article_view_title)
    TextView titleTextView;
    @BindView(R.id.fragment_article_view_published)
    TextView publishedTextView;
    @BindView(R.id.fragment_article_view_source)
    TextView sourceTextView;
    @BindView(R.id.fragment_article_details_nestedScrollView)
    NestedScrollView scrollView;
    @BindView(R.id.fragment_article_view_articleWebView)
    WebView articleWebView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_article_details_shareFacebookButton)
    ShareButton shareFacebookButton;
    @BindView(R.id.fragment_article_details_sendFacebookButton)
    SendButton sendFacebookButton;
    @BindView(R.id.fragment_article_details_appBar)
    AppBarLayout appBarLayout;

    @Override
    public ArticleDetailsPresenter getPresenter() {
        return presenter;
    }

    public static ArticleDetailsFragment newInstance(String articleId, Source source) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID_EXTRA, articleId);
        args.putParcelable(SOURCE_EXTRA, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        _source = getArguments().getParcelable(SOURCE_EXTRA);
        String articleId = getArguments().getString(ARTICLE_ID_EXTRA);
        articleRepository
                .getById(articleId)
                .subscribe(new Action1<Article>() {
                    @Override
                    public void call(Article article) {
                        _article = article;
                        showArticle();
                    }
                });
        setToolbar();
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void showArticle() {
        Picasso
                .with(getContext())
                .load(_article.getImageUrl())
                .resize(320, 256)
                .centerCrop()
                .error(R.drawable.un)
                .placeholder(R.drawable.un)
                .into(articleImage);

        String html = formatter.formatHtml(_article.getHtml() + "<br><br>");

        titleTextView.setText(_article.getTitle());
        publishedTextView.setText(formatter.formatDate(_article.getPublished()));
        sourceTextView.setText(_source.getTitle());
        articleWebView.clearCache(true);
        articleWebView.clearHistory();
        articleWebView.setWebChromeClient(new Client());
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                fixScroll();
            }
        });
        articleWebView.loadDataWithBaseURL(
                _source.getBaseUrl(), html, MIME_TYPE, _source.getEncoding(), null);
        shareFacebook();
        updateToolbar();
        fixScroll();
    }

    @Override
    public void onClick(View v) {
        getPresenter().close();
    }

    @OnClick(R.id.fragment_article_details_shareOther)
    public void shareOther(){
        if (_source == null || _article == null){
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(MIME_TYPE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, prepareHtml());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            shareIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, _article.getUrl());
        }

        startActivity(Intent.createChooser(shareIntent, _article.getTitle()));
    }
    @OnClick(R.id.fragment_article_view_shareButton)
    public void share(){
        appBarLayout.setExpanded(false, true);
        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
    }

    private Spanned prepareHtml(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(_article.getHtml(), Html.FROM_HTML_MODE_COMPACT);
        }

        return Html.fromHtml(_article.getHtml(), Html.FROM_HTML_MODE_LEGACY);
    }

    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setTitle(_source.getTitle());
    }

    private void updateToolbar(){
        toolbar
                .getMenu()
                .add(_article.getTitle())
                .setIcon(R.drawable.ic_launch_white_24dp)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return openInBrowser(_article);
                    }
                })
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void shareFacebook(){
        ShareLinkContent link = new ShareLinkContent
                .Builder()
                .setContentUrl(Uri.parse(_article.getUrl()))
                .build();

        shareFacebookButton.setContentDescription(_article.getTitle());
        shareFacebookButton.setShareContent(link);

        sendFacebookButton.setShareContent(link);
        CallbackManager manager = CallbackManager.Factory.create();
        sendFacebookButton.registerCallback(manager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void fixScroll(){
        scrollView.scrollTo(0,0);
    }

    private boolean openInBrowser(Article article){
        getPresenter().viewInBrowser(article);
        return true;
    }

    // fix of bug http://stackoverflow.com/questions/32050784/chromium-webview-does-not-seems-to-work-with-android-applyoverrideconfiguration
    private class Client extends WebChromeClient {
        @Override
        public Bitmap getDefaultVideoPoster() {
            Bitmap bmp = super.getDefaultVideoPoster();
            if (bmp != null){
                return bmp;
            }
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.un);
        }
    }
}


