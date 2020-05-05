package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.LikeFromAll;
import xyz.fairportstudios.popularin.models.User;

public class FromAllFragment extends Fragment {
    private Context context;
    private ProgressBar progressBar;
    private TextView emptyUser;
    private String reviewID;

    public FromAllFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Binding
        context = getActivity();
        progressBar = view.findViewById(R.id.progress_bar_ful_layout);
        emptyUser = view.findViewById(R.id.text_ful_empty);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_ful_layout);

        // Set-up list
        List<User> userList = new ArrayList<>();

        // Mendapatkan data
        LikeFromAll likeFromAll = new LikeFromAll(reviewID, context, userList, recyclerView);
        likeFromAll.sendRequest(new LikeFromAll.JSONCallback() {
            @Override
            public void onSuccess(Integer status) {
                if (status == 101) {
                    progressBar.setVisibility(View.GONE);
                } else if (status == 606) {
                    progressBar.setVisibility(View.GONE);
                    emptyUser.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
