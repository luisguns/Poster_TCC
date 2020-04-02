package com.tcc.luis.poster.helpers;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarHelper {

    public static void makeWithMensageOnly(View layout, String menssagem, boolean isLong, Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.getApplicationContext().INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus != null){

        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        Snackbar snack = Snackbar.make(layout,menssagem, (isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_INDEFINITE));
        snack.show();
    }

    public static void makeWithMensageAndAction(View layout, String menssagem,String actionName, View.OnClickListener action,boolean isIndefinid, Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.getApplicationContext().INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus != null){

            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        Snackbar snack = Snackbar.make(layout,menssagem, (isIndefinid ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG));
        snack.setAction(actionName, action);
        snack.show();
    }

}
