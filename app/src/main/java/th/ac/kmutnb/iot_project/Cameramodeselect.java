package th.ac.kmutnb.iot_project;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cameramodeselect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cameramodeselect extends Fragment implements VolleyListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String User_Name;
    private String Camera_Id;
    private String Camera_Name;
    private String Token;
    private boolean Camera_Sensor;
    private boolean Camera_Ready;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Cameramodeselect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cameramodeselect.
     */
    // TODO: Rename and change types and number of parameters
    public static Cameramodeselect newInstance(String param1, String param2) {
        Cameramodeselect fragment = new Cameramodeselect();
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
        view = inflater.inflate(R.layout.fragment_cameramodeselect, container, false);
        this.User_Name = getArguments().getString("UserName");
        this.Camera_Id = getArguments().getString("Cam_Id");
        this.Camera_Name = getArguments().getString("Cam_Name");
        this.Camera_Sensor = getArguments().getBoolean("Cam_Sensor");
        this.Camera_Ready = getArguments().getBoolean("Cam_Ready");
        this.Token = getArguments().getString("Token");
        TextView name = view.findViewById(R.id.Select_Camera_Name);
        name.setText(this.Camera_Name);
        EditText editname = view.findViewById(R.id.Select_Camera_Edit_Name);
        editname.setText(this.Camera_Name);
        VolleyListener callback = this;
        editname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    final String url = "http://192.168.0.111:9090/Camera/NameCamera/";
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    JSONObject UpdateData = new JSONObject();
                    try {
                        UpdateData.put("Camera_Name",editname.getText());
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url + Camera_Id,UpdateData, new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            callback.Loaded_Data(true);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            callback.Loaded_Data(true);
                            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(editname.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", Token);
                            return params;
                        }
                    };
                    requestQueue.add(jsonObjectRequest);
                    return true;
                }
                return false;
            }
        });
        name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                name.setVisibility(View.INVISIBLE);
                editname.setVisibility(View.VISIBLE);
                return true;
            }
        });
        if(this.Camera_Ready)
        {
            TextView ready = view.findViewById(R.id.Select_Camera_Ready);
            ready.setBackgroundColor(Color.parseColor("#4fff8d"));
        }
        if(this.Camera_Sensor)
        {
            TextView sensor = view.findViewById(R.id.Select_Camera_Sensor);
            sensor.setBackgroundColor(Color.parseColor("#4fff8d"));
        }
        ImageButton exit = view.findViewById(R.id.Select_Camera_Exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Button log = view.findViewById(R.id.Select_Camera_LOG);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Cameraimg IMG = new Cameraimg();
                    Bundle args = new Bundle();
                    args.putString("UserName",User_Name);
                    args.putString("Cam_Id",Camera_Id);
                    args.putString("Cam_Name",Camera_Name);
                    args.putString("Token",Token);
                    IMG.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,IMG).addToBackStack(null).commit();
            }
        });
        Button vdo = view.findViewById(R.id.Select_Camera_VDO);
        vdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camerarealtime real = new Camerarealtime();
                Bundle args = new Bundle();
                args.putString("UserName",User_Name);
                args.putString("Cam_Id",Camera_Id);
                args.putString("Cam_Name",Camera_Name);
                args.putString("Token",Token);
                real.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,real).addToBackStack(null).commit();
            }
        });
        if(!this.Camera_Ready)
        {
            vdo.setEnabled(false);
        }
        Button delete = view.findViewById(R.id.Camera_DELETE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.delete_dialog);
                dialog.show();

                Button confirm = dialog.findViewById(R.id.IMG_OK);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String url = "http://192.168.0.111:9090/Camera/ResetCamera/";
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        JSONObject UpdateData = new JSONObject();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url + Camera_Id,UpdateData, new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                callback.Loaded_Data(true);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                callback.Loaded_Data(true);
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", Token);
                                return params;
                            }
                        };
                        requestQueue.add(jsonObjectRequest);
                        dialog.dismiss();
                    }
                });
                Button cancel = dialog.findViewById(R.id.IMG_Cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return view;
    }
    public void Loaded_Data(boolean Work) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}