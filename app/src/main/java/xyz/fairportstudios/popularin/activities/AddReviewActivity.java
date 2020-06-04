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
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.AddReviewRequest;
import xyz.fairportstudios.popularin.fragments.DatePickerFragment;
import xyz.fairportstudios.popularin.statics.Popularin;

public class AddReviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private EditText inputReview;
    private Integer year;
    private Integer month;
    private Integer day;
    private LinearLayout anchorLayout;
    private RatingBar ratingBar;
    private String filmID;
    private String watchDate;
    private String rating;
    private TextView textWatchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        // Binding
        context = AddReviewActivity.this;
        inputReview = findViewById(R.id.input_adr_review);
        anchorLayout = findViewById(R.id.anchor_adr_layout);
        ratingBar = findViewById(R.id.rbr_adr_layout);
        textWatchDate = findViewById(R.id.text_adr_watch_date);
        ImageView imageFilmPoster = findViewById(R.id.image_adr_poster);
        TextView textFilmTitle = findViewById(R.id.text_adr_title);
        TextView textFilmYear = findViewById(R.id.text_adr_year);
        Toolbar toolbar = findViewById(R.id.toolbar_adr_layout);

        // Extra
        Intent intent = getIntent();
        filmID = intent.getStringExtra(Popularin.FILM_ID);
        String filmTitle = intent.getStringExtra(Popularin.FILM_TITLE);
        String filmYear = intent.getStringExtra(Popularin.FILM_YEAR);
        String filmPoster = intent.getStringExtra(Popularin.FILM_POSTER);

        // Menampilkan tanggal sekarang
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        watchDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Menampilkan info film
        textFilmTitle.setText(filmTitle);
        textFilmYear.setText(filmYear);
        textWatchDate.setText(watchDate);
        Glide.with(context).load(filmPoster).apply(requestOptions).into(imageFilmPoster);

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
                    addReview();
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

    private void addReview() {
        String review = inputReview.getText().toString();
        String date = year + "-" + (month + 1) + "-" + day;

        AddReviewRequest addReviewRequest = new AddReviewRequest(context, filmID, rating, review, date);
        addReviewRequest.sendRequest(new AddReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                Snackbar.make(anchorLayout, R.string.review_added, Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);
            }

            @Override
            public void onFailed(String message) {
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayout, R.string.failed_add_review, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
