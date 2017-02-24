package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<TItem>
        extends RecyclerView.Adapter<BaseViewHolder>
        implements View.OnClickListener {

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
        _items.clear();
        notifyDataSetChanged();
        _items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void addAll(List<TItem> items, int index){
        if (!checkNewItems(items)){
            return;
        }

        _items.addAll(index, items);
        notifyDataSetChanged();
    }

    public void addAll(List<TItem> items){
        if (!checkNewItems(items)){
            return;
        }

        _items.addAll(items);
        notifyDataSetChanged();
    }

    public TItem get(int position){
        return _items.get(position);
    }

    public TItem getLast(){
        if (_items.size() == 0){
            return null;
        }

        return _items.get(_items.size() - 1);
    }

    public TItem getFirst(){
        if (_items.size() == 0){
            return null;
        }

        return _items.get(0);
    }

    private Activity getActivity(){
        return _activity;
    }

    LayoutInflater getLayoutInflater(){
        return _layoutInflater;
    }

    View getSimpleView(@LayoutRes int resourceId, ViewGroup parent){
        View view = getLayoutInflater().inflate(resourceId, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(BaseViewHolder holder, int position){
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
