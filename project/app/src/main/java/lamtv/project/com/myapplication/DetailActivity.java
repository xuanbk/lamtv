package lamtv.project.com.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.Utils.Utils;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    private TextView tvLocation;
    private TextView mTitle;
    private Toolbar mToolbar;
    private Travles mTravles;
    private NestedScrollView mLayoutInfo;
    private WebView tvDescription;
    private ImageView imgTravelsTop, imgTravelsBottom, imvLike;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        id = Utils.getIdLike(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10002, new Intent().putExtra("TRAVLES", mTravles));
                finish();
            }
        });
        initLayout();
        mTravles = (Travles) getIntent().getSerializableExtra("TRAVLES");
        if (mTravles != null) {
            setTitle(mTravles.getName());
//            customTitle();
        }
        imvLike = (ImageView) findViewById(R.id.imvLike);
        if (mTravles.getLike().equals("1")) {
            imvLike.setImageDrawable(getResources().getDrawable(R.drawable.like3));
        } else {
            imvLike.setImageDrawable(getResources().getDrawable(R.drawable.like_1));
        }
        if (!getIntent().getBooleanExtra("HISTORY", false)) {
            imvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTravles.getLike().equals("1")) {
                        imvLike.setImageDrawable(getResources().getDrawable(R.drawable.like_1));

                        List<String> myList = new ArrayList<String>(Arrays.asList(Utils.getIdLike(DetailActivity.this).split(",")));
                        for (int i = 0; i < myList.size(); i++) {
                            if (mTravles.getId().equals(myList.get(i))) {
                                myList.remove(i);
                                break;
                            }
                        }
                        String idsave = "";
                        for (int i = 0; i < myList.size(); i++) {
                            if (i == 0) {
                                idsave = myList.get(i);
                            } else {
                                idsave += "," + myList.get(i);
                            }

                        }

                        Utils.saveIdLike(DetailActivity.this, idsave);
                    } else {
                        imvLike.setImageDrawable(getResources().getDrawable(R.drawable.like3));
                        if (id.equals("")) {
                            Utils.saveIdLike(DetailActivity.this, mTravles.getId());
                        } else {
                            Utils.saveIdLike(DetailActivity.this, id + "," + mTravles.getId());
                        }
                    }
                }
            });
        }
    }

    private void initLayout() {
        mTitle = (TextView) findViewById(android.R.id.title);
//        Log.d("MyLogcat", "DetailActivity--initLayout(line 101): "+mTitle);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        mLayoutInfo = (NestedScrollView) findViewById(R.id.layout_info);
        tvDescription = (WebView) findViewById(R.id.tvDescription);
        imgTravelsBottom = (ImageView) findViewById(R.id.imgTralvesBottom);
        imgTravelsTop = (ImageView) findViewById(R.id.imgTralvesTop);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_map);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DetailActivity.this, MapsActivity.class).putExtra("TRAVLES", mTravles));
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
        if (mTravles != null) {
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
