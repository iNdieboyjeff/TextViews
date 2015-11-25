/*
 *  Copyright (c) 2015 Jeff Sutton
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package util.android.support.v7.textviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;

import util.android.textviews.R;
import util.android.textviews.shimmer.ShimmerViewBase;
import util.android.textviews.shimmer.ShimmerViewHelper;

/**
 * <p>A {@link AppCompatFontTextView} that implements a Shimmer effect.</p>
 *
 * <p>The shimmer effect is controlled using the {@link util.android.textviews.shimmer.Shimmer Shimmer} class.</p>
 *
 * <p><i>For example:</i>
 * <pre>
 * {@code
 * new Shimmer().setDuration(1500).setStartDelay(4000).setRepeatDelay(5000).start(shimmerTextView);
 * }
 * </pre>
 * </p>
 *
 * <p><b>XML attributes</b></p>
 *
 * <p>See {@link R.styleable#ShimmerView ShimmerView attributes},{@link R.styleable#FontTextView FontTextView attributes},
 * {@link android.R.styleable#TextView TextView attributes}, {@link android.R.styleable#View View Attributes}
 *
 * @attr ref util.android.textviews.R.styleable#ShimmerView_reflectionColor</p>
 *
 * @since 1.2.7
 */
public class AppCompatShimmerTextView extends AppCompatFontTextView implements ShimmerViewBase {

    private static final String LOG_TAG = AppCompatShimmerTextView.class.getSimpleName();

    private ShimmerViewHelper shimmerViewHelper;

    public AppCompatShimmerTextView(Context context) {
        super(context);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), null);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public AppCompatShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), attrs);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public AppCompatShimmerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), attrs);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    @Override
    public float getGradientX() {
        return shimmerViewHelper.getGradientX();
    }

    @Override
    public void setGradientX(float gradientX) {
        shimmerViewHelper.setGradientX(gradientX);
    }

    @Override
    public boolean isShimmering() {
        return shimmerViewHelper.isShimmering();
    }

    @Override
    public void setShimmering(boolean isShimmering) {
        shimmerViewHelper.setShimmering(isShimmering);
    }

    @Override
    public boolean isSetUp() {
        return shimmerViewHelper.isSetUp();
    }

    @Override
    public void setAnimationSetupCallback(ShimmerViewHelper.AnimationSetupCallback callback) {
        shimmerViewHelper.setAnimationSetupCallback(callback);
    }

    @Override
    public int getPrimaryColor() {
        return shimmerViewHelper.getPrimaryColor();
    }

    @Override
    public void setPrimaryColor(int primaryColor) {
        shimmerViewHelper.setPrimaryColor(primaryColor);
    }

    @Override
    public int getReflectionColor() {
        return shimmerViewHelper.getReflectionColor();
    }

    @Override
    public void setReflectionColor(int reflectionColor) {
        shimmerViewHelper.setReflectionColor(reflectionColor);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.onSizeChanged();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (shimmerViewHelper != null) {
            shimmerViewHelper.onDraw();
        }
        super.onDraw(canvas);
    }
}