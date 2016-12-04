package lamtv.project.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MainActivity extends FragmentActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Query recentPostsQuery = mDatabase.child("travles")
                .limitToFirst(100);
    }

}
