package com.example.hanta.testgame2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static MainActivity mainActivity = null;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    GameView gameView;
    TextView goal,moveNum,curScore,bestScore;
    Button quit,replay;
    int score = 0;
    int moveTimes = 0;
    int myGoal = 8;

    public MainActivity(){
        mainActivity = this;
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (GameView)findViewById(R.id.gameView);
        goal = (TextView)findViewById(R.id.goal);
        moveNum = (TextView)findViewById(R.id.movNum);
        curScore = (TextView)findViewById(R.id.curScore);
        bestScore = (TextView)findViewById(R.id.bestScore);
        replay = (Button)findViewById(R.id.replay);
        quit = (Button)findViewById(R.id.quit);;
        showBestScore();

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBestScore();
                new AlertDialog.Builder(MainActivity.this).
                        setTitle("2048提示您").
                        setMessage("是否重置游戏最高纪录").
                        setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferences=getSharedPreferences("myBestScore", Context.MODE_PRIVATE);
                                editor = preferences.edit();
                                editor.putInt("myBestScore", 0);
                                editor.commit();
                                showBestScore();
                            }
                        }).
                        setNegativeButton("取消", null).
                        setCancelable(false).
                        show();
                gameView.startGame();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }

    //清空内容
    public void clearScore(){
        score = 0;
        showScore();
    }
    public void clearMoveNum(){
        moveTimes = 0;
        showMoveNum();
    }
    public void clearGoal(){
        myGoal = 8;
        showGoal();
    }
    //更新内容
    public void addScore(int s){
        score += s;
        addBestScore();
        showScore();
    }
    public void addMoveNum(){
        moveTimes++;
        showMoveNum();
    }
    public void addGoal(){
        myGoal *= 2;
        showGoal();
    }
    public void addBestScore(){
        preferences=getSharedPreferences("myBestScore", Context.MODE_PRIVATE);
        editor = preferences.edit();
        int myBestScore = preferences.getInt("myBestScore", 0);
        if (myBestScore < getScore()){
            editor.putInt("myBestScore", getScore());
            editor.commit();
        }
        showBestScore();
    }
    //显示内容
    public void showScore(){curScore.setText(score+"");}
    public void showMoveNum(){moveNum.setText(moveTimes+"");}
    public void showGoal(){goal.setText(myGoal+"");}
    public void showBestScore(){
        preferences=getSharedPreferences("myBestScore", Context.MODE_PRIVATE);
        int myBestScore = preferences.getInt("myBestScore", 0);
        bestScore.setText(myBestScore+"");
    }
    //获取数据
    public int getScore(){
        int myScore = score;
        return myScore;
    }
    public int getGoal(){
        return myGoal;
    }

}
