package com.hzq.app.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author: hezhiqiang
 * @date: 2017/6/6
 * @version:
 * @description:
 */

public class DashBoardView extends View {

    //默认宽高值
    private int defaultSize;

    //进度圆环与透明圆环的距离
    private int arcDistance;

    private int width,height;
    private int defaultPadding;

    //圆环起始角度
    private final static float mStartAngle = 0;

    //圆环总共的角度
    private final static float mTotalAngle = 360;

    private int raduis;

    private Bitmap bitmap; // 小圆点

    //当前点的实际位置
    private float[] pos;

    //当前点的tangent值
    private float[] tan;

    //矩阵
    private Matrix matrix;

    //圆环画笔
    private Paint arcPaint;

    //大刻度
    private Paint scalePaint;
    //小刻度
    private Paint smallScalePaint;

    private Paint textPaint;

    //进度圆环画笔
    private Paint mArcProgressPaint;

    private RectF arcRect;
    //进度矩形
    private RectF mProgressRect;

    //当前进度对应角度
    private float mCurrentAngle = 0f;
    //总进度对应角度
    private float mProgressAngle;

    private Paint mBitmapPaint;


    public DashBoardView(Context context) {
        this(context,null);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        defaultSize = dp2px(200);
        defaultPadding = dp2px(20);
        arcDistance = dp2px(6);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStrokeWidth(dp2px(10));
        arcPaint.setColor(Color.DKGRAY);
        arcPaint.setAlpha(51); //20%
        arcPaint.setStyle(Paint.Style.STROKE);

        scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scalePaint.setStrokeWidth(dp2px(1));
        scalePaint.setColor(Color.WHITE);
//        scalePaint.setAlpha(120);
        scalePaint.setStyle(Paint.Style.STROKE);

        smallScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallScalePaint.setStrokeWidth(dp2px(1));
        smallScalePaint.setColor(Color.WHITE);
//        smallScalePaint.setAlpha(130);
        smallScalePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(sp2px(12));

        //外层进度画笔，在onSizeChanged方法中设置其渐变色
        mArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressPaint.setStrokeWidth(dp2px(2));
        mArcProgressPaint.setColor(Color.WHITE);
        mArcProgressPaint.setAlpha(120);
        mArcProgressPaint.setStyle(Paint.Style.STROKE);

        //小圆点画笔
        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.icon_customer_insight_circle);
        pos = new float[2];
        tan = new float[2];
        matrix = new Matrix();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(arcRect,mStartAngle,mTotalAngle,false,arcPaint);
        drawScale(canvas);
        drawText(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas){
        Path path = new Path();
        //画线
        path.addArc(mProgressRect,-90,mCurrentAngle);
        canvas.drawPath(path,mArcProgressPaint);

        //绘制进度圆点
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 1, pos, tan);
        matrix.reset();
        matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);

        canvas.drawBitmap(bitmap, matrix, mBitmapPaint);

    }

    public void setDegree(int d){
        mProgressAngle = d;
        ValueAnimator mAngleAnim = ValueAnimator.ofFloat(mCurrentAngle, mProgressAngle);
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(2000);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentAngle = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mAngleAnim.start();
    }

    private String[] text = {"Ⅲ","Ⅵ","Ⅸ","Ⅻ"};
    private void drawText(Canvas canvas){
        canvas.save();
        float degree= (float) (360 / 4.0);
        canvas.rotate(degree,raduis,raduis);
        for (int i = 0; i < 4; i++) {
            canvas.drawText(text[i],raduis - dp2px(6),defaultPadding + arcDistance + dp2px(30),textPaint);
            canvas.rotate(degree,raduis,raduis);
        }
        canvas.restore();
    }

    //画刻度
    private void drawScale(Canvas canvas){
        canvas.save();
        //旋转画布
        float degree = (float)(360 / 60.0);
//        canvas.rotate(degree,raduis,raduis);
        //计算刻度的起止点
        int smsDst = (int)(defaultPadding + arcDistance + arcPaint.getStrokeWidth() / 2 - 1);
        int smeDst = smsDst + dp2px(3);
        for (int i = 0; i < 60; i++) {
            if(i % 5 == 0){
                canvas.drawLine(raduis, smsDst, raduis, smeDst + dp2px(5), scalePaint);
            }else {
                canvas.drawLine(raduis, smsDst, raduis, smeDst, smallScalePaint);
            }
            canvas.rotate(degree,raduis,raduis);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveMeasured(widthMeasureSpec,defaultSize),
                resolveMeasured(widthMeasureSpec,defaultSize));
    }

    private int resolveMeasured(int measureSpec,int defaultSize){
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode){
            case MeasureSpec.AT_MOST:
                //设置wrap_content的默认值
                return Math.min(specSize,defaultSize);
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.UNSPECIFIED:
                return specSize;
            default:
                return defaultSize;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        raduis = width / 2;

        mProgressRect = new RectF(
                defaultPadding, defaultPadding,
                width - defaultPadding, height - defaultPadding);

        arcRect = new RectF(defaultPadding + arcDistance,
                defaultPadding + arcDistance,
                width - defaultPadding - arcDistance,
                height - defaultPadding - arcDistance);
    }

    public int dp2px(int values) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

    public int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
