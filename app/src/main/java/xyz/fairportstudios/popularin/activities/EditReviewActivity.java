package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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
import xyz.fairportstudios.popularin.services.ParseImage;

public class EditReviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private EditText inputReview;
    private ImageView filmPoster;
    private Integer year;
    private Integer month;
    private Integer day;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private RelativeLayout relativeLayout;
    private String rating;
    private TextView filmTitle;
    private TextView filmYear;
    private TextView watchDate;
    private TextView networkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        // Binding
        context = EditReviewActivity.this;
        inputReview = findViewById(R.id.input_adr_review);
        filmPoster = findViewById(R.id.image_adr_poster);
        linearLayout = findViewById(R.id.review_adr_layout);
        progressBar = findViewById(R.id.pbr_adr_layout);
        ratingBar = findViewById(R.id.rbr_adr_layout);
        relativeLayout = findViewById(R.id.layout_adr_anchor);
        filmTitle = findViewById(R.id.text_adr_title);
        filmYear = findViewById(R.id.text_adr_year);
        watchDate = findViewById(R.id.text_adr_watch_date);
        networkError = findViewById(R.id.text_adr_empty);
        Toolbar toolbar = findViewById(R.id.toolbar_adr_layout);

        // Extra
        Intent intent = getIntent();
        final String reviewID = intent.getStringExtra("REVIEW_ID");

        // GET
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.APICallback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // Date
                /*
                ParseDate parseDate = new ParseDate();
                Calendar calendar = Calendar.getInstance();
                year = Integer.valueOf(parseDate.getYear(reviewDetail.getWatch_date()));
                month = Integer.parseInt(parseDate.getMonth(reviewDetail.getWatch_date())) - 1;
                day = Integer.valueOf(parseDate.getDay(reviewDetail.getWatch_date()));
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String parsedWatchDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                // Parsing
                double rating = reviewDetail.getRating();
                String parsedFilmYear = parseDate.getYear(reviewDetail.getRelease_date());
                String poster = new ParseImage().getImage(reviewDetail.getPoster());

                // Request
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

                filmTitle.setText(reviewDetail.getTitle());
                filmYear.setText(parsedFilmYear);
                watchDate.setText(parsedWatchDate);
                ratingBar.setRating((float) rating);
                inputReview.setText(reviewDetail.getReview_text());
                Glide.with(context).load(poster).apply(requestOptions).into(filmPoster);
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                 */
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                networkError.setVisibility(View.VISIBLE);
                Snackbar.make(relativeLayout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

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
                String reviewText = inputReview.getText().toString();
                String date = year + "-" + (month + 1) + "-" + day;

                UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(context, reviewID, rating, reviewText, date);
                updateReviewRequest.sendRequest(new UpdateReviewRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(relativeLayout, R.string.review_updated, Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        }, 500);
                    }

                    @Override
                    public void onFailed(String message) {
                        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError() {
                        Snackbar.make(relativeLayout, R.string.failed_update_review, Snackbar.LENGTH_LONG).show();
                    }
                });

                return true;
            }
        });

        watchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(ratingBar.getRating());
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
        String pickedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        watchDate.setText(pickedDate);
    }
}
