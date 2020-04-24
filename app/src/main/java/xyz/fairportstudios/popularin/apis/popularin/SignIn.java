package xyz.fairportstudios.popularin.apis.popularin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.activities.MainActivity;

public class SignIn {
    private Context context;
    private CoordinatorLayout layout;
    private String username, password;

    public SignIn(Context context, CoordinatorLayout layout, String username, String password) {
        this.context = context;
        this.layout = layout;
        this.username = username;
        this.password = password;
    }

    public void sendRequest(String requestURL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if (status == 515) {
                        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                        int AUTH_UID = jsonObjectResult.getInt("id");
                        String AUTH_TOKEN = jsonObjectResult.getString("token");

                        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("UID", AUTH_UID);
                        editor.putString("TOKEN", AUTH_TOKEN);
                        editor.apply();

                        Intent gotoMain = new Intent(context, MainActivity.class);
                        context.startActivity(gotoMain);
                    } else if (status == 616) {
                        String errorMessage = jsonObject.getString("message");
                        Snackbar.make(layout, errorMessage, Snackbar.LENGTH_SHORT).show();
                    } else if (status == 626) {
                        JSONArray jsonArrayResult = jsonObject.getJSONArray("result");
                        String errorMessage = jsonArrayResult.get(0).toString();
                        Snackbar.make(layout, errorMessage, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(layout, "Ada kesalahan dalam database", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content_Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
