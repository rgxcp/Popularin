package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ConvertGenre;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private Context mContext;
    private List<Film> mFilmList;
    private OnClickListener mOnClickListener;

    public FilmAdapter(Context context, List<Film> filmList, OnClickListener onClickListener) {
        mContext = context;
        mFilmList = filmList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onFilmItemClick(int position);

        void onFilmPosterClick(int position);

        void onFilmPosterLongClick(int position);
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_film, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        // Posisi
        Film currentItem = mFilmList.get(position);

        // Parsing
        String genre = new ConvertGenre().getGenreForHumans(currentItem.getGenre_id());
        String releaseDate = new ParseDate().getDateForHumans(currentItem.getRelease_date());
        String poster = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getPoster_path();

        // Isi
        holder.mTextTitle.setText(currentItem.getOriginal_title());
        holder.mTextGenre.setText(genre);
        holder.mTextReleaseDate.setText(releaseDate);
        Glide.with(mContext).load(poster).into(holder.mImagePoster);
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mImagePoster;
        private TextView mTextTitle;
        private TextView mTextGenre;
        private TextView mTextReleaseDate;
        private OnClickListener mOnClickListener;

        FilmViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImagePoster = itemView.findViewById(R.id.image_rf_poster);
            mTextTitle = itemView.findViewById(R.id.text_rf_title);
            mTextGenre = itemView.findViewById(R.id.text_rf_genre);
            mTextReleaseDate = itemView.findViewById(R.id.text_rf_release_date);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            mImagePoster.setOnClickListener(this);
            mImagePoster.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onFilmItemClick(getAdapterPosition());
            } else if (v == mImagePoster) {
                mOnClickListener.onFilmPosterClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == mImagePoster) {
                mOnClickListener.onFilmPosterLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
