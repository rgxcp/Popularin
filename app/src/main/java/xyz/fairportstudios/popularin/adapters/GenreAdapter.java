package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private Context mContext;
    private List<Genre> mGenreList;
    private OnClickListener mOnClickListener;

    public GenreAdapter(Context context, List<Genre> genreList, OnClickListener onClickListener) {
        mContext = context;
        mGenreList = genreList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onGenreItemClick(int position);
    }

    private int getDensity(int px) {
        float dp = px * mContext.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_genre, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        // Posisi
        Genre currentItem = mGenreList.get(position);

        // Isi
        holder.mTextTitle.setText(currentItem.getTitle());
        holder.mImageBackground.setImageResource(currentItem.getBackground());

        // Margin
        int left = getDensity(4);
        int top = getDensity(4);
        int right = getDensity(4);
        int bottom = getDensity(4);

        boolean isEdgeLeft = (position % 2) == 0;
        boolean isEdgeTop = position < 2;
        boolean isEdgeRight = (position % 2) == 1;
        boolean isEdgeBottom = position >= (getItemCount() - 2);

        if (isEdgeLeft) {
            left = getDensity(16);
        }
        if (isEdgeTop) {
            top = getDensity(16);
        }
        if (isEdgeRight) {
            right = getDensity(16);
        }
        if (isEdgeBottom) {
            bottom = getDensity(16);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mGenreList.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageBackground;
        private TextView mTextTitle;
        private OnClickListener mOnClickListener;

        GenreViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageBackground = itemView.findViewById(R.id.image_rg_background);
            mTextTitle = itemView.findViewById(R.id.text_rg_title);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onGenreItemClick(getAdapterPosition());
            }
        }
    }
}
