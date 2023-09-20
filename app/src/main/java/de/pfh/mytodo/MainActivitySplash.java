package de.pfh.mytodo;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

//https://developer.android.com/develop/ui/views/launch/splash-screen
public class MainActivitySplash extends AppCompatActivity {
    //Ladebildschirm bevor die App vollständig geöffnet ist. Logo. (Ästhetisch wenn es funktioniert)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);
        getSupportActionBar().hide(); //Actionbatr ausblenden

        final Intent i = new Intent(MainActivitySplash.this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        },1000);
    }
}