package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BaseAdsAdapter<TItem> extends BaseAdapter<TItem> {
    private AdsProvider _adsProvider;

    private final static int CONTENT_TYPE = -1;

    BaseAdsAdapter(
            Activity activity,
            AdsProvider adsProvider) {
        super(activity);

        _adsProvider = adsProvider;
    }

    @Override
    public int getItemViewType(int position) {
        return isAdPosition(position)
                ? _adsProvider.getAdTypeAtPosition(position)
                : CONTENT_TYPE;
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CONTENT_TYPE){
            return onCreateContentViewHolder(parent, viewType);
        }

        return _adsProvider.getViewHolder(getLayoutInflater(), parent, viewType);
    }

    protected abstract BaseViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isAdPosition(position)){
            _adsProvider.bindAdAtPosition(holder, position);
        } else {
            int offset = _adsProvider.getAdsOffset();
            int period = _adsProvider.getAdsPeriod() + 1;

            if (position >= offset){
                position -= ((position - offset) / period + 1);
            }

            onBindContentViewHolder(holder, position);
        }
    }

    protected void onBindContentViewHolder(BaseViewHolder holder, int position){
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (count == 0){
            return 0;
        }


        int offset = _adsProvider.getAdsOffset();
        int period = _adsProvider.getAdsPeriod();

        if (count < offset){
            return count;
        }

        int additional = (count - offset) / period + 1;
        return count + additional;
    }

    private boolean isAdPosition(int position){
        int offset = _adsProvider.getAdsOffset();

        if (position < offset){
            return false;
        }

        int period = _adsProvider.getAdsPeriod() + 1;
        return (position - offset) % period == 0;
    }




    public interface AdsProvider{
        int getAdsOffset();

        int getAdsPeriod();

        int getAdTypeAtPosition(int position);

        BaseViewHolder getViewHolder(LayoutInflater inflater, ViewGroup parent, int adType);

        void bindAdAtPosition(BaseViewHolder holder, int position);
    }


}