package xyz.fairportstudios.popularin.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdateReviewRequest;
import xyz.fairportstudios.popularin.fragments.DatePickerFragment;
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.Popularin;

public class EditReviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private EditText inputReview;
    private ImageView imageFilmPoster;
    private Integer year;
    private Integer month;
    private Integer day;
    private LinearLayout editReviewLayout;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private RelativeLayout anchorLayout;
    private String reviewID;
    private String watchDate;
    private String rating;
    private TextView textFilmTitle;
    private TextView textFilmYear;
    private TextView textWatchDate;
    private TextView textNetworkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        // Binding
        context = EditReviewActivity.this;
        inputReview = findViewById(R.id.input_aer_review);
        imageFilmPoster = findViewById(R.id.image_aer_poster);
        editReviewLayout = findViewById(R.id.edit_review_aer_layout);
        progressBar = findViewById(R.id.pbr_aer_layout);
        ratingBar = findViewById(R.id.rbr_aer_layout);
        anchorLayout = findViewById(R.id.anchor_aer_layout);
        textFilmTitle = findViewById(R.id.text_aer_title);
        textFilmYear = findViewById(R.id.text_aer_year);
        textWatchDate = findViewById(R.id.text_aer_watch_date);
        textNetworkError = findViewById(R.id.text_aer_network_error);
        Toolbar toolbar = findViewById(R.id.toolbar_aer_layout);

        // Extra
        Intent intent = getIntent();
        reviewID = intent.getStringExtra(Popularin.REVIEW_ID);

        // Menampilkan ulasan sekarang
        getCurrentReview();

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
                if (validateReview() && validateRating()) {
                    editReview();
                    return true;
                } else {
                    return false;
                }
            }
        });

        textWatchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(v);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        year = y;
        month = m;
        day = d;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        watchDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        textWatchDate.setText(watchDate);
    }

    private void getCurrentReview() {
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.APICallback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // Menampilkan tanggal tonton asli
                ParseDate parseDate = new ParseDate();
                Calendar calendar = Calendar.getInstance();
                year = Integer.valueOf(parseDate.getYear(reviewDetail.getWatch_date()));
                month = Integer.parseInt(parseDate.getMonth(reviewDetail.getWatch_date())) - 1;
                day = Integer.valueOf(parseDate.getDay(reviewDetail.getWatch_date()));
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                watchDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                // Parsing
                double reviewRating = reviewDetail.getRating();
                String filmYear = parseDate.getYear(reviewDetail.getRelease_date());
                String filmPoster = new ParseImage().getImage(reviewDetail.getPoster());

                // Isi
                textFilmTitle.setText(reviewDetail.getTitle());
                textFilmYear.setText(filmYear);
                textWatchDate.setText(watchDate);
                inputReview.setText(reviewDetail.getReview_detail());
                ratingBar.setRating((float) reviewRating);
                Glide.with(context).load(filmPoster).into(imageFilmPoster);
                progressBar.setVisibility(View.GONE);
                editReviewLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), Popularin.DATE_PICKER);
    }

    private boolean validateReview() {
        if (inputReview.getText().toString().isEmpty()) {
            Snackbar.make(anchorLayout, R.string.validate_review, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateRating() {
        if (ratingBar.getRating() == 0) {
            Snackbar.make(anchorLayout, R.string.validate_rating, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void editReview() {
        String review = inputReview.getText().toString();
        watchDate = year + "-" + (month + 1) + "-" + day;

        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(context, reviewID, rating, review, watchDate);
        updateReviewRequest.sendRequest(new UpdateReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                Snackbar.make(anchorLayout, R.string.review_updated, Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);
            }

            @Override
            public void onFailed(String message) {
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayout, R.string.failed_update_review, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
