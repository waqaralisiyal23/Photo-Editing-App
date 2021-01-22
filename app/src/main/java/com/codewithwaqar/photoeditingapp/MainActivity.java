package com.codewithwaqar.photoeditingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.codewithwaqar.photoeditingapp.databinding.ActivityMainBinding;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_IMAGE_REQUEST_CODE = 45;
    private static final int Edited_IMAGE_REQUEST_CODE = 200;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 32;
    private static final int CAMERA_IMAGE_REQUEST_CODE = 14;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        final AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        binding.adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                binding.adView.loadAd(adRequest);
            }
        });

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
            }
        });

        binding.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
                else{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(data.getData());

            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                    "Photo Editor");

            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                    toolsToHide);
            startActivityForResult(dsPhotoEditorIntent, Edited_IMAGE_REQUEST_CODE);
        }

        if(requestCode == CAMERA_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = getImageUri(photo);

            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);

            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                    "Photo Editor");

            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                    toolsToHide);
            startActivityForResult(dsPhotoEditorIntent, Edited_IMAGE_REQUEST_CODE);
        }

        if(requestCode == Edited_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        }
    }

    private Uri getImageUri(Bitmap bitmap){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", "Description");
        return Uri.parse(path);
    }
}