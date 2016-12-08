package lamtv.project.com.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

import lamtv.project.com.myapplication.Object.Travles;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Travles travles;
    private lamtv.project.com.myapplication.fragment.Document md;
    private Location location;
    private LatLng endLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        travles = (Travles) getIntent().getSerializableExtra("TRAVLES");
        endLocation = new LatLng(Double.valueOf(travles.getCoordinate_1().split(",")[0]),Double.valueOf(travles.getCoordinate_1().split(",")[1].replace(" ","")));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mylocation).title(""));
        mMap.addMarker(new MarkerOptions().position(endLocation).title(travles.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
        md = new lamtv.project.com.myapplication.fragment.Document();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        ReadTask readTask = new ReadTask();
        readTask.execute("");
    }
    private class ReadTask extends AsyncTask<String, Void, Document> {
        @Override
        protected org.w3c.dom.Document doInBackground(String... url) {

            org.w3c.dom.Document doc = md.getDocument(new LatLng(location.getLatitude(),location.getLongitude()),endLocation , lamtv.project.com.myapplication.fragment.Document.MODE_DRIVING);

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
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
            mMap.addPolyline(rectLine);
        }
    }
}