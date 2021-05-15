package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private TextView splashText;
    private ImageView splashImage;
    private static int GecisSuresi = 4000;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashpage);
        splashImage = findViewById(R.id.splashlogo);
        splashText = findViewById(R.id.splashText);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        splashText.startAnimation(animation);
        splashImage.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent gecis = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(gecis);
                finish();
            }
        },GecisSuresi);
    }
}
