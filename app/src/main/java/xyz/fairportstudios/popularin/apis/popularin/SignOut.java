package xyz.fairportstudios.popularin.apis.popularin;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignOut {
    private Context context;
    private int id;
    private String token;

    public SignOut(Context context, int id, String token) {
        this.context = context;
        this.id = id;
        this.token = token;
    }

    public void sendRequest(String requestURL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if (status == 525) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("UID");
                        editor.remove("TOKEN");
                        editor.apply();
                    } else if (status == 616) {
                        String errorMessage = jsonObject.getString("message");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Ada kelasalahan dalam database.", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", String.valueOf(id));
                headers.put("auth_token", token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
