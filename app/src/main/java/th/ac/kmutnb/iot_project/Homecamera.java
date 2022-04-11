package th.ac.kmutnb.iot_project;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Homecamera#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homecamera extends Fragment implements VolleyListener{
    private String Get_Camera_URL ="http://192.168.0.111:9090/Camera/ReadCamera/";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue mQueue;
    private String User_ID;
    ProgressDialog pDialog;
    private List<CameraData> Camera_Data_List = new ArrayList<>();
    private List<CameraData> Camera_Data_List_Temp = new ArrayList<>();
    private Boolean DataLoad = false;
    private View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Homecamera() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homecamera.
     */
    // TODO: Rename and change types and number of parameters
    public static Homecamera newInstance(String param1, String param2) {
        Homecamera fragment = new Homecamera();
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
        Camera_Data_List = new ArrayList<>();
        Camera_Data_List_Temp = new ArrayList<>();
        VolleyListener callback = this;
        view = inflater.inflate(R.layout.fragment_homecamera,container,false);
        this.User_ID  = getArguments().getString("User_ID");
        EditText searchCamera = view.findViewById(R.id.Search_Camera);
        searchCamera.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(searchCamera.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        searchCamera.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Camera_Data_List_Temp = new ArrayList<>();
                for(int j = 0;j<Camera_Data_List.size();j++)
                {
                    if(Camera_Data_List.get(j).getName().substring(0,searchCamera.getText().length()).equals(searchCamera.getText().toString()))
                    {
                        Camera_Data_List_Temp.add(Camera_Data_List.get(j));
                    }
                }
                callback.Loaded_Data(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button Add = view.findViewById(R.id.Add_Camer);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.addcamera_dialog);
                dialog.show();
                Button confirm = dialog.findViewById(R.id.Add_Ok);
                EditText editName = dialog.findViewById(R.id.Add_Cam_Name);
                EditText editId = dialog.findViewById(R.id.Add_Cam_Id);
                TextView Error = dialog.findViewById(R.id.Add_Error);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Name = editName.getText().toString();
                        String Id = editId.getText().toString();
                        if(!Name.equals("") && !Id.equals(""))
                        {
                            final String url = "http://192.168.0.111:9090/Camera/OwnerCamera/";
                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                            JSONObject UpdateData = new JSONObject();
                            try {
                                UpdateData.put("Camera_ID",Id);
                                UpdateData.put("Camera_Name",Name);
                                UpdateData.put("Camera_Owner",User_ID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url + Id,UpdateData, new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject response) {
                                    JSONObject Data = null;
                                    try {
                                        Data = response.getJSONObject("camera");
                                        Camera_Data_List.add(new CameraData(Data.getString("Camera_ID"),Data.getString("Camera_Name"),Data.getBoolean("Camera_Sensor"),Data.getBoolean("Camera_Ready")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Camera_Data_List_Temp = Camera_Data_List;
                                    callback.Loaded_Data(true);
                                    dialog.dismiss();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Error.setVisibility(View.VISIBLE);
                                    error.printStackTrace();
                                    callback.Loaded_Data(true);
                                }
                            });
                            requestQueue.add(jsonObjectRequest);
                        }
                    }
                });
                Button cancel = dialog.findViewById(R.id.Add_Cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Loading..");
        pDialog.show();
        JsonObjectRequest Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, this.Get_Camera_URL + this.User_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray Data = null;
                        try {
                            Data = response.getJSONArray("camera");
                            for(int i =0;i<Data.length();i++)
                            {
                                Camera_Data_List.add(new CameraData(Data.getJSONObject(i).getString("Camera_ID"),Data.getJSONObject(i).getString("Camera_Name"),Data.getJSONObject(i).getBoolean("Camera_Sensor"),Data.getJSONObject(i).getBoolean("Camera_Ready")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Camera_Data_List_Temp = Camera_Data_List;
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
        mQueue = Volley.newRequestQueue(view.getContext());
        mQueue.add(Camera_Data_Request);
        return view;
    }
    public String getName(int i){return this.Camera_Data_List.get(i).getName();}
    public String getId(int i){return this.Camera_Data_List.get(i).getID();}
    public Boolean getSensor(int i){return this.Camera_Data_List.get(i).getSensor();}
    public Boolean getCamera(int i){return this.Camera_Data_List.get(i).getReady();}
    @Override
    public void Loaded_Data(boolean Work){
        if(Work)
        {
            ListView Lv = view.findViewById(R.id.List_Camera);
            CameraAdapter adapter = new CameraAdapter(this.getActivity(),this.Camera_Data_List_Temp);
            Lv.setAdapter(adapter);
            Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                    EditText searchCamera = view.findViewById(R.id.Search_Camera);
                    searchCamera.setText("");
                    Cameramodeselect mode = new Cameramodeselect();
                    Bundle args = new Bundle();
                    args.putString("UserName",User_ID);
                    args.putString("Cam_Id",getId(i));
                    args.putString("Cam_Name",getName(i));
                    args.putBoolean("Cam_Sensor",getSensor(i));
                    args.putBoolean("Cam_Ready",getCamera(i));
                    mode.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,mode).addToBackStack(null).commit();
                }
            });
        }
    }
}