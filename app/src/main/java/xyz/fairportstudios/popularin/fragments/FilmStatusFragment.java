package xyz.fairportstudios.popularin.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import xyz.fairportstudios.popularin.R;

public class FilmStatusFragment extends BottomSheetDialogFragment {
    private String filmTitle, filmYear;

    public FilmStatusFragment(String filmTitle, String filmYear) {
        this.filmTitle = filmTitle;
        this.filmYear = filmYear;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_status, container, false);

        TextView txtTitle = view.findViewById(R.id.txt_ffs_title);
        TextView txtYear = view.findViewById(R.id.txt_ffs_year);
        ImageView imgAddReview = view.findViewById(R.id.img_ffs_add_review);
        ImageView imgAddToFavorite = view.findViewById(R.id.img_ffs_add_favorite);
        ImageView imgAddToWatchlist = view.findViewById(R.id.img_ffs_add_watchlist);
        RatingBar rtbRating = view.findViewById(R.id.bar_ffs);

        txtTitle.setText(filmTitle);
        txtYear.setText(filmYear);

        imgAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "REVIEW CLICKED");
            }
        });

        imgAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "FAVORITE CLICKED");
            }
        });

        imgAddToWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "WATCHLIST CLICKED");
            }
        });

        rtbRating.getOnRatingBarChangeListener();

        return view;
    }
}
