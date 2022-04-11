package th.ac.kmutnb.iot_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class IMGAdapter extends BaseAdapter {
    private List<IMGData> Data;
    private LayoutInflater mLayoutInflater;

    public IMGAdapter(Context context, List<IMGData> list)
    {
        this.Data = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    static class ViewHolder
    {
        TextView tvDate;
        TextView tvTime;
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
        IMGAdapter.ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.imglist,viewGroup,false);
            holder = new IMGAdapter.ViewHolder();
            holder.tvDate = (TextView)view.findViewById(R.id.IMG_Date);
            holder.tvTime = (TextView)view.findViewById(R.id.IMG_Time);
            view.setTag(holder);
        } else {
            holder = (IMGAdapter.ViewHolder)view.getTag();
        }
        String Date = new SimpleDateFormat("dd/MM/yyyy").format(Data.get(i).getIMGDate());
        String Time = new SimpleDateFormat("HH:mm:ss").format(Data.get(i).getIMGDate());
        System.out.println(Date+Time);
        holder.tvDate.setText("วันที่ "+Date);
        holder.tvTime.setText("เวลา "+Time+" น.");
        return view;
    }
}
