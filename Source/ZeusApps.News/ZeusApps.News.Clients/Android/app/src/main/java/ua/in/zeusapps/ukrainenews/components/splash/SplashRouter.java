package ua.in.zeusapps.ukrainenews.components.splash;

import android.content.Intent;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpRouter;
import ua.in.zeusapps.ukrainenews.modules.root.RootActivity;

class SplashRouter extends MvpRouter {

    @Inject
    public SplashRouter() {
    }

    void startApp() {
        Intent intent = createIntent(RootActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startIntentAndFinish(intent);
    }
}
