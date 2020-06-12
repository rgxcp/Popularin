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
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.statics.Popularin;

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
        // ID
        final String reviewID = String.valueOf(filmReviewList.get(position).getReview_id());
        final Integer userID = filmReviewList.get(position).getUser_id();

        // Auth
        final boolean isSelf = userID.equals(new Auth(context).getAuthID());

        // Parsing
        Integer reviewStar = new ParseStar().getStar(filmReviewList.get(position).getRating());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textUsername.setText(filmReviewList.get(position).getUsername());
        holder.textReviewTimestamp.setText(filmReviewList.get(position).getTimestamp());
        holder.textReviewDetail.setText(filmReviewList.get(position).getReview_detail());
        holder.imageReviewStar.setImageResource(reviewStar);
        Glide.with(context).load(filmReviewList.get(position).getProfile_picture()).apply(requestOptions).into(holder.imageUserProfile);

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
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra(Popularin.REVIEW_ID, reviewID);
                intent.putExtra(Popularin.IS_SELF, isSelf);
                context.startActivity(intent);
            }
        });

        holder.imageUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(Popularin.USER_ID, userID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmReviewList.size();
    }

    static class FilmReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUserProfile;
        ImageView imageReviewStar;
        TextView textUsername;
        TextView textReviewTimestamp;
        TextView textReviewDetail;
        View border;

        FilmReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUserProfile = itemView.findViewById(R.id.image_rfr_profile);
            imageReviewStar = itemView.findViewById(R.id.image_rfr_star);
            textUsername = itemView.findViewById(R.id.text_rfr_username);
            textReviewTimestamp = itemView.findViewById(R.id.text_rfr_timestamp);
            textReviewDetail = itemView.findViewById(R.id.text_rfr_review);
            border = itemView.findViewById(R.id.border_rfr_layout);
        }
    }
}
