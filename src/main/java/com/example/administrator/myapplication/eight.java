package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017-06-06.
 */

public class eight extends AppCompatActivity{
    ImageView eight;
    PhotoViewAttacher Attacher1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eight);
        eight=(ImageView)findViewById(R.id.eightmenu);
        Attacher1=new PhotoViewAttacher(eight);


    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
