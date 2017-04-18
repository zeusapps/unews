package ua.in.zeusapps.ukrainenews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class RecyclerViewAdapter<TItem>
        extends RecyclerView.Adapter<BaseViewHolder> {

    private final PublishSubject<TItem> itemClickedSubject = PublishSubject.create();
    private final LayoutInflater _layoutInflater;
    private final List<TItem> _items;
    private final Context _context;
    private AdsProvider _adsProvider;

    private final static int CONTENT_TYPE = -1;

    public RecyclerViewAdapter(Context context) {
        _context = context;
        _layoutInflater = LayoutInflater.from(context);
        _items = new ArrayList<>();
    }

    public Observable<TItem> getItemClicked(){
        return itemClickedSubject.asObservable();
    }

    public void addAll(List<TItem> items){
        _items.addAll(items);
        notifyDataSetChanged();
    }

    public void addAll(int index, List<TItem> items){
        for (TItem item: items) {
            _items.add(index, item);
            index++;
        }

        notifyDataSetChanged();
    }

    public void add(TItem item){
        _items.add(item);
        notifyDataSetChanged();
    }

    public void add(int position, TItem item){
        _items.add(position, item);
        notifyDataSetChanged();
    }

    public List<TItem> getAll(){
        return _items;
    }

    public void clear(){
        _items.clear();
        notifyDataSetChanged();
    }

    public TItem getFirst(){
        return _items.size() > 0
                ? _items.get(0)
                : null;
    }

    public TItem getLast(){
        return _items.size() > 0
                ? _items.get(_items.size() - 1)
                : null;
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

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isAdPosition(position)){
            int adPosition = getAdditionalCount(position);
            _adsProvider.bindAdAtPosition(holder, adPosition);
            return;
        }

        if (_adsProvider != null){
            int offset = _adsProvider.getAdsOffset();
            int period = _adsProvider.getAdsPeriod() + 1;

            if (position >= offset) {
                position -= Math.floor((position - offset) / period);
            }
        }

        final TItem item = _items.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickedSubject.onNext(item);
            }
        });
        //noinspection unchecked
        holder.update(_context, item);
    }

    protected abstract BaseViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType);

//    @Override
//    public final void onBindViewHolder(BaseViewHolder holder, int position) {
//        if (_adsProvider == null){
//            super.onBindViewHolder(holder, position);
//            return;
//        }
//
//        if (isAdPosition(position)){
//            position = (position - _adsProvider.getAdsOffset()) / _adsProvider.getAdsPeriod();
//            _adsProvider.bindAdAtPosition(holder, position);
//        } else {
//            int offset = _adsProvider.getAdsOffset();
//            int period = _adsProvider.getAdsPeriod() + 1;
//
//            if (position >= offset){
//                position -= ((position - offset) / period + 1);
//            }
//
//            onBindContentViewHolder(holder, position);
//        }
//    }

//    protected void onBindContentViewHolder(BaseViewHolder holder, int position){
//        super.onBindViewHolder(holder, position);
//    }

    @Override
    public int getItemCount() {
        int count = _items.size();

        if (_adsProvider == null){
            return count;
        }

        return count + getAdditionalCount(count);




//        if (count == 0){
//            return 0;
//        } else if (_adsProvider == null){
//            return count;
//        }
//
//
//        int offset = _adsProvider.getAdsOffset();
//        int period = _adsProvider.getAdsPeriod();
//
//        if (count < offset){
//            return count;
//        }
//
//        int additional = (count - offset) / period + 1;
//        return count + additional;
    }

    public void setAdsProvider(AdsProvider provider){
        _adsProvider = provider;
    }

    protected LayoutInflater getLayoutInflater() {
        return _layoutInflater;
    }

    private boolean isAdPosition(int position){
        if (_adsProvider == null){
            return false;
        }

        int offset = _adsProvider.getAdsOffset();

        if (position < offset){
            return false;
        }

        int period = _adsProvider.getAdsPeriod() + 1;
        return (position - offset) % period == 0;
    }

    private int getAdditionalCount(int count){

        if (_adsProvider == null || _adsProvider.getAdsOffset() > count){
            return 0;
        }

        return (int) Math.floor((count - _adsProvider.getAdsOffset()) / _adsProvider.getAdsPeriod());
    }

    public interface AdsProvider{
        int getAdsOffset();

        int getAdsPeriod();

        int getAdTypeAtPosition(int position);

        BaseViewHolder getViewHolder(LayoutInflater inflater, ViewGroup parent, int adType);

        void bindAdAtPosition(BaseViewHolder holder, int position);
    }
}