package com.codewithwaqar.photoeditingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.codewithwaqar.photoeditingapp.databinding.ActivityResultBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import java.io.File;
import java.io.FileOutputStream;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.myImageView.setImageURI(getIntent().getData());

        final AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2854521211489219/4633112486");
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd.loadAd(adRequest);
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        binding.shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(ResultActivity.this, getIntent().getData().getPath(), Toast.LENGTH_LONG).show();
////                Drawable drawable = binding.myImageView.getDrawable();
////                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
//
//                try {
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getContentResolver().query(getIntent().getData(),
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    File file = new File(picturePath);
////                    FileOutputStream fOut = new FileOutputStream(file);
////                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
////                    fOut.flush();
////                    fOut.close();
////                    file.setReadable(true, false);
//                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() +".provider", file);
//
//                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.setType("image/*");
//
//                    startActivity(Intent.createChooser(intent, "Share image via"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}