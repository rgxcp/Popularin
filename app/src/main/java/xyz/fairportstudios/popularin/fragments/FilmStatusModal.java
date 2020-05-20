package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.AddReviewActivity;
import xyz.fairportstudios.popularin.activities.EmptyAccountActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteFavoriteRequest;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteWatchlistRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmSelfRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.AddFavoriteRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.AddWatchlistRequest;
import xyz.fairportstudios.popularin.models.FilmSelf;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.Popularin;

public class FilmStatusModal extends BottomSheetDialogFragment {
    private Boolean isAuth;
    private Boolean inReview;
    private Boolean inFavorite;
    private Boolean inWatchlist;
    private Context context;
    private ImageView imageReview;
    private ImageView imageFavorite;
    private ImageView imageWatchlist;
    private RatingBar ratingBar;

    // Constructor
    private String id;
    private String title;
    private String year;
    private String poster;

    public FilmStatusModal(String id, String title, String year, String poster) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.poster = poster;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_film_status, container, false);

        // Binding
        context = getActivity();
        imageReview = view.findViewById(R.id.image_mfs_review);
        imageFavorite = view.findViewById(R.id.image_mfs_favorite);
        imageWatchlist = view.findViewById(R.id.image_mfs_watchlist);
        ratingBar = view.findViewById(R.id.rbr_mfs_layout);
        LinearLayout reviewLayout = view.findViewById(R.id.review_mfs_layout);
        LinearLayout favoriteLayout = view.findViewById(R.id.favorite_mfs_layout);
        LinearLayout watchlistLayout = view.findViewById(R.id.watchlist_mfs_layout);
        TextView textTitle = view.findViewById(R.id.text_mfs_title);
        TextView textYear = view.findViewById(R.id.text_mfs_year);

        // Auth
        isAuth = new Auth(context).isAuth();

        // Isi
        textTitle.setText(title);
        textYear.setText(year);

        // GET
        if (isAuth) {
            getFilmSelf();
        }

        // Activity
        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    addReview();
                } else {
                    gotoEmptyAccount();
                }
                dismiss();
            }
        });

        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!inFavorite) {
                        addToFavorite();
                    } else {
                        removeFromFavorite();
                    }
                } else {
                    gotoEmptyAccount();
                }
                dismiss();
            }
        });

        watchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!inWatchlist) {
                        addToWatchlist();
                    } else {
                        removeFromWatchlist();
                    }
                } else {
                    gotoEmptyAccount();
                }
                dismiss();
            }
        });

        return view;
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        startActivity(intent);
    }

    private void getFilmSelf() {
        FilmSelfRequest filmSelfRequest = new FilmSelfRequest(context, id);
        filmSelfRequest.sendRequest(new FilmSelfRequest.APICallback() {
            @Override
            public void onSuccess(FilmSelf filmSelf) {
                double lastRate = filmSelf.getLast_rate();
                ratingBar.setRating((float) lastRate);
                inReview = filmSelf.getIn_review();
                inFavorite = filmSelf.getIn_favorite();
                inWatchlist = filmSelf.getIn_watchlist();

                if (inReview) {
                    imageReview.setBackgroundResource(R.drawable.ic_review_fill);
                }

                if (inFavorite) {
                    imageFavorite.setBackgroundResource(R.drawable.ic_favorite_fill);
                }

                if (inWatchlist) {
                    imageWatchlist.setBackgroundResource(R.drawable.ic_watchlist_fill);
                }
            }
        });
    }

    private void addReview() {
        Intent intent = new Intent(context, AddReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        intent.putExtra(Popularin.FILM_TITLE, title);
        intent.putExtra(Popularin.FILM_YEAR, year);
        intent.putExtra(Popularin.FILM_POSTER, poster);
        startActivity(intent);
    }

    private void addToFavorite() {
        AddFavoriteRequest addFavoriteRequest = new AddFavoriteRequest(context, id);
        addFavoriteRequest.sendRequest(new AddFavoriteRequest.APICallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, R.string.failed_add_to_favorite, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromFavorite() {
        DeleteFavoriteRequest deleteFavoriteRequest = new DeleteFavoriteRequest(context, id);
        deleteFavoriteRequest.sendRequest(new DeleteFavoriteRequest.APICallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.removed_from_favorite, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, R.string.failed_remove_from_favorite, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToWatchlist() {
        AddWatchlistRequest addWatchlistRequest = new AddWatchlistRequest(context, id);
        addWatchlistRequest.sendRequest(new AddWatchlistRequest.APICallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.added_to_watchlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, R.string.failed_add_to_watchlist, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromWatchlist() {
        DeleteWatchlistRequest deleteWatchlistRequest = new DeleteWatchlistRequest(context, id);
        deleteWatchlistRequest.sendRequest(new DeleteWatchlistRequest.APICallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.removed_from_watchlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, R.string.failed_remove_from_watchlist, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
