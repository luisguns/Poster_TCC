package com.tcc.luis.poster.helpers;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ImageHelper {

    public static RoundedBitmapDrawable makeRoundDrawer(Bitmap imageBitmap, Resources resources) {
        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap);
        imageDrawable.setCircular(true);
        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
        return imageDrawable;
    }

    public static void carregaImagem(final View imagem, final String url, final Activity activity, final ProgressBar progressBar) {
        Picasso.with(activity)
                .load(url)
                .resize(512, 512)
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into((ImageView) imagem, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            ProgressHelper.show(progressBar, null, false);
                        }
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(activity)
                                .load(url)
                                //.error(R.drawable.header)
                                .resize(512, 512)
                                .centerCrop()
                                .into((ImageView) imagem, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        // mProgressBar.setVisibility(View.GONE);
                                        if (progressBar != null) {
                                            ProgressHelper.show(progressBar, null, false);
                                        }
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                        ProgressHelper.show(progressBar, null, false);

                                    }
                                });

                    }
                });
    }
}
