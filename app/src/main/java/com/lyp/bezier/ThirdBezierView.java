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

public class ThirdBezierView extends View {

    private float mStartX, mStartY;
    private float mEndX, mEndY;
    private float mControlX1, mControlY1;
    private float mControlX2, mControlY2;

    private Paint mLinePaint, mBezierPaint;
    private Path mBezierPath;

    private boolean mIsSecondPoint = false;

    public ThirdBezierView(Context context) {
        this(context, null);
    }

    public ThirdBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThirdBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mStartX = 50;
        mStartY = getMeasuredHeight() / 2;
        mEndX = getMeasuredWidth() - 50;
        mEndY = getMeasuredHeight() / 2;
        mControlX1 = (mStartX + mEndX) / 3;
        mControlY1 = (mStartY + mEndY) / 2;
        mControlX2 = (mStartX + mEndX) * 2 / 3;
        mControlY2 = (mStartY + mEndY) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mStartX, mStartY, 5, mLinePaint);
        canvas.drawText("起点", mStartX - 10, mStartY - 10, mLinePaint);

        canvas.drawCircle(mEndX, mEndY, 5, mLinePaint);
        canvas.drawText("终点", mEndX - 10, mEndY - 10, mLinePaint);

        canvas.drawCircle(mControlX1, mControlY1, 5, mLinePaint);
        canvas.drawText("控制点1", mControlX1 - 10, mControlY1 - 10, mLinePaint);

        canvas.drawCircle(mControlX2, mControlY2, 5, mLinePaint);
        canvas.drawText("控制点2", mControlX2 - 10, mControlY2 - 10, mLinePaint);

        canvas.drawLine(mStartX, mStartY, mControlX1, mControlY1, mLinePaint);
        canvas.drawLine(mControlX1, mControlY1, mControlX2, mControlY2, mLinePaint);
        canvas.drawLine(mControlX2, mControlY2, mEndX, mEndY, mLinePaint);

        mBezierPath.reset();
        mBezierPath.moveTo(mStartX, mStartY);
        mBezierPath.cubicTo(mControlX1, mControlY1, mControlX2, mControlY2, mEndX, mEndY); //三阶贝塞尔曲线，参数是控制点1、控制点2和终点坐标
        canvas.drawPath(mBezierPath, mBezierPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) { //多点触控
            case MotionEvent.ACTION_POINTER_DOWN:
                mIsSecondPoint = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mIsSecondPoint = false;
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                mControlX1 = event.getX(0);
                mControlY1 = event.getY(0);
                if (mIsSecondPoint) {
                    mControlX2 = event.getX(1);
                    mControlY2 = event.getY(1);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                ValueAnimator animX1 = ValueAnimator.ofFloat(mControlX1, (mStartX + mEndX) / 3);
                animX1.setDuration(400);
                animX1.setInterpolator(new OvershootInterpolator());
                animX1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlX1 = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animX1.start();

                ValueAnimator animY1 = ValueAnimator.ofFloat(mControlY1, (mStartY + mEndY) / 2);
                animY1.setDuration(400);
                animY1.setInterpolator(new OvershootInterpolator());
                animY1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlY1 = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animY1.start();

                ValueAnimator animX2 = ValueAnimator.ofFloat(mControlX2, (mStartX + mEndX) * 2 / 3);
                animX2.setDuration(400);
                animX2.setInterpolator(new OvershootInterpolator());
                animX2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlX2 = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animX2.start();

                ValueAnimator animY2 = ValueAnimator.ofFloat(mControlY2, (mStartY + mEndY) / 2);
                animY2.setDuration(400);
                animY2.setInterpolator(new OvershootInterpolator());
                animY2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlY2 = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animY2.start();

                break;
        }
        return true;
    }
}
