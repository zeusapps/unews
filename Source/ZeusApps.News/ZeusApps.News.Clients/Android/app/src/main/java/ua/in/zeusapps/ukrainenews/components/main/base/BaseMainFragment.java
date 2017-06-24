package ua.in.zeusapps.ukrainenews.components.main.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.components.main.MainActivity;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;

public abstract class BaseMainFragment
        extends MvpFragment
        implements BaseMainView {

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
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

    public MainActivity getRootActivity(){
        return (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String message = savedInstanceState == null
                ? "null"
                : String.valueOf(savedInstanceState.size());
        Log.i(getClass().getSimpleName(), "onCreate: " + message);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(getClass().getSimpleName(), "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(getClass().getSimpleName(), "onResume()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String message = outState == null
                ? "null"
                : String.valueOf(outState.size());

        Log.i(getClass().getSimpleName(), "onSaveInstanceState(): " + message);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(getClass().getSimpleName(), "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(getClass().getSimpleName(), "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(getClass().getSimpleName(), "onDestroy()");
    }
}
