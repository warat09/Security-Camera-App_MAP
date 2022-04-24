package th.ac.kmutnb.iot_project;

public class CameraData {
    private String Camera_ID;
    private String Camera_Name;
    private boolean Camera_Sensor;
    private boolean Camera_Ready;
    public CameraData(String id,String name,boolean sensor,boolean ready)
    {
        this.Camera_ID = id;
        this.Camera_Name = name;
        this.Camera_Sensor = sensor;
        this.Camera_Ready = ready;
    }
    public String getID()
    {
        return this.Camera_ID;
    }
    public String getName()
    {
        return this.Camera_Name;
    }
    public boolean getSensor()
    {
        return this.Camera_Sensor;
    }
    public boolean getReady()
    {
        return this.Camera_Ready;
    }
    public void setSensor(boolean C){this.Camera_Sensor=C;}
}
