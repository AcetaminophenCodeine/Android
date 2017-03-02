package cs371m.risk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class RedDie extends Die{

    public RedDie(Context context) {
        super(context);
        this.color = "red";
        super.color = "red";
        //setOnClickListener(this);
    }

    public RedDie(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.color = "red";
        super.color = "red";
        //setOnClickListener(this);
    }

    public RedDie(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.color = "red";
        super.color = "red";
        //setOnClickListener(this);
    }

    /*@Override
    public void onClick(View v) {
        super.onClick(v);
    }*/

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
