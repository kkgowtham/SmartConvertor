package fictionstudios.com.tsiconvertor;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;
import java.util.Queue;

import es.dmoral.toasty.Toasty;

public class TextToSpeechActivity extends AppCompatActivity {

    private TextToSpeech mtts;
    private SeekBar mpitch;
    private SeekBar mspeed;
    private EditText mtext;
    private Button mspeakout;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);
        mtext=(EditText)findViewById(R.id.text);
        mpitch=findViewById(R.id.seek_bar_pitch);
        mspeed=findViewById(R.id.seek_bar_speed);
        mspeakout=findViewById(R.id.speak_out_btn);
        mspeakout.setEnabled(false);
        adView=(AdView)findViewById(R.id.banner_tts);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mtts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    int result=mtts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.d("TTS","Language not supported");
                    }
                    else
                    {
                       mspeakout.setEnabled(true);
                    }
                }
                else
                {
                    Log.d("TTS","Initialization Failed");
                }
            }
        });
        mspeakout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

            public void speak()
                {
                    String text=mtext.getText().toString();
                    if(!text.isEmpty()) {
                        float pitch = (float) mpitch.getProgress() / 50;
                        if (pitch < 0.1) pitch = 0.1f;
                        float speed = (float) mspeed.getProgress() / 50;
                        if (speed < 0.1) speed = 0.1f;
                        mtts.setPitch(pitch);
                        mtts.setSpeechRate(speed);
                        mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(),"Enter Some Text",Toast.LENGTH_SHORT,true).show();
                    }
                }
    @Override
    protected void onDestroy() {
        if(mtts!=null) {
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }
}
