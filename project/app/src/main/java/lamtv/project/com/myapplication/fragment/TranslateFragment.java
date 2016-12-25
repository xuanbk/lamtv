package lamtv.project.com.myapplication.fragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import lamtv.project.com.myapplication.Application;
import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.R;
import lamtv.project.com.myapplication.adapter.TranslateAdapter;



public class TranslateFragment extends Fragment {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String TAG = "TranslateFragmen";
    private LinearLayout lnEnglish,lnVietnamese;
    private View view;
    private RecyclerView lsvTranslate;
    private ArrayList<Translate> translates;
    private TranslateAdapter adapter;
    private TextToSpeech textToSpeech;
    private boolean isEnglish = true;
    private Application application;
    private String translateAPIkey= "AIzaSyDD79kYXGrXhJoj2lfa8cSuav4JZCBkKCw";
    public TranslateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_translate, container, false);
        application = (lamtv.project.com.myapplication.Application)getActivity().getApplication();
        lnEnglish = (LinearLayout) view.findViewById(R.id.lnEnglish);
        lnVietnamese = (LinearLayout) view.findViewById(R.id.lnVietnamese);
        lnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnglish = true;
                promptSpeechInput(Locale.ENGLISH);
            }
        });
        lnVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnglish = false;
                promptSpeechInput(new Locale("vi_VN"));
            }
        });
        lsvTranslate = (RecyclerView) view.findViewById(R.id.lsvTranslte);
        lsvTranslate.setLayoutManager(new LinearLayoutManager(getActivity()));
        textToSpeech=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("vi_VN"));
                }
            }
        });
        translates = application.getTranslates();
        //TODO data demo. khi nao di bao cao nho xoa di
        translates.add(new Translate("Hello","Xin chào",true));
        translates.add(new Translate("Tôi có thể giúp gì cho bạn?","Can I help you?",false));
        translates.add(new Translate("My name is Davil","Tôi tên là Devil",true));
        adapter = new TranslateAdapter(translates,textToSpeech);
        lsvTranslate.setAdapter(adapter);


        return view;
    }

    private void promptSpeechInput(Locale locale) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            ;
        }
    }

    private void translate(final String text,final String from,final String to) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder result = new StringBuilder();
                String translatedText = "";
                try {
                    String encodedText = URLEncoder.encode(text, "UTF-8");
                    String urlStr = "https://www.googleapis.com/language/translate/v2?key=" +translateAPIkey + "&q=" + encodedText + "&target=" + to + "&source=" + from;

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
                translates.add(new Translate(text,s,isEnglish));
                adapter.notifyDataSetChanged();
                lsvTranslate.smoothScrollToPosition(translates.size()-1);
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
                    if (isEnglish) {
                        translate(result.get(0), "en", "vi");
                    }else {
                        translate(result.get(0), "vi", "en");
                    }
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

    @Override
    public void onDestroy() {
        application.setTranslates(translates);
        super.onDestroy();
    }
}
