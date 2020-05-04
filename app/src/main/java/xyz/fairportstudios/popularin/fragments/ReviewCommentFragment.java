package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.RetrieveComments;
import xyz.fairportstudios.popularin.apis.popularin.post.AddComment;
import xyz.fairportstudios.popularin.models.Comment;

public class ReviewCommentFragment extends Fragment {
    private Context context;
    private ProgressBar progressBar;
    private TextInputEditText inputComment;
    private TextView emptyComment;
    private String reviewID;

    public ReviewCommentFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_comment, container, false);

        // Binding
        context = getActivity();
        progressBar = view.findViewById(R.id.progress_bar_frc_layout);
        inputComment = view.findViewById(R.id.text_frc_comment);
        emptyComment = view.findViewById(R.id.text_frc_empty);
        Button buttonComment = view.findViewById(R.id.button_frc_comment);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_frc_layout);

        // Set-up list
        List<Comment> commentList = new ArrayList<>();

        // Mendapatkan data
        RetrieveComments retrieveComments = new RetrieveComments(reviewID, context, commentList, recyclerView);
        retrieveComments.sendRequest(new RetrieveComments.JSONCallback() {
            @Override
            public void onSuccess(Integer status) {
                if (status == 101) {
                    progressBar.setVisibility(View.GONE);
                } else if (status == 606) {
                    progressBar.setVisibility(View.GONE);
                    emptyComment.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Activity
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = Objects.requireNonNull(inputComment.getText()).toString();
                AddComment addComment = new AddComment(reviewID, comment, context);
                addComment.sendRequest(new AddComment.JSONCallback() {
                    @Override
                    public void onSuccess(Integer status) {
                        if (status == 202) {
                            Toast.makeText(context, "Komen terkirim.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
