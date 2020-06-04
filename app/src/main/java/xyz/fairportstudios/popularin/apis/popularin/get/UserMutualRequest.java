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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserMutualRequest {
    private Context context;
    private String id;
    private List<User> userList;
    private RecyclerView recyclerView;

    public UserMutualRequest(
            Context context,
            String id,
            List<User> userList,
            RecyclerView recyclerView
    ) {
        this.context = context;
        this.id = id;
        this.userList = userList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(Integer page) {
        return PopularinAPI.USER + "/" + id + "/mutuals?page=" + page;
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        JsonObjectRequest userMutual = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);

                            User user = new User();
                            user.setId(indexObject.getInt("id"));
                            user.setFull_name(indexObject.getString("full_name"));
                            user.setUsername(indexObject.getString("username"));
                            user.setProfile_picture(indexObject.getString("profile_picture"));
                            userList.add(user);
                        }

                        UserAdapter userAdapter = new UserAdapter(context, userList);
                        recyclerView.setAdapter(userAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else if (status == 606) {
                        callback.onEmpty();
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth-ID", new Auth(context).getAuthID());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userMutual);
    }
}
