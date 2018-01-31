package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017-06-06.
 */

public class miz extends AppCompatActivity {

ImageView mizmenu;

    PhotoViewAttacher Attacher1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miz);
        mizmenu=(ImageView)findViewById(R.id.mizmenu);
        Attacher1=new PhotoViewAttacher(mizmenu);


    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
