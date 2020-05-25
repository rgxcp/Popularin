package xyz.fairportstudios.popularin.activities;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmMetadataRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.FilmDetailRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.models.FilmMetadata;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.Popularin;

public class FilmDetailActivity extends AppCompatActivity {
    private Context context;
    private ImageView imageFilmPoster;
    private ImageView imageFilmBackdrop;
    private ProgressBar progressBar;
    private RelativeLayout anchorLayout;
    private RecyclerView recyclerCast;
    private RecyclerView recyclerCrew;
    private ScrollView scroll;
    private String filmID;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;
    private TextView textFilmTitle;
    private TextView textFilmGenre;
    private TextView textFilmDate;
    private TextView textFilmRuntime;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView texttTotalWatchlist;
    private TextView textFilmOverview;
    private TextView textAverageRating;
    private TextView textNetworkError;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Binding
        context = FilmDetailActivity.this;
        imageFilmPoster = findViewById(R.id.image_afd_poster);
        imageFilmBackdrop = findViewById(R.id.image_afd_backdrop);
        progressBar = findViewById(R.id.pbr_afd_layout);
        recyclerCast = findViewById(R.id.recycler_cast_afd_layout);
        recyclerCrew = findViewById(R.id.recycler_crew_afd_layout);
        anchorLayout = findViewById(R.id.layout_afd_anchor);
        scroll = findViewById(R.id.scroll_afd_layout);
        textFilmTitle = findViewById(R.id.text_afd_title);
        textFilmGenre = findViewById(R.id.text_afd_genre);
        textFilmDate = findViewById(R.id.text_afd_date);
        textFilmRuntime = findViewById(R.id.text_afd_runtime);
        textTotalReview = findViewById(R.id.text_afd_total_review);
        textTotalFavorite = findViewById(R.id.text_afd_total_favorite);
        texttTotalWatchlist = findViewById(R.id.text_afd_total_watchlist);
        textFilmOverview = findViewById(R.id.text_afd_overview);
        textAverageRating = findViewById(R.id.text_afd_avg_rating);
        textNetworkError = findViewById(R.id.text_afd_empty);
        toolbar = findViewById(R.id.toolbar_afd_layout);
        FloatingActionButton fab = findViewById(R.id.fab_afd_layout);
        LinearLayout totalReviewLayout = findViewById(R.id.review_afd_layout);
        LinearLayout totalFavoriteLayout = findViewById(R.id.favorite_afd_layout);
        LinearLayout totalWatchlistLayout = findViewById(R.id.watchlist_afd_layout);

        // Extra
        Intent intent = getIntent();
        filmID = intent.getStringExtra(Popularin.FILM_ID);

        // Mendapatkan detail film
        getFilmDetail();

        // Mendapatkan metadata film
        // getFilmMetadata();

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        totalReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFilmReview();
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFavoritedBy();
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoWatchlistedBy();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilmModal();
            }
        });
    }

    private void getFilmDetail() {
        List<Cast> castList = new ArrayList<>();
        List<Crew> crewList = new ArrayList<>();
        FilmDetailRequest filmDetailRequest = new FilmDetailRequest(context, castList, crewList, recyclerCast, recyclerCrew, filmID);
        filmDetailRequest.sendRequest(new FilmDetailRequest.APICallback() {
            @Override
            public void onSuccess(FilmDetail filmDetail) {
                // Parsing
                filmTitle = filmDetail.getOriginal_title();
                filmYear = new ParseDate().getYear(filmDetail.getRelease_date());
                filmPoster = new ParseImage().getImage(filmDetail.getPoster_path());
                String date = new ParseDate().getDate(filmDetail.getRelease_date());
                String backdrop = new ParseImage().getImage(filmDetail.getBackdrop_path());

                // Toolbar
                toolbar.setTitle(filmTitle);

                // Request
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).placeholder(R.color.colorPrimaryDark);

                // Isi
                textFilmTitle.setText(filmTitle);
                textFilmGenre.setText(filmDetail.getGenre());
                textFilmDate.setText(date);
                textFilmRuntime.setText(String.format("%s menit", String.valueOf(filmDetail.getRuntime())));
                textFilmOverview.setText(filmDetail.getOverview());
                Glide.with(context).load(backdrop).apply(requestOptions).into(imageFilmBackdrop);
                Glide.with(context).load(filmPoster).apply(requestOptions).into(imageFilmPoster);

                progressBar.setVisibility(View.GONE);
                scroll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void getFilmMetadata() {
        FilmMetadataRequest filmMetadataRequest = new FilmMetadataRequest(context, filmID);
        filmMetadataRequest.sendRequest(new FilmMetadataRequest.APICallback() {
            @Override
            public void onSuccess(FilmMetadata filmMetadata) {
                textTotalReview.setText(String.valueOf(filmMetadata.getReviews()));
                textTotalFavorite.setText(String.valueOf(filmMetadata.getFavorites()));
                texttTotalWatchlist.setText(String.valueOf(filmMetadata.getWatchlists()));
                textAverageRating.setText(String.format("%s/5.0", String.valueOf(filmMetadata.getAverage_rating())));
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void gotoFilmReview() {
        Intent intent = new Intent(context, FilmReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void gotoFavoritedBy() {
        Intent intent = new Intent(context, FavoritedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void gotoWatchlistedBy() {
        Intent intent = new Intent(context, WatchlistedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void showFilmModal() {
        FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_STATUS_MODAL);
    }
}
