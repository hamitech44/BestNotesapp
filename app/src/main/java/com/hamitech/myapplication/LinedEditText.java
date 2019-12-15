package com.hamitech.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

public class LinedEditText extends AppCompatEditText {
private Rect mrect;
private Paint mpaint;
    public LinedEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
        mrect= new Rect();
        mpaint= new Paint();
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeWidth(2);
        mpaint.setColor(0xFFFFD966);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int height =((View)this.getParent()).getHeight();
        int lineHeight = getLineHeight();
        int NumberOfLines= height/lineHeight;
        Rect r = mrect;
        Paint paint=mpaint;
         int baseline = getLineBounds(0,r);
         for (int i=0;i<NumberOfLines;i++)
         {
             canvas.drawLine(r.left,baseline+1,r.right,baseline+1,paint);
             baseline+=lineHeight;
         }

        super.onDraw(canvas);

    }

}
