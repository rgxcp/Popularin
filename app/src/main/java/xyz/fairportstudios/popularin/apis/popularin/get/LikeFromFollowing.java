package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.preferences.Auth;

public class LikeFromFollowing {
    private String id;
    private Context context;
    private List<User> userList;
    private RecyclerView recyclerView;

    public LikeFromFollowing(String id, Context context, List<User> userList, RecyclerView recyclerView) {
        this.id = id;
        this.context = context;
        this.userList = userList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess(Integer status);
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.REVIEW + "/" + id + "/likes/from/following";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    callback.onSuccess(status);

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObjectUser = jsonArrayData.getJSONObject(index).getJSONObject("user");

                            Integer id = jsonObjectUser.getInt("id");
                            String fullName = jsonObjectUser.getString("full_name");
                            String username = jsonObjectUser.getString("username");
                            String profilePicture = jsonObjectUser.getString("profile_picture");

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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", new Auth(context).getAuthID());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
