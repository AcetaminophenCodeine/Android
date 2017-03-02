package com.example.hatter.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.content.Intent;
import android.widget.Button;


public class HighScores extends AppCompatActivity {

    Button gobackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        gobackButton = (Button) findViewById(R.id.okButton);
        Intent activityThatCalled =  getIntent();
        Bundle callingBundle = activityThatCalled.getExtras();

        final ArrayList<Integer> scoresList = callingBundle.getIntegerArrayList("highScores");
        renderHighScores(scoresList);


        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goingBack = new Intent();
                Bundle b = new Bundle();
                b.putIntegerArrayList("scoresList",scoresList);
                goingBack.putExtras(b);
                setResult(RESULT_OK,goingBack);
                finish();
            }
        });
    }

    protected void renderHighScores(ArrayList<Integer> scoresList) {

        String[] renderThese = new String[scoresList.size()];
        for (int iii = 0; iii < scoresList.size(); iii++) {
            renderThese[iii] = scoresList.get(iii).toString();
        }

        ListAdapter theAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, renderThese);
        ListView highScores = (ListView) findViewById(R.id.highScoreList);
        highScores.setAdapter(theAdaptor);
    }



}
