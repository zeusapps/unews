package ua.in.zeusapps.ukrainenews.modules.settings;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public class SettingsActivity extends BaseActivity implements SettingsMVP.View{

    @Inject
    SettingsMVP.Presenter presenter;

    @OnClick(R.id.activity_settings_clearButton)
    public void onClearStorage(){
        getPresenter().clearStorage();
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @NonNull
    @Override
    protected SettingsMVP.Presenter getPresenter() {
        return presenter;
    }

}
