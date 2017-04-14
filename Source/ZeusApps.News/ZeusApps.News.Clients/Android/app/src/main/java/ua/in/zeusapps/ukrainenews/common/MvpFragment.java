package ua.in.zeusapps.ukrainenews.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;

import butterknife.ButterKnife;

public abstract class MvpFragment
        extends MvpAppCompatFragment {

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        Class cls = getClass();

        if (!cls.isAnnotationPresent(Layout.class)) {
            return null;
        }

        Layout layout = (Layout) cls.getAnnotation(Layout.class);
        View view = inflater.inflate(layout.value(), null);
        ButterKnife.bind(this, view);
        return view;
    }
}
