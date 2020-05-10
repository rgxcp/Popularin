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
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.services.ParseImage;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {
    private Context context;
    private List<Crew> crewList;

    public CrewAdapter(Context context, List<Crew> crewList) {
        this.context = context;
        this.crewList = crewList;
    }

    private Integer dpToPx(Integer dp) {
        float px = dp * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        // Cast ID
        final String crewID = String.valueOf(crewList.get(position).getId());

        // Parsing
        String profilePicture = new ParseImage().getImage(crewList.get(position).getProfile_path());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Mengisi data
        holder.crewName.setText(crewList.get(position).getName());
        holder.crewAs.setText(crewList.get(position).getJob());
        Glide.with(context).load(profilePicture).apply(requestOptions).into(holder.profilePicture);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = dpToPx(16);
            layoutParams.rightMargin = dpToPx(8);
        } else if (position == crewList.size() - 1) {
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
                gotoCreditDetail.putExtra("CREDIT_ID", crewID);
                context.startActivity(gotoCreditDetail);
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    static class CrewViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView crewName;
        TextView crewAs;

        CrewViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.image_rcr_profile);
            crewName = itemView.findViewById(R.id.text_rcr_name);
            crewAs = itemView.findViewById(R.id.text_rcr_as);
        }
    }
}
