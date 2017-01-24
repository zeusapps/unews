package ua.in.zeusapps.ukrainenews.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity implements BaseMVP.IView {

    protected App application;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (App) getApplication();

        setContentView(getContentResourceId());
        ButterKnife.bind(this);
        inject(application.getComponent());

        getPresenter().setView(this);
        onCreateOverride(savedInstanceState);
    }

    protected void onCreateOverride(@Nullable Bundle savedInstanceState){

    }

    @LayoutRes
    protected abstract int getContentResourceId();

    protected abstract void inject(ApplicationComponent component);

    @NonNull
    protected abstract BaseMVP.IPresenter getPresenter();
}
