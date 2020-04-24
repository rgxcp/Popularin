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

public class SignUp {
    private Context context;
    private CoordinatorLayout layout;
    private String firstName, lastName, username, email, password;

    public SignUp(Context context, CoordinatorLayout layout, String firstName, String lastName, String username, String email, String password) {
        this.context = context;
        this.layout = layout;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void sendRequest(String requestURL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if (status == 505) {
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
                    } else if (status == 626) {
                        JSONArray jsonArrayResult = jsonObject.getJSONArray("result");
                        String errorMessage = jsonArrayResult.get(0).toString();
                        Snackbar.make(layout, errorMessage, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(layout, "Ada kesalahan dalam database.", Snackbar.LENGTH_SHORT).show();
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
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("username", username);
                params.put("email", email);
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

    /*
    public String sendRequest(String requestURL) {
        Map<String, String> params = new HashMap<>();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, requestURL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 626) {
                        String error = response.getJSONArray("result").get(0).toString();
                        Snackbar.make(layout, error, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(layout, "Ada kesalahan dalam database.", Snackbar.LENGTH_SHORT).show();
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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(signUpRequest);

        return "A";
    }
     */
}
