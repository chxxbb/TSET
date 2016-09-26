package com.example.chen.tset.Utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class  BuileGestureExt  {
    public static final int GESTURE_UP=0;
    public static final int GESTURE_DOWN=1;
    public static final int GESTURE_LEFT=2;
    public static final int GESTURE_RIGHT=3;
    private OnGestureResult onGestureResult;
    private Context mContext;
    public BuileGestureExt(Context c, OnGestureResult onGestureResult) {
        this.mContext=c;
        this.onGestureResult=onGestureResult;
        screen = GestureUtils.getScreenPix(c);
    }
    public GestureDetector Buile() {
        return new GestureDetector(mContext, onGestureListener);
    }

    private GestureUtils.Screen screen;
    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            //限制必须得划过屏幕的1/4才能算划过
            float x_limit = screen.widthPixels / 4;
            float y_limit = screen.heightPixels / 4;
            float x_abs = Math.abs(x);
            float y_abs = Math.abs(y);
            if(x_abs >= y_abs){
                //gesture left or right
                if(x > x_limit || x < -x_limit){
                    if(x>0){
                        //right
                        doResult(GESTURE_RIGHT);
                    }else if(x<=0){
                        //left
                        doResult(GESTURE_LEFT);
                    }
                }
            }else{
                //gesture down or up
                if(y > y_limit || y < -y_limit){
                    if(y>0){
                        //down
                        doResult(GESTURE_DOWN);
                    }else if(y<=0){
                        //up
                        doResult(GESTURE_UP);
                    }
                }
            }
            return true;
        }

    };
    public void doResult(int result)
    {
        if(onGestureResult!=null)
        {
            onGestureResult.onGestureResult(result);
        }
    }

    public interface OnGestureResult
    {
        public void onGestureResult(int direction);
    }
}
