package fictionstudios.com.tsiconvertor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {

    private Button ittbtn,ttibtn,ttsbtn,sttbtn,abtbtn;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this,getString(R.string.admob_pub_id));
        adView=(AdView)findViewById(R.id.banner_main);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        ittbtn=findViewById(R.id.itt_btn);
        ttibtn=findViewById(R.id.tti_btn);
        ttsbtn=findViewById(R.id.tts_btn);
        sttbtn=findViewById(R.id.stt_btn);
        abtbtn=findViewById(R.id.about_btn);
        ittbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImageToTextActivity.class);
                startActivity(intent);
            }
        });
        ttibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TextToImageActivity.class);
                startActivity(intent);
            }
        });
        ttsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TextToSpeechActivity.class);
                startActivity(intent);
            }
        });
        sttbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SpeechToTextActivity.class);
                startActivity(intent);
            }
        });
        abtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }

}
