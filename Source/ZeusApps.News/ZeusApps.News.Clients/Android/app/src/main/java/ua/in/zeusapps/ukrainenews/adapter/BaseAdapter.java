package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<THolder extends BaseViewHolder, TItem> extends RecyclerView.Adapter<THolder> {

    protected final List<TItem> items;
    private final Activity _activity;
    private final LayoutInflater _layoutInflater;

    public BaseAdapter(Activity activity) {
        this.items = new ArrayList<>();
        _activity = activity;
        _layoutInflater = activity.getLayoutInflater();
    }

    public void replaceAll(List<TItem> newItems){
        items.clear();
        items.addAll(newItems);
        notifyItemRangeInserted(0, items.size());
    }

    public TItem get(int position){
        return items.get(position);
    }

    protected Activity getActivity(){
        return _activity;
    }

    protected LayoutInflater getLayoutInflater(){
        return _layoutInflater;
    }

    protected View getSimpleView(@LayoutRes int resourceId, ViewGroup parent){
        return getLayoutInflater().inflate(resourceId, parent, false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public abstract THolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(THolder holder, int position){
        holder.update(getActivity(), get(position));
    }
}
