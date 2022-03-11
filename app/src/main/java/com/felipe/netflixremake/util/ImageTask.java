package com.felipe.netflixremake.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.felipe.netflixremake.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> movieImageView;
    private boolean shadowsEnabled;

    public ImageTask(ImageView imageView) {
        this.movieImageView = new WeakReference<>(imageView);
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];

        HttpsURLConnection urlConnection = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = (HttpsURLConnection) requestUrl.openConnection();

            if (urlConnection.getResponseCode() >= 400) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null)
                return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
            bitmap = null;

        ImageView imageView = this.movieImageView.get();
        if (imageView != null && bitmap != null) {

            if (shadowsEnabled) {
                LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(imageView.getContext(),
                        R.drawable.movie_shadows);
                if (drawable != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    drawable.setDrawableByLayerId(R.id.drawable_layer_list_image, bitmapDrawable);
                    imageView.setImageDrawable(drawable);
                }
            } else {
                if (bitmap.getWidth() < imageView.getWidth() || bitmap.getHeight() < imageView.getHeight()) {
                    Matrix matrix = new Matrix();
                    matrix.postScale((float) imageView.getWidth() / (float) bitmap.getWidth(),
                            (float) imageView.getHeight() / (float) bitmap.getHeight());

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                }

                imageView.setImageBitmap(bitmap);
            }
        }

    }
}
