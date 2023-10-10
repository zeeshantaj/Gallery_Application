package com.example.gallery_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageShowActivity extends AppCompatActivity {

    private ImageView showImage;
    private boolean isToolbarVisible;
    private Toolbar toolbar;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        gestureDetector = new GestureDetector(this,new MyGestureListener());


        showImage = findViewById(R.id.showImage);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        Glide.with(this)
                .load(imagePath)
                .into(showImage);

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToolbarVisible = !isToolbarVisible;
                if (isToolbarVisible){
                    toolbar.setVisibility(View.VISIBLE);
                }
                else {
                    toolbar.setVisibility(View.GONE);
                }
            }
        });

        gesture();
    }

    private void gesture() {
        showImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    public class MyGestureListener  extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getY() - e1.getY() > 200 && Math.abs(velocityY ) > 100){
                getOnBackPressedDispatcher().onBackPressed();
            }
            return false;
        }
    }

}