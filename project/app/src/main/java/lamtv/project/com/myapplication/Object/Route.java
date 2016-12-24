package lamtv.project.com.myapplication.Object;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
    public ArrayList<String> html_instructions;
}
