package ua.in.zeusapps.ukrainenews.common;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import ua.in.zeusapps.ukrainenews.helpers.FragmentHelper;

public class MvpRouter {

    private MvpActivity _activity;

    void registerActivity(MvpActivity activity){
        _activity = activity;
    }

    protected void addClearStack(MvpFragment fragment, @IdRes int containerId){
        FragmentHelper.clearStack(getSupportFragmentManager());
        FragmentHelper.replace(
                getSupportFragmentManager(),
                fragment,
                containerId);
    }

    protected void addToStack(MvpFragment fragment, @IdRes int containerId){
        FragmentHelper.add(
                getSupportFragmentManager(),
                fragment,
                containerId);
    }

    protected Intent createIntent(Class cls){
        return new Intent(getActivity(), cls);
    }

    protected void startIntentAndFinish(Intent intent){
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private FragmentManager getSupportFragmentManager(){
        return getActivity().getSupportFragmentManager();
    }

    private MvpActivity getActivity() {
        return _activity;
    }
}
