package cs371m.risk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class WhiteDie extends Die{

    public WhiteDie(Context context) {
        super(context);
        this.color = "white";
        super.color = "white";
    }

    public WhiteDie(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.color = "white";
        super.color = "white";
    }

    public WhiteDie(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.color = "white";
        super.color = "white";
    }


    @Override
    public void setColor(String color)
    {
        super.setColor("red");
    }

    @Override
    public String getColor()
    {
        return super.getColor();
    }

    @Override
    public void setNum(int num)
    {
        super.setNum(num);
    }

    @Override
    public int getNum()
    {
        return super.getNum();
    }



}
