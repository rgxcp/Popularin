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
    private boolean mInReview;
    private boolean mInFavorite;
    private boolean mInWatchlist;
    private float mLastRate;
    private ImageView mImageReview;
    private ImageView mImageFavorite;
    private ImageView mImageWatchlist;
    private RatingBar mRatingBar;

    // Variable constructor
    private int mFilmID;
    private String mFilmTitle;
    private String mFilmYear;
    private String mFilmPoster;

    public FilmModal(
            int filmID,
            String filmTitle,
            String filmYear,
            String filmPoster
    ) {
        mFilmID = filmID;
        mFilmTitle = filmTitle;
        mFilmYear = filmYear;
        mFilmPoster = filmPoster;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_film, container, false);

        // Context
        final Context context = getActivity();

        // Binding
        mImageReview = view.findViewById(R.id.image_mf_review);
        mImageFavorite = view.findViewById(R.id.image_mf_favorite);
        mImageWatchlist = view.findViewById(R.id.image_mf_watchlist);
        mRatingBar = view.findViewById(R.id.rbr_mf_layout);
        TextView textFilmTitle = view.findViewById(R.id.text_mf_title);
        TextView textFilmYear = view.findViewById(R.id.text_mf_year);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Isi
        textFilmTitle.setText(mFilmTitle);
        textFilmYear.setText(mFilmYear);

        // Mendapatkan status film
        if (isAuth) {
            getFilmSelf(context);
        }

        // Activity
        mImageReview.setOnClickListener(new View.OnClickListener() {
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

        mImageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!mInFavorite) {
                        addToFavorite(context);
                    } else {
                        removeFromFavorite(context);
                    }
                } else {
                    gotoEmptyAccount(context);
                }
                dismiss();
            }
        });

        mImageWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!mInWatchlist) {
                        addToWatchlist(context);
                    } else {
                        removeFromWatchlist(context);
                    }
                } else {
                    gotoEmptyAccount(context);
                }
                dismiss();
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float newRate, boolean b) {
                if (isAuth) {
                    if (mLastRate != newRate) {
                        mLastRate = newRate;
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

    private void getFilmSelf(final Context context) {
        FilmSelfRequest filmSelfRequest = new FilmSelfRequest(context, mFilmID);
        filmSelfRequest.sendRequest(new FilmSelfRequest.Callback() {
            @Override
            public void onSuccess(FilmSelf filmSelf) {
                mInReview = filmSelf.getIn_review();
                mInFavorite = filmSelf.getIn_favorite();
                mInWatchlist = filmSelf.getIn_watchlist();
                mLastRate = (float) filmSelf.getLast_rate();
                mRatingBar.setRating(mLastRate);

                if (mInReview) {
                    mImageReview.setImageResource(R.drawable.ic_fill_eye);
                }
                if (mInFavorite) {
                    mImageFavorite.setImageResource(R.drawable.ic_fill_heart);
                }
                if (mInWatchlist) {
                    mImageWatchlist.setImageResource(R.drawable.ic_fill_watchlist);
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
        intent.putExtra(Popularin.FILM_ID, mFilmID);
        intent.putExtra(Popularin.FILM_TITLE, mFilmTitle);
        intent.putExtra(Popularin.FILM_YEAR, mFilmYear);
        intent.putExtra(Popularin.FILM_POSTER, mFilmPoster);
        intent.putExtra(Popularin.RATING, mLastRate);
        startActivity(intent);
    }

    private void addToFavorite(final Context context) {
        AddFavoriteRequest addFavoriteRequest = new AddFavoriteRequest(context, mFilmID);
        addFavoriteRequest.sendRequest(new AddFavoriteRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.film_added_to_favorite, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromFavorite(final Context context) {
        DeleteFavoriteRequest deleteFavoriteRequest = new DeleteFavoriteRequest(context, mFilmID);
        deleteFavoriteRequest.sendRequest(new DeleteFavoriteRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.film_removed_from_favorite, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToWatchlist(final Context context) {
        AddWatchlistRequest addWatchlistRequest = new AddWatchlistRequest(context, mFilmID);
        addWatchlistRequest.sendRequest(new AddWatchlistRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.film_added_to_watchlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromWatchlist(final Context context) {
        DeleteWatchlistRequest deleteWatchlistRequest = new DeleteWatchlistRequest(context, mFilmID);
        deleteWatchlistRequest.sendRequest(new DeleteWatchlistRequest.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.film_removed_from_watchlist, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
