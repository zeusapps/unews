package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<THolder extends BaseViewHolder, TItem>
        extends RecyclerView.Adapter<THolder>
        implements View.OnClickListener {

    public final static int CONTENT_TYPE = 1;
    public final static int AD_TYPE = 2;

    private final List<TItem> _items;
    private final Activity _activity;
    private final LayoutInflater _layoutInflater;
    private OnItemClickListener<TItem> _listener;

    BaseAdapter(Activity activity) {
        _items = new ArrayList<>();
        _activity = activity;
        _layoutInflater = activity.getLayoutInflater();
    }

    public void replaceAll(List<TItem> newItems){
        int count = _items.size();
        _items.clear();
        notifyItemRangeRemoved(0, count);
        _items.addAll(newItems);
        count = _items.size();
        notifyItemRangeInserted(0, count);
    }

    public void addAll(List<TItem> items, int index){
        if (!checkNewItems(items)){
            return;
        }

        _items.addAll(index, items);
        notifyItemRangeInserted(0, items.size());
    }

    public void addAll(List<TItem> items){
        if (!checkNewItems(items)){
            return;
        }

        int position = getItemCount();
        _items.addAll(items);
        notifyItemRangeInserted(position, items.size());
    }

    public TItem get(int position){
        return _items.get(position);
    }

    protected Activity getActivity(){
        return _activity;
    }

    protected LayoutInflater getLayoutInflater(){
        return _layoutInflater;
    }

    protected View getSimpleView(@LayoutRes int resourceId, ViewGroup parent){
        View view = getLayoutInflater().inflate(resourceId, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 3){
            return AD_TYPE;
        }

        return CONTENT_TYPE;

        //return super.getItemViewType(position);
    }

    public abstract THolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(THolder holder, int position){
        holder.update(getActivity(), get(position));
    }

    public void setOnItemClickListener(OnItemClickListener<TItem> listener){
        _listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        if (_listener == null){
            return;
        }

        TItem item = (TItem) v.getTag();
        _listener.onItemClick(item);
    }

    public interface OnItemClickListener<TItem>{
        void onItemClick(TItem item);
    }

    private boolean checkNewItems(List<TItem> items){
        if (items == null || _items.size() == 0){
            return false;
        }

        return true;
    }
}
