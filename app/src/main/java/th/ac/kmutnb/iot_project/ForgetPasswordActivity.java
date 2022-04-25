package th.ac.kmutnb.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText lEmail;
    Button lSubmit ,rCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        lEmail = findViewById(R.id.FGemail);
        lSubmit = findViewById(R.id.btnsubmit);
        rCancel = findViewById(R.id.Forget_Cancel);
        rCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        lSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    lEmail.setError("Email is Required");
                    return;
                }
                else{
                    String postUrl = "http://192.168.1.101:9090/User/forgetpassword";
                    RequestQueue requestQueue = Volley.newRequestQueue(ForgetPasswordActivity.this);

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("User_Email",email);
                   } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ForgetPasswordActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgetPasswordActivity.this, "Email or Password Already USE!!!", Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }
}