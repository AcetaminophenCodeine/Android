package cs371m.risk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    public static String PACKAGE_NAME;
    public static Context context;
    public static int attackersNumber;
    public static int defendersNumber;
    public static int [] array3;
    public static int [] array2;
    public TextView winnerUpdateText;
    public TextView attackerUpdateText;
    public TextView defenderUpdateText;
    public Button but;
    public boolean start;
    protected Toast mainToast;

    public void textDisplay()
    {
        if (attackersNumber == 0) {
            winnerUpdateText.setText("Defender is the winner!");
            freezeGame();
        }
        else if(defendersNumber == 0)
        {
            winnerUpdateText.setText("Attacker is the winner!");
            freezeGame();
        }

        attackerUpdateText.setText("Attackers: " + attackersNumber);
        defenderUpdateText.setText("Defenders: " + defendersNumber);

    }

    public void aDieIsPressed(View view)
    {
        if (start) {

            rollDie();

            Die die = (WhiteDie) findViewById(R.id.whiteDie1);
            String imageName = die.color + "_die_" + array2[0];
            int resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
            die.setImageResource(resId);

            die = (WhiteDie) findViewById(R.id.whiteDie2);
            imageName = die.color + "_die_" + array2[1];
            resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
            die.setImageResource(resId);

            die = (RedDie) findViewById(R.id.redDie1);
            imageName = die.color + "_die_" + array3[0];
            resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
            die.setImageResource(resId);

            die = (RedDie) findViewById(R.id.redDie2);
            imageName = die.color + "_die_" + array3[1];
            resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
            die.setImageResource(resId);

            die = (RedDie) findViewById(R.id.redDie3);
            imageName = die.color + "_die_" + array3[2];
            resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
            die.setImageResource(resId);

            game();
            textDisplay();
        }
        else
        {
            Toast t;
            t = Toast.makeText(this, "Start the game!", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
    }

    protected void doToast(String message) {
        if (this.mainToast != null) {
            this.mainToast.cancel();
        }
        this.mainToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        this.mainToast.show();
    }

    public static void rollDie()
    {
        array3 = new int[3];
        array2 = new int[2];

        Random rand = new Random();
        int min = 1;
        int max = 6;

        array3[0] = rand.nextInt((max - min) + 1) + min;
        array3[1] = rand.nextInt((max - min) + 1) + min;
        array3[2] = rand.nextInt((max - min) + 1) + min;
        array2[0] = rand.nextInt((max - min) + 1) + min;
        array2[1] = rand.nextInt((max - min) + 1) + min;

        Arrays.sort(array2);
        Arrays.sort(array3);
    }


    public void game()
    {
        if(MainActivity.attackersNumber != 0 && MainActivity.defendersNumber!= 0)
        {
            int attackerLoss = 0;
            int defenderLoss = 0;

            if(array3[2] > array2[1])
            {
                MainActivity.defendersNumber --;
                defenderLoss++;
            }
            else if(array3[2] <= array2[1])
            {
                MainActivity.attackersNumber--;
                attackerLoss++;
            }



            if (MainActivity.attackersNumber == 0)
            {
                freezeGame();
                return;
            }
            else if (MainActivity.defendersNumber == 0)
            {
                freezeGame();
                return;
            }


            if(array3[1] > array2[0])
            {
                MainActivity.defendersNumber --;
                defenderLoss++;
            }
            else if(array3[1] <= array2[0])
            {
                MainActivity.attackersNumber--;
                attackerLoss++;
            }

            ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
            bar.setProgress(attackersNumber);
            bar = (ProgressBar) findViewById(R.id.progressBar2);
            bar.setProgress(defendersNumber);
            doToast("Attackers loss: " + attackerLoss + ", Defenders loss: " + defenderLoss);


            if (MainActivity.attackersNumber == 0)
            {
                freezeGame();
                return;
            }
            else if (MainActivity.defendersNumber == 0)
            {
                freezeGame();
                return;
            }

            if (attackersNumber == 2)
            {
                Die die = (Die) findViewById(R.id.redDie3);
                die.setEnabled(false);
                die.setClickable(false);

                String imageName = die.color + "_die_" + array3[2] + "_disabled";
                int resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
                die.setImageResource(resId);

            }

            if (attackersNumber == 1)
            {
                Die die = (Die) findViewById(R.id.redDie2);
                die.setEnabled(false);
                die.setEnabled(false);
                die.setClickable(false);

                String imageName = die.color + "_die_" + array3[1] + "_disabled";
                int resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
                die.setImageResource(resId);

            }

            if (defendersNumber == 1)
            {
                Die die = (Die) findViewById(R.id.whiteDie2);
                die.setEnabled(false);
                die.setClickable(false);

                String imageName = die.color + "_die_" + array3[1] + "_disabled";
                int resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
                die.setImageResource(resId);

            }

        }

        else if (MainActivity.attackersNumber == 0)
        {
            freezeGame();
            return;
        }
        else if (MainActivity.defendersNumber == 0)
        {
            freezeGame();
            return;
        }

    }


    public void freezeGame()
    {
        start = false;


        Die die = (WhiteDie) findViewById(R.id.whiteDie1);
        String imageName = die.color + "_die_" + array2[0] + "_disabled" ;
        int resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
        die.setImageResource(resId);
        if (array2[0] == 0)
        {   die.setImageResource(R.drawable.images);}
        die.setClickable(false);
        die.setEnabled(false);

        die = (WhiteDie) findViewById(R.id.whiteDie2);
        imageName = die.color + "_die_" + array2[1] + "_disabled";
        resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
        die.setImageResource(resId);
        if (array2[1] == 0)
        {   die.setImageResource(R.drawable.images);}
        die.setClickable(false);
        die.setEnabled(false);

        die = (RedDie) findViewById(R.id.redDie1);
        imageName = die.color + "_die_" + array3[0] + "_disabled";
        resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
        die.setImageResource(resId);
        if (array3[0] == 0)
        {   die.setImageResource(R.drawable.images);}
        die.setClickable(false);
        die.setEnabled(false);

        die = (RedDie) findViewById(R.id.redDie2);
        imageName = die.color + "_die_" + array3[1] + "_disabled";
        resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
        die.setImageResource(resId);
        if (array3[1] == 0)
        {   die.setImageResource(R.drawable.images);}
        die.setClickable(false);
        die.setEnabled(false);

        die = (RedDie) findViewById(R.id.redDie3);
        imageName = die.color + "_die_" + array3[2] + "_disabled";
        resId = getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
        die.setImageResource(resId);
        if (array3[2] == 0)
        {   die.setImageResource(R.drawable.images);}
        die.setClickable(false);
        die.setEnabled(false);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        context = getApplicationContext();
        start = false;
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        winnerUpdateText = (TextView) findViewById(R.id.winnerText);
        attackerUpdateText = (TextView) findViewById(R.id.attackersProgressDisplayText);
        defenderUpdateText = (TextView) findViewById(R.id.defendersProgressDisplayText);


        but = (Button) findViewById(R.id.start);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attackersNumber = 0;
                defendersNumber = 0;

                ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
                bar.setProgress(100);
                bar = (ProgressBar) findViewById(R.id.progressBar2);
                bar.setProgress(100);

                attackerUpdateText.setText("Attackers: " );
                defenderUpdateText.setText("Defenders: " );
                winnerUpdateText.setText("");

                EditText attackerStartText = (EditText) findViewById(R.id.attackerInputText);
                EditText defendersStartText = (EditText) findViewById(R.id.defenderInputText);
                if (attackerStartText.getText().toString().equals("") ||
                        defendersStartText.getText().toString().equals("")) {
                    doToast("Incorrect solder numbers.");
                    freezeGame();
                    return;
                }

                attackersNumber = Integer.parseInt(attackerStartText.getText().toString());
                defendersNumber = Integer.parseInt(defendersStartText.getText().toString());

                if (attackersNumber <= 0 || defendersNumber <=0) {

                    doToast("Start by entering positive values for soldiers.");
                    freezeGame();
                    return;
                }


                array2 = new int[2];
                array3 = new int[3];

                bar = (ProgressBar) findViewById(R.id.progressBar1);
                bar.setProgress(attackersNumber);
                bar = (ProgressBar) findViewById(R.id.progressBar2);
                bar.setProgress(defendersNumber);


                attackerStartText.setText("");
                defendersStartText.setText("");

                textDisplay();

                Die die = (Die) findViewById(R.id.whiteDie1);
                die.setImageResource(R.drawable.images);
                die.setClickable(true);
                die.setEnabled(true);

                die = (Die) findViewById(R.id.whiteDie2);
                die.setImageResource(R.drawable.images);
                die.setClickable(true);
                die.setEnabled(true);

                die = (Die) findViewById(R.id.redDie1);
                die.setImageResource(R.drawable.images);
                die.setClickable(true);
                die.setEnabled(true);

                die = (Die) findViewById(R.id.redDie2);
                die.setImageResource(R.drawable.images);
                die.setClickable(true);
                die.setEnabled(true);

                die = (Die) findViewById(R.id.redDie3);
                die.setImageResource(R.drawable.images);
                die.setClickable(true);
                die.setEnabled(true);

                start = true;

            }
        });


    }
}
