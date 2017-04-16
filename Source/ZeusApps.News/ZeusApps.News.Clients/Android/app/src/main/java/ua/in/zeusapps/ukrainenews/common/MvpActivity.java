package ua.in.zeusapps.ukrainenews.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public abstract class MvpActivity extends MvpAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class cls = getClass();
        if (!cls.isAnnotationPresent(Layout.class)){
            return;
        }

        Layout layout = (Layout) cls.getAnnotation(Layout.class);
        setContentView(layout.value());
        ButterKnife.bind(this);

        inject(App.getInstance().getComponent());

        //noinspection unchecked
        getPresenter().setRouter((RouterBase) this);
    }

    protected void inject(ApplicationComponent component){

    }

    protected abstract MvpPresenterBase getPresenter();
}
