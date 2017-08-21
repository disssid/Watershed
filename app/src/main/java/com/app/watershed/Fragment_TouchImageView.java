package com.app.watershed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

public class Fragment_TouchImageView extends Fragment {

    Uri imageUri;
    private TouchImageView myTVF;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    static final int PICK_IMAGE = 100;
    private static Context context;
    private static final String TAG = "Arunachala";

    public Fragment_TouchImageView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment__touch_image_view, container, false);
        context= getActivity().getApplicationContext();
        myTVF = (TouchImageView) v.findViewById(R.id.img);
        myTVF.setImageBitmap(null);
        return v;
    }

    public void startCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //take a picture and pass the results (image) along to onActivityResult
        //this kicks up the intent
        //we have passes in startActivityForResult() as we want some info back from the intent and so this..
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //include this code for no crop app


        //if the image captured is correctly sent and the result has no errors
        //super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            myTVF.setImageURI(imageUri);
            //remove this line if there are any inconsistencies
            myTVF.setRotation(90);
             NewMainDrawerActivity activity = (NewMainDrawerActivity)getActivity();
            activity.showDialog();
        }
        else if (requestCode == PICK_IMAGE  && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            myTVF.setImageURI(imageUri);
             NewMainDrawerActivity activity = (NewMainDrawerActivity)getActivity();
            activity.huefeedbackDialog();
            // activity.multiHueDialog();
            //activity.hueDialog();
          //  activity.showDialog();

        }


        //include this code for cropping facility

/*

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            print("Camera requested");
            beginCrop(data.getData());
            print("Crop called and came to next line");
        }
        else if (requestCode == PICK_IMAGE  && resultCode == Activity.RESULT_OK) {
            print("Gallery requested");
            print("REQUESTCROP value is" +Crop.REQUEST_CROP);
            beginCrop(data.getData());
            print("Crop called and came to next line");
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            print("Crop.REQUEST_CROP called");
            handleCrop(resultCode, data);
            print("handleCrop called and came to next line");
        } */
    }

    private void beginCrop(Uri source) {
        print("Crop had begun");
        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
        //Crop.of(source, destination).asSquare().start(getActivity());
        Crop.of(source, destination).asSquare().start(getContext(), Fragment_TouchImageView.this, Crop.REQUEST_CROP);
        print("Crop had ended");
    }

    private void handleCrop(int resultCode, Intent result) {
        print("Came to handleCrop");
        if (resultCode == Activity.RESULT_OK) {
            print("RESULT OK");
            myTVF.setImageURI(Crop.getOutput(result));
            NewMainDrawerActivity activity = (NewMainDrawerActivity)getActivity();
            activity.showDialog();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void print(String s){
        Log.d(TAG, s);
    }



}






