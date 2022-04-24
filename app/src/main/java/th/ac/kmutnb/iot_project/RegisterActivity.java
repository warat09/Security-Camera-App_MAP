package th.ac.kmutnb.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity implements VolleyListener{
    EditText rName,rPhone,rEmail,rPasswod,rConpassword;
    Button rRegister,rCancel;
    private final String KEY_PREFS = "prefs_user";
    private final String KEY_USERNAME = "email";
    private final String KEY_TOKEN = "token";
    private String Token;
    private RequestQueue mQueue;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyListener callback = this;
        setContentView(R.layout.activity_register);
        rName = findViewById(R.id.Register_Username);
        rPhone = findViewById(R.id.Register_Phone);
        rEmail = findViewById(R.id.Register_Email);
        rPasswod = findViewById(R.id.Register_Password);
        rConpassword = findViewById(R.id.Register_Confirm_Password);
        rRegister = findViewById(R.id.Register_Ok);
        rCancel = findViewById(R.id.Register_Cancel);
        rCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = rName.getText().toString().trim();
                String phone = rPhone.getText().toString().trim();
                String email = rEmail.getText().toString().trim();
                String password= rPasswod.getText().toString().trim();
                String conpassword= rConpassword.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    rName.setError("Name is Required");
                }
                else if(TextUtils.isEmpty(phone)){
                    rPhone.setError("Phone is Required");
                }
                else if(TextUtils.isEmpty(email)){
                    rEmail.setError("Email is Required");
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    rPasswod.setError("Password is Required");
                    return;
                }
                else if (TextUtils.isEmpty(conpassword)){
                    rConpassword.setError("Confirmpassword is Required");
                    return;
                }
                else if(!password.equals(conpassword)){
                    rConpassword.setError("Confirmpassword notequal Password");
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    rEmail.setError("Please enter a valid email address");
                    return;
                }
                else if(password.length() < 6){
                    rPasswod.setError("Your password must be at least 6 characters long.");
                    return;
                }
                else
                {
                    String postUrl = "http://192.168.1.101:9090/User/register";
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("User_Name",name);
                        postData.put("User_Phone",phone);
                        postData.put("User_Email",email);
                        postData.put("User_Password",password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Token = response.getString("Token");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("TEST",Token);
                            callback.Loaded_Data(true);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Email or Password Already USE!!!", Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }

    @Override
    public void Loaded_Data(boolean Work) {
        if(Work==true)
        {
            sharedPreferences = getSharedPreferences(KEY_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USERNAME, rEmail.getText().toString().trim());
            editor.putString(KEY_TOKEN,this.Token);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}