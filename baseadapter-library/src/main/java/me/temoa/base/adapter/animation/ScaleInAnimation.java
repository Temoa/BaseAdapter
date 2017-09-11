package me.temoa.base.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
    public Animator[] getAnimator(View v) {
        return new Animator[]{
                ObjectAnimator.ofFloat(v, View.SCALE_X, from, 1),
                ObjectAnimator.ofFloat(v, View.SCALE_Y, from, 1)
        };
    }
}
