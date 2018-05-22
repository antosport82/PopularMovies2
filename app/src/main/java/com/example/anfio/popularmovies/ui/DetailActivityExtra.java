package com.example.anfio.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.models.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivityExtra extends AppCompatActivity {

    @BindView(R.id.tv_author) TextView authorTv;
    @BindView(R.id.tv_content) TextView contentTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_extra);
        ButterKnife.bind(this);

        final Review myParcelable = getIntent().getParcelableExtra("myReviewKey");
        String author = myParcelable.getAuthor();
        String content = myParcelable.getContent();

        authorTv.setText(author);
        contentTv.setText(content);
    }
}