package th.ac.kmutnb.iot_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Camerarealtime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camerarealtime extends Fragment implements VolleyListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String User_Name;
    private String Camera_Id;
    private String Camera_Name;
    private String Token;
    private boolean RealtimeState = false;
    private Button OnRealtime,OffRealtime;
    private RequestQueue mQueue;
    private String WebLink;
    private WebView Realtime_WV;
    private String IP;
    public Camerarealtime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Camerarealtime.
     */
    // TODO: Rename and change types and number of parameters
    public static Camerarealtime newInstance(String param1, String param2) {
        Camerarealtime fragment = new Camerarealtime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.User_Name = getArguments().getString("UserName");
        this.Camera_Id = getArguments().getString("Cam_Id");
        this.Camera_Name = getArguments().getString("Cam_Name");
        this.Token = getArguments().getString("Token");
        view = inflater.inflate(R.layout.fragment_camerarealtime, container, false);
        mQueue = Volley.newRequestQueue(view.getContext());
        VolleyListener callback = this;
        TextView tx = view.findViewById(R.id.Camera_Name_Realtime);
        tx.setText(this.Camera_Name);
        Realtime_WV = view.findViewById(R.id.Realtime_Web);
        Realtime_WV.getSettings().setLoadWithOverviewMode(true);
        Realtime_WV.getSettings().setUseWideViewPort(true);
        OnRealtime = view.findViewById(R.id.Realtime_On);
        OffRealtime = view.findViewById(R.id.Realtime_Off);
        if(!RealtimeState)
        {
            OffRealtime.setEnabled(false);
        }
        OnRealtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealtimeState = !RealtimeState;
                OnRealtime.setEnabled(false);
                OffRealtime.setEnabled(true);
                JsonObjectRequest Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.111:9090/Camera/Onrealtime/"+Camera_Id, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject Data = null;
                                try {
                                    Data = response.getJSONObject("Result");
                                    IP = Data.getString("Camera_IP");
                                    WebLink = "http://"+Data.getString("Camera_IP")+"/800x600.mjpeg";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callback.Loaded_Data(true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callback.Loaded_Data(false);
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", Token);
                        return params;
                    }
                };
                mQueue.add(Camera_Data_Request);
            }
        });
        OffRealtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealtimeState = !RealtimeState;
                OnRealtime.setEnabled(true);
                OffRealtime.setEnabled(false);
                Realtime_WV.loadUrl("");
                Realtime_WV.setVisibility(View.INVISIBLE);
                JsonObjectRequest Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.111:9090/Camera/Offrealtime/"+Camera_Id, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callback.Loaded_Data(false);
                            }
                        })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", Token);
                        return params;
                    }
                };
                mQueue.add(Camera_Data_Request);
            }
        });
        ImageButton exit = view.findViewById(R.id.Camera_Realtime_Exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realtime_WV.loadUrl("");
                Realtime_WV.setVisibility(View.INVISIBLE);
                JsonObjectRequest Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.111:9090/Camera/Offrealtime/"+Camera_Id, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callback.Loaded_Data(false);
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", Token);
                        return params;
                    }
                };
                mQueue.add(Camera_Data_Request);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    @Override
    public void Loaded_Data(boolean Work) {
        if(Work==true)
        {
            Realtime_WV.loadUrl(this.WebLink);
            Realtime_WV.setVisibility(View.VISIBLE);
        }
    }
}