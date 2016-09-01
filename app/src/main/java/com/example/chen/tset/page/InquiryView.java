package com.example.chen.tset.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.chen.tset.R;

public class InquiryView extends View {
    private Paint paint;
    private RectF oval;
    private int baseColor;


    public InquiryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        baseColor = getResources().getColor(R.color.backgroundblue);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        oval = new RectF(0, 0, viewWidth, viewHeight);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(baseColor);
        canvas.drawArc(oval, 0, 360, true, paint);
    }
}
