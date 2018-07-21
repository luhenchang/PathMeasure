package com.example.ls.pathmeasure

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import java.lang.ref.WeakReference
import java.lang.reflect.Array.getLength
import java.lang.reflect.Array.getLength


/**
 *作者:王飞
 *邮箱:1276998208@qq.com
 *create on 2018/7/20 11:09
 */
class CustomView : View {
    private lateinit var circle_outside_path: Path
    private lateinit var circle_inside_path: Path
    private lateinit var mPaint: Paint
    private var mViewWidth: Int = 0//原始数据类型不能lateinit修饰
    private var mViewHeight: Int = 0
    private lateinit var measure: PathMeasure
    private lateinit var mUpdateListener: ValueAnimator.AnimatorUpdateListener;
    private var mAnimaltorValue: Float = 0.0f;
    private lateinit var mAnimaltorListenner: Animator.AnimatorListener
    private lateinit var animtorHandler: Handler
    private lateinit var mStartingAnimator: ValueAnimator
    private lateinit var mSearchingAnimator: ValueAnimator
    private lateinit var mEndingAnimator: ValueAnimator
    // 当前的状态(非常重要)
    public var mCurrentState = State.NONE

    // 这个视图拥有的状态
    enum class State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    private var count: Int = 0

    // 判断是否已经搜索结束
    public var isOver = false

    constructor(context: Context) : super(context) {
        initPaint()//初始化画笔
        initPath()//初始化路径
        initAnimalLisenner()//初始化监听
        initHandler()
        initAnimaltor()
        // 进入开始动画
        mCurrentState = State.STARTING
        //刚进入动画
        mStartingAnimator.start()

    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 16f
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
    }


    private fun initAnimalLisenner() {
        mUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
            mAnimaltorValue = animation.animatedValue as Float//0-1
            invalidate()
        }
        mAnimaltorListenner = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                //这里动画结束监听。
                val message = Message.obtain()
                message.what = 1001
                animtorHandler.sendMessage(message);

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }

    }


    private fun initHandler() {
        animtorHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (mCurrentState) {
                    State.STARTING -> {
                        // 从开始动画转换好搜索动画
                        isOver = false
                        mCurrentState = State.SEARCHING
                        mStartingAnimator.removeAllListeners()
                        mSearchingAnimator.start()
                    }
                    State.SEARCHING -> if (!isOver) {  // 如果搜索未结束 则继续执行搜索动画
                        mSearchingAnimator.start()
                        count++
                        if (count > 2) {       // count大于2则进入结束状态
                            isOver = true
                        }
                    } else {        // 如果搜索已经结束 则进入结束动画
                        mCurrentState = State.ENDING
                        mEndingAnimator.start()
                    }
                    State.ENDING ->
                        // 从结束动画转变为无状态
                        mCurrentState = State.NONE
                }
            }
        }
    }


    private fun initAnimaltor() {
        mStartingAnimator = ValueAnimator.ofFloat(0F, 1F).setDuration(2000)
        mSearchingAnimator = ValueAnimator.ofFloat(0F, 1F).setDuration(2000);
        mEndingAnimator = ValueAnimator.ofFloat(0F, 1F).setDuration(2000);
        mStartingAnimator.addUpdateListener(mUpdateListener)
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);


        mStartingAnimator.addListener(mAnimaltorListenner)
        mSearchingAnimator.addListener(mAnimaltorListenner);
        mEndingAnimator.addListener(mAnimaltorListenner);
    }

    private fun initPath() {
        circle_inside_path = Path()
        circle_outside_path = Path()

        measure = PathMeasure()
        //内画圆
        val rectF = RectF(-50f, -50f, 50f, 50f)
        val rectF1 = RectF(-100f, -100f, 100f, 100f)
        //画弧度
        circle_inside_path.addArc(rectF, 45f, 357f)
        circle_outside_path.addArc(rectF1, 45f, 357f)
        val pos = FloatArray(2)
        measure.setPath(circle_outside_path, false)//把圆弧到这个测量里面用来获取把守位置
        measure.getPosTan(0f, pos, null)//获得把守位置
        //画把守的线
        circle_inside_path.lineTo(pos[0], pos[1])
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate((mViewWidth / 2).toFloat(), (mViewHeight / 2).toFloat())
        canvas.drawColor(Color.parseColor("#0082D7"))

        when (mCurrentState) {
            State.NONE -> canvas.drawPath(circle_inside_path, mPaint);
            State.STARTING -> {
                measure.setPath(circle_inside_path, false)
                val dst = Path()
                measure.getSegment(measure.getLength() * mAnimaltorValue, measure.getLength(), dst, true);
                canvas.drawPath(dst, mPaint);
            }
            State.SEARCHING -> {
                measure.setPath(circle_inside_path, false)
                val dst2 = Path()
                val stop = measure.getLength() * mAnimaltorValue
                val start = (stop - (0.5F - Math.abs(mAnimaltorValue - 0.5F)) * 200F) as Float
                measure.getSegment(start, stop, dst2, true)
                canvas.drawPath(dst2, mPaint)
            }
            State.ENDING -> {
                measure.setPath(circle_inside_path, false)
                val dst3 = Path()
                measure.getSegment(0F,measure.getLength() * mAnimaltorValue, dst3, true)
                canvas.drawPath(dst3, mPaint)
            }
        }

    }

}

