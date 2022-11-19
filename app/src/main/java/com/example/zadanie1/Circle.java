package com.example.zadanie1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class Circle extends View {
    private Paint paint = null;
    private float radius1, radius2, x, y, txDiff, tyDiff;
    private boolean isMoving;
    private long limit1, limit2;

    public Circle(Context context) {
        super(context);
        init(null);
    }

    public Circle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        if (attrs == null)
            return;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Circle);
        radius1 = typedArray.getDimension(R.styleable.Circle_movable_circle_radius, 50);
        radius2 = typedArray.getDimension(R.styleable.Circle_non_movable_circle_radius, 150);
        typedArray.recycle();

        limit1 = Math.round(Math.pow(radius1, 2));
        limit2 = Math.round(Math.pow(radius2 - radius1, 2));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Math.signum(x) == 0 || Math.signum(y) == 0) {
            x = getWidth() / 2f;
            y = getHeight() / 2f;
        }

        canvas.drawCircle(x, y, radius1, paint);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tx = event.getX();
        float ty = event.getY();

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        long px1 = Math.round(Math.pow(tx - x, 2));
        long py1 = Math.round(Math.pow(ty - y, 2));

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (px1 + py1 <= limit1) {
                isMoving = true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isMoving = false;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            long px2 = Math.round(Math.pow(tx+txDiff - cx, 2));
            long py2 = Math.round(Math.pow(ty+tyDiff - cy, 2));

            if (isMoving) {
                if (px2 + py2 <= limit2) {
                    x = tx+txDiff;
                    y = ty+tyDiff;
                    invalidate();
                    requestLayout();
                }
                txDiff = x-event.getX();
                tyDiff = y-event.getY();
            }
        }

        return true;
    }
}