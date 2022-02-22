package com.felipe.netflixremake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Objects;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        LayerDrawable layerDrawable =
                (LayerDrawable) ContextCompat.getDrawable(this,R.drawable.movie_shadows);

        if (layerDrawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie);
            layerDrawable.setDrawableByLayerId(R.id.drawable_layer_list_image, movieCover);
            ((ImageView) findViewById(R.id.image_view_cover)).setImageDrawable(layerDrawable);
        }
    }
}