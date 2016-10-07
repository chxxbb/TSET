package com.example.chen.tset.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class NoteEditor extends EditText {
    /**
     * 提前需要画的线的条数
     */
    private int drawLine = 5;
    /**
     * 为了不让指针压线太明显，将线下移的像素，可根据字体大小和行间距自己调整
     */
    public int lineDis = 8;
    private Rect mRect;
    private Paint mPaint;

    public NoteEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(android.graphics.Color.parseColor("#bbbbbb"));
    }

    public void setNotesMinLines(int lines) {
        this.drawLine = lines;
        setMinLines(lines);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = getLineCount();
        Rect r = mRect;
        Paint paint = mPaint;
        int basicline = 0;
        //第一次画第一条线。以后每次输入，换行时仍然检测，继续画线
        for (int i = 0; i < count; i++) {
            int baseline = getLineBounds(i, r);
            basicline = baseline;
            canvas.drawLine(r.left, baseline + lineDis, r.right, baseline + lineDis, paint);
        }
        //根据判定条件，画出固定条数的线,从第二套开始画
        if (count < drawLine) {
            for (int j = 1; j < drawLine; j++) {
                int baseline = basicline + j * getLineHeight();
                canvas.drawLine(r.left, baseline + lineDis, r.right, baseline + lineDis, paint);
            }
        }
        super.onDraw(canvas);
    }

}
