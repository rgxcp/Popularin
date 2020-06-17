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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdateReviewRequest;
import xyz.fairportstudios.popularin.fragments.DatePickerFragment;
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class EditReviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText mInputReview;
    private Float mRating;
    private ImageView mImageFilmPoster;
    private LinearLayout mEditReviewLayout;
    private ProgressBar mProgressBar;
    private RatingBar mRatingBar;
    private RelativeLayout mAnchorLayout;
    private String mWatchDate;
    private String mReview;
    private TextView mTextFilmTitle;
    private TextView mTextFilmYear;
    private TextView mTextWatchDate;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        // Context
        final Context context = EditReviewActivity.this;

        // Binding
        mInputReview = findViewById(R.id.input_aer_review);
        mImageFilmPoster = findViewById(R.id.image_aer_poster);
        mEditReviewLayout = findViewById(R.id.layout_aer_edit_review);
        mProgressBar = findViewById(R.id.pbr_aer_layout);
        mRatingBar = findViewById(R.id.rbr_aer_layout);
        mAnchorLayout = findViewById(R.id.anchor_aer_layout);
        mTextFilmTitle = findViewById(R.id.text_aer_title);
        mTextFilmYear = findViewById(R.id.text_aer_year);
        mTextWatchDate = findViewById(R.id.text_aer_watch_date);
        mTextMessage = findViewById(R.id.text_aer_message);
        Toolbar toolbar = findViewById(R.id.toolbar_aer_layout);

        // Extra
        Intent intent = getIntent();
        final Integer reviewID = intent.getIntExtra(Popularin.REVIEW_ID, 0);

        // Menampilkan informasi ulasan awal
        getCurrentReview(context, reviewID);

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
                if (reviewValidated() && ratingValidated()) {
                    editReview(context, reviewID);
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

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float newRate, boolean b) {
                mRating = newRate;
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

    private void getCurrentReview(final Context context, Integer reviewID) {
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.Callback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // Parsing
                mRating = reviewDetail.getRating().floatValue();
                mReview = reviewDetail.getReview_detail();
                String filmYear = new ParseDate().getYear(reviewDetail.getRelease_date());
                String filmPoster = TMDbAPI.IMAGE + reviewDetail.getPoster();

                // Request gambar
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.colorSurface)
                        .error(R.color.colorSurface);

                // Menampilkan tanggal tonton awal
                getCurrentWatchDate(reviewDetail.getWatch_date());

                // Isi
                mTextFilmTitle.setText(reviewDetail.getTitle());
                mTextFilmYear.setText(filmYear);
                mRatingBar.setRating(mRating);
                mInputReview.setText(mReview);
                Glide.with(context).load(filmPoster).apply(requestOptions).into(mImageFilmPoster);
                mProgressBar.setVisibility(View.GONE);
                mEditReviewLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(message);
            }
        });
    }

    private void getCurrentWatchDate(String watchDate) {
        ParseDate parseDate = new ParseDate();
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(parseDate.getYear(watchDate));
        int month = Integer.parseInt(parseDate.getMonth(watchDate)) - 1;
        int day = Integer.parseInt(parseDate.getDay(watchDate));
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

    private boolean reviewValidated() {
        mReview = mInputReview.getText().toString();
        if (mReview.isEmpty()) {
            Snackbar.make(mAnchorLayout, R.string.validate_review, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean ratingValidated() {
        if (mRating == 0) {
            Snackbar.make(mAnchorLayout, R.string.validate_rating, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void editReview(final Context context, Integer reviewID) {
        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(context, reviewID, mRating, mReview, mWatchDate);
        updateReviewRequest.sendRequest(new UpdateReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                onBackPressed();
                Toast.makeText(context, R.string.review_updated, Toast.LENGTH_SHORT).show();
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
