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
    private double lastRate;
    private float currentRate;
    private Boolean inReview;
    private Boolean inFavorite;
    private Boolean inWatchlist;
    private Context context;
    private ImageView imageReview;
    private ImageView imageFavorite;
    private ImageView imageWatchlist;
    private RatingBar ratingBar;

    // Constructor variable
    private Integer id;
    private String title;
    private String year;
    private String poster;

    public FilmModal(Integer id, String title, String year, String poster) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.poster = poster;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_film, container, false);

        // Binding
        context = getActivity();
        imageReview = view.findViewById(R.id.image_mf_review);
        imageFavorite = view.findViewById(R.id.image_mf_favorite);
        imageWatchlist = view.findViewById(R.id.image_mf_watchlist);
        ratingBar = view.findViewById(R.id.rbr_mf_layout);
        TextView textFilmTitle = view.findViewById(R.id.text_mf_title);
        TextView textFilmYear = view.findViewById(R.id.text_mf_year);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Isi
        textFilmTitle.setText(title);
        textFilmYear.setText(year);

        // Status film
        if (isAuth) {
            getFilmSelf();
        }

        // Activity
        imageReview.setOnClickListener(new View.OnClickListener() {
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

        imageFavorite.setOnClickListener(new View.OnClickListener() {
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

        imageWatchlist.setOnClickListener(new View.OnClickListener() {
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

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (isAuth) {
                    if (lastRate != v) {
                        currentRate = v;
                        addReview();
                        dismiss();
                    }
                } else {
                    gotoEmptyAccount();
                    dismiss();
                }
            }
        });

        return view;
    }

    private void getFilmSelf() {
        FilmSelfRequest filmSelfRequest = new FilmSelfRequest(context, String.valueOf(id));
        filmSelfRequest.sendRequest(new FilmSelfRequest.APICallback() {
            @Override
            public void onSuccess(FilmSelf filmSelf) {
                inReview = filmSelf.getIn_review();
                inFavorite = filmSelf.getIn_favorite();
                inWatchlist = filmSelf.getIn_watchlist();
                lastRate = filmSelf.getLast_rate();
                ratingBar.setRating((float) lastRate);

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

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        startActivity(intent);
    }

    private void addReview() {
        Intent intent = new Intent(context, AddReviewActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        intent.putExtra(Popularin.FILM_TITLE, title);
        intent.putExtra(Popularin.FILM_YEAR, year);
        intent.putExtra(Popularin.FILM_POSTER, poster);
        intent.putExtra(Popularin.RATING, currentRate);
        startActivity(intent);
    }

    private void addToFavorite() {
        AddFavoriteRequest addFavoriteRequest = new AddFavoriteRequest(context, String.valueOf(id));
        addFavoriteRequest.sendRequest(new AddFavoriteRequest.APICallback() {
            @Override
            public void onSuccess() {
                String message = title + " " + context.getString(R.string.added_to_favorite);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                String message = title + " " + context.getString(R.string.failed_add_to_favorite);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromFavorite() {
        DeleteFavoriteRequest deleteFavoriteRequest = new DeleteFavoriteRequest(context, String.valueOf(id));
        deleteFavoriteRequest.sendRequest(new DeleteFavoriteRequest.APICallback() {
            @Override
            public void onSuccess() {
                String message = title + " " + context.getString(R.string.removed_from_favorite);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                String message = title + " " + context.getString(R.string.failed_remove_from_favorite);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToWatchlist() {
        AddWatchlistRequest addWatchlistRequest = new AddWatchlistRequest(context, String.valueOf(id));
        addWatchlistRequest.sendRequest(new AddWatchlistRequest.APICallback() {
            @Override
            public void onSuccess() {
                String message = title + " " + context.getString(R.string.added_to_watchlist);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                String message = title + " " + context.getString(R.string.failed_add_to_watchlist);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromWatchlist() {
        DeleteWatchlistRequest deleteWatchlistRequest = new DeleteWatchlistRequest(context, String.valueOf(id));
        deleteWatchlistRequest.sendRequest(new DeleteWatchlistRequest.APICallback() {
            @Override
            public void onSuccess() {
                String message = title + " " + context.getString(R.string.removed_from_watchlist);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                String message = title + " " + context.getString(R.string.failed_remove_from_watchlist);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
