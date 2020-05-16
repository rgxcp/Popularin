package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EditReviewActivity;
import xyz.fairportstudios.popularin.activities.ReviewDetailActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteReviewRequest;
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseStar;

public class FilmReviewAdapter extends RecyclerView.Adapter<FilmReviewAdapter.FilmReviewViewHolder> {
    private Context context;
    private List<FilmReview> filmReviewList;

    public FilmReviewAdapter(Context context, List<FilmReview> filmReviewList) {
        this.context = context;
        this.filmReviewList = filmReviewList;
    }

    private Integer dpToPx() {
        float px = 16 * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public FilmReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_film_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmReviewViewHolder holder, int position) {
        // ReviewID
        final String reviewID = String.valueOf(filmReviewList.get(position).getId());

        // User ID
        final String userID = String.valueOf(filmReviewList.get(position).getUser_id());

        // Auth ID
        final String authID = new Auth(context).getAuthID();
        if (userID.equals(authID)) {
            holder.authLayout.setVisibility(View.VISIBLE);
        } else {
            holder.authLayout.setVisibility(View.GONE);
        }

        // Parsing
        String date = new ParseDate().getDate(filmReviewList.get(position).getReview_date());
        Integer star = new ParseStar().getStar(filmReviewList.get(position).getRating());

        // Request
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Isi
        holder.userFirstName.setText(filmReviewList.get(position).getFirst_name());
        holder.reviewDate.setText(date);
        holder.reviewDetail.setText(filmReviewList.get(position).getReview_text());
        holder.reviewStar.setImageResource(star);
        Glide.with(context).load(filmReviewList.get(position).getProfile_picture()).apply(requestOptions).into(holder.userProfile);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == filmReviewList.size() - 1) {
            layoutParams.bottomMargin = dpToPx();
            holder.border.setVisibility(View.GONE);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewDetail = new Intent(context, ReviewDetailActivity.class);
                gotoReviewDetail.putExtra("REVIEW_ID", reviewID);
                context.startActivity(gotoReviewDetail);
            }
        });

        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUserDetail = new Intent(context, UserDetailActivity.class);
                gotoUserDetail.putExtra("USER_ID", userID);
                context.startActivity(gotoUserDetail);
            }
        });

        holder.editReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoEditReview = new Intent(context, EditReviewActivity.class);
                gotoEditReview.putExtra("REVIEW_ID", reviewID);
                context.startActivity(gotoEditReview);
            }
        });

        holder.deleteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteReviewRequest deleteReviewRequest = new DeleteReviewRequest(context, reviewID);
                deleteReviewRequest.sendRequest(new DeleteReviewRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, R.string.review_removed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, R.string.failed_remove_review, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmReviewList.size();
    }

    static class FilmReviewViewHolder extends RecyclerView.ViewHolder {
        Button editReview;
        Button deleteReview;
        ImageView userProfile;
        ImageView reviewStar;
        RelativeLayout authLayout;
        TextView userFirstName;
        TextView reviewDate;
        TextView reviewDetail;
        View border;

        FilmReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            editReview = itemView.findViewById(R.id.button_rfr_edit);
            deleteReview = itemView.findViewById(R.id.button_rfr_delete);
            userProfile = itemView.findViewById(R.id.image_rfr_profile);
            reviewStar = itemView.findViewById(R.id.image_rfr_star);
            authLayout = itemView.findViewById(R.id.auth_rfr_layout);
            userFirstName = itemView.findViewById(R.id.text_rfr_first_name);
            reviewDate = itemView.findViewById(R.id.text_rfr_date);
            reviewDetail = itemView.findViewById(R.id.text_rfr_review);
            border = itemView.findViewById(R.id.border_rfr_layout);
        }
    }
}
