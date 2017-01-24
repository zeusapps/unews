package ua.in.zeusapps.ukrainenews.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public abstract class BaseFragment extends Fragment implements BaseMVP.IView{

    protected App application;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentResourceId(), container, false);
        ButterKnife.bind(this, view);
        onCreateViewOverride(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        application = (App) getActivity().getApplication();
        inject(application.getComponent());
        getPresenter().setView(this);
    }

    protected void onCreateViewOverride(View view){

    }

    protected abstract BaseMVP.IPresenter getPresenter();

    @LayoutRes
    protected abstract int getContentResourceId();

    protected abstract void inject(ApplicationComponent component);
}
