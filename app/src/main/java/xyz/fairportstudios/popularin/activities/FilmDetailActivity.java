package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.CastAdapter;
import xyz.fairportstudios.popularin.adapters.CrewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmMetadataRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.FilmDetailRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.models.FilmMetadata;
import xyz.fairportstudios.popularin.services.ConvertGenre;
import xyz.fairportstudios.popularin.services.ConvertRuntime;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class FilmDetailActivity extends AppCompatActivity implements CastAdapter.OnClickListener, CrewAdapter.OnClickListener {
    // Variable member
    private int mGenreID;
    private Context mContext;
    private CastAdapter.OnClickListener mOnCastClickListener;
    private Chip mChipGenre;
    private Chip mChipRuntime;
    private Chip mChipRating;
    private CoordinatorLayout mAnchorLayout;
    private CrewAdapter.OnClickListener mOnCrewClickListener;
    private ImageView mImagePoster;
    private ImageView mImageEmptyOverview;
    private ImageView mImageEmptyCast;
    private ImageView mImageEmptyCrew;
    private List<Cast> mCastList;
    private List<Crew> mCrewList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerCast;
    private RecyclerView mRecyclerCrew;
    private String mGenreTitle;
    private String mFilmTitle;
    private String mFilmYear;
    private String mFilmPoster;
    private String mYoutubeKey;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextTotalReview;
    private TextView mTextTotalFavorite;
    private TextView mTextTotalWatchlist;
    private TextView mTextOverview;
    private TextView mTextMessage;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Context
        mContext = FilmDetailActivity.this;

        // Binding
        mChipGenre = findViewById(R.id.chip_afd_genre);
        mChipRuntime = findViewById(R.id.chip_afd_runtime);
        mChipRating = findViewById(R.id.chip_afd_rating);
        mAnchorLayout = findViewById(R.id.anchor_afd_layout);
        mImagePoster = findViewById(R.id.image_afd_poster);
        mImageEmptyOverview = findViewById(R.id.image_afd_empty_overview);
        mImageEmptyCast = findViewById(R.id.image_afd_empty_cast);
        mImageEmptyCrew = findViewById(R.id.image_afd_empty_crew);
        mProgressBar = findViewById(R.id.pbr_afd_layout);
        mRecyclerCast = findViewById(R.id.recycler_afd_cast);
        mRecyclerCrew = findViewById(R.id.recycler_afd_crew);
        mSwipeRefresh = findViewById(R.id.swipe_refresh_afd_layout);
        mTextTotalReview = findViewById(R.id.text_afd_total_review);
        mTextTotalFavorite = findViewById(R.id.text_afd_total_favorite);
        mTextTotalWatchlist = findViewById(R.id.text_afd_total_watchlist);
        mTextOverview = findViewById(R.id.text_afd_overview);
        mTextMessage = findViewById(R.id.text_afd_message);
        mToolbar = findViewById(R.id.toolbar_afd_layout);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar_afd_layout);
        FloatingActionButton fab = findViewById(R.id.fab_afd_layout);
        ImageView imagePlayTrailer = findViewById(R.id.image_afd_play);
        ImageView imageReview = findViewById(R.id.image_afd_review);
        ImageView imageFavorite = findViewById(R.id.image_afd_favorite);
        ImageView imageWatchlist = findViewById(R.id.image_afd_watchlist);

        // Extra
        Intent intent = getIntent();
        final int filmID = intent.getIntExtra(Popularin.FILM_ID, 0);

        // Mengatur jenis font untuk collapsing toolbar
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.monument_extended_regular);
        collapsingToolbar.setExpandedTitleTypeface(typeface);

        // Mendapatkan detail film
        mOnCastClickListener = this;
        mOnCrewClickListener = this;
        getFilmDetail(filmID);

        // Mendapatkan metadata film
        getFilmMetadata(filmID);

        // Activity
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imagePlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mYoutubeKey.isEmpty()) {
                    playTrailer(mYoutubeKey);
                } else {
                    searchTrailer(mFilmTitle.toLowerCase() + " trailer");
                }
            }
        });

        mChipGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGenreID != 0) {
                    gotoDiscoverFilm(mGenreID, mGenreTitle);
                }
            }
        });

        imageReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFilmReview(filmID);
            }
        });

        imageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFavoritedBy(filmID);
            }
        });

        imageWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWatchlistedBy(filmID);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilmModal(filmID);
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                getFilmMetadata(filmID);
            }
        });
    }

    @Override
    public void onCastItemClick(int position) {
        Cast currentItem = mCastList.get(position);
        int id = currentItem.getId();
        gotoCredit(id, 1);
    }

    @Override
    public void onCrewItemClick(int position) {
        Crew currentItem = mCrewList.get(position);
        int id = currentItem.getId();
        gotoCredit(id, 2);
    }

    private void getFilmDetail(int id) {
        FilmDetailRequest filmDetailRequest = new FilmDetailRequest(mContext, id);
        filmDetailRequest.sendRequest(new FilmDetailRequest.Callback() {
            @Override
            public void onSuccess(FilmDetail filmDetail, List<Cast> castList, List<Crew> crewList) {
                // Parsing
                mGenreID = filmDetail.getGenre_id();
                mGenreTitle = new ConvertGenre().getGenreForHumans(mGenreID);
                mFilmTitle = filmDetail.getOriginal_title();
                mFilmYear = new ParseDate().getYear(filmDetail.getRelease_date());
                mFilmPoster = filmDetail.getPoster_path();
                mYoutubeKey = filmDetail.getVideo_key();
                String runtime = new ConvertRuntime().getRuntimeForHumans(filmDetail.getRuntime());
                String overview = filmDetail.getOverview();

                // Detail
                mToolbar.setTitle(mFilmTitle);
                mChipGenre.setText(mGenreTitle);
                mChipRuntime.setText(runtime);
                if (!overview.isEmpty()) {
                    mImageEmptyOverview.setVisibility(View.GONE);
                    mTextOverview.setVisibility(View.VISIBLE);
                    mTextOverview.setText(overview);
                }
                Glide.with(mContext).load(TMDbAPI.BASE_LARGE_IMAGE_URL + mFilmPoster).into(mImagePoster);
                mProgressBar.setVisibility(View.GONE);
                mAnchorLayout.setVisibility(View.VISIBLE);

                // Cast
                if (!castList.isEmpty()) {
                    mCastList = new ArrayList<>();
                    mCastList.addAll(castList);
                    CastAdapter castAdapter = new CastAdapter(mContext, mCastList, mOnCastClickListener);
                    mRecyclerCast.setAdapter(castAdapter);
                    mRecyclerCast.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                    mRecyclerCast.setHasFixedSize(true);
                    mRecyclerCast.setVisibility(View.VISIBLE);
                    mImageEmptyCast.setVisibility(View.GONE);
                }

                // Crew
                if (!crewList.isEmpty()) {
                    mCrewList = new ArrayList<>();
                    mCrewList.addAll(crewList);
                    CrewAdapter crewAdapter = new CrewAdapter(mContext, mCrewList, mOnCrewClickListener);
                    mRecyclerCrew.setAdapter(crewAdapter);
                    mRecyclerCrew.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                    mRecyclerCrew.setHasFixedSize(true);
                    mRecyclerCrew.setVisibility(View.VISIBLE);
                    mImageEmptyCrew.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(message);
            }
        });
    }

    private void getFilmMetadata(int id) {
        FilmMetadataRequest filmMetadataRequest = new FilmMetadataRequest(mContext, id);
        filmMetadataRequest.sendRequest(new FilmMetadataRequest.Callback() {
            @Override
            public void onSuccess(FilmMetadata filmMetadata) {
                mChipRating.setText(String.format("%s/5", filmMetadata.getAverage_rating()));
                mTextTotalReview.setText(String.format(Locale.getDefault(), "%d Ulasan", filmMetadata.getTotal_review()));
                mTextTotalFavorite.setText(String.format(Locale.getDefault(), "%d Favorit", filmMetadata.getTotal_favorite()));
                mTextTotalWatchlist.setText(String.format(Locale.getDefault(), "%d Watchlist", filmMetadata.getTotal_watchlist()));
            }

            @Override
            public void onNotFound() {
                // Tidak digunakan
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, R.string.failed_retrieve_metadata, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mSwipeRefresh.setRefreshing(false);
    }

    private void gotoCredit(int id, int viewPagerIndex) {
        Intent intent = new Intent(mContext, CreditDetailActivity.class);
        intent.putExtra(Popularin.CREDIT_ID, id);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, viewPagerIndex);
        startActivity(intent);
    }

    private void playTrailer(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Popularin.YOUTUBE_VIDEO_URL + key));
        startActivity(intent);
    }

    private void searchTrailer(String query) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Popularin.YOUTUBE_SEARCH_URL + query));
        startActivity(intent);
    }

    private void gotoDiscoverFilm(int id, String genreTitle) {
        Intent intent = new Intent(mContext, DiscoverFilmActivity.class);
        intent.putExtra(Popularin.GENRE_ID, id);
        intent.putExtra(Popularin.GENRE_TITLE, genreTitle);
        startActivity(intent);
    }

    private void gotoFilmReview(int id) {
        Intent intent = new Intent(mContext, FilmReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void gotoFavoritedBy(int id) {
        Intent intent = new Intent(mContext, FavoritedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void gotoWatchlistedBy(int id) {
        Intent intent = new Intent(mContext, WatchlistedByActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void showFilmModal(int id) {
        FilmModal filmModal = new FilmModal(id, mFilmTitle, mFilmYear, mFilmPoster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_MODAL);
    }
}
