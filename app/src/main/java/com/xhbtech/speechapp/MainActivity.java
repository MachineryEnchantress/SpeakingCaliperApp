package com.xhbtech.speechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech toSpeech;
    EditText textEt;
    RelativeLayout rootView;
    boolean isKeyboardShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEt = findViewById(R.id.textEt);
        rootView  = findViewById(R.id.rootView);

        // Init TextToSpeech
        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = toSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "This language is not supported!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        toSpeech.setPitch(0.6f);
                        toSpeech.setSpeechRate(1.0f);
                    }
                }
            }
        });

        textEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //  Log.i(TAG,"Enter pressed");
                    speak();
                }
                return false;
            }
        });

    }


    private void speak() {
        String text = textEt.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        textEt.setHint(text);
        textEt.getText().clear();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            textEt.setTextSize(90);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textEt.setTextSize(130);
        } else
            Log.w("tag", "other: " + orientation);

        Log.w("tag", "other Keyboard: " + newConfig.hardKeyboardHidden);


    }

    //check current orientation when this activity start
    private void getCurrentOrientation() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            textEt.setTextSize(90);
        } else {
            textEt.setTextSize(130);

        }
    }

    @Override
    protected void onDestroy() {
        if (toSpeech != null) {
            toSpeech.stop();
            toSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentOrientation();
    }

}
