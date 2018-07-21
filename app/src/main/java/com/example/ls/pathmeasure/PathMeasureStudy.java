package com.example.ls.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 作者:王飞
 * 邮箱:1276998208@qq.com
 * create on 2018/7/20 11:10
 */
public class PathMeasureStudy extends View {
    private int mViewWidth;
    private int mViewHight;
    private  Paint paint;
    public PathMeasureStudy(Context context) {
        super(context);
        initPaint();
    }
    private void initPaint() {

        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
    }

    public PathMeasureStudy(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathMeasureStudy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHight=h;
        mViewWidth=w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth/2,mViewHight/2);
        canvas.drawColor(Color.GREEN);
        //TODO-------------------------------------PathMeasure---------------------------------start
        /*Path path=new Path();
        path.lineTo(0,mViewHight/4);
        path.lineTo(mViewWidth/4,mViewHight/4);
        path.lineTo(mViewWidth/4,0);


        PathMeasure measure=new PathMeasure(path,false);
        PathMeasure measure1=new PathMeasure(path,true);//这个是强制关闭，但是长度增加了。没有显示。
        Log.i("rickey=",measure.getLength()+"");
        Log.i("rickey1=",measure1.getLength()+"");*/
        //07-20 14:15:59.742 19255-19255/com.example.ls.pathmeasure I/rickey=: 1116.0
        //07-20 14:15:59.742 19255-19255/com.example.ls.pathmeasure I/rickey1=: 1386.0

        //canvas.drawPath(path,paint);
        //-------------------------------------PathMeasure---------------------------------end

        //TODO--------------------------------------mearsure.getSegment----------------------------sart
       /* Path path=new Path();

        //path.addRect(-100,-100,100,100,Path.Direction.CW);
        path.addRect(-200,-200,200,200,Path.Direction.CW);
        PathMeasure measure=new PathMeasure(path,false);
        float length=measure.getLength();
        Log.i("length=",length+"");
        canvas.drawPath(path,paint);       //硬件加速和路径绘制有关系。这里需要关闭硬件加速。

        //储存截取的路径
        Path dst=new Path();
        dst.lineTo(-300,-300);
        measure.getSegment(222,666,dst,true);
        paint.setColor(Color.YELLOW);
        canvas.drawPath(dst,paint);*/
        //--------------------------------------mearsure.getSegment----------------------------end

        //TODO-------------------------------------mearsure.getPosTan-------------------------start
        Path path=new Path();
        path.addCircle(0,0,300, Path.Direction.CW);

        PathMeasure measure=new PathMeasure(path,false);
        float[] Pos=new float[2];
        float[] Tan=new float[2];
        measure.getPosTan(measure.getLength()/8,Pos,Tan);
        Log.e("Pos","pos="+Pos[0]+"pos1="+Pos[1]);
        Log.e("Tan","Tan="+Tan[0]+"pos1="+Tan[1]);

        canvas.drawPath(path,paint);

    }
}
