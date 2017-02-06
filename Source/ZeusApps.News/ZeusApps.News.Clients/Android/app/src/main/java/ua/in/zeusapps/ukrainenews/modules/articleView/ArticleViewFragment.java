package ua.in.zeusapps.ukrainenews.modules.articleView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class ArticleViewFragment
        extends BaseFragment
        implements ArticleViewMVP.IView, AppBarLayout.OnOffsetChangedListener {

    public static final String TAG = ArticleViewFragment.class.getSimpleName();

    private static final String MIME_TYPE = "text/html";
    private static final String BLANK_TITLE = " ";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    private boolean _isVisible;
    private int _scrollRange = -1;
    private Article _article;
    private Source _source;

    @BindView(R.id.fragment_article_view_title)
    TextView titleTextView;
    @BindView(R.id.fragment_article_view_published)
    TextView publishedTextView;
    @BindView(R.id.fragment_article_view_source)
    TextView sourceTextView;

    @BindView(R.id.fragment_article_view_image)
    ImageView articleImage;
    @BindView(R.id.fragment_article_view_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_article_view_collapsingToolbar)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.fragment_article_view_appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.fragment_article_view_articleWebView)
    WebView articleWebView;


    @Inject
    ArticleViewMVP.IPresenter presenter;

    @Inject
    Formatter formatter;

    public ArticleViewFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void showArticle(Article article, Source source) {
        _article = article;
        _source = source;
    }

    @Override
    protected void onCreateViewOverride(View view) {
        if (_article == null){
            return;
        }

        setSupportActionBar();

        appBarLayout.addOnOffsetChangedListener(this);
        toolbarLayout.setTitleEnabled(false);

        Picasso
                .with(getContext())
                .load(_article.getImageUrl())
                .into(articleImage);


        titleTextView.setText(_article.getTitle());
        publishedTextView.setText(formatter.formatDate(_article.getPublished()));
        sourceTextView.setText(_source.getTitle());

        String html = formatter.formatHtml(_article.getHtml());

        articleWebView.setWebChromeClient(new Client());
        articleWebView.clearCache(true);
        articleWebView.clearHistory();
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.loadDataWithBaseURL(_source.getBaseUrl(), html, MIME_TYPE, _source.getEncoding(), null);
    }

    @Override
    protected BaseMVP.IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_article_view;
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
                getCompatActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSupportActionBar(){
        toolbar.setTitle(BLANK_TITLE);
        getCompatActivity().setSupportActionBar(toolbar);
        ActionBar actionBar = getCompatActivity().getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // fix of bug http://stackoverflow.com/questions/32050784/chromium-webview-does-not-seems-to-work-with-android-applyoverrideconfiguration
    private class Client extends WebChromeClient{
        @Override
        public Bitmap getDefaultVideoPoster() {
            Bitmap bmp = super.getDefaultVideoPoster();
            if (bmp != null){
                return bmp;
            }
            return BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_media_video_poster);
        }
    }
}
