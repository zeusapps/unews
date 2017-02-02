package ua.in.zeusapps.ukrainenews.adapter;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.models.Article;

public class AdHolder extends BaseViewHolder<Article>{

    @BindView(R.id.fragment_article_ads_template_adView)
    AdView adView;

    public AdHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void update(Context context, Article article) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
