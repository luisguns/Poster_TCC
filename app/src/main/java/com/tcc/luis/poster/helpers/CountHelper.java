package com.tcc.luis.poster.helpers;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.tcc.luis.poster.usuario.activits.LoginActivity;

public class CountHelper {

    public static void logout(final Activity activity){
        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
}
