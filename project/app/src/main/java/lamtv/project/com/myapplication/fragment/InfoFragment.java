package lamtv.project.com.myapplication.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.R;
import lamtv.project.com.myapplication.adapter.TranslateAdapter;

public class InfoFragment extends Fragment {
    private View view;
    private Travles travles;
    private WebView tvDescription;
    private ImageView imgTravelsTop,imgTravelsBottom;
    public InfoFragment(Travles travles) {
        // Required empty public constructor
        this.travles = travles;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info_travles, container, false);
        tvDescription = (WebView) view.findViewById(R.id.tvDescription);
        imgTravelsBottom = (ImageView) view.findViewById(R.id.imgTralvesBottom);
        imgTravelsTop = (ImageView)view.findViewById(R.id.imgTralvesTop);
        Glide.with(getActivity())
                .load(travles.getLinkimage_1())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imgTravelsTop);
        Glide.with(getActivity())
                .load(travles.getLinkimage_2())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imgTravelsBottom);

        String text = "<html><body>"
                + "<p align=\"justify\">"
                + travles.getDescription()
                + "</p> "
                + "</body></html>";
        tvDescription.loadDataWithBaseURL (null,text, "text/html", "utf-8", null);
        return view;
    }

}
