package th.ac.kmutnb.iot_project;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IMGShow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IMGShow extends Fragment implements VolleyListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String Camera_Name;
    private String IMG_Date;
    private String Token;
    private String[] IMG;
    private String Database_Date;
    private String Camera_Id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int index = 0;
    public IMGShow() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IMGShow.
     */
    // TODO: Rename and change types and number of parameters
    public static IMGShow newInstance(String param1, String param2) {
        IMGShow fragment = new IMGShow();
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
        view = inflater.inflate(R.layout.fragment_i_m_g_show,container,false);
        this.IMG = getArguments().getStringArray("IMG");
        this.IMG_Date = getArguments().getString("Date");
        this.Camera_Name = getArguments().getString("CameraName");
        this.Camera_Id = getArguments().getString("Camera_ID");
        this.Database_Date = getArguments().getString("Database_Date");
        this.Token = getArguments().getString("Token");
        TextView Cam_name = view.findViewById(R.id.IMG_Camera_Name);
        Cam_name.setText(this.Camera_Name);
        TextView Cam_date = view.findViewById(R.id.IMG_Camera_Date);
        Cam_date.setText(this.IMG_Date);
        ImageButton imb = view.findViewById(R.id.Exit_IMG_Show);
        ImageSwitcher imgS = view.findViewById(R.id.IMG_Show);
        imgS.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView= new ImageView(view.getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // returning imageview
                return imageView;
            }
        });
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        ImageButton Before = view.findViewById(R.id.IMG_Before);
        Before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                if(index<0)
                {
                    index = IMG.length-1;
                }
                byte[] imageBytes = Base64.decode(IMG[index], Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0,imageBytes.length);
                imgS.setImageDrawable(new BitmapDrawable(getResources(), decodedImage));
            }
        });
        ImageButton Next = view.findViewById(R.id.IMG_Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if(index==IMG.length)
                {
                    index = 0;
                }
                byte[] imageBytes = Base64.decode(IMG[index], Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0,imageBytes.length);
                imgS.setImageDrawable(new BitmapDrawable(getResources(), decodedImage));
            }
        });
        Button delete = view.findViewById(R.id.IMG_DELETE);
        VolleyListener callback = this;
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
                        final String url = "http://192.168.0.111:9090/IMG/deleteIMG/";
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        JSONObject UpdateData = new JSONObject();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url + Camera_Id+"/"+Database_Date,UpdateData, new Response.Listener<JSONObject>() {
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
        byte[] imageBytes = Base64.decode(this.IMG[index], Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0,imageBytes.length);
        imgS.setImageDrawable(new BitmapDrawable(getResources(), decodedImage));
        return view;
    }

    @Override
    public void Loaded_Data(boolean Work) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}