package com.example.hatter.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Context;
import java.util.ArrayList;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static TGrid theDisplayGrid;

    protected int level = 1;
    protected int score = 0;
    protected int rowCount = 0;
    protected int time = 1000;

    protected Button resetButton;
    protected Button moveDown;

    protected ImageButton rotateLeft;
    protected ImageButton rotateRight;
    protected ImageButton moveLeft;
    protected ImageButton moveRight;

    protected TextView levelText;
    protected TextView scoreText;
    protected TextView rowsText;

    protected Handler freeFallHandler = new Handler();

    protected Tetromino goesFirst;
    protected Tetromino goesNext;
    protected boolean shiftDown;
    protected boolean canLunch;

    protected DisplayGameBoard displayBoard;
    protected DisplayNextTetris displayNext;
    protected ArrayList<Integer> highScores;

    protected Runnable runFreeFall = new Runnable()
    {
        public void run()
        {

            checkForRowsLevelScore();
            displayBoard.invalidate();

            shiftDown = goesFirst.shiftDown();

            if(!shiftDown)
            {
                goesFirst = goesNext;
                goesNext.removeFromGrid();
                canLunch = goesFirst.insertIntoGrid(4,2,theDisplayGrid);

                if(!canLunch) {
                    goToHighScores();
                    return;
                }

                goesNext = TetrominoBuilder.Random();
                goesNext.insertIntoGrid(4,0,theDisplayGrid);
                displayNext.invalidate();
            }

            checkForRowsLevelScore();
            displayBoard.invalidate();

            freeFallHandler.postDelayed(this, time);

        }
    };

    protected void freeFall()
    {
        initBoard();
        freeFallHandler.postDelayed(runFreeFall, time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theDisplayGrid = new TGrid(10,23);
        this.highScores = new ArrayList<>();

        widgetInit();
        freeFall();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                freeFallHandler.removeCallbacks(runFreeFall);
                theDisplayGrid.clear();
                freeFall();

            }
        });

        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goesFirst.zoomDown();

            }
        });

        rotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesFirst.rotateCounterClockwise();

            }
        });

        rotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goesFirst.rotateClockwise();

            }
        });

        moveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goesFirst.shiftLeft();

            }
        });

        moveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goesFirst.shiftRight();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.level_up)
        {
            setLevelAndSpeedLevel();
            setTextView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    protected void checkForRowsLevelScore()
    {
        for (int i = 0 ; i < 2 ; i++)
        {
            int firstFullRow = theDisplayGrid.getFirstFullRow();

            if(firstFullRow != -1)
            {
                theDisplayGrid.deleteRow(firstFullRow );
                rowCount++;
                calibrate();
            }
        }
    }

    protected void calibrate()
    {
        if (rowCount < 5) {

            score += level ;
        }

        else
        {
            score += level;
            rowCount = 0;
            setLevelAndSpeedLevel();

        }

        setTextView();
    }

    protected void setLevelAndSpeedLevel()
    {
        // I tried making an int variable to make this arithmatic look better but ran into Java
        // type casting problems
        if ((time - 0.2 * time) > 0)
        {
            level++;
            time -= 0.2 * time;
        }
    }

    protected void widgetInit()
    {
        resetButton = (Button) findViewById(R.id.reset_button);
        moveDown = (Button) findViewById(R.id.down_button);

        rotateLeft = (ImageButton) findViewById(R.id.rotateLeft_button);
        rotateRight = (ImageButton) findViewById(R.id.rotateRight_button);
        moveLeft = (ImageButton) findViewById(R.id.moveLeft_button);
        moveRight = (ImageButton) findViewById(R.id.moveRight_button);

        levelText = (TextView) findViewById(R.id.level_text);
        scoreText = (TextView) findViewById(R.id.score_text);
        rowsText = (TextView) findViewById(R.id.rowsnum_text);

        displayBoard = (DisplayGameBoard) findViewById(R.id.game_board);
        displayNext = (DisplayNextTetris)findViewById(R.id.nextTCell_board);
    }

    protected void setTextView()
    {
        levelText.setText("\nLEVEL:\n" + level);
        scoreText.setText("\nSCORE:\n" + score);
        rowsText.setText("\nROWS:\n" + rowCount);
    }

    protected void initBoard()
    {
        level = 1;
        score = 0;
        rowCount = 0;
        time = 1000;

        goesNext = TetrominoBuilder.Random();
        goesFirst = TetrominoBuilder.Random();

        goesNext.insertIntoGrid(4, 0, theDisplayGrid);
        goesFirst.insertIntoGrid(4, 2, theDisplayGrid);
    }

    protected void addHighScore(int score) {
        boolean inserted = false;
        for (int iii = 0; iii < highScores.size(); iii++) {
            if (score > highScores.get(iii)) {
                highScores.add(iii, score);
                inserted = true;
                break;
            }
        }
        if (!inserted) {
            highScores.add(score);
        }
    }

    protected void goToHighScores()
    {
        addHighScore(score);
        Intent intent = new Intent(this,HighScores.class);
        Bundle b = new Bundle();
        b.putIntegerArrayList("highScores",highScores);
        intent.putExtras(b);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        if (extras != null)
        {
            highScores = extras.getIntegerArrayList("scoresList");

        }

        freeFallHandler.removeCallbacks(runFreeFall);
        theDisplayGrid.clear();
        freeFall();

    }

}
