package th.ac.kmutnb.iot_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CameraAdapter extends BaseAdapter {
    private List<CameraData> Data;
    private LayoutInflater mLayoutInflater;

    public CameraAdapter(Context context, List<CameraData> list)
    {
        this.Data = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    static class ViewHolder
    {
        TextView tvName;
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
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.cameralist,viewGroup,false);
            holder = new ViewHolder();
            holder.tvName = (TextView)view.findViewById(R.id.CameraName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        String Name = Data.get(i).getName();
        holder.tvName.setText(Name);
        return view;
    }
}
