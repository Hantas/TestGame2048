package com.example.hanta.testgame2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanta on 2016/9/29.
 */
public class GameView extends GridLayout {
//
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    //初始化方法
    private void initGameView(){
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);
        //通过手势进行操作
        setOnTouchListener(new OnTouchListener() {
            private float startX;
            private float startY;
            private float endX;
            private float endY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX() - startX;
                        endY = event.getY() - startY;
                        //水平滑动
                        if (Math.abs(endX) > Math.abs(endY)){
                            if (endX < -5)
                                leftSlide();
                            else if (endX > 5)
                                rightSlide();
                        }
                        else if (Math.abs(endX) < Math.abs(endY)){
                            if (endY < -5)
                                upSlide();
                            else if (endY > 5)
                                downSlide();
                        }
                        break;
                }
                return true;
            }
        });
    }
    //为适应不同手机的规格,获取卡片适合大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //-10是为了与屏幕边缘留下缝隙,获取宽高最小值/4是为了满足该游戏4X4卡片的规格
        int cardWidth = (Math.min(w, h) - 10)/4;
        //卡片为正方形
        addCards(cardWidth, cardWidth);
        startGame();
    }

    public void startGame(){
        MainActivity.getMainActivity().clearGoal();
        MainActivity.getMainActivity().clearScore();
        MainActivity.getMainActivity().clearMoveNum();
        for (int y = 0;y < 4;y++){
            for (int x = 0;x < 4;x++) {
                cardMaps[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    //为方便程序使用,定义一个二维数组存放Card类
    private Card[][] cardMaps = new Card[4][4];
    private void addCards(int cardWidth, int cardHeight){
        Card card;
        for (int y = 0;y < 4;y++){
            for (int x = 0;x < 4;x++){
                card = new Card(getContext());
                card.setNum(0);
                addView(card, cardWidth, cardHeight);
                cardMaps[x][y] = card;
            }
        }
    }

    private List<Point> emptyCards = new ArrayList<Point>();
    private void addRandomNum(){

        emptyCards.clear();

        for (int y = 0;y < 4;y++){
            for (int x = 0;x < 4;x++) {
                if(cardMaps[x][y].getNum() <= 0){
                    emptyCards.add(new Point(x, y));
                }
            }
        }
        //随机移除一个emptyCards并将该点的x,y记录下来
        Point p = emptyCards.remove((int)(Math.random()*emptyCards.size()));
        //给p点坐标表示的CardMaps添加随机数,2与4出现概率为9:1
        cardMaps[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }

    private void leftSlide(){
        MainActivity.getMainActivity().addMoveNum();
        boolean flag = false;
        for (int y = 0;y < 4;y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1;x1 < 4;x1++){
                    if (cardMaps[x1][y].getNum() > 0){
                        if (cardMaps[x][y].getNum() <= 0){
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);
                            x--;
                            flag = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x1][y])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            flag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (flag){
            checkGoal();
            addRandomNum();
            checkComplete();
        }
    }

    private void rightSlide(){
        MainActivity.getMainActivity().addMoveNum();
        boolean flag = false;
        for (int y = 0;y < 4;y++) {
            for (int x = 3; x >= 0;x--) {
                for (int x1 = x -1;x1 >= 0;x1--){
                    if (cardMaps[x1][y].getNum() > 0){
                        if (cardMaps[x][y].getNum() <= 0){
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);
                            x++;
                            flag = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x1][y])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            flag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (flag){
            checkGoal();
            addRandomNum();
            checkComplete();
        }
    }

    private void upSlide(){
        MainActivity.getMainActivity().addMoveNum();
        boolean flag = false;
        for (int x = 0;x < 4;x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y+1;y1 < 4;y1++){
                    if (cardMaps[x][y1].getNum() > 0){
                        if (cardMaps[x][y].getNum() <= 0){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);
                            y--;
                            flag = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x][y1])){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum()*2);
                            cardMaps[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            flag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (flag){
            checkGoal();
            addRandomNum();
            checkComplete();
        }
    }

    private void downSlide(){
        boolean flag = false;
        MainActivity.getMainActivity().addMoveNum();
        for (int x = 0;x < 4;x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y-1;y1 >= 0;y1--){
                    if (cardMaps[x][y1].getNum() > 0){
                        if (cardMaps[x][y].getNum() <= 0){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);
                            y++;
                            flag = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x][y1])){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum()*2);
                            cardMaps[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            flag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (flag){
            checkGoal();
            addRandomNum();
            checkComplete();
        }
    }



    public int maxNum(){
        int max = 0;
        for (int y = 0;y < 4;y++) {
            for (int x = 0; x < 4; x++) {
                if(max < cardMaps[x][y].getNum()){
                    max = cardMaps[x][y].getNum();
                }
            }
        }
        return max;
    }

    public void checkGoal(){
        for (int y = 0;y < 4;y++) {
            for (int x = 0; x < 4; x++) {
                if(cardMaps[x][y].getNum() == MainActivity.getMainActivity().getGoal()){
                    MainActivity.getMainActivity().addGoal();
                }
            }
        }
    }

    public void checkComplete(){
        boolean complete = true;
        //二重循环标签
        ALL:
        for (int y = 0;y < 4;y++) {
            for (int x = 0; x < 4; x++) {
                if(cardMaps[x][y].getNum() == 0 ||
                        (x > 0&&cardMaps[x][y].equals(cardMaps[x-1][y]))||
                        (x < 3&&cardMaps[x][y].equals(cardMaps[x+1][y]))||
                        (y > 0&&cardMaps[x][y].equals(cardMaps[x][y-1]))||
                        (y < 3&&cardMaps[x][y].equals(cardMaps[x][y+1]))){
                    complete = false;
                    //跳出二重循环
                    break ALL;
                }
            }
        }
        if (complete){
            MainActivity.getMainActivity().addBestScore();
            new AlertDialog.Builder(getContext()).
                    setTitle("2048提示您").
                    setMessage("游戏结束,本次叠加到最高的数字为: "+maxNum()+",你的总得分为: "
                            +MainActivity.getMainActivity().getScore()+"").
                    setPositiveButton("重来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).
                    setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.getMainActivity().finish();
                        }
                    }).
                    setCancelable(false).
                    show();
        }
    }
}

