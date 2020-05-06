package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class SearchUser {
    private Context context;
    private List<User> userList;
    private RecyclerView recyclerView;

    public SearchUser(Context context, List<User> userList, RecyclerView recyclerView) {
        this.context = context;
        this.userList = userList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess();
        void onEmpytResult();
    }

    public String getRequestURL(String query) {
        return PopularinAPI.SEARCH_USER + query;
    }

    public void sendRequest(String requestURL, final JSONCallback callback) {
        // Membersihkan sebelum mencari
        userList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total = response.getJSONObject("result").getInt("total");

                    if (total > 0) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);

                            Integer id = jsonObject.getInt("id");
                            String fullName = jsonObject.getString("full_name");
                            String username = jsonObject.getString("username");
                            String profilePicture = jsonObject.getString("profile_picture");

                            User user = new User(id, fullName, username, profilePicture);
                            user.setId(id);
                            user.setFull_name(fullName);
                            user.setUsername(username);
                            user.setProfile_picture(profilePicture);

                            userList.add(user);
                        }

                        UserAdapter userAdapter = new UserAdapter(context, userList);
                        recyclerView.setAdapter(userAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmpytResult();
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
