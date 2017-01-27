package ua.in.zeusapps.ukrainenews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private T _data;

    public BaseViewHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void update(Context context, T data){
        itemView.setTag(data);
        _data = data;
    }

    public T getData(){
        return _data;
    }
}
