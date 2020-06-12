package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class UserFollowingRequest {
    private Context context;
    private Integer id;

    public UserFollowingRequest(Context context, Integer id) {
        this.context = context;
        this.id = id;
    }

    public interface Callback {
        void onSuccess(Integer pages, List<User> users);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(Integer page, final Callback callback) {
        String requestURL = PopularinAPI.USER + id + "/followings?page=" + page;

        JsonObjectRequest userFollowing = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        List<User> userList = new ArrayList<>();
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");
                        Integer totalPage = resultObject.getInt("to");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject followingObject = indexObject.getJSONObject("following");

                            User user = new User();
                            user.setId(followingObject.getInt("id"));
                            user.setFull_name(followingObject.getString("full_name"));
                            user.setUsername(followingObject.getString("username"));
                            user.setProfile_picture(followingObject.getString("profile_picture"));
                            userList.add(user);
                        }

                        callback.onSuccess(totalPage, userList);
                    } else if (status == 606) {
                        callback.onNotFound();
                    } else {
                        callback.onError(context.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(context.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(context.getString(R.string.server_error));
                } else {
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", PopularinAPI.API_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userFollowing);
    }
}
