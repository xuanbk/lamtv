package lamtv.project.com.myapplication.fragment;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.R;
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

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,null,false);
        arr = new ArrayList<>();
        arrTemp = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery = mDatabase.child("travles")
                .limitToFirst(100);
        ivBack = (ImageView) view.findViewById(R.id.btnBack);
        ivSpeech = (ImageView) view.findViewById(R.id.ivSpeech);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
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
        mAdapter = new MyAdapter(arr, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        recentPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Map<String, String> value = (Map<String, String>) snapshot.getValue();
                Travles travles = new Travles();
                travles.setId(snapshot.getKey());
                travles.setCoordinate_1(value.get("coordinate_1"));
                travles.setCoordinate_2(value.get("coordinate_2"));
                travles.setDescription(value.get("description"));
                travles.setLike(value.get("like"));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("", "onActivityResult: " + result.get(0));
                    etSearch.setText(result.get(0));

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
