package com.felipe.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipe.netflixremake.model.Movie;
import com.felipe.netflixremake.model.MovieDetail;
import com.felipe.netflixremake.util.Constants;
import com.felipe.netflixremake.util.ImageTask;
import com.felipe.netflixremake.util.MovieDetailTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

    private RecyclerView recyclerView;
    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        recyclerView = findViewById(R.id.recycler_view_similar);
        txtTitle = findViewById(R.id.text_view_title);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }

        LayerDrawable layerDrawable =
                (LayerDrawable) ContextCompat.getDrawable(this,R.drawable.movie_shadows);

        List<Movie> movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(movies);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = (int) extras.get("id");
            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this);
            movieDetailTask.execute(Constants.MOVIE_DETAIL_BASE_URL + id);
        }
    }

    @Override
    public void onResponse(MovieDetail movieDetail) {
        this.txtTitle.setText(movieDetail.getMovie().getTitle());
        this.txtDesc.setText(movieDetail.getMovie().getDesc());
        this.txtCast.setText(movieDetail.getMovie().getCast());

        movieAdapter.setMovies(movieDetail.getSimilarMovies());
        movieAdapter.notifyDataSetChanged();
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {

        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieActivity.MovieHolder> {

        private List<Movie> movies;

        public MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies.clear();
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieActivity.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieActivity.MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieActivity.MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImageTask(holder.imageViewCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}