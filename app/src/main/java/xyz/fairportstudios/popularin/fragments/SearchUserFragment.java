package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.SearchUserRequest;
import xyz.fairportstudios.popularin.models.User;

public class SearchUserFragment extends Fragment {
    private Context context;
    private List<User> userList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RelativeLayout layout;
    private TextView emptyResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        // Binding
        context = getActivity();
        progressBar = view.findViewById(R.id.pbr_fsu_layout);
        recyclerView = view.findViewById(R.id.recycler_fsu_layout);
        layout = view.findViewById(R.id.layout_fsu_anchor);
        emptyResult = view.findViewById(R.id.text_fsu_empty);
        SearchView searchView = view.findViewById(R.id.search_fsu_layout);

        // List
        userList = new ArrayList<>();

        // Activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                emptyResult.setVisibility(View.GONE);

                // GET
                SearchUserRequest searchUserRequest = new SearchUserRequest(context, userList, query, recyclerView);
                searchUserRequest.sendRequest(new SearchUserRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEmpty() {
                        progressBar.setVisibility(View.GONE);
                        emptyResult.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        emptyResult.setVisibility(View.VISIBLE);
                        Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return view;
    }
}
