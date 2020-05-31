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

    private Integer pxToDp(Integer px) {
        float dp = px * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        // ID
        // final String crewID = String.valueOf(crewList.get(position).getId());

        // Parsing
        String crewProfile = new ParseImage().getImage(crewList.get(position).getProfile_path());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textCrewName.setText(crewList.get(position).getName());
        holder.textCrewAs.setText(crewList.get(position).getJob());
        Glide.with(context).load(crewProfile).apply(requestOptions).into(holder.imageCrewProfile);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = pxToDp(16);
            layoutParams.rightMargin = pxToDp(8);
        } else if (position == crewList.size() - 1) {
            layoutParams.rightMargin = pxToDp(16);
        } else {
            layoutParams.rightMargin = pxToDp(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Belum ada
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    static class CrewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageCrewProfile;
        private TextView textCrewName;
        private TextView textCrewAs;

        CrewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCrewProfile = itemView.findViewById(R.id.image_rcr_profile);
            textCrewName = itemView.findViewById(R.id.text_rcr_name);
            textCrewAs = itemView.findViewById(R.id.text_rcr_as);
        }
    }
}
