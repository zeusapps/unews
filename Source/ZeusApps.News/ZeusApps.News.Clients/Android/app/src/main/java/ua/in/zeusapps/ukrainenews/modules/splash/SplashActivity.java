package ua.in.zeusapps.ukrainenews.modules.splash;

import android.content.Intent;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;
import ua.in.zeusapps.ukrainenews.modules.root.RootActivity;

@Layout(R.layout.activity_splash)
public class SplashActivity
        extends MvpActivity
        implements SplashView,SplashRouter {

    @InjectPresenter
    SplashPresenter presenter;

    @BindView(R.id.activity_splash_status)
    TextView statusTextView;

    @Override
    protected MvpPresenterBase getPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {
        statusTextView.setText(getString(R.string.splash_activity_loadingStatus));
    }

    @Override
    public void showChecking() {
        statusTextView.setText(R.string.splash_activity_checkingStatus);
    }

    @Override
    public void showError() {
        statusTextView.setText(R.string.splash_activity_oopsMessage);
        NotificationHelper.showSnackbarErrorMessage(
                statusTextView,
                getString(R.string.splash_activity_errorMessage));
    }

    @Override
    public void startApp() {
        Intent intent = new Intent(SplashActivity.this, RootActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}