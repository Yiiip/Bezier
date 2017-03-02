package com.lyp.bezier;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by lyp on 2017/3/2.
 */

public class SecondBezierView extends View {

    private float mControlX, mControlY;
    private float mStartX, mStartY;
    private float mEndX, mEndY;
    private Paint mLinePaint, mBezierPaint;
    private Path mBezierPath;

    public SecondBezierView(Context context) {
        this(context, null);
    }

    public SecondBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setColor(Color.parseColor("#454545"));
        mLinePaint.setTextSize(20);

        mBezierPaint = new Paint();
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setDither(true);
        mBezierPaint.setColor(Color.RED);
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeWidth(3);

        mBezierPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mStartX, mStartY, 5, mLinePaint);
        canvas.drawText("起点", mStartX - 10, mStartY - 10, mLinePaint);
        canvas.drawCircle(mEndX, mEndY, 5, mLinePaint);
        canvas.drawText("终点", mEndX - 10, mEndY - 10, mLinePaint);
        canvas.drawCircle(mControlX, mControlY, 5, mLinePaint);
        canvas.drawText("控制点", mControlX - 10, mControlY -10 , mLinePaint);
        canvas.drawLine(mStartX, mStartY, mControlX, mControlY, mLinePaint);
        canvas.drawLine(mEndX, mEndY, mControlX, mControlY, mLinePaint);

        mBezierPath.reset(); //因为不断重绘，path的路径也要重置，不然页面上会显示很多条线
        mBezierPath.moveTo(mStartX, mStartY); //移至起点
        mBezierPath.quadTo(mControlX, mControlY, mEndX, mEndY); //二阶贝塞尔曲线，参数是控制点和终点坐标
        canvas.drawPath(mBezierPath, mBezierPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                mControlX = event.getX();
                mControlY = event.getY();
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                ValueAnimator animX = ValueAnimator.ofFloat(mControlX, (mStartX + mEndX) / 2);
                animX.setDuration(400);
                animX.setInterpolator(new OvershootInterpolator());
                animX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlX = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animX.start();

                ValueAnimator animY = ValueAnimator.ofFloat(mControlY, (mStartY + mEndY) / 2);
                animY.setDuration(400);
                animY.setInterpolator(new OvershootInterpolator());
                animY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlY = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animY.start();
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mStartX = 50;
        mStartY = getMeasuredHeight() / 2;
        mEndX = getMeasuredWidth() - 50;
        mEndY = getMeasuredHeight() / 2;
        mControlX = (mStartX + mEndX) / 2;
        mControlY = (mStartY + mEndY) / 2;
    }
}
