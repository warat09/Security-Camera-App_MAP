package th.ac.kmutnb.iot_project;

import android.util.Log;

import java.sql.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IMGData {
    Date IMG_DateTime;
    String IMG[];
    public IMGData(String date,String img[])
    {
        try {
            this.IMG_DateTime = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.IMG = img;
    }
    public Date getIMGDate()
    {
        return this.IMG_DateTime;
    }
    public String[] getIMG()
    {
        return this.IMG;
    }
}
