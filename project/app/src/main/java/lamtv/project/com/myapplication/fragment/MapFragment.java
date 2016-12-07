package lamtv.project.com.myapplication.fragment;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.*;

import java.util.ArrayList;

import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.R;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private MapView mapView;
    private Travles travles;
    private GoogleMap map;
    private  Document md;
    public MapFragment(Travles travles) {
        // Required empty public constructor
        this.travles = travles;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        md = new Document();
        this.map=map;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.0466092,105.7842424)));
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        ReadTask readTask = new ReadTask();
        readTask.execute("");
    }
    private class ReadTask extends AsyncTask<String, Void, org.w3c.dom.Document> {
        @Override
        protected org.w3c.dom.Document doInBackground(String... url) {

            org.w3c.dom.Document doc = md.getDocument(new LatLng(21.0466092,105.7842424), new LatLng(21.071161,105.865253), Document.MODE_DRIVING);

            return doc;
        }

        @Override
        protected void onPostExecute(org.w3c.dom.Document result) {
            super.onPostExecute(result);
            ArrayList<LatLng> directionPoint = md.getDirection(result);
            PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED); // Màu và độ rộng[/FONT]
            for(int i = 0 ; i < directionPoint.size() ; i++) {
                rectLine.add(directionPoint.get(i));
            }
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
            map.addPolyline(rectLine);
        }
    }
}
