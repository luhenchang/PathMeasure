package com.example.ls.pathmeasure;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;


/**
 * 作者:王飞
 * 邮箱:1276998208@qq.com
 * create on 2018/7/21 9:48
 */
public class SearchView extends View {
    private Path circle_outside_path;
    private Path circle_inside_path;
    private Paint mPaint;
    private int mViewWidth, mViewHeight;
    private PathMeasure measure;

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private float mAnimaltorValue;
    private ValueAnimator mStartingAnimator;
    private Animator.AnimatorListener mAnimaltorListenner;
    private Handler animtorHandler;
    private Context context;

    public SearchView(Context context) {
        super(context);
        this.context = context;
        initPaint();//初始化画笔
        initPath();//初始化路径
        initAnimalLisenner();//初始化监听
        initHandler();
        initAnimaltor();
        //刚进入动画
        mStartingAnimator.start();
    }


    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
    }

    private void initAnimalLisenner() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimaltorValue = (float) animation.getAnimatedValue();//0-1
                invalidate();
            }
        };
        mAnimaltorListenner = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //这里动画结束监听。
                Message message=Message.obtain();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

    }

    private void initHandler() {
        animtorHandler = new MyHandler(context);
    }

    static class MyHandler extends Handler {
        private final WeakReference<Context> mContext;

        MyHandler(Context context) {
            this.mContext = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mContext!=null){

            }
        }
    }

    private void initAnimaltor() {
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(2000);
        mStartingAnimator.addUpdateListener(mUpdateListener);
        mStartingAnimator.addListener(mAnimaltorListenner);
    }

    private void initPath() {
        circle_inside_path = new Path();
        circle_outside_path = new Path();

        measure = new PathMeasure();
        //内画圆
        RectF rectF = new RectF(-50, -50, 50, 50);
        RectF rectF1 = new RectF(-100, -100, 100, 100);
        //画弧度
        circle_inside_path.addArc(rectF, 45, 357f);
        circle_outside_path.addArc(rectF1, 45, 357f);
        float[] pos = new float[2];
        measure.setPath(circle_outside_path, false);//把圆弧到这个测量里面用来获取把守位置
        measure.getPosTan(0, pos, null);//获得把守位置
        //画把守的线
        circle_inside_path.lineTo(pos[0], pos[1]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        canvas.drawColor(Color.parseColor("#0082D7"));
        measure.setPath(circle_inside_path, false);
        Path des = new Path();
        measure.getSegment(0, mAnimaltorValue * measure.getLength(), des, true);
        canvas.drawPath(des, mPaint);
    }

}
