package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017-06-06.
 */

public class bukjist extends AppCompatActivity {

    ImageView bukist;
    PhotoViewAttacher Attacher1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bukjist);
        bukist = (ImageView) findViewById(R.id.bukjistmenu);
        Attacher1 = new PhotoViewAttacher(bukist);


    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}