package com.netforceinfotech.tripsplit.tutorial;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.netforceinfotech.tripsplit.R;

import tyrantgit.explosionfield.ExplosionField;


public final class DefaultIntro extends BaseIntro {


    Bitmap icon;
    MaterialDialog dailog;
    ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(R.layout.activity_intro_first));
        addSlide(SampleSlide.newInstance(R.layout.activity_intro_second));
        addSlide(SampleSlide.newInstance(R.layout.activity_intro_three));
        addSlide(SampleSlide.newInstance(R.layout.activity_intro_four));

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);


        mExplosionField = ExplosionField.attach2Window(this);

        mExplosionField.expandExplosionBound(200, 300);



       // loadMainActivity();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        Toast.makeText(getApplicationContext(), "skip", Toast.LENGTH_SHORT).show();
    }

    public void getStarted(View v) {
        loadMainActivity();
    }




    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            });
        }
    }
}
