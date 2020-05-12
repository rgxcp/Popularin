package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmMetadataRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.FilmDetailRequest;
import xyz.fairportstudios.popularin.fragments.FilmStatusModal;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.models.FilmMetadata;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;

public class FilmDetailActivity extends AppCompatActivity {
    private Context context;
    private ImageView filmPoster;
    private ImageView filmBackdrop;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private ScrollView scroll;
    private String title;
    private String year;
    private TextView filmTitle;
    private TextView filmGenre;
    private TextView filmDate;
    private TextView filmRuntime;
    private TextView totalReview;
    private TextView totalFavorite;
    private TextView totalWatchlist;
    private TextView filmOverview;
    private TextView averageRating;
    private TextView rate05;
    private TextView rate10;
    private TextView rate15;
    private TextView rate20;
    private TextView rate25;
    private TextView rate30;
    private TextView rate35;
    private TextView rate40;
    private TextView rate45;
    private TextView rate50;
    private TextView emptyResult;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Binding
        context = FilmDetailActivity.this;
        filmPoster = findViewById(R.id.image_afd_poster);
        filmBackdrop = findViewById(R.id.image_afd_backdrop);
        progressBar = findViewById(R.id.pbr_afd_layout);
        layout = findViewById(R.id.layout_afd_anchor);
        scroll = findViewById(R.id.scroll_afd_layout);
        filmTitle = findViewById(R.id.text_afd_title);
        filmGenre = findViewById(R.id.text_afd_genre);
        filmDate = findViewById(R.id.text_afd_date);
        filmRuntime = findViewById(R.id.text_afd_runtime);
        totalReview = findViewById(R.id.text_afd_total_review);
        totalFavorite = findViewById(R.id.text_afd_total_favorite);
        totalWatchlist = findViewById(R.id.text_afd_total_watchlist);
        filmOverview = findViewById(R.id.text_afd_overview);
        averageRating = findViewById(R.id.text_afd_avg_rating);
        rate05 = findViewById(R.id.text_afd_rate_05);
        rate10 = findViewById(R.id.text_afd_rate_10);
        rate15 = findViewById(R.id.text_afd_rate_15);
        rate20 = findViewById(R.id.text_afd_rate_20);
        rate25 = findViewById(R.id.text_afd_rate_25);
        rate30 = findViewById(R.id.text_afd_rate_30);
        rate35 = findViewById(R.id.text_afd_rate_35);
        rate40 = findViewById(R.id.text_afd_rate_40);
        rate45 = findViewById(R.id.text_afd_rate_45);
        rate50 = findViewById(R.id.text_afd_rate_50);
        emptyResult = findViewById(R.id.text_afd_empty);
        toolbar = findViewById(R.id.toolbar_afd_layout);
        FloatingActionButton fab = findViewById(R.id.fab_afd_layout);
        LinearLayout reviewLayout = findViewById(R.id.review_afd_layout);
        LinearLayout favoriteLayout = findViewById(R.id.favorite_afd_layout);
        LinearLayout watchlistLayout = findViewById(R.id.watchlist_afd_layout);
        RecyclerView recyclerCast = findViewById(R.id.recycler_cast_afd_layout);
        RecyclerView recyclerCrew = findViewById(R.id.recycler_crew_afd_layout);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        final String filmID = Objects.requireNonNull(bundle).getString("FILM_ID");

        // List
        List<Cast> castList = new ArrayList<>();
        List<Crew> crewList = new ArrayList<>();

        // GET Detail
        FilmDetailRequest filmDetailRequest = new FilmDetailRequest(context, castList, crewList, recyclerCast, recyclerCrew, filmID);
        filmDetailRequest.sendRequest(new FilmDetailRequest.APICallback() {
            @Override
            public void onSuccess(FilmDetail filmDetail) {
                // Parsing
                title = filmDetail.getOriginal_title();
                year = new ParseDate().getYear(filmDetail.getRelease_date());
                String date = new ParseDate().getDate(filmDetail.getRelease_date());
                String backdrop = new ParseImage().getImage(filmDetail.getBackdrop_path());
                String poster = new ParseImage().getImage(filmDetail.getPoster_path());

                // Toolbar
                toolbar.setTitle(title);

                // Request
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).placeholder(R.color.colorPrimaryDark);

                // Isi
                filmTitle.setText(title);
                filmGenre.setText(filmDetail.getGenre());
                filmDate.setText(date);
                filmRuntime.setText(String.format("%s menit", String.valueOf(filmDetail.getRuntime())));
                filmOverview.setText(filmDetail.getOverview());
                Glide.with(context).load(backdrop).apply(requestOptions).into(filmBackdrop);
                Glide.with(context).load(poster).apply(requestOptions).into(filmPoster);

                progressBar.setVisibility(View.GONE);
                scroll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // GET Metadata
        FilmMetadataRequest filmMetadataRequest = new FilmMetadataRequest(context, filmID);
        filmMetadataRequest.sendRequest(new FilmMetadataRequest.APICallback() {
            @Override
            public void onSuccess(FilmMetadata filmMetadata) {
                totalReview.setText(String.valueOf(filmMetadata.getReviews()));
                totalFavorite.setText(String.valueOf(filmMetadata.getFavorites()));
                totalWatchlist.setText(String.valueOf(filmMetadata.getWatchlists()));
                averageRating.setText(String.format("%s/5.0", String.valueOf(filmMetadata.getAverage_rating())));
                rate05.setText(String.valueOf(filmMetadata.getRate_05()));
                rate10.setText(String.valueOf(filmMetadata.getRate_10()));
                rate15.setText(String.valueOf(filmMetadata.getRate_15()));
                rate20.setText(String.valueOf(filmMetadata.getRate_20()));
                rate25.setText(String.valueOf(filmMetadata.getRate_25()));
                rate30.setText(String.valueOf(filmMetadata.getRate_30()));
                rate35.setText(String.valueOf(filmMetadata.getRate_35()));
                rate40.setText(String.valueOf(filmMetadata.getRate_40()));
                rate45.setText(String.valueOf(filmMetadata.getRate_45()));
                rate50.setText(String.valueOf(filmMetadata.getRate_50()));
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmReview = new Intent(context, FilmReviewActivity.class);
                gotoFilmReview.putExtra("FILM_ID", filmID);
                startActivity(gotoFilmReview);
            }
        });

        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFavoritedBy = new Intent(context, FavoritedByActivity.class);
                gotoFavoritedBy.putExtra("FILM_ID", filmID);
                startActivity(gotoFavoritedBy);
            }
        });

        watchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoWatchlistedBy = new Intent(context, WatchlistedByActivity.class);
                gotoWatchlistedBy.putExtra("FILM_ID", filmID);
                startActivity(gotoWatchlistedBy);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilmStatusModal filmStatusModal = new FilmStatusModal(filmID, title, year);
                filmStatusModal.show(getSupportFragmentManager(), "FILM_STATUS_MODAL");
            }
        });
    }
}
