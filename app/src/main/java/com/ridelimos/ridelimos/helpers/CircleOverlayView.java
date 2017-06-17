package com.ridelimos.ridelimos.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.ridelimos.ridelimos.R;

/**
 * Created by hiren on 10/01/16.
 */
public class CircleOverlayView extends LinearLayout {
    private Bitmap bitmap;

    public CircleOverlayView(Context context) {
        super(context);
    }

    public CircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected void createWindowFrame() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setAlpha(200);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);


        int dpSize =  30;
        DisplayMetrics dm = getResources().getDisplayMetrics() ;
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        mStrokePaint.setStrokeWidth(strokeWidth);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(Color.RED);
        mStrokePaint.setStrokeWidth(2);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = getResources().getDimensionPixelSize(R.dimen.radius);
        osCanvas.drawCircle(centerX, centerY, radius, paint);
//        osCanvas.drawCircle(centerX, centerY, radius, mStrokePaint);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }
}