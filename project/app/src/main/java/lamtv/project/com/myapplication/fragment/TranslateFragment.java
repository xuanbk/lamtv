package lamtv.project.com.myapplication.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import lamtv.project.com.myapplication.R;
import lamtv.project.com.myapplication.adapter.TranslateAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TranslateFragment extends Fragment {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String TAG = "TranslateFragmen";
    private LinearLayout lnBottom;
    private View view;
    private ListView lsvTranslate;
    private ArrayList<Translate> translates;
    private TranslateAdapter adapter;
    public TranslateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_translate, container, false);
        lnBottom = (LinearLayout) view.findViewById(R.id.lnBottom);
        lnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        lsvTranslate = (ListView)view.findViewById(R.id.lsvTranslte);
        translates = new ArrayList<>();
        adapter = new TranslateAdapter(getActivity(),translates);
        lsvTranslate.setAdapter(adapter);
        return view;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            ;
        }
    }

    private void Translate(final String text,final String from,final String to) {
        final String url =
                "https://www.googleapis.com/language/translate/v2?key=AIzaSyAWdBphWehwRizBWm3eOvoojU0XT5AOsRU&source=en&target=de&q=Hello";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder result = new StringBuilder();
                String translatedText = "";
                try {
                    String encodedText = URLEncoder.encode(text, "UTF-8");
                    String urlStr = "https://www.googleapis.com/language/translate/v2?key=AIzaSyAXBVkmF8RaaSX9TEzQZ0bUSv53LTjXeZ0" + "&q=" + encodedText + "&target=" + to + "&source=" + from;

                    URL url = new URL(urlStr);

                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    InputStream stream;
                    if (conn.getResponseCode() == 200) //success
                    {
                        stream = conn.getInputStream();
                    } else
                        stream = conn.getErrorStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JsonParser parser = new JsonParser();

                    JsonElement element = parser.parse(result.toString());

                    if (element.isJsonObject()) {
                        JsonObject obj = element.getAsJsonObject();
                        if (obj.get("error") == null) {
                             translatedText = obj.get("data").getAsJsonObject().
                                    get("translations").getAsJsonArray().
                                    get(0).getAsJsonObject().
                                    get("translatedText").getAsString();
                            Log.d(TAG, "onActivityResult: " + translatedText);
                            return translatedText;

                        }
                    }

                    if (conn.getResponseCode() != 200) {
                        System.err.println(result);
                    }

                } catch (IOException | JsonSyntaxException ex) {
                    System.err.println(ex.getMessage());
                }
                return translatedText;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                translates.add(new Translate(text,s));
                adapter.notifyDataSetChanged();
            }
        }.execute();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(TAG, "onActivityResult: " + result.get(0));
                    Translate(result.get(0),"en","vi");
                }
                break;
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
