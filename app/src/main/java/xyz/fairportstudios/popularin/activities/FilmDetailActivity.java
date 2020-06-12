package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmMetadataRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.FilmDetailRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.models.FilmMetadata;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseGenre;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseTime;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmDetailActivity extends AppCompatActivity {
    private Chip chipGenre;
    private Chip chipRuntime;
    private Chip chipRating;
    private CoordinatorLayout anchorLayout;
    private ImageView imagePoster;
    private ImageView imageEmptyOverview;
    private Integer genreID;
    private ProgressBar progressBar;
    private RecyclerView recyclerCast;
    private RecyclerView recyclerCrew;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;
    private String genreTitle;
    private String youtubeKey;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textOverview;
    private TextView textMessage;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Context
        final Context context = FilmDetailActivity.this;

        // Binding
        chipGenre = findViewById(R.id.chip_afd_genre);
        chipRuntime = findViewById(R.id.chip_afd_runtime);
        chipRating = findViewById(R.id.chip_afd_rating);
        anchorLayout = findViewById(R.id.anchor_afd_layout);
        imagePoster = findViewById(R.id.image_afd_poster);
        imageEmptyOverview = findViewById(R.id.image_afd_empty_overview);
        progressBar = findViewById(R.id.pbr_afd_layout);
        recyclerCast = findViewById(R.id.recycler_afd_cast);
        recyclerCrew = findViewById(R.id.recycler_afd_crew);
        textTotalReview = findViewById(R.id.text_afd_total_review);
        textTotalFavorite = findViewById(R.id.text_afd_total_favorite);
        textTotalWatchlist = findViewById(R.id.text_afd_total_watchlist);
        textOverview = findViewById(R.id.text_afd_overview);
        textMessage = findViewById(R.id.text_afd_message);
        toolbar = findViewById(R.id.toolbar_afd_layout);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar_afd_layout);
        FloatingActionButton fab = findViewById(R.id.fab_afd_layout);
        ImageView imagePlayTrailer = findViewById(R.id.image_afd_play);
        RelativeLayout totalReviewLayout = findViewById(R.id.layout_afd_total_review);
        RelativeLayout totalFavoriteLayout = findViewById(R.id.layout_afd_total_favorite);
        RelativeLayout totalWatchlistLayout = findViewById(R.id.layout_afd_total_watchlist);

        // Extra
        Intent intent = getIntent();
        final Integer filmID = intent.getIntExtra(Popularin.FILM_ID, 0);

        // Mengatur jenis font untuk collapsing toolbar
        Typeface typeface = ResourcesCompat.getFont(context, R.font.monument_extended_regular);
        collapsingToolbar.setExpandedTitleTypeface(typeface);

        // Mendapatkan detail film
        getFilmDetail(context, filmID);

        // Mendapatkan metadata film
        getFilmMetadata(context, filmID);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imagePlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!youtubeKey.isEmpty()) {
                    playTrailer(youtubeKey);
                } else {
                    searchTrailer(filmTitle.toLowerCase() + " trailer");
                }
            }
        });

        chipGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFilmList(context, genreID, genreTitle);
            }
        });

        totalReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFilmReview(context, filmID);
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFavoritedBy(context, filmID);
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWatchlistedBy(context, filmID);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilmModal(filmID, filmTitle, filmYear, filmPoster);
            }
        });
    }

    private void getFilmDetail(final Context context, Integer filmID) {
        FilmDetailRequest filmDetailRequest = new FilmDetailRequest(context, filmID, recyclerCast, recyclerCrew);
        filmDetailRequest.sendRequest(new FilmDetailRequest.Callback() {
            @Override
            public void onSuccess(FilmDetail filmDetail) {
                // Setter
                filmTitle = filmDetail.getOriginal_title();
                genreID = filmDetail.getGenre_id();
                youtubeKey = filmDetail.getVideo_key();
                String overview = filmDetail.getOverview();

                // Parsing
                filmYear = new ParseDate().getYear(filmDetail.getRelease_date());
                filmPoster = new ParseImage().getImage(filmDetail.getPoster_path());
                genreTitle = new ParseGenre().getGenre(genreID);
                String runtime = new ParseTime().getHourMinute(filmDetail.getRuntime());

                // Request gambar
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.colorSurface)
                        .error(R.color.colorSurface);

                // Isi
                toolbar.setTitle(filmTitle);
                chipGenre.setText(genreTitle);
                chipRuntime.setText(runtime);
                if (!overview.isEmpty()) {
                    textOverview.setVisibility(View.VISIBLE);
                    textOverview.setText(overview);
                } else {
                    imageEmptyOverview.setVisibility(View.VISIBLE);
                }
                Glide.with(context).load(filmPoster).apply(requestOptions).into(imagePoster);
                progressBar.setVisibility(View.GONE);
                anchorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(message);
            }
        });
    }

    private void getFilmMetadata(Context context, Integer filmID) {
        FilmMetadataRequest filmMetadataRequest = new FilmMetadataRequest(context, filmID);
        filmMetadataRequest.sendRequest(new FilmMetadataRequest.Callback() {
            @Override
            public void onSuccess(FilmMetadata filmMetadata) {
                chipRating.setText(String.format("%s/5", filmMetadata.getAverage_rating()));
                textTotalReview.setText(String.format(Locale.getDefault(), "%d Ulasan", filmMetadata.getTotal_review()));
                textTotalFavorite.setText(String.format(Locale.getDefault(), "%d Favorit", filmMetadata.getTotal_favorite()));
                textTotalWatchlist.setText(String.format(Locale.getDefault(), "%d Watchlist", filmMetadata.getTotal_watchlist()));
            }

            @Override
            public void onNotFound() {
                // Tidak digunakan
            }

            @Override
            public void onError(String message) {
                Snackbar.make(anchorLayout, R.string.failed_retrieve_metadata, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void playTrailer(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Popularin.YOUTUBE_VIDEO + key));
        startActivity(intent);
    }

    private void searchTrailer(String query) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Popularin.YOUTUBE_SEARCH + query));
        startActivity(intent);
    }

    private void gotoFilmList(Context context, Integer genreID, String genreTitle) {
        Intent intent = new Intent(context, FilmListActivity.class);
        intent.putExtra(Popularin.GENRE_ID, genreID);
        intent.putExtra(Popularin.GENRE_TITLE, genreTitle);
        startActivity(intent);
    }

    private void gotoFilmReview(Context context, Integer filmID) {
        Intent intent = new Intent(context, FilmReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void gotoFavoritedBy(Context context, Integer filmID) {
        Intent intent = new Intent(context, FavoritedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void gotoWatchlistedBy(Context context, Integer filmID) {
        Intent intent = new Intent(context, WatchlistedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void showFilmModal(Integer filmID, String filmTitle, String filmYear, String filmPoster) {
        FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_STATUS_MODAL);
    }
}
