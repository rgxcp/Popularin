package xyz.fairportstudios.popularin.modals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmModal extends BottomSheetDialogFragment {
    // Variable member
    private double lastRate;
    private float currentRate;
    private Boolean inReview;
    private Boolean inFavorite;
    private Boolean inWatchlist;
    private ImageView imageReview;
    private ImageView imageFavorite;
    private ImageView imageWatchlist;
    private RatingBar ratingBar;

    // Variable constructor
    private Integer filmID;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;

    public FilmModal(Integer filmID, String filmTitle, String filmYear, String filmPoster) {
        this.filmID = filmID;
        this.filmTitle = filmTitle;
        this.filmYear = filmYear;
        this.filmPoster = filmPoster;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_film, container, false);

        // Context
        final Context context = getActivity();

        // Binding
        imageReview = view.findViewById(R.id.image_mf_review);
        imageFavorite = view.findViewById(R.id.image_mf_favorite);
        imageWatchlist = view.findViewById(R.id.image_mf_watchlist);
        ratingBar = view.findViewById(R.id.rbr_mf_layout);
        TextView textFilmTitle = view.findViewById(R.id.text_mf_title);
        TextView textFilmYear = view.findViewById(R.id.text_mf_year);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Isi
        textFilmTitle.setText(filmTitle);
        textFilmYear.setText(filmYear);

        // Mendapatkan status film diri sendiri
        if (isAuth) {
            getFilmSelf(context, filmID);
        }

        // Activity
        imageReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    addReview(context);
                } else {
                    gotoEmptyAccount(context);
                }
                dismiss();
            }
        });

        imageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!inFavorite) {
                        addToFavorite(context, filmID);
                    } else {
                        removeFromFavorite(context, filmID);
                    }
                } else {
                    gotoEmptyAccount(context);
                }
                dismiss();
            }
        });

        imageWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!inWatchlist) {
                        addToWatchlist(context, filmID);
                    } else {
                        removeFromWatchlist(context, filmID);
                    }
                } else {
                    gotoEmptyAccount(context);
                }
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float newRate, boolean b) {
                if (isAuth) {
                    if (lastRate != newRate) {
                        currentRate = newRate;
                        addReview(context);
                        dismiss();
                    }
                } else {
                    gotoEmptyAccount(context);
                    dismiss();
                }
            }
        });

        return view;
    }

    private void getFilmSelf(final Context context, Integer filmID) {
        FilmSelfRequest filmSelfRequest = new FilmSelfRequest(context, filmID);
        filmSelfRequest.sendRequest(new FilmSelfRequest.Callback() {
            @Override
            public void onSuccess(FilmSelf filmSelf) {
                inReview = filmSelf.getIn_review();
                inFavorite = filmSelf.getIn_favorite();
                inWatchlist = filmSelf.getIn_watchlist();
                lastRate = filmSelf.getLast_rate();
                ratingBar.setRating((float) lastRate);

                if (inReview) {
                    imageReview.setImageResource(R.drawable.ic_fill_eye);
                }
                if (inFavorite) {
                    imageFavorite.setImageResource(R.drawable.ic_fill_heart);
                }
                if (inWatchlist) {
                    imageWatchlist.setImageResource(R.drawable.ic_fill_watchlist);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoEmptyAccount(Context context) {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        startActivity(intent);
    }

    private void addReview(Context context) {
        Intent intent = new Intent(context, AddReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        intent.putExtra(Popularin.FILM_TITLE, filmTitle);
        intent.putExtra(Popularin.FILM_YEAR, filmYear);
        intent.putExtra(Popularin.FILM_POSTER, filmPoster);
        intent.putExtra(Popularin.RATING, currentRate);
        startActivity(intent);
    }

    private void addToFavorite(final Context context, Integer filmID) {
        AddFavoriteRequest addFavoriteRequest = new AddFavoriteRequest(context, filmID);
        addFavoriteRequest.sendRequest(new AddFavoriteRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromFavorite(final Context context, Integer filmID) {
        DeleteFavoriteRequest deleteFavoriteRequest = new DeleteFavoriteRequest(context, filmID);
        deleteFavoriteRequest.sendRequest(new DeleteFavoriteRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.removed_from_favorite, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToWatchlist(final Context context, Integer filmID) {
        AddWatchlistRequest addWatchlistRequest = new AddWatchlistRequest(context, filmID);
        addWatchlistRequest.sendRequest(new AddWatchlistRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.added_to_watchlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromWatchlist(final Context context, Integer filmID) {
        DeleteWatchlistRequest deleteWatchlistRequest = new DeleteWatchlistRequest(context, filmID);
        deleteWatchlistRequest.sendRequest(new DeleteWatchlistRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.removed_from_watchlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
