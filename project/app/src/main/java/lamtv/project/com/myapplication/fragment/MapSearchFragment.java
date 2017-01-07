package lamtv.project.com.myapplication.fragment;


import java.io.IOException;
import java.util.List;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;


import lamtv.project.com.myapplication.Object.Route;
import lamtv.project.com.myapplication.R;
import lamtv.project.com.myapplication.Utils.StringUtils;
import lamtv.project.com.myapplication.Utils.Utils;
import lamtv.project.com.myapplication.adapter.Map2Adapter;
import lamtv.project.com.myapplication.adapter.MapAdapter;
import lamtv.project.com.myapplication.fragment.DirectionFinderListener;
import lamtv.project.com.myapplication.fragment.DirectionFinder;


public class MapSearchFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener {
    private GoogleMap mMap;
    private Button btnFindPath;
    private ImageView Imgfindpath,imageView;
    private EditText etOrigin;
    private EditText etDestination;
    private ImageView ivSpeech1;
    private ImageView ivSpeech2;
    private boolean ivSpeech = true;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView tvDistance;
    private MapView mapView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> arr;
    //Khai báo Progress Bar dialog để làm màn hình chờ
    ProgressDialog myProgress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps_search, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rcvMap);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        /*setContentView(R.layout.activity_main);*/


        // specify an adapter (see also next example)
        arr = new ArrayList<>();
        mAdapter = new MapAdapter(arr, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        Imgfindpath = (ImageView) view.findViewById(R.id.imgfindpath);
        etOrigin = (EditText) view.findViewById(R.id.etOrigin);
        etDestination = (EditText) view.findViewById(R.id.etDestination);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        ivSpeech1 = (ImageView) view.findViewById(R.id.ivSpeech1);
        ivSpeech2 = (ImageView) view.findViewById(R.id.ivSpeech2);

        Imgfindpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkInternet(getActivity())) {
                    mapView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    sendRequest(true);
                }else {
                    Toast.makeText(getActivity(), "No network access", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkInternet(getActivity())) {
                    mapView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    arr.clear();
                    mAdapter.notifyDataSetChanged();
                    sendRequest(false);
                }else {
                    Toast.makeText(getActivity(), "No network access", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ivSpeech1.setOnClickListener((this);
        ivSpeech1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSpeech = true;
                //promptSpeechInput(Locale.ENGLISH);
                promptSpeechInput("vi-VN");
            }
        });
        ivSpeech2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSpeech = false;
                //promptSpeechInput(Locale.ENGLISH);
                promptSpeechInput("vi-VN");
            }
        });
        return view;
    }


    private void sendRequest(boolean isFind) {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty() &&!isFind) {
            Toast.makeText(getActivity(), "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            if (originMarkers != null) {
                for (Marker marker : originMarkers) {
                    marker.remove();
                }
            }

            if (destinationMarkers != null) {
                for (Marker marker : destinationMarkers) {
                    marker.remove();
                }
            }

            if (polylinePaths != null) {
                for (Polyline polyline : polylinePaths) {
                    polyline.remove();
                }
            }
            tvDistance.setText("0.0 Km");
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                Address address = geocoder.getFromLocationName(origin, 1).get(0);
                LatLng hn = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hn, 16));
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(origin)
                        .position(hn)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                new DirectionFinder(this, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mMap = googleMap;
        /*LatLng hn = new LatLng(21.048928, 105.785468);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hn,16));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hn,16));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Học viện kĩ thuật quân sự")
                .position(hn)));*/
        //Tạo Progress Bar
        myProgress = new ProgressDialog(getActivity());
        myProgress.setTitle("Loading Map ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(true);
     //Hiển thị Progress Bar
        myProgress.show();
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myProgress.dismiss();
            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled( true );
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

    }
// chuyen giong noi
 private void promptSpeechInput(String locale) {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt));
    try {
        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    } catch (ActivityNotFoundException a) {
        Toast.makeText(getActivity(),
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        arr.clear();
        arr.addAll(routes.get(0).html_instructions);
        mAdapter.notifyDataSetChanged();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        if (routes != null || routes.size() > 0){
            for (Route route : routes) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
                /// ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
                tvDistance.setText(route.distance.text);

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                        .title(route.startAddress)
                        .position(route.startLocation)));
                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                        .title(route.endAddress)
                        .position(route.endLocation)));

                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(10);

                for (int i = 0; i < route.points.size(); i++)
                    polylineOptions.add(route.points.get(i));

                polylinePaths.add(mMap.addPolyline(polylineOptions));
            }
    } else {
            Toast.makeText(getActivity(), "Sorry! location not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("", "onActivityResult: " + result.get(0));
                    if (ivSpeech) {
                        String textafter1  = StringUtils.removeAccent(result.get(0));
                        Log.d("", "txxx1: " + textafter1.toString());
                        etOrigin.setText(textafter1);
                    }else{
                        String textafter2  = StringUtils.removeAccent(result.get(0));
                        Log.d("", "txxx1: " + textafter2.toString());
                        etDestination.setText(textafter2);
                    }
                }
                break;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}


