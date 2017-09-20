package me.temoa.base.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * Created by Lai
 * on 2017/9/11 17:58
 */

public class ScaleInAnimation implements BaseAnimation {

    private static float DEFAULT_FROM_SCALE = 0.5F;
    private float from;

    public ScaleInAnimation() {
        this(DEFAULT_FROM_SCALE);
    }

    public ScaleInAnimation(float fromScale) {
        from = fromScale;
    }

    @Override
    public Animator getAnimator(View v) {
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat(View.SCALE_X, from, 1F);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat(View.SCALE_Y, from, 1F);
        return ObjectAnimator.ofPropertyValuesHolder(v, holder1, holder2);
    }
}
