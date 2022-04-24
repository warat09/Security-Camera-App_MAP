package th.ac.kmutnb.iot_project;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraSettingAdapter extends BaseAdapter {
    private List<CameraData> Data;
    private LayoutInflater mLayoutInflater;
    private String Token;
    public CameraSettingAdapter (Context context, List<CameraData> list,String Token)
    {
        this.Data = list;
        this.Token = Token;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    static class ViewHolder
    {
        TextView tvName;
        TextView tvSensor;
        Switch tvSensorSwitch;
    }
    @Override
    public int getCount() {
        return Data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RequestQueue mQueue;
        CameraSettingAdapter.ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.camerasettinglist,viewGroup,false);
            holder = new CameraSettingAdapter.ViewHolder();
            holder.tvName = (TextView)view.findViewById(R.id.Setting_CameraName);
            holder.tvSensor = (TextView)view.findViewById(R.id.Setting_Camera_sensor);
            holder.tvSensorSwitch = (Switch)view.findViewById(R.id.Setting_Camera_semsor_onoff);
            view.setTag(holder);
        } else {
            holder = (CameraSettingAdapter.ViewHolder)view.getTag();
        }
        String Name = Data.get(i).getName();
        holder.tvName.setText(Name);
        Boolean Sensor = Data.get(i).getSensor();
        holder.tvSensorSwitch.setChecked(Sensor);
        mQueue = Volley.newRequestQueue(view.getContext());
        holder.tvSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                JsonObjectRequest Camera_Data_Request;
                if(b)
                {
                    Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.111:9090/Camera/OnSensor/" + Data.get(i).getID(), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Data.get(i).setSensor(true);
                                    holder.tvSensor.setBackgroundColor(Color.parseColor("#4fff8d"));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

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
                }
                else
                {
                    Camera_Data_Request = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.111:9090/Camera/OffSensor/" + Data.get(i).getID(), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Data.get(i).setSensor(false);
                                    holder.tvSensor.setBackgroundColor(Color.parseColor("#ff4f4f"));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", Token);
                            return params;
                        }
                    };
                }
                mQueue.add(Camera_Data_Request);
            }
        });
        if(Sensor)
        {
            holder.tvSensor.setBackgroundColor(Color.parseColor("#4fff8d"));
        }
        else
        {
            holder.tvSensor.setBackgroundColor(Color.parseColor("#ff4f4f"));
        }
        return view;
    }
}
