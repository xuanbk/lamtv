package lamtv.project.com.myapplication.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import lamtv.project.com.myapplication.Application;
import lamtv.project.com.myapplication.HistoryActivity;
import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.R;
import lamtv.project.com.myapplication.Utils.StringUtils;
import lamtv.project.com.myapplication.Utils.Utils;
import lamtv.project.com.myapplication.adapter.MyAdapter;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private ArrayList<Travles> arr;
    private ArrayList<Travles> arrTemp;
    private ImageView ivBack, ivSpeech;
    private EditText etSearch;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String[] ids;
    private String id;
    private Application application;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null, false);
        arr = new ArrayList<>();
        arrTemp = new ArrayList<>();
        application = (Application) getActivity().getApplication();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery = mDatabase.child("travles")
                .limitToFirst(100);
        ivBack = (ImageView) view.findViewById(R.id.btnBack);
        ivSpeech = (ImageView) view.findViewById(R.id.ivSpeech);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        Button btnHistory = (Button) view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Travles> arrayList = new ArrayList<Travles>();
                id = Utils.getIdLike(getActivity());
                ids = id.split(",");
                for (int i = 0; i < arrTemp.size(); i++) {
                    for (int j = 0; j < ids.length; j++) {
                        if (ids[j].equals(arr.get(i).getId())) {
                            Travles travles = arrTemp.get(i);
                            travles.setLike("1");
                            arrayList.add(travles);
                            break;
                        }
                    }
                }
                application.setArrTemp(arrayList);
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
        id = Utils.getIdLike(getActivity());
        Log.d("", "ID = " + id);
        if (!id.equals("")) {
            ids = id.split(",");
        }
        ivBack.setOnClickListener(this);
        ivSpeech.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arr.clear();
                for (Travles travles : arrTemp) {
                    if (travles.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        arr.add(travles);
                    }
                }
                id = Utils.getIdLike(getActivity());
                ids = id.split(",");
                for (int i = 0; i < arr.size(); i++) {
                    arr.get(i).setLike("");
                    for (int j = 0; j < ids.length; j++) {
                        if (ids[j].equals(arr.get(i).getId())) {
                            arr.get(i).setLike("1");
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(arr, getActivity(),false);
        mRecyclerView.setAdapter(mAdapter);
        recentPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Map<String, String> value = (Map<String, String>) snapshot.getValue();
                Travles travles = new Travles();
                travles.setId(snapshot.getKey());
                if (ids != null) {
                    for (int i = 0; i < ids.length; i++) {
                        if (ids[i].equals(travles.getId())) {
                            travles.setLike("1");
                        }
                    }
                }
                travles.setCoordinate_1(value.get("coordinate_1"));
                travles.setCoordinate_2(value.get("coordinate_2"));
                travles.setDescription(value.get("description"));
                travles.setLinkimage_1(value.get("linkimage_1"));
                travles.setLinkimage_2(value.get("linkimage_2"));
                travles.setLoacation(value.get("loacation"));
                travles.setName(value.get("name"));
                arr.add(travles);
                arrTemp.add(travles);
                mAdapter.notifyDataSetChanged();

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!id.equals(Utils.getIdLike(getActivity()))) {
            id = Utils.getIdLike(getActivity());
            ids = id.split(",");
            for (int i = 0; i < arr.size(); i++) {
                arr.get(i).setLike("");
                for (int j = 0; j < ids.length; j++) {
                    if (ids[j].equals(arr.get(i).getId())) {
                        arr.get(i).setLike("1");
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void promptSpeechInput(Locale locale) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
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
                    String textafter  = StringUtils.removeAccent(result.get(0));
                    Log.d("", "txxx: " + textafter.toString());
                    etSearch.setText(textafter);

                }
                break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSpeech:
                promptSpeechInput(Locale.ENGLISH);
                break;
            default:
                break;
        }
    }

}
