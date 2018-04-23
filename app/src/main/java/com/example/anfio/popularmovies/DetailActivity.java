package com.example.anfio.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anfio.popularmovies.adapters.MovieAdapter;
import com.example.anfio.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleTv = findViewById(R.id.tv_title);
        ImageView imageUrlIv = findViewById(R.id.iv_movie_image);
        TextView synopsisTv = findViewById(R.id.tv_synopsis);
        TextView ratingTv = findViewById(R.id.tv_rating);
        TextView releaseDateTv = findViewById(R.id.tv_release_date);

        // get Movie data from the intent (Parcelable is used)
        Movie myParcelable = getIntent().getParcelableExtra("myDataKey");
        String title = myParcelable.getTitle();
        String imageUrl = myParcelable.getImageUrl();
        String synopsis = myParcelable.getSynopsis();
        double rating = myParcelable.getRating();
        String releaseDate = myParcelable.getReleaseDate();

        titleTv.setText(title);
        // build complete image path
        String path = MovieAdapter.BASE_URL_IMAGE + imageUrl;
        Picasso.with(this).load(path).into(imageUrlIv);
        synopsisTv.setText(synopsis);
        ratingTv.setText(String.valueOf(rating));
        releaseDateTv.setText(releaseDate);
    }
}