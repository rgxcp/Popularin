package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.UserReview;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {
    private Context context;
    private List<UserReview> userReviewList;
    private Boolean isSelf;

    public UserReviewAdapter(Context context, List<UserReview> userReviewList, Boolean isSelf) {
        this.context = context;
        this.userReviewList = userReviewList;
        this.isSelf = isSelf;
    }

    private Integer pxToDp() {
        float dp = 16 * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_user_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewViewHolder holder, int position) {
        // ID
        final String reviewID = String.valueOf(userReviewList.get(position).getId());
        final String filmID = String.valueOf(userReviewList.get(position).getTmdb_id());

        // Parsing
        final String filmTitle = userReviewList.get(position).getTitle();
        final String filmYear = new ParseDate().getYear(userReviewList.get(position).getRelease_date());
        final String filmPoster = new ParseImage().getImage(userReviewList.get(position).getPoster());
        final Integer reviewStar = new ParseStar().getStar(userReviewList.get(position).getRating());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textFilmTitle.setText(filmTitle);
        holder.textFilmYear.setText(filmYear);
        holder.textReviewTimestamp.setText(userReviewList.get(position).getTimestamp());
        holder.textReviewDetail.setText(userReviewList.get(position).getReview_detail());
        holder.imageReviewStar.setImageResource(reviewStar);
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imageFilmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == userReviewList.size() - 1) {
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

        holder.imageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.imageFilmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmModal filmModal = new FilmModal(Integer.parseInt(filmID), filmTitle, filmYear, filmPoster);
                filmModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userReviewList.size();
    }

    static class UserReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageReviewStar;
        private ImageView imageFilmPoster;
        private TextView textFilmTitle;
        private TextView textFilmYear;
        private TextView textReviewTimestamp;
        private TextView textReviewDetail;
        private View border;

        UserReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageReviewStar = itemView.findViewById(R.id.image_rur_star);
            imageFilmPoster = itemView.findViewById(R.id.image_rur_poster);
            textFilmTitle = itemView.findViewById(R.id.text_rur_title);
            textFilmYear = itemView.findViewById(R.id.text_rur_year);
            textReviewTimestamp = itemView.findViewById(R.id.text_rur_timestamp);
            textReviewDetail = itemView.findViewById(R.id.text_rur_review);
            border = itemView.findViewById(R.id.layout_rur_border);
        }
    }
}
