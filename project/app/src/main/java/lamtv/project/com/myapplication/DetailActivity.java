package lamtv.project.com.myapplication;

import android.content.Intent;
import android.os.Bundle;

import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.fragment.InfoFragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by XLV on 12/5/16.
 */

public class DetailActivity extends FragmentActivity {
    private TextView tvTitle;
    private Travles travles;
    private Button btnMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        travles = (Travles) getIntent().getSerializableExtra("TRAVLES");
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(travles.getName());
        InfoFragment mapFragment = new InfoFragment(travles);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, mapFragment);
        transaction.commit();
        btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this,MapsActivity.class).putExtra("TRAVLES",travles));
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
