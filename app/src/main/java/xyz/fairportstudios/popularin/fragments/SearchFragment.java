package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.SearchUserRequest;
import xyz.fairportstudios.popularin.apis.tmdb.get.SearchFilmRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class SearchFragment extends Fragment implements FilmAdapter.OnClickListener, UserAdapter.OnClickListener {
    // Variable untuk fitur search
    private boolean mIsSearchFilmFirstTime = true;
    private boolean mIsSearchUserFirstTime = true;
    private boolean mIsLoadFilmFirstTimeSuccess = false;
    private boolean mIsLoadUserFirstTimeSuccess = false;

    // Variable member
    private Context mContext;
    private FilmAdapter mFilmAdapter;
    private List<Film> mFilmList;
    private List<User> mUserList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerSearch;
    private SearchFilmRequest mSearchFilmRequest;
    private SearchUserRequest mSearchUserRequest;
    private String mSearchQuery;
    private TextView mTextMessage;
    private UserAdapter mUserAdapter;
    private FilmAdapter.OnClickListener mFilmOnClickListener = this;
    private UserAdapter.OnClickListener mUserOnClickListener = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mProgressBar = view.findViewById(R.id.pbr_fs_layout);
        mRecyclerSearch = view.findViewById(R.id.recycler_fs_layout);
        mTextMessage = view.findViewById(R.id.text_fs_message);
        SearchView searchView = view.findViewById(R.id.search_fs_layout);
        final Chip chipSearchInFilm = view.findViewById(R.id.chip_fs_in_film);
        final Chip chipSearchInUser = view.findViewById(R.id.chip_fs_in_user);
        final LinearLayout searchInLayout = view.findViewById(R.id.layout_fs_search_in);

        // Activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Tidak digunakan
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    mSearchQuery = newText;
                    mRecyclerSearch.setVisibility(View.GONE);
                    searchInLayout.setVisibility(View.VISIBLE);
                    chipSearchInFilm.setText(String.format("Cari \"%s\" dalam film", mSearchQuery));
                    chipSearchInUser.setText(String.format("Cari \"%s\" dalam pengguna", mSearchQuery));
                } else {
                    searchInLayout.setVisibility(View.GONE);
                    mRecyclerSearch.setVisibility(View.VISIBLE);
                }
                mTextMessage.setVisibility(View.GONE);
                return true;
            }
        });

        chipSearchInFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSearchFilmFirstTime) {
                    mSearchFilmRequest = new SearchFilmRequest(mContext);
                }
                searchInLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                searchFilm(mSearchQuery);
            }
        });

        chipSearchInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSearchUserFirstTime) {
                    mSearchUserRequest = new SearchUserRequest(mContext);
                }
                searchInLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                searchUser(mSearchQuery);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetState();
    }

    @Override
    public void onFilmItemClick(int position) {
        Film currentItem = mFilmList.get(position);
        int id = currentItem.getId();
        gotoFilmDetail(id);
    }

    @Override
    public void onFilmPosterClick(int position) {
        Film currentItem = mFilmList.get(position);
        int id = currentItem.getId();
        gotoFilmDetail(id);
    }

    @Override
    public void onFilmPosterLongClick(int position) {
        Film currentItem = mFilmList.get(position);
        int id = currentItem.getId();
        String title = currentItem.getOriginal_title();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster_path();
        showFilmModal(id, title, year, poster);
    }

    @Override
    public void onUserItemClick(int position) {
        User currentItem = mUserList.get(position);
        int id = currentItem.getId();
        gotoUserDetail(id);
    }

    private void searchFilm(String query) {
        mSearchFilmRequest.sendRequest(query, new SearchFilmRequest.Callback() {
            @Override
            public void onSuccess(List<Film> filmList) {
                if (mIsSearchFilmFirstTime || !mIsLoadFilmFirstTimeSuccess) {
                    mFilmList = new ArrayList<>();
                    int insertIndex = mFilmList.size();
                    mFilmList.addAll(insertIndex, filmList);
                    mFilmAdapter = new FilmAdapter(mContext, mFilmList, mFilmOnClickListener);
                    mRecyclerSearch.setLayoutManager(new LinearLayoutManager(mContext));
                    mIsLoadFilmFirstTimeSuccess = true;
                } else {
                    mFilmList.clear();
                    mFilmAdapter.notifyDataSetChanged();
                    int insertIndex = mFilmList.size();
                    mFilmList.addAll(insertIndex, filmList);
                    mFilmAdapter.notifyItemRangeInserted(insertIndex, filmList.size());
                }
                mRecyclerSearch.setAdapter(mFilmAdapter);
                mRecyclerSearch.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNotFound() {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_search_result);
            }

            @Override
            public void onError(String message) {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(message);
            }
        });

        mIsSearchFilmFirstTime = false;
    }

    private void searchUser(String query) {
        mSearchUserRequest.sendRequest(query, new SearchUserRequest.Callback() {
            @Override
            public void onSuccess(List<User> userList) {
                if (mIsSearchUserFirstTime || !mIsLoadUserFirstTimeSuccess) {
                    mUserList = new ArrayList<>();
                    int insertIndex = mUserList.size();
                    mUserList.addAll(insertIndex, userList);
                    mUserAdapter = new UserAdapter(mContext, mUserList, mUserOnClickListener);
                    mRecyclerSearch.setLayoutManager(new LinearLayoutManager(mContext));
                    mIsLoadUserFirstTimeSuccess = true;
                } else {
                    mUserList.clear();
                    mUserAdapter.notifyDataSetChanged();
                    int insertIndex = mUserList.size();
                    mUserList.addAll(insertIndex, userList);
                    mUserAdapter.notifyItemRangeInserted(insertIndex, userList.size());
                }
                mRecyclerSearch.setAdapter(mUserAdapter);
                mRecyclerSearch.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNotFound() {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_search_result);
            }

            @Override
            public void onError(String message) {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(message);
            }
        });

        mIsSearchUserFirstTime = false;
    }

    private void gotoFilmDetail(int id) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void gotoUserDetail(int id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void showFilmModal(int id, String title, String year, String poster) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(id, title, year, poster);
        filmModal.show(fragmentManager, Popularin.FILM_MODAL);
    }

    private void resetState() {
        mIsSearchFilmFirstTime = true;
        mIsSearchUserFirstTime = true;
        mIsLoadFilmFirstTimeSuccess = false;
        mIsLoadUserFirstTimeSuccess = false;
    }
}
