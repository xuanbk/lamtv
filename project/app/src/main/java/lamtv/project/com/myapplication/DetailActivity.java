package lamtv.project.com.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.fragment.InfoFragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


public class DetailActivity extends AppCompatActivity {
//    private TextView tvTitle,tvTitle1;
//    private Travles travles;
//    private Button btnMap;

    private TextView tvLocation;
    private TextView mTitle;
    private Toolbar mToolbar;
    private Travles mTravles;
    private NestedScrollView mLayoutInfo;
    private WebView tvDescription;
    private ImageView imgTravelsTop,imgTravelsBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initLayout();
        mTravles = (Travles) getIntent().getSerializableExtra("TRAVLES");
        if (mTravles != null) {
            setTitle(mTravles.getName());
//            customTitle();
        }
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvTitle.setText(travles.getName());
//        tvTitle1 = (TextView)findViewById(R.id.tvTitle1);
//        tvTitle1.setText(travles.getLoacation());
//        InfoFragment mapFragment = new InfoFragment(travles);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame, mapFragment);
//        transaction.commit();
//        btnMap = (Button) findViewById(R.id.btnMap);
//        btnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DetailActivity.this,MapsActivity.class).putExtra("TRAVLES",travles));
//            }
//        });
//        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private void initLayout() {
      mTitle = (TextView) findViewById(android.R.id.title);
//        Log.d("MyLogcat", "DetailActivity--initLayout(line 101): "+mTitle);
        tvLocation =(TextView) findViewById(R.id.tvLocation);
        mLayoutInfo = (NestedScrollView) findViewById(R.id.layout_info);
        tvDescription = (WebView) findViewById(R.id.tvDescription);
        imgTravelsBottom = (ImageView) findViewById(R.id.imgTralvesBottom);
        imgTravelsTop = (ImageView)findViewById(R.id.imgTralvesTop);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_map);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DetailActivity.this,MapsActivity.class).putExtra("TRAVLES",mTravles));
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadImage();
        loadInfo();
    }

    private void loadImage() {
        if (mTravles != null) {
            Glide.with(getApplicationContext())
                    .load(mTravles.getLinkimage_1())
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imgTravelsTop);

            Glide.with(getApplicationContext())
                    .load(mTravles.getLinkimage_2())
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imgTravelsBottom);
        }
    }

    private void loadInfo() {
        if (mTravles !=null) {
            tvLocation.setText(mTravles.getLoacation());

            String text = "<html><body>"
                    + "<p align=\"justify\">"
                    + mTravles.getDescription()
                    + "</p> "
                    + "</body></html>";
            tvDescription.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        }
    }
}
