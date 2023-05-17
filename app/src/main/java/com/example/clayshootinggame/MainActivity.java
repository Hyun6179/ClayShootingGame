package com.example.clayshootinggame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;

import java.lang.invoke.ConstantCallSite;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_gun;
    ImageView iv_bullet;
    ImageView iv_clay;

    double screen_width, screen_height;

    float bullet_width, bullet_height;
    float gun_width, gun_height;
    float clay_width, clay_height;

    float bullet_center_x, bullet_center_y;
    float clay_center_x, clay_center_y;

    double gun_x, gun_y;
    double gun_center_x;

    final int NO_OF_CLAYS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button btnStart = findViewById(R.id.btnStart);
        Button btnStop = findViewById(R.id.btnStop);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        ConstraintLayout layout = findViewById(R.id.layout);
        screen_width = Resources.getSystem().getDisplayMetrics().widthPixels;
        screen_height = Resources.getSystem().getDisplayMetrics().heightPixels;

        iv_gun = new ImageView(this);
        iv_bullet = new ImageView(this);
        iv_clay = new ImageView(this);

        iv_gun.setImageResource(R.drawable.gun);
        iv_gun.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gun_width = iv_gun.getMeasuredWidth();
        gun_height = iv_gun.getHeight();
        layout.addView(iv_gun);

        iv_bullet.setImageResource(R.drawable.bullet);
        iv_bullet.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        bullet_width = iv_bullet.getMeasuredWidth();
        bullet_height = iv_bullet.getHeight();
        layout.addView(iv_bullet);

        iv_clay.setImageResource(R.drawable.clay);
        iv_clay.setScaleX(0.8f);
        iv_clay.setScaleY(0.8f);
        iv_clay.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        clay_width = iv_bullet.getMeasuredWidth();
        clay_height = iv_bullet.getHeight();
        layout.addView(iv_bullet);

        gun_center_x = screen_width;
        gun_x = gun_center_x - gun_width*0.5;
        gun_y = screen_height - gun_height;
        iv_gun.setX((float)gun_x);
        iv_gun.setY((float)gun_y);
        iv_gun.setClickable(true);
        iv_gun.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnStart)
            gameStart();
        else if (view.getId() == R.id.btnStart)
            gameStop();
        else if (view == iv_gun)
            shootingStart();

    }

    private void shootingStart() {
    }

    private void gameStop() {
        finish();
    }

    private void gameStart() {
        ObjectAnimator clay_x = ObjectAnimator.ofFloat(iv_clay, "translationX", -100f, (float)screen_width);
        ObjectAnimator clay_R = ObjectAnimator.ofFloat(iv_clay, "rotation", 0f, 360f*5f);

        clay_x.setRepeatCount(NO_OF_CLAYS - 1);
        clay_R.setRepeatCount(NO_OF_CLAYS - 1);

        clay_x.setDuration(3000); //3초
        clay_R.setDuration(3000); //3초

        clay_x.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                iv_clay.setVisibility(View.VISIBLE);

                ObjectAnimator bullet_SDX = ObjectAnimator.ofFloat(iv_bullet, "scaleX", 1f, 0f);
                ObjectAnimator bullet_SDY = ObjectAnimator.ofFloat(iv_bullet, "scaleY", 1f, 0f);
                ObjectAnimator bullet_Y = ObjectAnimator.ofFloat(iv_bullet, "translationY", 1f, 0f);

                double bullet_X = gun_center_x - 0.5 * bullet_width;
                iv_bullet.setX((float)bullet_X);

                bullet_Y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        bullet_center_x = iv_bullet.getX() + 0.5f * bullet_width;
                        bullet_center_y = iv_bullet.getY() + 0.5f * bullet_height;

                        clay_center_x = iv_clay.getX() + 0.5f * clay_width;
                        clay_center_y = iv_clay.getY() + 0.5f * clay_height;

                        double dist = Math.sqrt(Math.pow(bullet_center_x-clay_center_x, 2) + Math.pow(bullet_center_y-clay_center_y,2));
                        if (dist <=100)
                            iv_clay.setVisibility(View.INVISIBLE);

                    }
                });

                bullet_SDX.start();
                bullet_SDY.start();
                bullet_Y.start();
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                Toast.makeText(getApplicationContext(), "게임종료",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {
                iv_clay.setVisibility(View.VISIBLE);
            }
        });

        clay_x.start();
        clay_R.start();

    }
}