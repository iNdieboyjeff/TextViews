package util.android.textviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;

import util.android.textviews.shimmer.ShimmerViewBase;
import util.android.textviews.shimmer.ShimmerViewHelper;

/**
 * <p>A {@link FontTextView} that implements a Shimmer effect.</p>
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
 * <p>See {@link util.android.textviews.R.styleable#ShimmerView ShimmerView attributes},{@link util.android.textviews.R.styleable#FontTextView FontTextView attributes},
 * {@link android.R.styleable#TextView TextView attributes}, {@link android.R.styleable#View View Attributes}
 *
 * @attr ref util.android.textviews.R.styleable#ShimmerView_reflectionColor</p>
 *
 * @since 1.2.5
 */
public class ShimmerTextView extends FontTextView implements ShimmerViewBase {

    private static final String LOG_TAG = ShimmerTextView.class.getSimpleName();

    private ShimmerViewHelper shimmerViewHelper;

    public ShimmerTextView(Context context) {
        super(context);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), null);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public ShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), attrs);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public ShimmerTextView(Context context, AttributeSet attrs, int defStyle) {
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