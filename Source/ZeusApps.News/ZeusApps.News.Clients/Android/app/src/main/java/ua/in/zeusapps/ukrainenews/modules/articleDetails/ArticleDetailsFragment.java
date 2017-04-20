package ua.in.zeusapps.ukrainenews.modules.articleDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.helpers.FragmentHelper;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.BaseRootFragment;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article_details)
public class ArticleDetailsFragment
        extends BaseRootFragment
        implements ArticleDetailsView, AppBarLayout.OnOffsetChangedListener {

    private static final String ARTICLE_ID_EXTRA = "article_id";
    private static final String SOURCE_EXTRA = "source";

    private static final String MIME_TYPE = "text/html";
    private static final String BLANK_TITLE = " ";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";


    private boolean _isVisible;
    private int _scrollRange = -1;
    private Article _article;

    @BindView(R.id.fragment_article_details_title)
    TextView titleTextView;
    @BindView(R.id.fragment_article_details_published)
    TextView publishedTextView;
    @BindView(R.id.fragment_article_details_source)
    TextView sourceTextView;

    @BindView(R.id.fragment_article_details_image)
    ImageView articleImage;
    @BindView(R.id.fragment_article_details_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_article_details_collapsingToolbar)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.fragment_article_details_appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.fragment_article_details_articleWebView)
    WebView articleWebView;

    @Inject
    Formatter formatter;
    @InjectPresenter
    ArticleDetailsPresenter presenter;

    @Override
    public ArticleDetailsPresenter getPresenter() {
        return presenter;
    }

    public ArticleDetailsFragment() {
        setHasOptionsMenu(true);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Source source = getArguments().getParcelable(SOURCE_EXTRA);
        String articleId = getArguments().getString(ARTICLE_ID_EXTRA);

        getPresenter().init(articleId, source);
    }



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void showArticle(Article article, Source source) {
        _article = article;

        Picasso
                .with(getContext())
                .load(_article.getImageUrl())
                .into(articleImage);


        titleTextView.setText(_article.getTitle());
        publishedTextView.setText(formatter.formatDate(_article.getPublished()));
        sourceTextView.setText(source.getTitle());

        String html = formatter.formatHtml(_article.getHtml());

        articleWebView.setWebChromeClient(new Client());
        articleWebView.clearCache(true);
        articleWebView.clearHistory();
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.loadDataWithBaseURL(source.getBaseUrl(), html, MIME_TYPE, source.getEncoding(), null);
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }


    @Override
    public void onDetach() {
        articleWebView.destroy();
        super.onDetach();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (_scrollRange == -1) {
            _scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (_scrollRange + verticalOffset == 0) {
            toolbar.setTitle(_article.getTitle());
            _isVisible = true;
        } else if(_isVisible) {
            toolbar.setTitle(BLANK_TITLE);
            _isVisible = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_article_view_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.fragment_article_view_viewOnSite:
                String url = _article.getUrl();
                if (!url.startsWith(HTTP_PREFIX) && !url.startsWith(HTTPS_PREFIX)){
                    url = HTTP_PREFIX + url;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            case android.R.id.home:
                FragmentHelper.pop(getFragmentManager());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fragment_article_view_shareButton)
    public void onShare(){
        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share
                .putExtra(Intent.EXTRA_TITLE, _article.getTitle())
                .putExtra(Intent.EXTRA_TEXT, _article.getTitle() + " " + _article.getUrl())
                .putExtra(Intent.EXTRA_SUBJECT, _article.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            share.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(_article.getHtml()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            share.putExtra(Intent.EXTRA_ORIGINATING_URI, _article.getUrl());
        }

        share.setType("text/plain");


        startActivity(Intent.createChooser(share, _article.getTitle()));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getFabButtonIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getFabButtonAction() {
        return null;
    }

    // fix of bug http://stackoverflow.com/questions/32050784/chromium-webview-does-not-seems-to-work-with-android-applyoverrideconfiguration
    private class Client extends WebChromeClient{
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
