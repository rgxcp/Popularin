package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.User;

public class SearchUserRequest {
    private Context context;
    private List<User> userList;
    private RecyclerView recyclerView;
    private String query;

    public SearchUserRequest(Context context, List<User> userList, RecyclerView recyclerView, String query) {
        this.context = context;
        this.userList = userList;
        this.recyclerView = recyclerView;
        this.query = query;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        // Membersihkan array
        userList.clear();

        String requestURL = PopularinAPI.SEARCH_USER + query;

        JsonObjectRequest searchUserRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObjectResult = response.getJSONObject("result");
                    int total = jsonObjectResult.getInt("total");

                    if (total > 0) {
                        JSONArray jsonArrayData = jsonObjectResult.getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);

                            User user = new User();
                            user.setId(jsonObject.getInt("id"));
                            user.setFull_name(jsonObject.getString("full_name"));
                            user.setUsername(jsonObject.getString("username"));
                            user.setProfile_picture(jsonObject.getString("profile_picture"));

                            userList.add(user);
                        }

                        UserAdapter userAdapter = new UserAdapter(context, userList);
                        recyclerView.setAdapter(userAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmpty();
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onError();
            }
        });

        Volley.newRequestQueue(context).add(searchUserRequest);
    }
}
