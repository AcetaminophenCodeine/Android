package com.example.hatter.tetris;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by Hatter on 10/2/16.
 */

public class DisplayNextTetris extends View {

    public DisplayNextTetris(Context context) {
        super(context);
    }

    public DisplayNextTetris(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayNextTetris(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // clear the backgroud
        this.paint.setColor(Color.WHITE);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(this.paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText( "NEXT: ", 50, 75, paint);

        paintGridOnCanvas(canvas);

    }

    protected void paintGridOnCanvas(Canvas canvas)
    {
        for(int iii = 0; iii < MainActivity.theDisplayGrid.columns; iii++)
        {
            for (int jjj = 0; jjj < 2; jjj++)
            {
                TCell tcell = MainActivity.theDisplayGrid.getCellAt(iii, jjj);

                if (tcell != null)
                {
                    this.paint.setStyle(Paint.Style.FILL);
                    this.paint.setColor(tcell.getColor());
                    canvas.drawRect(left(tcell.xPos), top(tcell.yPos), right(tcell.xPos),
                            buton(tcell.yPos), this.paint);
                    this.paint.setStyle(Paint.Style.STROKE);
                    this.paint.setColor(Color.BLACK);
                    canvas.drawRect(left(tcell.xPos), top(tcell.yPos), right(tcell.xPos),
                            buton(tcell.yPos), this.paint);

                }
            }
        }
    }

    protected float left(int xPosition)
    { return   (float) (xPosition * getWidth()) / 6 - (float)getWidth()/2 ; }

    protected float top(int yPosition)
    {
        return  (float)getWidth()/2 + ((float) yPosition) * getHeight() / 6;
    }

    protected float right(int xPosition)
    {
        return left(xPosition) + (float)getWidth()/6;
    }

    protected float buton(int yPosition)
    {
        return top(yPosition) + (float)getWidth()/6;
    }

}
