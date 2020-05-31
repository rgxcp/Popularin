package xyz.fairportstudios.popularin.activities;

import android.content.ActivityNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmMetadataRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.FilmDetailRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.models.FilmMetadata;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseGenre;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseTime;
import xyz.fairportstudios.popularin.services.Popularin;

public class FilmDetailActivity extends AppCompatActivity {
    private Integer genreID;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;
    private String genreTitle;
    private String youtubeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Binding
        final Context context = FilmDetailActivity.this;
        final Chip chipGenre = findViewById(R.id.chip_afd_genre);
        final Chip chipRuntime = findViewById(R.id.chip_afd_runtime);
        final Chip chipRating = findViewById(R.id.chip_afd_rating);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar_afd_layout);
        final CoordinatorLayout anchorLayout = findViewById(R.id.anchor_afd_layout);
        final FloatingActionButton fab = findViewById(R.id.fab_afd_layout);
        final ImageView imagePoster = findViewById(R.id.image_afd_poster);
        final ImageView imagePlayTrailer = findViewById(R.id.image_afd_play);
        final ImageView imageEmptyOverview = findViewById(R.id.image_afd_empty_overview);
        final ProgressBar progressBar = findViewById(R.id.pbr_afd_layout);
        final RecyclerView recyclerCast = findViewById(R.id.recycler_afd_cast);
        final RecyclerView recyclerCrew = findViewById(R.id.recycler_afd_crew);
        final RelativeLayout totalReviewLayout = findViewById(R.id.layout_afd_total_review);
        final RelativeLayout totalFavoriteLayout = findViewById(R.id.layout_afd_total_favorite);
        final RelativeLayout totalWatchlistLayout = findViewById(R.id.layout_afd_total_watchlist);
        final TextView textTotalReview = findViewById(R.id.text_afd_total_review);
        final TextView textTotalFavorite = findViewById(R.id.text_afd_total_favorite);
        final TextView textTotalWatchlist = findViewById(R.id.text_afd_total_watchlist);
        final TextView textOverview = findViewById(R.id.text_afd_overview);
        final TextView textNetworkError = findViewById(R.id.text_afd_network_error);
        final Toolbar toolbar = findViewById(R.id.toolbar_afd_layout);

        // Extra
        Intent intent = getIntent();
        final String filmID = intent.getStringExtra(Popularin.FILM_ID);

        // Font untuk collapsing toolbar
        Typeface typeface = ResourcesCompat.getFont(context, R.font.monument_extended_regular);
        collapsingToolbar.setExpandedTitleTypeface(typeface);

        // Request detail (TMDb)
        List<Cast> castList = new ArrayList<>();
        List<Crew> crewList = new ArrayList<>();
        FilmDetailRequest filmDetailRequest = new FilmDetailRequest(
                context,
                filmID,
                castList,
                crewList,
                recyclerCast,
                recyclerCrew
        );
        filmDetailRequest.sendRequest(new FilmDetailRequest.APICallback() {
            @Override
            public void onSuccess(FilmDetail filmDetail) {
                // Setter
                genreID = filmDetail.getGenre_id();
                filmTitle = filmDetail.getOriginal_title();
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
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Request metadata (Popularin)
        FilmMetadataRequest filmMetadataRequest = new FilmMetadataRequest(context, filmID);
        filmMetadataRequest.sendRequest(new FilmMetadataRequest.APICallback() {
            @Override
            public void onSuccess(FilmMetadata filmMetadata) {
                chipRating.setText(String.format("%s/5", filmMetadata.getAverage_rating()));
                textTotalReview.setText(String.format(Locale.getDefault(), "%d Ulasan", filmMetadata.getTotal_review()));
                textTotalFavorite.setText(String.format(Locale.getDefault(), "%d Favorit", filmMetadata.getTotal_favorite()));
                textTotalWatchlist.setText(String.format(Locale.getDefault(), "%d Watchlist", filmMetadata.getTotal_watchlist()));
            }

            @Override
            public void onEmpty() {
                chipRating.setText("0/5");
            }

            @Override
            public void onError() {
                chipRating.setText("0/5");
                Snackbar.make(anchorLayout, R.string.failed_retrieve_metadata, Snackbar.LENGTH_LONG).show();
            }
        });

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

        imageEmptyOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverviewMessage(anchorLayout);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilmModal(filmID, filmTitle, filmYear, filmPoster);
            }
        });
    }

    private void playTrailer(String youtubeKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeKey));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException exception) {
            startActivity(browserIntent);
        }
    }

    private void searchTrailer(String query) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + query));

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            startActivity(intent);
        }
    }

    private void gotoFilmList(Context context, Integer genreID, String genreTitle) {
        Intent intent = new Intent(context, FilmListActivity.class);
        intent.putExtra(Popularin.GENRE_ID, String.valueOf(genreID));
        intent.putExtra(Popularin.GENRE_TITLE, genreTitle);
        startActivity(intent);
    }

    private void gotoFilmReview(Context context, String filmID) {
        Intent intent = new Intent(context, FilmReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void gotoFavoritedBy(Context context, String filmID) {
        Intent intent = new Intent(context, FavoritedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void gotoWatchlistedBy(Context context, String filmID) {
        Intent intent = new Intent(context, WatchlistedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void showOverviewMessage(CoordinatorLayout anchorLayout) {
        Snackbar.make(anchorLayout, R.string.empty_overview, Snackbar.LENGTH_LONG).show();
    }

    private void showFilmModal(String filmID, String filmTitle, String filmYear, String filmPoster) {
        FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_STATUS_MODAL);
    }
}
