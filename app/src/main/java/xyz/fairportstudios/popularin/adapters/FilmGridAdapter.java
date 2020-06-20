package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class FilmGridAdapter extends RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder> {
    private Context mContext;
    private List<Film> mFilmList;
    private OnClickListener mOnClickListener;

    public FilmGridAdapter(Context context, List<Film> filmList, OnClickListener onClickListener) {
        mContext = context;
        mFilmList = filmList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    @NonNull
    @Override
    public FilmGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmGridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_film_grid, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmGridViewHolder holder, int position) {
        // Posisi
        Film currentItem = mFilmList.get(position);

        // Parsing
        String poster = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getPoster_path();

        // Isi
        Glide.with(mContext).load(poster).into(holder.mImagePoster);
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    static class FilmGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mImagePoster;
        private OnClickListener mOnClickListener;

        FilmGridViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImagePoster = itemView.findViewById(R.id.image_rfg_poster);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == itemView) {
                mOnClickListener.onItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
