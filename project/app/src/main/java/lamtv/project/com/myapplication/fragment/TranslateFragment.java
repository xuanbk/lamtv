package lamtv.project.com.myapplication.fragment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import lamtv.project.com.myapplication.Application;
import lamtv.project.com.myapplication.MyDatabaseHelper;
import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.R;
import lamtv.project.com.myapplication.adapter.TranslateAdapter;



public class TranslateFragment extends Fragment  {
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
    private int position;

    private String translateAPIkey= "AIzaSyDD79kYXGrXhJoj2lfa8cSuav4JZCBkKCw";
   ///key API *:AIzaSyDQmVlaHYcZJ-wnYoCuGj-lRrJJFa5FQ1Q
    private Button btnDelete;

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
                promptSpeechInput("en-US");
            }
        });
        lnVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnglish = false;
                promptSpeechInput("vi-VN");
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

        //load db- lamtv
        translates = application.getTranslates();
        adapter = new TranslateAdapter(translates,textToSpeech,getActivity());
        lsvTranslate.setAdapter(adapter);
        registerForContextMenu(lsvTranslate);
        btnDelete =(Button) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete All Data")
                        .setMessage("Are you sure you want to delete all Data?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // hàm xoá tất cả dữ liệuxxxx
                                MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
                                db.delteALL();
                                translates.clear();
                               translates.addAll(application.getTranslates());
                                adapter.notifyDataSetChanged();
                                adapter.notifyItemRemoved(translates.size()-1);
                                lsvTranslate.smoothScrollToPosition(translates.size()-1);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return view;
    }

    private void promptSpeechInput(String locale) {
        Log.d("locale" , locale.toString());
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,locale);
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
   /* private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
//                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }*/

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
                        //Toast.makeText(getActivity(),
                               // result,
                               // Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException | JsonSyntaxException ex) {
                    System.err.println(ex.getMessage());
                    Toast.makeText(getActivity(),
                            "ERROR",
                            Toast.LENGTH_SHORT).show();
                }
                return translatedText;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
                db.addNote(text,s,isEnglish);
                translates.clear();
                translates.addAll(application.getTranslates());
                adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(translates.size()-1);

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
                    for (int i = 0 ; i <result.size();i++){
                        Log.d("abc",result.get(i));
                    }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_item:
                Toast.makeText(getActivity(), "djshdjhsjd",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
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
    public void refresh() {
        translates.clear();
        translates.addAll(application.getTranslates());
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(translates.size()-1);
        lsvTranslate.smoothScrollToPosition(translates.size()-1);
    }
}
