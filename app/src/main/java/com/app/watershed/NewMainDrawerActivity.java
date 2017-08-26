package com.app.watershed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class NewMainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper myDB;
    String str;
    double myThreshold;
    double Threshold;
    int h1;
    int h2;
    int mh1;
    int mh2;
    int mh3;
    int mh4;
    Uri imageUri;
    ImageView iv;
    TouchImageView myTVF;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 1;
    private static final String TAG = "Arunachala";
    TextView tv;
    Button b;
    int numberOfSeeds = 0 ;
    File myFile =null;
    boolean multiHueRange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDB =new DatabaseHelper(this);
        iv = (ImageView) findViewById(R.id.myIV);
       // iv.setAlpha(0.02f);
        showBackgroundImage();
        myTVF = (TouchImageView)findViewById(R.id.img);
        b= (Button)findViewById(R.id.myBtn);
        imagePickDialog();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.new_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.i1:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                if (checkForImage()) {
                    toast("Cleared");
                    myTVF.setImageBitmap(null);
                } else {
                    toast("Nothing to clear!");
                }
                item.setChecked(false);
                showBackgroundImage();
                break;

            case R.id.i2:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                if (checkForImage()) {
                    toast("Rotating...!!!");
                    final CharSequence[] items = {"90", "180", "270"};
                    final AlertDialog dialog = new AlertDialog.Builder(NewMainDrawerActivity.this)
                            .setTitle("Select the angle of rotation")
                            .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            myTVF.setRotation(myTVF.getRotation() + 90);
                                            break;
                                        case 1:
                                            myTVF.setRotation(myTVF.getRotation() + 180);
                                            break;
                                        case 2:
                                            myTVF.setRotation(myTVF.getRotation() + 270);
                                            break;
                                    }
                                    dialogInterface.dismiss();
                                }
                            }).create();
                    dialog.show();
                } else {
                    toast("No image to rotate! Please upload one");
                }
                item.setChecked(false);
                break;

            case R.id.i3:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                hideBackgroundImage();
                if (checkForImage()) {
                    b.setVisibility(View.VISIBLE);
                    b.setEnabled(true);
                } else {
                    toast("Capture or select an image first!!");
                }
                item.setChecked(false);
                break;

            case R.id.i4:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                hideBackgroundImage();
                if (checkForImage()) {
                    huefeedbackDialog();
                    b.setVisibility(View.VISIBLE);
                    b.setEnabled(true);
                } else {
                    toast("Capture or select an image first!!");
                }
                item.setChecked(false);
                break;

            case R.id.i5:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                if (checkForImage()) {
                    showDialog();
                    b.setVisibility(View.VISIBLE);
                    b.setEnabled(true);
                } else {
                    toast("Capture or select an image first!!");
                }
                item.setChecked(false);
                hideBackgroundImage();
                break;

            case R.id.i6:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                //myTVF.setImageBitmap(loadImage());
                b.setEnabled(false);
                b.setVisibility(View.INVISIBLE);
                //showDialog();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            toast("Opening camera");
            //replacing our activity with the fragment
            /*
            Fragment_TouchImageView camFrag = new Fragment_TouchImageView();
            FragmentManager fm1 = getSupportFragmentManager();
            //                                 replacee           replacer   used when we call the fragment from fragment manager
            fm1.beginTransaction().replace(R.id.myRL_for_fragment, camFrag, camFrag.getTag()).commit();*/

            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            Fragment_TouchImageView camFrag = (Fragment_TouchImageView) getSupportFragmentManager().findFragmentById(R.id.imageFragment);
            camFrag.startCamera();
            hideBackgroundImage();
            //showDialog();

        }
        else if (id == R.id.nav_gallery) {
            toast("Opening Gallery");
            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            b.setEnabled(true);
            Fragment_TouchImageView galFrag = (Fragment_TouchImageView) getSupportFragmentManager().findFragmentById(R.id.imageFragment);
            galFrag.openGallery();
            hideBackgroundImage();
           //showDialog();;
        }
        else if (id == R.id.nav_back) {
            Toast.makeText(this, "Back to main screen", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_save){
            if(checkForImage()){
                toast("Saving");
                BitmapDrawable drawable = (BitmapDrawable) myTVF.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                //myTVF.buildDrawingCache();
                //Bitmap bitmap= myTVF.getDrawingCache();
                Save saveFile = new Save();
                File file = null;
                file = saveFile.saveImage(bitmap);
                refreshGallery(file);
                toast("Image saved!");
            }
            else
            {
                toast( "Nothing to save. Please capture an image!");
            }
        }
        else if(id == R.id.nav_share){
            if(checkForImage()){
                //BitmapDrawable drawable = (BitmapDrawable) myTVF.getDrawable();
                //Bitmap bitmap = drawable.getBitmap();
                toast("Sharing..");
                startShare();
               // toast("Image shared!");
            }

        }
        else if(id == R.id.nav_results) {
            Intent i = new Intent(this, FinalScreen.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showDialog(){
        print("called show dialog");
        final AlertDialog.Builder dialg= new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.activity_dialog, (ViewGroup)findViewById(R.id.layout_dialog));
        final TextView tv = (TextView)viewLayout.findViewById(R.id.textView);
        dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Select the Threshold!");
        // dialg.setView(viewLayout);
        dialg.setView(viewLayout);
        SeekBar seek = (SeekBar)viewLayout.findViewById(R.id.seekBar);
        seek.setMax(20);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float value = ((float)i / 10.0f);
                myThreshold = (double)value;
                tv.setText("Threshold value is: "+ value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Threshold = (double) Math.round(myThreshold * 10) / 10;
                toast("Selected threshold is: "+Threshold);
                dialogInterface.dismiss();
            }
        });

        dialg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Threshold = 1.2;
                toast("Default threshold is: "+Threshold);
                dialogInterface.dismiss();
            }
        });

        dialg.create();
        dialg.show();
    }

    public float getConvertedValue(int intVal){
        float floatVal = 0.0f;
        floatVal = .1f * intVal;
        return floatVal;
    }

    public boolean checkForImage(){
        BitmapDrawable drawable = (BitmapDrawable)myTVF.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        if(bitmap == null){
            return false;
        }else
        {
            return true;
        }
    }


    public void startShare(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");;
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Watershed/WatershedImages/Image_" +Save.MyDate+".jpg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        startActivity(Intent.createChooser(share,"Share via"));
    }

    public Bitmap loadImage(File file){
     //   print("called loadimage filename is "+filename);
      //  File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Watershed/WatershedImages", filename);
      //  print(file.getAbsolutePath());
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }

    public void watershed(View v){

        if(checkForImage()){
            hideBackgroundImage();
            BitmapDrawable drawable = (BitmapDrawable) myTVF.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Save saveFile = new Save();
            File file = null;
            file = saveFile.saveImage(bitmap);
            myFile = file;
            refreshGallery(file);
            Toast.makeText(this, "Processing...!!", Toast.LENGTH_LONG).show();
            Watershed w = new Watershed();
            w.setThresh(Threshold);
            if(multiHueRange){
                w.setMultipleHue(mh1, mh2, mh3, mh4);
            }
            else
            {
                    w.setHue(h1, h2);
            }
            w.checkMultiHue(multiHueRange);
            Bitmap bitmap1 = w.process();
            myTVF.setImageBitmap(bitmap1);
            b.setVisibility(View.INVISIBLE);
            numberOfSeeds = w.getSeedCount();
            seedCountDialog();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    feedbackDialog();
                }
            }, 3000);


        }
        else
        {
            toast("Nothing to process. Please capture an image!");
        }

    }

    //Functionality - Dialog for user input to select a single Hue range
    //Description :
    //  The dialog shows two seek bars for the user to select a hue range
    //      1. The first seek bar is the starting range and ranges between 0-180
    //      2. The second seek bar is the ending range, it should be greater than the starting range and ranges between 0-180

    public void hueDialog(int hueValue1, int hueValue2){
        final AlertDialog.Builder dialg= new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.hue_layout, (ViewGroup)findViewById(R.id.hue_dialog));
        final TextView tv1 = (TextView)viewLayout.findViewById(R.id.textView4);
        final TextView tv2 = (TextView)viewLayout.findViewById(R.id.textView5);
        final TextView tvHueStatus = (TextView)viewLayout.findViewById(R.id.textView10);

        tv1.setText("Starting range value is : " + hueValue1);
        tv2.setText("Ending range value is : " + hueValue2);
        tvHueStatus.setText("");

        if(hueValue1 != 0 && hueValue2 != 0)
            tvHueStatus.setText("Starting hue range cannot be greater than ending rang");
        //dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Select the Hue ranges!");
        // dialg.setView(viewLayout);
        dialg.setView(viewLayout);
        SeekBar seek1 = (SeekBar)viewLayout.findViewById(R.id.seekBar2);
        seek1.setMax(180);
        seek1.setProgress(h1);
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                h1 = i;
                if(h1 > h2) {
                    tvHueStatus.setText("Starting range cannot be greater than ending range");
                    tv1.setText("Starting Range value is : "+ h1);
                    tv2.setText("Ending range value is : " + h2);
                }
                else {
                    tv1.setText("Starting range value is: " + i);
                    tv2.setText("Ending range value is : " + h2);
                    tvHueStatus.setText("");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seek2 = (SeekBar)viewLayout.findViewById(R.id.seekBar3);
        seek2.setMax(180);
        seek2.setProgress(h2);
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                h2 = i;
                if(h2 < h1) {
                    tv1.setText("Starting range value is :" + h1);
                    tv2.setText("Ending range value is : " +h2);
                    tvHueStatus.setText("Ending range cannot be less than starting range");
                }
                else {
                    tv1.setText("Starting range value is :" + h1);
                    tv2.setText("Ending range value is: " + h2);
                    tvHueStatus.setText("");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        b.setEnabled(false);
        dialg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(h1 > h2) {
                    dialogInterface.dismiss();
                    hueDialog(h1, h2);
                }
                else{
                    String name = "";
                    if ((h1 == 0) && (h2 == 50)) {
                        name = "Wheat";
                    } else {
                        name = "Default";
                    }
                    dialogInterface.cancel();
                    String hueString = h1 + "," + h2;
                    print("name is " + name + " and hue is " + hueString);
                    myDB.insertData(name, hueString);
                    toast("Selected range is: " + h1 + " to " + h2);
                    multiHueRange = false;
                    showDialog();
                    dialogInterface.dismiss();
                }
            }
        });

        dialg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                h1 = 0;
                h2 = 50;
                multiHueRange = false;
                showDialog();
                toast("Default Hue range is: "+h1+" to "+h2);
                dialogInterface.dismiss();
            }
        });

        dialg.setNeutralButton("Recent Inputs", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Cursor res = myDB.getData();
                if(res.getCount()==0){
                    showMessage("Error", "Nothing to display");
                    return;
                }
                StringBuffer sb = new StringBuffer();
                while(res.moveToNext()){
                    sb.append("ID: " +res.getString(0)+"\n");
                    sb.append("Name: " +res.getString(1)+"\n");
                    String[] range = res.getString(2).split(",");
                    if(range.length==2) {
                        sb.append("Hue range1: " + res.getString(2).split(",")[0] + "\n");
                        sb.append("Hue range2: " + res.getString(2).split(",")[1] + "\n\n");
                    }
                    else if(range.length == 4){
                        sb.append("Hue range1: " + res.getString(2).split(",")[0] + "\n");
                        sb.append("Hue range2: " + res.getString(2).split(",")[1] + "\n");
                        sb.append("Hue range3: " + res.getString(2).split(",")[2] + "\n");
                        sb.append("Hue range4: " + res.getString(2).split(",")[3] + "\n\n");
                    }

                }
                showMessage("Recent inputs", sb.toString());
                dialogInterface.dismiss();
            }
        });

        dialg.create();
        dialg.show();
    }

    public void multiHueDialog(){
        final AlertDialog.Builder dialg= new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.multiplehue_dialog, (ViewGroup)findViewById(R.id.multiplehue_layoutdialog));
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "AllerDisplay.ttf");
        final TextView tv1 = (TextView)viewLayout.findViewById(R.id.textView7);
        final TextView tv2 = (TextView)viewLayout.findViewById(R.id.textView9);
        //dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Select the Hue ranges!");
        // dialg.setView(viewLayout);
        dialg.setView(viewLayout);
        SeekBar seek1 = (SeekBar)viewLayout.findViewById(R.id.seekBar4);
        seek1.setMax(180);
        mh1=0;
        mh2=0;
        mh3=0;
        mh4=0;

        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mh1 = i;
                tv1.setText("Range1 is from "+ mh1+" to "+ mh2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seek2 = (SeekBar)viewLayout.findViewById(R.id.seekBar5);
        seek2.setMax(180);
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mh2 = i;
                tv1.setText("Range1 is from "+ mh1 +" to "+ mh2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seek3 = (SeekBar)viewLayout.findViewById(R.id.seekBar6);
        seek3.setMax(180);

        seek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mh3 = i;
                tv2.setText("Range2 is from "+ mh3 +" to "+ mh4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seek4 = (SeekBar)viewLayout.findViewById(R.id.seekBar7);
        seek4.setMax(180);

        seek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mh4 = i;
                tv2.setText("Range2 is from "+ mh3 +" to "+ mh4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        dialg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = "";
                if((mh1==0) && (mh2==75) && (mh3==177) && (mh4==255)){
                    name = "Canola";
                }
                else{
                    name = "Default";
                }
                String hueString = mh1+","+mh2+","+mh3+","+mh4;
                myDB.insertData(name, hueString);
                toast("Selected ranges are: "+mh1+" to "+mh2+" and from "+mh3+" to "+mh4);
                multiHueRange = true;
                showDialog();
                dialogInterface.dismiss();
            }
        });

        dialg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                h1 = 0;
                h2 = 50;
                multiHueRange = false;
                showDialog();
                toast("Default Hue range is: "+h1+" to "+h2);
                dialogInterface.dismiss();
            }
        });

        dialg.setNeutralButton("Recent Inputs", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Cursor res = myDB.getData();
                if(res.getCount()==0){
                    showMessage("Error", "Nothing to display");
                    return;
                }
                StringBuffer sb = new StringBuffer();
                while(res.moveToNext()){
                    sb.append("ID: " +res.getString(0)+"\n");
                    sb.append("Name: " +res.getString(1)+"\n");
                    String[] range = res.getString(2).split(",");
                    if(range.length==2) {
                        sb.append("Hue range1: " + res.getString(2).split(",")[0] + "\n");
                        sb.append("Hue range2: " + res.getString(2).split(",")[1] + "\n\n");
                    }
                    else if(range.length == 4){
                        sb.append("Hue range1: " + res.getString(2).split(",")[0] + "\n");
                        sb.append("Hue range2: " + res.getString(2).split(",")[1] + "\n");
                        sb.append("Hue range3: " + res.getString(2).split(",")[2] + "\n");
                        sb.append("Hue range4: " + res.getString(2).split(",")[3] + "\n\n");
                    }

                }
                showMessage("Recent inputs", sb.toString());
                dialogInterface.dismiss();
            }
        });

        dialg.create();
        dialg.show();
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                huefeedbackDialog();
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public void seedHueList(){
        final CharSequence[] items = {"Wheat: Hue range [0-50]", "Canola: Hue Range [0-75]&[177-255]", "Cassava: Hue Range []", "Potato: Hue range []", "Beans: Hue range []"};

        AlertDialog.Builder dialg = new AlertDialog.Builder(this);

        dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Seed types").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        str = (String)items[i];
                        h1 = 0;
                        h2 = 50;
                        multiHueRange = false;
                        break;
                    case 1:
                        str = (String)items[i];
                        mh1 = 0;
                        mh2 = 50;
                        mh3 = 177;
                        mh4 = 255;
                        multiHueRange = true;
                        break;
                    case 2:
                        str = (String)items[i];
                        h1 = 0;
                        h2 = 50;
                        multiHueRange = false;
                        break;
                    case 3:
                        str = (String)items[i];
                        h1 = 0;
                        h2 = 50;
                        multiHueRange = false;
                        break;
                    case 4:
                        str = (String)items[i];
                        h1 = 0;
                        h2 = 50;
                        multiHueRange = false;
                        break;
                    default:
                        str = (String)items[i];
                        h1 = 0;
                        h2 = 50;
                        multiHueRange = false;
                        break;
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] strarr = str.split(":");
                str = strarr[0];
                Toast.makeText(getApplicationContext() ,"You have selected "+str+ " seeds", Toast.LENGTH_LONG).show();
                showDialog();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"You have not selected any kind of seeds", Toast.LENGTH_LONG).show();
                showDialog();
                dialogInterface.dismiss();
            }
        });
        dialg.create();
        dialg.show();
    }

    //Functionality - Dialog for user input to select Hue ranges
    //Description :
    //  The dialog shows three options for the user to select multiple hue ranges, specify a range and view older inputs
    //      1. On selecting "Ok" a list of predefined multiple hue ranges is displayed for the user to select
    //      2. On selecting "Cancel" the user is presented with a dialog to select a range for starting and ending (starting < ending)
    //      3. On selecting "Recent Inputs" the user is presented with a list of previous hue ranges selected

    public void huefeedbackDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you have multiple hue ranges to select?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        multiHueDialog();
                        b.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       hueDialog(0,0);
                        dialogInterface.cancel();
                    }
                })
                .setNeutralButton("Select from list", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        seedHueList();
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void seedCountDialog(){
        AlertDialog.Builder dialg = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.seed_dialog, (ViewGroup)findViewById(R.id.seedcount_dialog));
        final TextView tv = (TextView)viewLayout.findViewById(R.id.myseedTV);
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "AllerDisplay.ttf");
        tv.setTypeface(myTypeFace);
        tv.setText(""+numberOfSeeds);
        dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Seed Count!");
        // dialg.setView(viewLayout);
        dialg.setView(viewLayout);
        dialg.setMessage("The number of seeds in the image are: ")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = dialg.create();
        alert.show();
    }

    public void feedbackDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to revert the processing?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imagePickDialog();
                        b.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        b.setVisibility(View.VISIBLE);
                        b.setEnabled(false);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void imagePickDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("How do you want to load your image?")
                .setCancelable(false)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Fragment_TouchImageView camFrag = (Fragment_TouchImageView) getSupportFragmentManager().findFragmentById(R.id.imageFragment);
                        camFrag.startCamera();
                        hideBackgroundImage();
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Fragment_TouchImageView galFrag = (Fragment_TouchImageView) getSupportFragmentManager().findFragmentById(R.id.imageFragment);
                        galFrag.openGallery();
                        hideBackgroundImage();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.setEnabled(false);
                        b.setVisibility(View.INVISIBLE);
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showBackgroundImage(){
        iv.setAlpha(0.05f);
    }

    public void hideBackgroundImage(){
        iv.setAlpha(0.0f);
    }

    private void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private void print(String s){
        Log.d(TAG, s);
    }
}
