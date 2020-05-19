package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.fragments.FilmStatusModal;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.Popularin;

public class RecentFavoriteAdapter extends RecyclerView.Adapter<RecentFavoriteAdapter.RecentFavoriteViewHolder> {
    private Context context;
    private List<RecentFavorite> recentFavoriteList;

    public RecentFavoriteAdapter(Context context, List<RecentFavorite> recentFavoriteList) {
        this.context = context;
        this.recentFavoriteList = recentFavoriteList;
    }

    private Integer dpToPx(Integer dp) {
        float px = dp * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public RecentFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentFavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_recent_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentFavoriteViewHolder holder, int position) {
        // Film ID
        final String filmID = String.valueOf(recentFavoriteList.get(position).getTmdb_id());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimary);

        // Mendapatkan data
        final String title = recentFavoriteList.get(position).getTitle();
        final String year = new ParseDate().getYear(recentFavoriteList.get(position).getRelease_date());
        final String poster = new ParseImage().getImage(recentFavoriteList.get(position).getPoster());
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = dpToPx(16);
            layoutParams.rightMargin = dpToPx(8);
        } else if (position == recentFavoriteList.size() - 1) {
            layoutParams.rightMargin = dpToPx(16);
        } else {
            layoutParams.rightMargin = dpToPx(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.filmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmStatusModal filmStatusModal = new FilmStatusModal(filmID, title, year, poster);
                filmStatusModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentFavoriteList.size();
    }

    static class RecentFavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;

        RecentFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.image_rrf_poster);
        }
    }
}
