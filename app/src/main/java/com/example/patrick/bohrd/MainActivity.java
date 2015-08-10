package com.example.patrick.bohrd;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    int highScore = 0;
    int current = 3;        //represents current size of computer sequence, increments with level
    int cellInc = 0;
    int level = 1;          //as level increases so does current
    int showTime = 1000;    //decreases as level increases


    ArrayList<Integer> compSequence = new ArrayList<Integer>();
    ArrayList<Integer> userSequence = new ArrayList<Integer>();



    Random r = new Random();
    final Handler handler = new Handler();



    TextView cell;
    TextView text;
    Button play;

    TextView[] cellList = new TextView[9];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_layout);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);
                cellList[0] = (TextView) findViewById(R.id.cell1);
                cellList[1] = (TextView) findViewById(R.id.cell2);
                cellList[2] = (TextView) findViewById(R.id.cell3);
                cellList[3] = (TextView) findViewById(R.id.cell4);
                cellList[4] = (TextView) findViewById(R.id.cell5);
                cellList[5] = (TextView) findViewById(R.id.cell6);
                cellList[6] = (TextView) findViewById(R.id.cell7);
                cellList[7] = (TextView) findViewById(R.id.cell8);
                cellList[8] = (TextView) findViewById(R.id.cell9);
                text = (TextView) findViewById(R.id.tracker);
                play = (Button) findViewById(R.id.play);
                deActivate();
                fillComp();
            }
        }, (10000));
    }


    public void adClick(View v){
        Uri url = Uri.parse("https://www.cs.fsu.edu/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, url);
        startActivity(launchBrowser);
    }

    public void fillComp() {
        for (int i = 0; i < current; i++) {       //fill comp move array with randomized numbers 1 - 9
            compSequence.add(r.nextInt(9) + 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cellClick(View v) {
        //Get the id of the clicked object and assign it to a Textview variable
        text.setText("Move " + (cellInc + 1) + " of " + current + "               " + "Level " + level);
        cell = (TextView) findViewById(v.getId());
        cell.setBackgroundColor(Color.RED);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cell.setBackgroundResource(R.drawable.back);
            }
        }, (100));

        if (cell.getId() == cellList[compSequence.get(cellInc) - 1].getId()) {
            userSequence.add(compSequence.get(cellInc));
            cellInc += 1;
        } else {
                lose();
                return;
            }

        if (userSequence.equals(compSequence)) {    //Go to next level and have computer turn begin
            current += 1;
            userSequence.clear();

            if(showTime > 200)
                showTime = showTime - 100;

            compPlay(current + 1);
            cellInc = 0;
            level++;
        }


    }



    public void playClick(View v) {
        compPlay(current);
    }

    private void lose() {
        //reset text and play button
        text.setText("You Lose, click play to try again!");
        play.setClickable(true);
        userSequence.clear();
        compSequence.clear();
        level = 1;
        showTime = 1000;
        current = 3;
        cellInc = 0;
        fillComp();
        deActivate();

    }

    public void deActivate() {
        //deActivate the board while comp plays
        for (int i = 0; i < cellList.length; i++) {
            cellList[i].setClickable(false);
        }

    }

    public Boolean userPlay(int current) {
        //reActivate board and ensure matching cells to those in compSeq
        activate();
        return true;
    }

    private void activate() {
        for (int i = 0; i < cellList.length; i++) {
            cellList[i].setClickable(true);
        }
    }

    public void compPlay(int c) {
        play.setClickable(false);
        text.setText("Computers Turn");
        deActivate();
        for (int i = current; i < c; i++) {       //fill comp move array with randomized numbers 1 - 9
            compSequence.add(r.nextInt(9) + 1);
        }

        int incre = 0;

        for (int i = 0; i < current; i++) {   //Go through randomized computer move
            final int t = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cellList[(compSequence.get(t) - 1)].setBackgroundColor(Color.RED);
                }
            }, (showTime * incre + 100));

            incre = incre + 1;


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cellList[compSequence.get(t) - 1].setBackgroundResource(R.drawable.back);
                    if((t+1) == current) {
                        text.setText("Users Turn");
                        activate();
                    }
                }
            }, showTime * incre);
        }
    }

}
