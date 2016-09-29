package com.example.hanta.testgame2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Hanta on 2016/9/29.
 */
public class Card extends FrameLayout {
    //
    private int num = 0;
    private TextView label;

    public Card(Context context) {
        super(context);
        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);
        //填满整个父级容器
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);
        setNum(0);
    }

    public int getNum(){
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0) {
            label.setText("");
        } else {
            label.setText(num + "");
        }
        setBackgroundColor(num);
    }

    public void setBackgroundColor(int num){
        switch (num){
            case 0:
                label.setBackgroundColor(0xffcdc1b4);
                break;
            case 2:
                label.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                label.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                label.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                label.setBackgroundColor(0xfff59563);
                break;
            case 32:
                label.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                label.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                label.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                label.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                label.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                label.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                label.setBackgroundColor(0xff3c3a32);
                break;
            case 4096:
                label.setBackgroundColor(0xff3d3a33);
                break;
        }
    }

    public boolean equals(Card card){
        //判断两张卡片绑定的数字是否相同
        return getNum() == card.getNum();
    }
}
