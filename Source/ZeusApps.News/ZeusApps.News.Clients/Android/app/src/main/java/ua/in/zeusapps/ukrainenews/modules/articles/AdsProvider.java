package ua.in.zeusapps.ukrainenews.modules.articles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.AdHolder;
import ua.in.zeusapps.ukrainenews.adapter.BaseViewHolder;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;

public class AdsProvider implements RecyclerViewAdapter.AdsProvider {

    private final Context _context;

    public AdsProvider(Context context) {
        _context = context;
    }

    @Override
    public int getAdsOffset() {
        return 3;
    }

    @Override
    public int getAdsPeriod() {
        return 5;
    }

    @Override
    public int getAdTypeAtPosition(int position) {
        return 0;
    }

    @Override
    public BaseViewHolder getViewHolder(LayoutInflater inflater, ViewGroup parent, int adType) {
        View view = inflater.inflate(R.layout.fragment_article_ads_template, parent, false);
        return new AdHolder(view);
    }

    @Override
    public void bindAdAtPosition(BaseViewHolder holder, int position) {
        holder.update(_context, null);
    }
}
