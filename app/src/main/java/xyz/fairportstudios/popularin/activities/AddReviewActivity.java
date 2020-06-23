package xyz.fairportstudios.popularin.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.AddReviewRequest;
import xyz.fairportstudios.popularin.fragments.DatePickerFragment;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class AddReviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    // Variable member
    private float mRating;
    private EditText mInputReview;
    private LinearLayout mAnchorLayout;
    private String mWatchDate;
    private String mReview;
    private TextView mTextWatchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        // Context
        final Context context = AddReviewActivity.this;

        // Binding
        mInputReview = findViewById(R.id.input_adr_review);
        mAnchorLayout = findViewById(R.id.anchor_adr_layout);
        mTextWatchDate = findViewById(R.id.text_adr_watch_date);
        ImageView imageFilmPoster = findViewById(R.id.image_adr_poster);
        RatingBar ratingBar = findViewById(R.id.rbr_adr_layout);
        TextView textFilmTitle = findViewById(R.id.text_adr_title);
        TextView textFilmYear = findViewById(R.id.text_adr_year);
        Toolbar toolbar = findViewById(R.id.toolbar_adr_layout);

        // Extra
        Intent intent = getIntent();
        mRating = intent.getFloatExtra(Popularin.RATING, 0);
        final int filmID = intent.getIntExtra(Popularin.FILM_ID, 0);
        String filmTitle = intent.getStringExtra(Popularin.FILM_TITLE);
        String filmYear = intent.getStringExtra(Popularin.FILM_YEAR);
        String filmPoster = TMDbAPI.BASE_SMALL_IMAGE_URL + intent.getStringExtra(Popularin.FILM_POSTER);

        // Menampilkan tanggal sekarang
        getCurrentDate();

        // Menampilkan info film dan rating
        textFilmTitle.setText(filmTitle);
        textFilmYear.setText(filmYear);
        ratingBar.setRating(mRating);
        Glide.with(context).load(filmPoster).into(imageFilmPoster);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ratingValidated() && reviewValidated()) {
                    addReview(context, filmID);
                    return true;
                } else {
                    return false;
                }
            }
        });

        mTextWatchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                mRating = rating;
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        mWatchDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mTextWatchDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mWatchDate = year + "-" + (month + 1) + "-" + day;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        mTextWatchDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
    }

    private void showDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), Popularin.DATE_PICKER);
    }

    private boolean ratingValidated() {
        if (mRating == 0) {
            Snackbar.make(mAnchorLayout, R.string.validate_rating, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean reviewValidated() {
        mReview = mInputReview.getText().toString();
        if (mReview.isEmpty()) {
            Snackbar.make(mAnchorLayout, R.string.validate_review, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void addReview(final Context context, int filmID) {
        AddReviewRequest addReviewRequest = new AddReviewRequest(context, filmID, mRating, mReview, mWatchDate);
        addReviewRequest.sendRequest(new AddReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                onBackPressed();
                Toast.makeText(context, R.string.review_added, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
