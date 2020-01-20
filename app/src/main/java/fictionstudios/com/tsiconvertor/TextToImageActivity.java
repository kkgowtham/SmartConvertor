package fictionstudios.com.tsiconvertor;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

import es.dmoral.toasty.Toasty;


public class TextToImageActivity extends AppCompatActivity {
    String TAG = "Milcanx_TAG";
    EditText editText;
    int ic,ifont;
    float is;
    private InterstitialAd mInterstitialAd;
    Button viewimage,changefontbtn,changebgcolor,changetextcolor,changetextsize;
    RelativeLayout relativeLayout;
    String[] fonts={"fonts/alexbrush_regular.ttf","fonts/allura_regular.otf","fonts/antonio_bold.ttf","fonts/antonio_light.ttf",
    "fonts/Antonio-Regular.ttf","fonts/Chunkfive.otf","fonts/DancingScript-Regular.otf","fonts/FFF_Tusj.ttf",
    "fonts/GreatVibes-Regular.otf","fonts/KaushanScript-Regular.otf","fonts/Lobster_1.3.otf","fonts/Montserrat-Black.otf",
    "fonts/Montserrat-Italic.otf","fonts/Montserrat-Light.otf","fonts/MontserratAlternates-Black.otf","fonts/MontserratAlternates-Italic.otf",
    "fonts/OpenSans-Bold.ttf","fonts/OpenSans-Regular.ttf","fonts/Pacifico.ttf","fonts/Quicksand-Italic.otf","fonts/Quicksand-LightItalic.otf",
            "fonts/Roboto-Black.ttf","fonts/RobotoCondensed-Bold.ttf","fonts/Sansation-Bold.ttf","fonts/Sansation-Regular.ttf","fonts/Sofia-Regular.otf"
    };
    String[] colour={"#008000","#0000FF","#000000","#808080","#00FF00","#008080","#FFFF00","#C0C0C0","#FFFFFF","#800000","#FF0000","#808000",
            "#00FFFF","#000080","#800080","#FF00FF"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_image);
        ic=0;
        ifont=0;
        is=115;
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
       editText= (EditText) findViewById(R.id.edtext_content);
        viewimage=(Button)findViewById(R.id.view_image_btn);
        relativeLayout= (RelativeLayout) findViewById(R.id.rlyt_outputtingImage);
        changefontbtn=(Button)findViewById(R.id.text_font);
        changebgcolor=(Button)findViewById(R.id.change_bgcolorbtn);
        changetextcolor=(Button)findViewById(R.id.text_color);
        changetextsize=(Button)findViewById(R.id.text_size);
        changebgcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setBackgroundColor(Color.parseColor(colour[ic]));
                ic++;
                if (ic==colour.length-1)
                {
                    ic=0;
                }
            }
        });
        changetextcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setTextColor(Color.parseColor(colour[ic]));
                editText.setHintTextColor(Color.parseColor(colour[ic]));
                ic++;
                if (ic==colour.length-1)
                {
                    ic=0;
                }
            }
        });
       changetextsize.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               editText.setTextSize(is);
               is=is+5;
               if(is>130) {
                   is = 12;
               }
           }
       });
       changefontbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               editText.setTypeface(Typeface.createFromAsset(getAssets(),fonts[ifont]));
               ifont++;
               if (ifont==fonts.length-1)
               {
                   ifont=0;
               }
           }
       });
        Button btn_Create_image = (Button) findViewById(R.id.btn_getImage);
        btn_Create_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (outPuttingImage() != null) {
                    savetoStorage(outPuttingImage());
                } else {
                    Toasty.error(getApplicationContext(), "Couldn't able to create image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_share_Image = (Button) findViewById(R.id.btn_shareImage);
        btn_share_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWriteStoragePermissionGranted();
                isReadStoragePermissionGranted();
                shareImage(ImageSavetoExternalStorageToFolder(outPuttingImage(),getApplicationContext()));
            }
        });
        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment customFragment=new CustomFragment();
                Bundle b=new Bundle();
                Bitmap bm=outPuttingImage();
                b.putParcelable("bitmap",bm);
                customFragment.setArguments(b);
                FragmentTransaction fragmentManager=getFragmentManager().beginTransaction();
                Fragment fragment=getFragmentManager().findFragmentByTag("dialog");
                if(fragment!=null)
                {
                    fragmentManager.remove(fragment);
                }
                fragmentManager.addToBackStack(null);
                customFragment.show(fragmentManager,"dialog");
            }
        });

    }
    private Bitmap outPuttingImage() {

        editText.setCursorVisible(false);
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache(true);

        final Bitmap bmp = Bitmap.createBitmap(relativeLayout.getDrawingCache());

        relativeLayout.setDrawingCacheEnabled(false);

        editText.setCursorVisible(true);
        return bmp;
    }
    private void savetoStorage(Bitmap bm) {
        isWriteStoragePermissionGranted();
        isReadStoragePermissionGranted();
          ImageSavetoExternalStorageToFolder(bm, getApplicationContext());
    }
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }
    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted1");

            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);

                } else {
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                } else {
                }
                break;
        }
    }
    String MAINFOLDER_NAME = "Smart  Converter";
    String FILE_NAME = "Converted_image";
    String MAINROOTDIR = Environment.getExternalStorageDirectory()
            + File.separator + MAINFOLDER_NAME;
    String FULLNAME;
    File FILE;
    private File ImageSavetoExternalStorageToFolder(Bitmap bmp, Context context) {
        Calendar c = Calendar.getInstance();
        Random r = new Random();
        String number = String.valueOf(r.nextInt(1000 - 0) + 1);
        FULLNAME = FILE_NAME + c.get(Calendar.MILLISECOND) + number + ".jpg";
   File pat = new File(MAINROOTDIR + File.separator);
        if (!pat.exists()) {
            pat.mkdirs();
        }
        File file = new File(pat, FULLNAME);
        try {
            OutputStream os = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
            Toasty.success(getApplicationContext(),"Saved to Gallery",Toast.LENGTH_LONG,true).show();
            if(mInterstitialAd.isLoaded())
            {
                mInterstitialAd.show();
            }
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    return FILE = file;
    }
    public  void shareImage(File Imagepath) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Imagepath));
        shareIntent.setType("image/jpg");
        try {
            startActivity(Intent.createChooser(shareIntent,"Select App"));
        } catch (ActivityNotFoundException ex) {
            Toasty.error(getApplicationContext(), "Failed to share", Toast.LENGTH_SHORT,true).show();
        }
    }
}
