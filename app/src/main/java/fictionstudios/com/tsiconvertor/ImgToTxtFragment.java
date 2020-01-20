package fictionstudios.com.tsiconvertor;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import es.dmoral.toasty.Toasty;

public class ImgToTxtFragment extends DialogFragment {
    EditText convertedtxt;
    Button copytext,sharetext;
    private InterstitialAd mInterstitialAd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.custom_layout,container,false);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        convertedtxt=view.findViewById(R.id.converted_text);
        copytext=view.findViewById(R.id.copy_text_btn);
        sharetext=view.findViewById(R.id.share_text_btn);
        final String a=getArguments().getString("key");
        convertedtxt.setText(a);
        convertedtxt.setSelection(convertedtxt.getText().length());
        copytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager=(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("clip",a);
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toasty.success(view.getContext(),"Text Copied",Toast.LENGTH_LONG).show();
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        sharetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,a);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,"Choose an app"));
            }
        });
        return view;
    }

}
