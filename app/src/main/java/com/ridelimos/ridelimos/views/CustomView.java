package com.ridelimos.ridelimos.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class CustomView extends View {
    Paint paint;
    int radius = 0;

    public CustomView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
    }

    public void updateView(int radius) {
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }
}