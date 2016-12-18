package lamtv.project.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import lamtv.project.com.myapplication.fragment.SearchFragment;



public class DefaultActitivy extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_layout);
        findViewById(R.id.Travels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DefaultActitivy.this, SearchFragment.class));
            }
        });

        findViewById(R.id.Translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DefaultActitivy.this, MainActivity.class));
            }
        });
        findViewById(R.id.Map_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DefaultActitivy.this, MapSearchActivity.class));
            }
        });
    }
}
