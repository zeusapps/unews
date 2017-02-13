package ua.in.zeusapps.ukrainenews.modules.settings;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public class SettingsFragment
        extends BaseFragment
        implements SettingsMVP.View{

    public static final String TAG = SettingsFragment.class.getSimpleName();

    @Inject
    SettingsMVP.Presenter presenter;

    @OnClick(R.id.activity_settings_clearButton)
    public void onClearStorage(){
        getPresenter().clearStorage();
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_settings;
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
