package ua.in.zeusapps.ukrainenews.modules.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.modules.main.MainActivity;

public class SplashActivity
        extends BaseActivity
        implements SplashMVP.IView {

    @Inject
    SplashMVP.IPresenter presenter;

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @NonNull
    @Override
    protected SplashMVP.IPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void forceClose() {
        System.exit(0);
    }

    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        getPresenter()
                .prepare()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        forceClose();
                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (!result){
                            forceClose();
                            return;
                        }

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
