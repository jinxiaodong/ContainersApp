package com.project.container.containersapp.frame.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/***
 * 自定义Dialog
 */
public class LoadingDialog extends View {
    private double height;                    //屏幕高
    private double width;                    //屏幕宽
    private double xCircle;                    //圆心x
    private double yCircle;                    //圆心y
    private double startAngle = 0;            //开始角度
    private double rCircleMax;                //大圆半径
    private double rCircleSma;                //小圆半径
    private double widthLine;                //线宽
    private Paint paint;
    private int[] Red, Green, Blue;
    private String colors[];
    private int windowDensityDpi;            //像素密度
    private ScheduledExecutorService scheduledExecutorService;    //定时任务
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //设置当前页面
            invalidate();
        }

    };

    public LoadingDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public LoadingDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public LoadingDialog(Context context) {
        super(context);

        Red = new int[]{200, 188, 176, 164, 152, 141, 129, 129, 129, 129, 128, 128};
        Green = new int[]{200, 188, 176, 164, 152, 141, 129, 129, 129, 129, 128, 128};
        Blue = new int[]{200, 188, 176, 164, 152, 141, 129, 129, 129, 129, 128, 128};

        colors = new String[]{
                "#C8C8C8", "#BCBCBC", "#B0B0B0", "#A4A4A4",
                "#989898", "#8D8D8D", "#818181", "#818181",
                "#818181", "#818181", "#808080", "#808080"};
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 150, 150, TimeUnit.MILLISECONDS);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        windowDensityDpi = metric.densityDpi;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //初始化
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        xCircle = width / 2;
        yCircle = height / 2;
        if (windowDensityDpi > 320) {
            rCircleMax = 35.00 * 1.2;
            rCircleSma = 20.00 * 1.3;
            widthLine = 6.00 * 1.2;
        } else {
            rCircleMax = 35;
            rCircleSma = 20;
            widthLine = 6;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        double angle = startAngle;
        for (int i = 0; i < colors.length; i++) {

            double startX = xCircle - getXbyAngle(angle, (int) rCircleSma);
            double startY = yCircle + getYbyAngle(angle, (int) rCircleSma);
            double stopX = xCircle - getXbyAngle(angle, (int) rCircleMax);
            double stopY = yCircle + getYbyAngle(angle, (int) rCircleMax);

            paint = new Paint();
            paint.setColor(Color.parseColor(colors[i]));
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(7);
            paint.setAntiAlias(true); //去锯齿
            canvas.drawLine((float) startX, (float) startY, (float) stopX, (float) stopY, paint);

            paint = new Paint();
            paint.setColor(Color.parseColor(colors[i]));
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true); //去锯齿
            canvas.drawCircle((float) stopX, (float) stopY, (float) (widthLine / 2 - 0.8), paint);

            paint = new Paint();
            paint.setColor(Color.parseColor(colors[i]));
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true); //去锯齿
            canvas.drawCircle((float) startX, (float) startY, (float) (widthLine / 2 - 0.8), paint);

//			Log.i("", "执行次数1111111----"+i+"----stopX="+stopX+"----stopY="+stopY+"----angle="+angle);
            angle = angle + 30;
        }
    }

    /**
     * 根据斜边和角度计算邻边
     */
    private int getYbyAngle(double Angle, int hypotenuse) {
        int y = (int) (hypotenuse * Math.cos(Angle * Math.PI / 180));
        return y;
    }

    /**
     * 根据斜边和角度计算对边
     */
    private int getXbyAngle(double Angle, int hypotenuse) {
        int x = (int) (hypotenuse * Math.sin(Angle * Math.PI / 180));
        return x;
    }

    //定时任务线程
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            startAngle = startAngle + 30;
            if (startAngle > 360) {
                startAngle = startAngle - 360;
            }
            handler.obtainMessage().sendToTarget();
        }
    }

}
