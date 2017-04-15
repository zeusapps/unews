package ua.in.zeusapps.ukrainenews.helpers;


import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class NotificationHelper {
    private NotificationHelper(){

    }

    public static void showSnackbarMessage(View view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showSnackbarMessage(View view, String message, @ColorRes int resource){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        int color = ContextCompat
                .getColor(view.getContext(), resource);
        snackbar.getView()
                .setBackgroundColor(color);
        snackbar.show();
    }

    public static void showSnackbarErrorMessage(View view, String message){
        showSnackbarMessage(view, message, android.R.color.holo_red_dark);
    }

    public static void showSnackbarInfoMessage(View view, String message){
        showSnackbarMessage(view, message, android.R.color.holo_green_dark);
    }
}
