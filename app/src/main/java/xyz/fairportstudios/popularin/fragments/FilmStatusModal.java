package xyz.fairportstudios.popularin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import xyz.fairportstudios.popularin.R;

public class FilmStatusModal extends BottomSheetDialogFragment {
    private String id;
    private String title;
    private String year;

    public FilmStatusModal(String id, String title, String year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_film_status, container, false);

        // Binding
        LinearLayout reviewLayout = view.findViewById(R.id.review_mfs_layout);
        LinearLayout favoriteLayout = view.findViewById(R.id.favorite_mfs_layout);
        LinearLayout watchlistLayout = view.findViewById(R.id.watchlist_mfs_layout);
        RatingBar ratingBar = view.findViewById(R.id.rbr_mfs_layout);
        TextView filmTitle = view.findViewById(R.id.text_mfs_title);
        TextView filmYear = view.findViewById(R.id.text_mfs_year);

        // Isi
        filmTitle.setText(title);
        filmYear.setText(year);

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
