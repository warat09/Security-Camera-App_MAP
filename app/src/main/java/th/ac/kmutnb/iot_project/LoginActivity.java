package th.ac.kmutnb.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText lEmail,lpassword;
    Button lLogin,lRegister;
    SharedPreferences sharedPreferences;
    private final String KEY_PREFS = "prefs_user";
    private final String KEY_USERNAME = "email";
    private final String KEY_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lEmail = findViewById(R.id.Login_Email);
        lpassword = findViewById(R.id.Login_Password);
        lLogin = findViewById(R.id.Login_Ok);
        lRegister = findViewById(R.id.Login_Register);
        sharedPreferences = getSharedPreferences(KEY_PREFS,MODE_PRIVATE);
        String EMAIL = sharedPreferences.getString(KEY_USERNAME,null);
        if(EMAIL != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        lRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lEmail.getText().toString().trim();
                String password = lpassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    lEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    lpassword.setError("Password is Required");
                    return;
                }
                else {
                    String postUrl = "http://192.168.0.111:9090/User";
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("User_Email", email);
                        postData.put("User_Password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                if (response.getString("message").equals("Login Success!!")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_USERNAME, email);
                                    editor.putString(KEY_PASSWORD, password);
                                    editor.apply();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }
}