package com.example.gallery_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.gallery_application.Adapter.ImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private boolean isToolbarVisible;
    private Toolbar toolbar;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo show the image without margin from left right

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_view_pager);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);


        hideSystemUI();

        setViewPager();
        setToolbar();
    }
    private void hideSystemUI() {
        //todo hide the navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void setToolbar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.circle_arrow);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void setViewPager() {

        ArrayList<String> imagePaths = getIntent().getStringArrayListExtra("imagePath"); // Retrieve the ArrayList

        int selectedPosition = getIntent().getIntExtra("position", 0);

        Log.e("MyApp","ViewPager"+imagePaths.size());
        Log.e("MyApp","position"+selectedPosition);
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imagePaths);

        viewPager.setAdapter(imagePagerAdapter);
        viewPager.setCurrentItem(selectedPosition);
       }

}