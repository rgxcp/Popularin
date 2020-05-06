package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.SearchUser;
import xyz.fairportstudios.popularin.models.User;

public class SearchUserFragment extends Fragment {
    private Context context;
    private ProgressBar progressBar;
    private List<User> userList;
    private RecyclerView recyclerView;
    private TextView emptyResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        // Binding
        context = getActivity();
        progressBar = view.findViewById(R.id.progress_bar_fsu_layout);
        recyclerView = view.findViewById(R.id.recycler_view_fsu_layout);
        emptyResult = view.findViewById(R.id.text_fsu_empty);
        SearchView searchView = view.findViewById(R.id.search_fsu_layout);

        // Set-up list
        userList = new ArrayList<>();

        // Activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                emptyResult.setVisibility(View.GONE);

                SearchUser searchUser = new SearchUser(context, userList, recyclerView);
                String requestURL = searchUser.getRequestURL(query);
                searchUser.sendRequest(requestURL, new SearchUser.JSONCallback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEmpytResult() {
                        progressBar.setVisibility(View.GONE);
                        emptyResult.setVisibility(View.VISIBLE);
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
