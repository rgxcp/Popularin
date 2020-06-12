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
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Posisi
        User currentItem = userList.get(position);

        // Extra
        final Integer userID = currentItem.getId();

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textFullName.setText(currentItem.getFull_name());
        holder.textUsername.setText(String.format("@%s", currentItem.getUsername()));
        Glide.with(context).load(currentItem.getProfile_picture()).apply(requestOptions).into(holder.imageProfile);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProfile;
        private TextView textFullName;
        private TextView textUsername;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_ru_profile);
            textFullName = itemView.findViewById(R.id.text_ru_full_name);
            textUsername = itemView.findViewById(R.id.text_ru_username);
        }
    }
}
