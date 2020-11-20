package com.serosoft.academiassu.Widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.serosoft.academiassu.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ajitmaya on 21/06/16.
 */
public class PercentageInCircle extends androidx.appcompat.widget.AppCompatTextView{

    Context context;
    public PercentageInCircle(Context context) {
        super(context);
        this.context=context;
    }

    public PercentageInCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentageInCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        int delta = 5;
        int percent =  getNumber(this.getText().toString());

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h >= w) ? h : w);

        this.setHeight(diameter);
        this.setWidth(diameter);

        int arcSize = ((diameter/2) - (delta / 2)) * 2;
        mPaint.setColor(getResources().getColor(R.color.colorGreen));
        this.setTextColor(getResources().getColor(R.color.colorGreen));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

        RectF box = new RectF(delta,delta,arcSize,arcSize);
        float sweep = 360 * percent * 0.01f;
        canvas.drawArc(box, 270, sweep, false, mPaint);
        super.onDraw(canvas);
    }

    public static int getNumber(String str) {
        int number = 0;
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        while (m.find()) {
            number=Integer.parseInt(m.group());
        }
        return number;
    }
}
