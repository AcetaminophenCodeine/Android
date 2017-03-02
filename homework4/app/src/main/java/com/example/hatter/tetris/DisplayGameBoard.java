package com.example.hatter.tetris;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;

/**
 * Created by Hatter on 9/29/16.
 */

public class DisplayGameBoard extends View {
    public DisplayGameBoard(Context context) {
        super(context);
    }

    public DisplayGameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayGameBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected Paint paint = new Paint();
    protected Handler timeHandler = new Handler();


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        invalidate();
        // clear the backgroud
        this.paint.setColor(Color.WHITE);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(this.paint);

        paintGridOnCanvas(canvas);


    }

    protected void paintGridOnCanvas(Canvas canvas)
    {
        for(int iii = 0; iii < MainActivity.theDisplayGrid.columns; iii++)
        {
            for (int jjj = 3; jjj < MainActivity.theDisplayGrid.rows; jjj++)
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

    protected float left(float xPosition) { return  (xPosition * getWidth()) / 10; }

    protected float top(float yPosition)
    {
        return (yPosition - 3) * getHeight() / 20;
    }

    protected float right(float xPosition)
    {
        return left(xPosition) + (float)getWidth()/10;
    }

    protected float buton(float yPosition)
    {
        return top(yPosition) + (float)getWidth()/10;
    }
}