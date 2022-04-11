package th.ac.kmutnb.iot_project;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cameraimg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cameraimg extends Fragment implements VolleyListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private String User_Name;
    private String Camera_Id;
    private String Camera_Name;
    private RequestQueue mQueue;
    private String Get_Camera_URL ="http://192.168.0.111:9090/IMG/getIMG/";
    ProgressDialog pDialog;
    private List<IMGData> IMG_Data_List = new ArrayList<>();
    private List<IMGData> IMG_Data_List_Temp = new ArrayList<>();
    public Cameraimg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cameraimg.
     */
    // TODO: Rename and change types and number of parameters
    public static Cameraimg newInstance(String param1, String param2) {
        Cameraimg fragment = new Cameraimg();
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
        IMG_Data_List = new ArrayList<>();
        IMG_Data_List_Temp = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_cameraimg,container,false);
        this.User_Name = getArguments().getString("UserName");
        this.Camera_Id = getArguments().getString("Cam_Id");
        this.Camera_Name = getArguments().getString("Cam_Name");
        TextView tx = view.findViewById(R.id.Camera_Name);
        tx.setText(this.Camera_Name);
        ImageButton imb = view.findViewById(R.id.Exit_IMG);
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        VolleyListener callback = this;
        EditText search_img = view.findViewById(R.id.Search_IMG);
        search_img.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(search_img.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        search_img.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                IMG_Data_List_Temp = new ArrayList<>();
                for(int j =0;j<IMG_Data_List.size();j++)
                {
                    String IMG_Date = new SimpleDateFormat("dd/MM/yyyy").format(IMG_Data_List.get(j).getIMGDate());
                    if(IMG_Date.substring(0,search_img.getText().length()).equals(search_img.getText().toString()))
                    {
                        IMG_Data_List_Temp.add(IMG_Data_List.get(j));
                    }
//                    if(Camera_Data_List.get(j).getName().substring(0,searchCamera.getText().length()).equals(searchCamera.getText().toString()))
//                    {
//                        Camera_Data_List_Temp.add(Camera_Data_List.get(j));
//                    }
                }
                callback.Loaded_Data(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Loading..");
        pDialog.show();
        JsonObjectRequest Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, this.Get_Camera_URL + this.Camera_Id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray Data = null;
                        try {
                            Data = response.getJSONArray("IMG_DATA");
                            for(int i =0;i<Data.length();i++)
                            {
                                String temp_img[] = {"","",""};
                                for(int j = 0;j<Data.getJSONObject(i).getJSONArray("Img").length();j++)
                                {
                                    temp_img[j] = Data.getJSONObject(i).getJSONArray("Img").getString(j);
                                }
                                IMG_Data_List.add(new IMGData(Data.getJSONObject(i).getString("Date_Time"),temp_img));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        IMG_Data_List_Temp = IMG_Data_List;
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

    @Override
    public void Loaded_Data(boolean Work) {
        ListView Lv = view.findViewById(R.id.List_IMG);
        IMGAdapter adapter = new IMGAdapter (this.getActivity(),this.IMG_Data_List_Temp);
        Lv.setAdapter(adapter);
        Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                EditText search_img = view.findViewById(R.id.Search_IMG);
                search_img.setText("");
                IMGShow IMG = new IMGShow();
                Bundle args = new Bundle();
                String Date = new SimpleDateFormat("dd/MM/yyyy").format(IMG_Data_List.get(i).getIMGDate());
                String Time = new SimpleDateFormat("HH:mm:ss").format(IMG_Data_List.get(i).getIMGDate());
                String Dabase_Date = new SimpleDateFormat("yyyy-MM-dd").format(IMG_Data_List.get(i).getIMGDate())+"|"+new SimpleDateFormat("HH:mm:ss").format(IMG_Data_List.get(i).getIMGDate());
                args.putStringArray("IMG",IMG_Data_List.get(i).getIMG());
                args.putString("Date","วันที่ "+Date+"\n"+"เวลา "+Time+" น.");
                args.putString("Database_Date",Dabase_Date);
                args.putString("CameraName",Camera_Name);
                args.putString("Camera_ID",Camera_Id);
                IMG.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home,IMG).addToBackStack(null).commit();
            }
        });
    }
}