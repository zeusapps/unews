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
import android.util.Log;
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
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.domain.GetArticleInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article_details)
public class ArticleDetailsFragment
    extends MvpFragment
    implements ArticleDetailsView, View.OnClickListener, AppBarLayout.OnOffsetChangedListener, NestedScrollView.OnScrollChangeListener {

    private static final String TAG = ArticleDetailsFragment.class.getSimpleName();
    private static final String ARTICLE_ID_EXTRA = "article_id";
    private static final String MIME_TYPE = "text/html";
    private static final String HASH_TAG = "#UkraineNews";

    private Source _source;
    private Article _article;
    private boolean _isExpanded;

    @InjectPresenter
    ArticleDetailsPresenter presenter;
    @Inject
    Formatter formatter;
    @Inject
    GetArticleInteractor _articleInteractor;

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

    public static ArticleDetailsFragment newInstance(String articleId) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID_EXTRA, articleId);
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
        String articleId = getArguments().getString(ARTICLE_ID_EXTRA);
        _articleInteractor.execute(bundle -> {
            _article = bundle.getArticle();
            _source = bundle.getSource();

            setToolbar();
            showArticle();
        }, articleId);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        toolbar.setNavigationOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        scrollView.setOnScrollChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        toolbar.setNavigationOnClickListener(null);
        appBarLayout.removeOnOffsetChangedListener(this);
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) null);
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

    @OnClick(R.id.fragment_article_details_shareTwitter)
    public void shareTwitter(){
        String tweet = new TwitterTextBuilder()
                .setLink(_article.getUrl())
                .setTitle(_article.getTitle())
                .addHashTag(HASH_TAG)
                .build();

        new TweetComposer
                .Builder(getContext())
                .text(tweet)
                .show();
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
        toolbar.setTitle(_source.getTitle());
    }

    private void updateToolbar(){
        toolbar
                .getMenu()
                .add(_article.getTitle())
                .setIcon(R.drawable.ic_launch_white_24dp)
                .setOnMenuItemClickListener(item -> openInBrowser(_article))
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
        Log.d(TAG, "Trying to fixScroll");
        scrollView.scrollTo(0,0);
    }

    private boolean openInBrowser(Article article){
        getPresenter().viewInBrowser(article);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        _isExpanded = (verticalOffset == 0);

        Log.d(TAG, _isExpanded ? "Expanded" : "Collapsed");
    }

    @Override
    public void onScrollChange(
            NestedScrollView v, int scrollX, int scrollY,
            int oldScrollX, int oldScrollY) {
        if (_isExpanded){
            fixScroll();
        }
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

class TwitterTextBuilder {
    private static final int TWEET_LENGTH = 140;
    private static final String SEPARATOR = " ";
    private String _title;
    private String _link;
    private List<String> _hashTags = new ArrayList<>();

    public TwitterTextBuilder setTitle(String title){
        if (title == null){
            throw new IllegalArgumentException("Title should be not null.");
        }

        if (_title != null){
            throw new IllegalStateException("Title already set.");
        }

        _title = title;
        return this;
    }

    TwitterTextBuilder setLink(String link){
        if (link == null){
            throw new IllegalArgumentException("Link should be not null.");
        }

        if (_link != null){
            throw new IllegalStateException("Link already set.");
        }

        _link = link;
        return this;
    }

    TwitterTextBuilder addHashTag(String hashTag){
        if (hashTag == null){
            throw new IllegalArgumentException("HashTag should be not null.");
        }

        if (!_hashTags.contains(hashTag)){
            _hashTags.add(hashTag);
        }

        return this;
    }

    public TwitterTextBuilder removeHashTag(String hashTag){
        if (hashTag == null){
            throw new IllegalArgumentException("HashTag should be not null.");
        }

        if (!_hashTags.contains(hashTag)){
            _hashTags.remove(hashTag);
        }

        return this;
    }

    String build(){
        StringBuilder sb = new StringBuilder();

        int titleLength = _title == null
                ? 0
                : _title.length();
        int linkLength = _link == null
                ? 0
                : _link.length();

        if (titleLength > TWEET_LENGTH ||
            linkLength > TWEET_LENGTH ||
            (titleLength + linkLength) > TWEET_LENGTH) {
            return _link == null
                    ? ""
                    : _link;
        }

        if (_title != null){
            sb.append(_title).append(SEPARATOR);
        }


        if (_link != null){
            sb.append(_link).append(SEPARATOR);
        }

        if (sb.length() >= TWEET_LENGTH){
            return sb.toString();
        }

        for (String hashTag: _hashTags){
            sb.append(hashTag).append(SEPARATOR);

            if (sb.length() >= TWEET_LENGTH){
                return sb.toString();
            }
        }

        return sb.toString();
    }
}


