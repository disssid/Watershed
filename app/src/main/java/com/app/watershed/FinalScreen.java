package com.app.watershed;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class FinalScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setTypeface(myTypeFace);
        ImageView iv = (ImageView)findViewById(R.id.imageView) ;
        iv.setAlpha(0.02f);


    }



}
