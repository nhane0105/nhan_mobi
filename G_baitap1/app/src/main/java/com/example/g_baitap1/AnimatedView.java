package com.example.g_baitap1;

import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class AnimatedView extends View {
    private static final int BASE_STEP = 10;
    private static final long FRAME_DELAY_MS = 30;

    private static final int RADIUS = 50;

    private final Paint paint;
    private int x = RADIUS;
    private int direction = 1;
    private float speedMultiplier = 1f;

    public AnimatedView(Context context) {
        super(context);
        paint = createPaint();
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = createPaint();
    }

    public AnimatedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = createPaint();
    }

    private Paint createPaint() {
        Paint p = new Paint();
        p.setColor(Color.RED);
        return p;
    }

    public void setSpeedMultiplier(float multiplier) {
        speedMultiplier = Math.max(0.25f, multiplier);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0 || getHeight() == 0) {
            postInvalidateDelayed(FRAME_DELAY_MS);
            return;
        }

        int cy = Math.max(RADIUS, getHeight() / 2);
        canvas.drawCircle(x, cy, RADIUS, paint);

        int step = Math.max(1, Math.round(BASE_STEP * speedMultiplier));
        x += step * direction;

        int maxX = getWidth() - RADIUS;
        if (maxX < RADIUS) {
            x = Math.min(RADIUS, getWidth() / 2);
        } else if (x >= maxX) {
            x = maxX;
            direction = -1;
        } else if (x <= RADIUS) {
            x = RADIUS;
            direction = 1;
        }

        postInvalidateDelayed(FRAME_DELAY_MS);
    }
}
