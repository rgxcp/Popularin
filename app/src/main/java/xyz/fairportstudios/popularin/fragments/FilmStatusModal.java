package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
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
import xyz.fairportstudios.popularin.apis.popularin.get.FilmSelfRequest;
import xyz.fairportstudios.popularin.models.FilmSelf;
import xyz.fairportstudios.popularin.preferences.Auth;

public class FilmStatusModal extends BottomSheetDialogFragment {
    private Boolean inReview;
    private Boolean inFavorite;
    private Boolean inWatchlist;
    private Boolean isAuth;
    private Context context;
    private ImageView imageReview;
    private ImageView imageFavorite;
    private ImageView imageWatchlist;
    private String filmID;
    private String title;
    private String year;

    public FilmStatusModal(String filmID, String title, String year) {
        this.filmID = filmID;
        this.title = title;
        this.year = year;
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
        LinearLayout reviewLayout = view.findViewById(R.id.review_mfs_layout);
        LinearLayout favoriteLayout = view.findViewById(R.id.favorite_mfs_layout);
        LinearLayout watchlistLayout = view.findViewById(R.id.watchlist_mfs_layout);
        RatingBar ratingBar = view.findViewById(R.id.rbr_mfs_layout);
        TextView filmTitle = view.findViewById(R.id.text_mfs_title);
        TextView filmYear = view.findViewById(R.id.text_mfs_year);

        // Auth
        isAuth = new Auth(context).isAuth();

        // Isi
        filmTitle.setText(title);
        filmYear.setText(year);

        // GET
        if (isAuth) {
            FilmSelfRequest filmSelfRequest = new FilmSelfRequest(context, filmID);
            filmSelfRequest.sendRequest(new FilmSelfRequest.APICallback() {
                @Override
                public void onSuccess(FilmSelf filmSelf) {
                    inReview = filmSelf.getIn_review();
                    inFavorite = filmSelf.getIn_favorite();
                    inWatchlist = filmSelf.getIn_watchlist();

                    if (inReview) {
                        imageReview.setBackgroundResource(R.drawable.ic_check);
                    }

                    if (inFavorite) {
                        imageFavorite.setBackgroundResource(R.drawable.ic_check);
                    }

                    if (inWatchlist) {
                        imageWatchlist.setBackgroundResource(R.drawable.ic_check);
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(context, R.string.get_error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Activity
        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        watchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        return view;
    }
}
