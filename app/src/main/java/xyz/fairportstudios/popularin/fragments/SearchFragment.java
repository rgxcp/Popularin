package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.SearchUserRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.SearchFilmRequest;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.statics.Popularin;

public class SearchFragment extends Fragment implements UserAdapter.OnClickListener {
    // Variable untuk fitur search
    private Boolean isSearchFilmFirstTime;
    private Boolean isSearchUserFirstTime;

    // Variable member
    private Context mContext;
    private FilmAdapter filmAdapter;
    private List<Film> filmList;
    private List<User> mUserList;
    private ProgressBar progressBar;
    private RecyclerView recyclerSearch;
    private SearchFilmRequest searchFilmRequest;
    private SearchUserRequest searchUserRequest;
    private String searchQuery;
    private TextView textMessage;
    private UserAdapter userAdapter;
    private UserAdapter.OnClickListener mOnClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Context
        mContext = getActivity();

        // Binding
        progressBar = view.findViewById(R.id.pbr_fs_layout);
        recyclerSearch = view.findViewById(R.id.recycler_fs_layout);
        textMessage = view.findViewById(R.id.text_fs_message);
        SearchView searchView = view.findViewById(R.id.search_fs_layout);
        final Chip chipSearchInFilm = view.findViewById(R.id.chip_fs_in_film);
        final Chip chipSearchInUser = view.findViewById(R.id.chip_fs_in_user);
        final LinearLayout searchInLayout = view.findViewById(R.id.layout_fs_search_in);

        // Assign variable untuk fitur search setiap kali fragment di buat
        mOnClickListener = this;
        isSearchFilmFirstTime = true;
        isSearchUserFirstTime = true;

        // Activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Tidak digunakan
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    searchQuery = newText;
                    textMessage.setVisibility(View.GONE);
                    recyclerSearch.setVisibility(View.GONE);
                    searchInLayout.setVisibility(View.VISIBLE);
                    chipSearchInFilm.setText(String.format("Cari \"%s\" dalam film", searchQuery));
                    chipSearchInUser.setText(String.format("Cari \"%s\" dalam pengguna", searchQuery));
                } else {
                    searchInLayout.setVisibility(View.GONE);
                    textMessage.setVisibility(View.GONE);
                    recyclerSearch.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        chipSearchInFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchFilmFirstTime) {
                    filmList = new ArrayList<>();
                    searchFilmRequest = new SearchFilmRequest(mContext);
                }
                searchInLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                searchFilm(mContext, searchQuery);
            }
        });

        chipSearchInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchUserFirstTime) {
                    mUserList = new ArrayList<>();
                    searchUserRequest = new SearchUserRequest(mContext);
                }
                searchInLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                searchUser(searchQuery);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        User currentItem = mUserList.get(position);
        int id = currentItem.getId();
        gotoUserDetail(id);
    }

    private void searchFilm(final Context context, String query) {
        searchFilmRequest.sendRequest(query, new SearchFilmRequest.Callback() {
            @Override
            public void onSuccess(List<Film> films) {
                if (isSearchFilmFirstTime) {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmAdapter = new FilmAdapter(context, filmList);
                    recyclerSearch.setLayoutManager(new LinearLayoutManager(context));
                    isSearchFilmFirstTime = false;
                } else {
                    Log.i("TAG", "EXECUTED");
                    filmList.clear();
                    filmAdapter.notifyDataSetChanged();
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmAdapter.notifyItemRangeInserted(insertIndex, films.size());
                }
                recyclerSearch.setAdapter(filmAdapter);
                recyclerSearch.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_search_result);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(message);
            }
        });
    }

    private void searchUser(String query) {
        searchUserRequest.sendRequest(query, new SearchUserRequest.Callback() {
            @Override
            public void onSuccess(List<User> users) {
                if (isSearchUserFirstTime) {
                    int insertIndex = mUserList.size();
                    mUserList.addAll(insertIndex, users);
                    userAdapter = new UserAdapter(mContext, mUserList, mOnClickListener);
                    recyclerSearch.setLayoutManager(new LinearLayoutManager(mContext));
                    isSearchUserFirstTime = false;
                } else {
                    mUserList.clear();
                    userAdapter.notifyDataSetChanged();
                    int insertIndex = mUserList.size();
                    mUserList.addAll(insertIndex, users);
                    userAdapter.notifyItemRangeInserted(insertIndex, users.size());
                }
                recyclerSearch.setAdapter(userAdapter);
                recyclerSearch.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_search_result);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(message);
            }
        });
    }

    private void gotoUserDetail(int id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }
}
