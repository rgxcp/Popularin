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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.services.ParseImage;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private Context context;
    private List<Cast> castList;

    public CastAdapter(Context context, List<Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    private Integer dpToPx(Integer dp) {
        float px = dp * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        // Cast ID
        final String castID = String.valueOf(castList.get(position).getId());

        // Parsing
        String profilePicture = new ParseImage().getImage(castList.get(position).getProfile_path());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Mengisi data
        holder.castName.setText(castList.get(position).getName());
        holder.castAs.setText(castList.get(position).getCharacter());
        Glide.with(context).load(profilePicture).apply(requestOptions).into(holder.profilePicture);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = dpToPx(16);
            layoutParams.rightMargin = dpToPx(8);
        } else if (position == castList.size() - 1) {
            layoutParams.rightMargin = dpToPx(16);
        } else {
            layoutParams.rightMargin = dpToPx(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent gotoCreditDetail = new Intent(context, CreditDetailActivity.class);
                gotoCreditDetail.putExtra("CREDIT_ID", castID);
                context.startActivity(gotoCreditDetail);
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView castName;
        TextView castAs;

        CastViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.image_rcr_profile);
            castName = itemView.findViewById(R.id.text_rcr_name);
            castAs = itemView.findViewById(R.id.text_rcr_as);
        }
    }
}
