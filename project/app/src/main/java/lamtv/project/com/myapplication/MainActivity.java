package lamtv.project.com.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import lamtv.project.com.myapplication.fragment.MapSearchFragment;
import lamtv.project.com.myapplication.fragment.SearchFragment;
import lamtv.project.com.myapplication.fragment.TranslateFragment;


public class MainActivity extends FragmentActivity {
    private Button btnTravel,btnTranslate,btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchFragment fragment = new SearchFragment();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        btnTranslate = (Button) findViewById(R.id.Translate);

        btnMap = (Button) findViewById(R.id.Map_search);

        btnTravel = (Button) findViewById(R.id.Travels);
        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTravel.setBackground(getResources().getDrawable(R.drawable.bg_button_on));
                btnTravel.setTextColor(getResources().getColor(R.color.color_white));
                btnMap.setBackground(getResources().getDrawable(R.drawable.bg_button_off));
                btnMap.setTextColor(getResources().getColor(R.color.color_black));
                btnTranslate.setBackground(getResources().getDrawable(R.drawable.bg_button_off));
                btnTranslate.setTextColor(getResources().getColor(R.color.color_black));
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SearchFragment fragment = new SearchFragment();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTravel.setBackground(getResources().getDrawable(R.drawable.bg_button_off));
                btnTravel.setTextColor(getResources().getColor(R.color.color_black));
                btnMap.setBackground(getResources().getDrawable(R.drawable.bg_button_off));
                btnMap.setTextColor(getResources().getColor(R.color.color_black));
                btnTranslate.setBackground(getResources().getDrawable(R.drawable.bg_button_on));
                btnTranslate.setTextColor(getResources().getColor(R.color.color_white));
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction trans = fragmentManager.beginTransaction();
                TranslateFragment fragment = new TranslateFragment();
                trans.replace(R.id.frame, fragment);
                trans.commit();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTravel.setBackground(getResources().getDrawable(R.drawable.bg_button_off));
                btnTravel.setTextColor(getResources().getColor(R.color.color_black));
                btnMap.setBackground(getResources().getDrawable(R.drawable.bg_button_on));
                btnMap.setTextColor(getResources().getColor(R.color.color_white));
                btnTranslate.setBackground(getResources().getDrawable(R.drawable.bg_button_off));
                btnTranslate.setTextColor(getResources().getColor(R.color.color_black));
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction trans = fragmentManager.beginTransaction();
                MapSearchFragment fragment = new MapSearchFragment();
                trans.replace(R.id.frame, fragment);
                trans.commit();
            }
        });

    }
}
