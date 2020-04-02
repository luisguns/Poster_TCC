package com.tcc.luis.poster.helpers;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressHelper {

    public static void show(ProgressBar pb, View layout, boolean show) {
        pb.setVisibility(show ? View.VISIBLE : View.GONE);
        if (layout != null) {

            layout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
