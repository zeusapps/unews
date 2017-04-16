package ua.in.zeusapps.ukrainenews.modules.root;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;

import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;

public abstract class BaseRootFragment
        extends MvpFragment
        implements BaseRootView {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setRetainInstance(true);

        RootActivity activity = (RootActivity) getActivity();

        //noinspection unchecked
        //TODO SET ROUTER
        //getPresenter().setRouter(mainActivity);
        activity.resolveToolbar(this);
        activity.resolveFab(this);
    }

    public abstract String getTitle();

    @DrawableRes
    public abstract int getFabButtonIcon();

    public abstract View.OnClickListener getFabButtonAction();

    @Override
    public void showError(String message){
        NotificationHelper.showSnackbarErrorMessage(getView(), message);
    }

    @Override
    public void showInfo(String message){
        NotificationHelper.showSnackbarInfoMessage(getView(), message);
    }
}
