package com.app.watershed;

import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import io.apptik.widget.MultiSlider;

public class NewMainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper myDB;
    String str;
    double myThreshold;
    double Threshold;
    int hueValue1;
    int hueValue2;
    int multiHueValue1;
    int multiHueValue2;
    int multiHueValue3;
    int multiHueValue4;
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
    boolean hueRangeInclude = true;
    int hueValueMin,hueValueMax, multiHueValueMin, multiHueValueMax;
    CheckBox hueIncludeRange1,hueIncludeRange2;

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
        dialg.setTitle("Select threshold");
        // dialg.setView(viewLayout);
        dialg.setView(viewLayout);
        SeekBar seek = (SeekBar)viewLayout.findViewById(R.id.seekBar);
        seek.setMax(20);
        tv.setText("Threshold value : " + 0);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float value = ((float)i / 10.0f);
                myThreshold = (double)value;
                tv.setText("Threshold value : "+ value);
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
                w.setMultipleHue(multiHueValue1, multiHueValue2, multiHueValue3, multiHueValue4);
            }
            else
            {
                    w.setHue(hueValue1, hueValue2);
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

    public void singleOrMultiHueConfirm(final int singleOrMultiHue1, final int singleOrMultiHue2){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Multiple Hue ranges selected, the process will change from Single to Multi Hue, Do you want to continue?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        multiHueRange = true;
                        dialog.dismiss();
                        multiHueDialog(singleOrMultiHue1,singleOrMultiHue2);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        hueDialog(singleOrMultiHue1,singleOrMultiHue2);
                    }
                });
        builder.create();
        builder.show();
    }

    //Functionality - Dialog for user input to select a single Hue range
    //Description :
    //  The dialog shows a range seek bar for the user to select a hue range
    //  The min value is 0 and the max is 255

    public void hueDialog(int singleHue1, int singleHue2) {
        final AlertDialog.Builder dialg = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.hue_layout, (ViewGroup) findViewById(R.id.hue_dialog));
        final TextView tvHueMin = (TextView) viewLayout.findViewById(R.id.textView4);
        final TextView tvHueMax = (TextView) viewLayout.findViewById(R.id.textView5);
        final TextView tvHueStatus = (TextView) viewLayout.findViewById(R.id.textView10);
        final MultiSlider hueRangeSlider = (MultiSlider) viewLayout.findViewById(R.id.hueRangeSlider);
        final Switch hueRangeSwitch = (Switch) viewLayout.findViewById(R.id.hueRangeSwitch);
        final TextView tvhueIncludeRanges = (TextView) viewLayout.findViewById(R.id.hueIncludeRanges);
        hueIncludeRange1 = (CheckBox) viewLayout.findViewById(R.id.hueIncludeRange1);
        hueIncludeRange2 = (CheckBox) viewLayout.findViewById(R.id.hueIncludeRange2);

        //dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Hue filter range");
        dialg.setView(viewLayout);

        //set the initial settings for the multi range slider for hue values
        hueRangeSlider.setDrawThumbsApart(true);
        hueRangeSlider.setStepsThumbsApart(20);
        hueRangeSlider.setMin(0);
        hueRangeSlider.setMax(255);
        hueRangeSlider.repositionThumbs();

        hueValueMin = hueRangeSlider.getMin();
        hueValueMax = hueRangeSlider.getMax();
        if (singleHue1 != 0 && singleHue2 != 255){
            hueValue1 = singleHue1;
            hueValue2 = singleHue2;
            hueRangeSlider.clearThumbs();
            hueRangeSlider.addThumb(hueValue1);
            hueRangeSlider.addThumb(hueValue2);
        }
        else {
            hueValue1 = hueValueMin;
            hueValue2 = hueValueMax;
        }
        //create a value change listener for the multi range slider for hue values
        hueRangeSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    hueValue1 = value;
                    //tv1.setText("Min : " + String.valueOf(value));
                    tvHueStatus.setText("Selected Hue Range is : " + String.valueOf(hueValue1) + "-" +String.valueOf(hueValue2));
                    updateHueDialogTextViews();
                } else {
                    hueValue2 = value;
                    //tv2.setText("Max : " + String.valueOf(value));
                    tvHueStatus.setText("Selected Hue Range is : " + String.valueOf(hueValue1) + "-" +String.valueOf(hueValue2));
                    updateHueDialogTextViews();
                }
            }
        });

        //update the text views with the min, max and selected range of hue values
        tvHueMin.setText("Min : " + hueValueMin);
        tvHueMax.setText("Max : " + hueValueMax);
        tvHueStatus.setText("Selected Hue Range is : " + String.valueOf(hueValue1) + "-" +String.valueOf(hueValue2));

        hueRangeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    hueRangeInclude = false;
                    tvhueIncludeRanges.setText("Select the range(s) to include");
                    updateHueDialogTextViews();
                    tvhueIncludeRanges.setVisibility(View.VISIBLE);
                    hueIncludeRange1.setVisibility(View.VISIBLE);
                    hueIncludeRange2.setVisibility(View.VISIBLE);
                    hueIncludeRange1.setChecked(true);
                    hueIncludeRange2.setChecked(true);
                }
                else {
                    hueRangeInclude = true;
                    tvhueIncludeRanges.setVisibility(View.GONE);
                    hueIncludeRange1.setVisibility(View.GONE);
                    hueIncludeRange2.setVisibility(View.GONE);
                    hueIncludeRange1.setChecked(false);
                    hueIncludeRange2.setChecked(false);
                }
            }
        });

        dialg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!hueRangeInclude){
                    if(hueIncludeRange1.isChecked() && hueIncludeRange2.isChecked()){
                        dialogInterface.cancel();
                        singleOrMultiHueConfirm(hueValue1,hueValue2);
                        dialogInterface.dismiss();
                    }
                }
                else{
                    if(hueIncludeRange1.isChecked()){
                        hueValue2 = checkHueMinValue(hueValue1);
                        hueValue1 = hueValueMin;
                        multiHueRange = false;
                    }
                    else if(hueIncludeRange2.isChecked()){
                        hueValue1 = checkHueMaxValue(hueValue2);
                        hueValue2 = hueValueMax;
                        multiHueRange = false;
                    }
                    String name = "";
                    if ((hueValue1 == 0) && (hueValue2 == 50)) {
                        name = "Wheat";
                    } else {
                        name = "User HueRange : ";
                    }
                    dialogInterface.cancel();
                    String hueString = hueValue1 + "," + hueValue2;
                    print("name is " + name + " and hue is " + hueString);
                    myDB.insertData(name, hueString);
                    toast("Selected range is: " + hueValue1 + " to " + hueValue2);
                    showDialog();
                    dialogInterface.dismiss();
                }
        }});

        dialg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hueValue1 = 0;
                hueValue2 = 50;
                multiHueRange = false;
                showDialog();
                toast("Default Hue range is: "+hueValue1+" to "+hueValue2);
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

    //Functionality - update the text view in Hue Dialog
    //Description :
    //  this function is called to update the text views in the hue dialog whenever there's a change in range seek bar or the include/exclude switch is toggled

    public void updateHueDialogTextViews(){
        if (hueValue1 == 0)
            hueIncludeRange1.setText(hueValueMin + " - " + hueValue2);
        else
            hueIncludeRange1.setText(hueValueMin +" - " + (hueValue1-1));
        if(hueValue2 == 255)
            hueIncludeRange2.setText(hueValue1 +" - " + hueValueMax);
        else
            hueIncludeRange2.setText((hueValue2+1) +" - " + hueValueMax);
    }

    public int checkHueMinValue(int hueValue){
        if(hueValue == 0)
            return 0;
        else
            return hueValue-1;
    }

    public int checkHueMaxValue(int hueValue){
        if(hueValue == 255)
            return 255;
        else
            return hueValue+1;
    }

    public void multiHueDialog(int multiHue1, int multiHue2){
        final AlertDialog.Builder dialg= new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.multiplehue_dialog, (ViewGroup)findViewById(R.id.multiplehue_layoutdialog));
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "AllerDisplay.ttf");
        final TextView tvHueMin1 = (TextView) viewLayout.findViewById(R.id.multiHueMin1);
        final TextView tvHueMax1 = (TextView) viewLayout.findViewById(R.id.multiHueMax1);
        final TextView tvHueMin2 = (TextView) viewLayout.findViewById(R.id.multiHueMin2);
        final TextView tvHueMax2 = (TextView) viewLayout.findViewById(R.id.multiHueMax2);

        final TextView tvHueStatus1 = (TextView) viewLayout.findViewById(R.id.multiHueStatus1);
        final TextView tvHueStatus2 = (TextView) viewLayout.findViewById(R.id.multiHueStatus2);

        final MultiSlider hueRangeSlider1 = (MultiSlider) viewLayout.findViewById(R.id.hueRangeSlider1);
        final MultiSlider hueRangeSlider2 = (MultiSlider) viewLayout.findViewById(R.id.hueRangeSlider2);

        final Switch hueRangeSwitch = (Switch) viewLayout.findViewById(R.id.hueRangeSwitch);
        final TextView tvhueIncludeRanges = (TextView) viewLayout.findViewById(R.id.hueIncludeRanges);
        hueIncludeRange1 = (CheckBox) viewLayout.findViewById(R.id.hueIncludeRange1);
        hueIncludeRange2 = (CheckBox) viewLayout.findViewById(R.id.hueIncludeRange2);

        multiHueValue1=0;
        multiHueValue2=0;
        multiHueValue3=0;
        multiHueValue4=0;

        //dialg.setIcon(android.R.drawable.sym_def_app_icon);
        dialg.setTitle("Hue filter multi range");
        dialg.setView(viewLayout);

        //set the initial settings for the multi range slider for hue values
        hueRangeSlider1.setDrawThumbsApart(true);
        hueRangeSlider1.setStepsThumbsApart(10);
        hueRangeSlider1.setMin(0);
        hueRangeSlider1.setMax(255);
        hueRangeSlider1.repositionThumbs();
        hueRangeSlider2.setDrawThumbsApart(true);
        hueRangeSlider2.setStepsThumbsApart(10);
        hueRangeSlider2.setMin(0);
        hueRangeSlider2.setMax(255);
        hueRangeSlider2.repositionThumbs();

        multiHueValueMin = hueRangeSlider1.getMin();
        multiHueValueMax = hueRangeSlider1.getMax();
        if (multiHue1 != 0 && multiHue2 != 255){
            multiHueValue1 = multiHueValueMin;
            multiHueValue2 = checkHueMinValue(multiHue1);
            multiHueValue3 = checkHueMaxValue(multiHue2);
            multiHueValue4 = multiHueValueMax;
            hueRangeSlider1.clearThumbs();
            hueRangeSlider1.addThumb(multiHueValue1);
            hueRangeSlider1.addThumb(multiHueValue2);
            tvHueStatus1.setText("Selected Hue Range is : " + String.valueOf(multiHueValue1) + "-" +String.valueOf(multiHueValue2));
            hueRangeSlider2.clearThumbs();
            hueRangeSlider2.addThumb(multiHueValue3);
            hueRangeSlider2.addThumb(multiHueValue4);
            tvHueStatus2.setText("Selected Hue Range 2 is : " + String.valueOf(multiHueValue3) + "-" +String.valueOf(multiHueValue4));
        }
        else {
            multiHueValue1 = multiHueValueMin;
            multiHueValue2 = multiHueValueMax;
            multiHueValue3 = multiHueValueMin;
            multiHueValue4 = multiHueValueMax;
        }
        //create a value change listener for the multi range slider for hue values
        hueRangeSlider1.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    multiHueValue1 = value;
                    //tv1.setText("Min : " + String.valueOf(value));
                    tvHueStatus1.setText("Selected Hue Range is : " + String.valueOf(multiHueValue1) + "-" +String.valueOf(multiHueValue2));
                    //updateHueDialogTextViews();
                } else {
                    multiHueValue2 = value;
                    //tv2.setText("Max : " + String.valueOf(value));
                    tvHueStatus1.setText("Selected Hue Range is : " + String.valueOf(multiHueValue1) + "-" +String.valueOf(multiHueValue2));
                    //updateHueDialogTextViews();
                }
            }
        });

        hueRangeSlider2.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    multiHueValue3 = value;
                    //tv2.setText("Max : " + String.valueOf(value));
                    tvHueStatus2.setText("Selected Hue Range is : " + String.valueOf(multiHueValue3) + "-" + String.valueOf(multiHueValue4));
                    //updateHueDialogTextViews();
                } else {
                    multiHueValue4 = value;
                    //tv2.setText("Max : " + String.valueOf(value));
                    tvHueStatus2.setText("Selected Hue Range is : " + String.valueOf(multiHueValue3) + "-" + String.valueOf(multiHueValue4));
                    //updateHueDialogTextViews();
                }
            }
        });
        //update the text views with the min, max and selected range of hue values
        tvHueMin1.setText("Min : " + multiHueValueMin);
        tvHueMax1.setText("Max : " + multiHueValueMax);
        tvHueStatus1.setText("Selected Hue Range 1 is : " + String.valueOf(multiHueValue1) + "-" +String.valueOf(multiHueValue2));
        tvHueMin2.setText("Min : " + multiHueValueMin);
        tvHueMax2.setText("Max : " + multiHueValueMax);
        tvHueStatus2.setText("Selected Hue Range 2 is : " + String.valueOf(multiHueValue3) + "-" +String.valueOf(multiHueValue4));

        dialg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = "";
                if((multiHueValue1==0) && (multiHueValue2==75) && (multiHueValue3==177) && (multiHueValue4==255)){
                    name = "Canola";
                }
                else{
                    name = "Default";
                }
                String hueString = multiHueValue1+","+multiHueValue2+","+multiHueValue3+","+multiHueValue4;
                myDB.insertData(name, hueString);
                toast("Selected ranges are: "+multiHueValue1+" to "+multiHueValue2+" and from "+multiHueValue3+" to "+multiHueValue4);
                multiHueRange = true;
                showDialog();
                dialogInterface.dismiss();
            }
        });

        dialg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hueValue1 = 0;
                hueValue2 = 50;
                multiHueRange = false;
                showDialog();
                toast("Default Hue range is: "+hueValue1+" to "+hueValue2);
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
                        hueValue1 = 0;
                        hueValue2 = 50;
                        multiHueRange = false;
                        break;
                    case 1:
                        str = (String)items[i];
                        multiHueValue1 = 0;
                        multiHueValue2 = 50;
                        multiHueValue3 = 177;
                        multiHueValue4 = 255;
                        multiHueRange = true;
                        break;
                    case 2:
                        str = (String)items[i];
                        hueValue1 = 0;
                        hueValue2 = 50;
                        multiHueRange = false;
                        break;
                    case 3:
                        str = (String)items[i];
                        hueValue1 = 0;
                        hueValue2 = 50;
                        multiHueRange = false;
                        break;
                    case 4:
                        str = (String)items[i];
                        hueValue1 = 0;
                        hueValue2 = 50;
                        multiHueRange = false;
                        break;
                    default:
                        str = (String)items[i];
                        hueValue1 = 0;
                        hueValue2 = 50;
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
                        multiHueDialog(0,255);
                        b.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hueDialog(0,255);
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
