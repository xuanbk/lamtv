package lamtv.project.com.myapplication.fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


import lamtv.project.com.myapplication.Object.Route;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);


}