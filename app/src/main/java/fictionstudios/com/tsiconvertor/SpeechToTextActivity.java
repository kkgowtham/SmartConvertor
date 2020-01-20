package fictionstudios.com.tsiconvertor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class SpeechToTextActivity extends AppCompatActivity {

    EditText text_result;
    ImageButton speakbtn;
    Button copy_text,share_text;
    SpeechRecognizer speechRecognizer;
    int REQUEST_CODE=10;
    private AdView adView;
    String s;
    Switch addbtn_switch;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        text_result=findViewById(R.id.text_result_stt);
        speakbtn=findViewById(R.id.tap_to_speak);
        copy_text=(Button)findViewById(R.id.copy_text_stt);
        share_text=(Button)findViewById(R.id.share_text_stt);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adView=(AdView)findViewById(R.id.banner_stt);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        addbtn_switch=(Switch)findViewById(R.id.add_to_text_switch);
        addbtn_switch.setChecked(true);

        copy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=text_result.getText().toString();
                if(!a.isEmpty()) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("text", a);
                    assert clipboardManager != null;
                    clipboardManager.setPrimaryClip(clipData);
                    Toasty.success(getApplicationContext(),"Copied",Toast.LENGTH_LONG,true).show();
                    if(mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                    }
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else
                    Toasty.error(getApplicationContext(),"No Text",Toast.LENGTH_SHORT).show();
            }
        });

        share_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b=text_result.getText().toString();
                if (!b.isEmpty()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, s);
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent,"Choose an app"));
                }
                else
                    Toasty.error(getApplicationContext(),"No Text",Toast.LENGTH_SHORT).show();
            }
        });
        speakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }
    private void speak() {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(resultCode==RESULT_OK && requestCode==REQUEST_CODE)
        {
            ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            s=arrayList.get(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
               if(addbtn_switch.isChecked())
                {
                  String s1=text_result.getText().toString();
                  String s2=s;
                  String s3=s1+" "+s2;
                  text_result.setText(s3);
                  text_result.setSelection(text_result.getText().length());
                }
                else
                {
                    text_result.setText(s);
                    text_result.setSelection(text_result.getText().length());
                }



    }
}
