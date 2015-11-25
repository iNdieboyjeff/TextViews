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

package util.android.textviews;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * <p>A custom TextView that displays text vertically - i.e. rotated +/- 90-degrees from a
 * standard TextView</p>
 *
 * @since 1.1.3
 * @author Jeff Sutton
 */
public class VerticalTextView extends TextView {

    private static final String LOG_TAG = VerticalTextView.class.getSimpleName();

    private final boolean topDown;

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
            topDown = false;
        } else
            topDown = true;

        // We need to remap the padding so that values apply to the correct sides.  This is due
        // to the fact that we are rotating a view.
        if (topDown) {
            this.setPadding(getPaddingTop(), getPaddingRight(), getPaddingBottom(), getPaddingLeft());
        } else {
            this.setPadding(getPaddingBottom(), getPaddingLeft(), getPaddingTop(), getPaddingRight());
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (topDown) {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        } else {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        }


        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }

}
