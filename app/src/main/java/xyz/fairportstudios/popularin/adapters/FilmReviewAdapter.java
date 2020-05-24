package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
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

    private Integer pxToDp() {
        float dp = 16 * context.getResources().getDisplayMetrics().density;
        return (int) dp;
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

        // Auth
        final String authID = new Auth(context).getAuthID();
        final boolean isSelf = userID.equals(authID);

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
            layoutParams.bottomMargin = pxToDp();
            holder.border.setVisibility(View.GONE);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewDetail = new Intent(context, ReviewActivity.class);
                gotoReviewDetail.putExtra("REVIEW_ID", reviewID);
                gotoReviewDetail.putExtra("IS_SELF", isSelf);
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
    }

    @Override
    public int getItemCount() {
        return filmReviewList.size();
    }

    static class FilmReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfile;
        ImageView reviewStar;
        TextView userFirstName;
        TextView reviewDate;
        TextView reviewDetail;
        View border;

        FilmReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.image_rfr_profile);
            reviewStar = itemView.findViewById(R.id.image_rfr_star);
            userFirstName = itemView.findViewById(R.id.text_rfr_first_name);
            reviewDate = itemView.findViewById(R.id.text_rfr_date);
            reviewDetail = itemView.findViewById(R.id.text_rfr_review);
            border = itemView.findViewById(R.id.border_rfr_layout);
        }
    }
}
