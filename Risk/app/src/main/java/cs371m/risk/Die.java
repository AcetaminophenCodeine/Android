package cs371m.risk;

import android.view.View;
import android.widget.ImageButton;
import android.content.Context;
import android.util.AttributeSet;

import java.util.Random;
import java.util.Arrays;

public abstract class Die extends ImageButton {

    protected String color;
    protected int num;


    public Die(Context context) {super(context);}

    public Die(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Die(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

        public void setColor(String color)
        {
            this.color = color;
        }

        public String getColor()
        {
            return color;
        }

        public void setNum(int num)
        {
            this.num = num;
        }

        public int getNum()
        {
            return num;
        }


}
