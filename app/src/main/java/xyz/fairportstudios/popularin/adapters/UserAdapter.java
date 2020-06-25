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
import xyz.fairportstudios.popularin.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mContext;
    private List<User> mUserList;
    private OnClickListener mOnClickListener;

    public UserAdapter(Context context, List<User> userList, OnClickListener onClickListener) {
        mContext = context;
        mUserList = userList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onUserItemClick(int position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_user, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Posisi
        User currentItem = mUserList.get(position);

        // Isi
        holder.mTextFullName.setText(currentItem.getFull_name());
        holder.mTextUsername.setText(String.format("@%s", currentItem.getUsername()));
        Glide.with(mContext).load(currentItem.getProfile_picture()).into(holder.mImageProfile);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageProfile;
        private TextView mTextFullName;
        private TextView mTextUsername;
        private OnClickListener mOnClickListener;

        UserViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageProfile = itemView.findViewById(R.id.image_ru_profile);
            mTextFullName = itemView.findViewById(R.id.text_ru_full_name);
            mTextUsername = itemView.findViewById(R.id.text_ru_username);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onUserItemClick(getAdapterPosition());
            }
        }
    }
}
