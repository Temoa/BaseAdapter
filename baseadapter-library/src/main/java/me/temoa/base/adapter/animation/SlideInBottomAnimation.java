package me.temoa.base.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Lai
 * on 2017/9/11 17:56
 */

public class SlideInBottomAnimation implements BaseAnimation {

    @Override
    public Animator getAnimator(View v) {
        return ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, v.getRootView().getHeight(), 0);
    }
}
