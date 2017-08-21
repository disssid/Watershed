package com.app.watershed;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Arunachala";

    static{
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        }
        else{
            Log.d(TAG, "OpenCV is loaded successfully!");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting th font and applying it to the textview
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "AllerDisplay.ttf");
        TextView tv = (TextView)findViewById(R.id.myTV);
        tv.setTypeface(myTypeFace);
        Button b  = (Button)findViewById(R.id.myB);
        Typeface myTypeFace2 = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        b.setTypeface(myTypeFace2);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void btnClicked(View v){
        Log.i(TAG, "clicked");
        Intent i = new Intent(this, NewMainDrawerActivity.class);
        Log.i(TAG, "intent created");
        startActivity(i);
    }
}