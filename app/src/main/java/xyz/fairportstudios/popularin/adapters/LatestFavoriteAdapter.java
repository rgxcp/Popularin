package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.services.ParseImage;

public class LatestFavoriteAdapter extends RecyclerView.Adapter<LatestFavoriteAdapter.LatestFavoriteViewHolder> {
    private Context context;
    private List<LatestFavorite> latestFavoriteList;

    public LatestFavoriteAdapter(Context context, List<LatestFavorite> latestFavoriteList) {
        this.context = context;
        this.latestFavoriteList = latestFavoriteList;
    }

    private Integer dpToPx(Integer dp) {
        float px = dp * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public LatestFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LatestFavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_latest_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LatestFavoriteViewHolder holder, int position) {
        // Film ID
        final String filmID = String.valueOf(latestFavoriteList.get(position).getTmdb_id());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        String poster = new ParseImage().getImage(latestFavoriteList.get(position).getPoster());

        // Mengisi data
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0 ) {
            layoutParams.leftMargin = dpToPx(16);
            layoutParams.rightMargin = dpToPx(8);
        } else if (position == latestFavoriteList.size() - 1) {
            layoutParams.rightMargin = dpToPx(16);
        } else {
            layoutParams.rightMargin = dpToPx(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", filmID);
                context.startActivity(gotoFilmDetail);
            }
        });

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return latestFavoriteList.size();
    }

    static class LatestFavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;

        LatestFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.image_rlf_poster);
        }
    }
}
