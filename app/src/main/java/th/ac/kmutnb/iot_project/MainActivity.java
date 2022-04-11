package th.ac.kmutnb.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , VolleyListener{
    private String User_ID;
    private String User_Phone;
    private static String currentPage = "Camera";
    Button Logout;
    ProgressDialog pDialog;
    private final String KEY_PREFS = "prefs_user";
    private final String KEY_USERNAME = "email";
    private final String KEY_PASSWORD = "password";
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VolleyListener callback = this;
        Logout = findViewById(R.id.Main_Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences =getSharedPreferences("prefs_user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREFS, MODE_PRIVATE);
        JsonObjectRequest Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.111:9090/User/getprofile/" + sharedPreferences.getString(KEY_USERNAME,null), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject Data = null;
                        try {
                            Data = response.getJSONObject("user");
                            User_ID = Data.getString("User_Name");
                            User_Phone = Data.getString("User_Phone");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.Loaded_Data(true);
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.Loaded_Data(false);
                    }
                });
        mQueue = Volley.newRequestQueue(getApplicationContext());
        mQueue.add(Camera_Data_Request);
    }
    private void resetButton()
    {
        ImageButton camera_home = findViewById(R.id.Camera_Home);
        camera_home.setImageResource(R.drawable.camera);
        ImageButton noti_home = findViewById(R.id.Notification_Home);
        noti_home.setImageResource(R.drawable.noti);
        ImageButton setting_home = findViewById(R.id.Setting_Home);
        setting_home.setImageResource(R.drawable.setting);
    }
    private void setButton(ImageButton button)
    {
        switch (this.currentPage)
        {
            case "Notification":
                button.setImageResource(R.drawable.notisel);
                break;
            case "Camera":
                button.setImageResource(R.drawable.camerasel);
                break;
            case "Setting":
                button.setImageResource(R.drawable.settingsel);
                break;
        }
    }
    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("User_ID",this.User_ID);
        switch (view.getId())
        {
            case R.id.Notification_Home:
                if(!currentPage.equals("Notification"))
                {
                    resetButton();
                    Homenotification notification = new Homenotification();
                    notification.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,notification).addToBackStack(null).commit();
                    this.currentPage = "Notification";
                    this.setButton(findViewById(R.id.Notification_Home));
                }
                break;
            case R.id.Camera_Home:
                if(!currentPage.equals("Camera"))
                {
                    resetButton();
                    Homecamera camera = new Homecamera();
                    camera.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,camera).addToBackStack(null).commit();
                    this.currentPage = "Camera";
                    this.setButton(findViewById(R.id.Camera_Home));
                }
                break;
            case R.id.Setting_Home:
                if(!currentPage.equals("Setting"))
                {
                    resetButton();
                    Homesetting setting = new Homesetting();
                    setting.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,setting).addToBackStack(null).commit();
                    this.currentPage = "Setting";
                    this.setButton(findViewById(R.id.Setting_Home));
                }
            default:
                break;
        }
    }

    @Override
    public void Loaded_Data(boolean Work) {
        if(Work == true)
        {
            TextView USERNAME = findViewById(R.id.Main_Username);
            USERNAME.setText(this.User_ID);
            Bundle bundle = new Bundle();
            bundle.putString("User_ID",this.User_ID);
            Homecamera camera = new Homecamera();
            camera.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.Fragment_Home,camera).commit();
        }
    }
}