package cs371m.risk;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    public String PACKAGE_NAME;
    protected Context context;
    protected int attackersBeginNumber;
    protected int defendersBeginNumber;
    protected int attackersNumber;
    protected int defendersNumber;
    protected static int [] attackerArray;
    protected static int [] defenderArray;
    protected TextView winnerUpdateText;
    protected TextView attackerUpdateText;
    protected TextView defenderUpdateText;
    protected Button but;
    protected boolean start;
    protected Toast mainToast;


    // if a die is pressed and start is true, View will be passed to this function
    // dice are rolled, game starts and results are displayed through textDisplay
    // otherwise a toast appears to start the game
    public void aDieIsPressed(View view)
    {
        if (start) {

            rollDie();

            Die die = (WhiteDie) findViewById(R.id.whiteDie1);
            String imageName = die.color + "_die_" + defenderArray[0];
            int resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);

            die = (WhiteDie) findViewById(R.id.whiteDie2);
            imageName = die.color + "_die_" + defenderArray[1];
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);

            die = (RedDie) findViewById(R.id.redDie1);
            imageName = die.color + "_die_" + attackerArray[0];
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);

            die = (RedDie) findViewById(R.id.redDie2);
            imageName = die.color + "_die_" + attackerArray[1];
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);

            die = (RedDie) findViewById(R.id.redDie3);
            imageName = die.color + "_die_" + attackerArray[2];
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);

            game();
        }
        else
        {
            doToast("Start the game!");
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
        attackerArray = new int[3];
        defenderArray = new int[2];

        Random rand = new Random();
        int min = 1;
        int max = 6;

        attackerArray[0] = rand.nextInt((max - min) + 1) + min;
        attackerArray[1] = rand.nextInt((max - min) + 1) + min;
        attackerArray[2] = rand.nextInt((max - min) + 1) + min;
        defenderArray[0] = rand.nextInt((max - min) + 1) + min;
        defenderArray[1] = rand.nextInt((max - min) + 1) + min;

        Arrays.sort(defenderArray);
        Arrays.sort(attackerArray);
    }

    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
    public void game()
    {
            int attackerLoss = 0;
            int defenderLoss = 0;
            int attackerWin = 0;
            int defenderWin = 0;


            if (attackerArray[2] > defenderArray[1])
            {
                attackerWin++;
                defenderLoss++;
            }
            else
            {
                defenderWin++;
                attackerLoss++;
            }

            if (attackerArray[1] > defenderArray[0])
            {
                attackerWin++;
                defenderLoss++;
            }
            else
            {
                defenderWin++;
                attackerLoss++;
            }

            if (attackerWin > defenderWin)
            {
                attackersNumber++;
                winnerUpdateText.setText("Attacker is the winner!");
                freezeGame();
            }
            else
            {
                defendersNumber++;
                winnerUpdateText.setText("Defender is the winner!");
                freezeGame();
            }

        attackerUpdateText.setText("Attackers: " + attackersNumber);
        defenderUpdateText.setText("Defenders: " + defendersNumber);

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
        bar.setProgress(attackersNumber);
        bar = (ProgressBar) findViewById(R.id.progressBar2);
        bar.setProgress(defendersNumber);
    }



    public void freezeGame()
    {
        start = false;

        String imageName;
        int resId;

        Die die = (WhiteDie) findViewById(R.id.whiteDie1);
        if (defenderArray[0] == 0)
        {   die.setImageResource(R.drawable.images);
        }
        else {
            imageName = die.color + "_die_" + defenderArray[0] + "_disabled";
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);
            die.setClickable(false);
            die.setEnabled(false);
        }

        die = (WhiteDie) findViewById(R.id.whiteDie2);
        if (defenderArray[1] == 0)
        {   die.setImageResource(R.drawable.images);}
        else {
            imageName = die.color + "_die_" + defenderArray[1] + "_disabled";
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);
            die.setClickable(false);
            die.setEnabled(false);
        }

        die = (RedDie) findViewById(R.id.redDie1);
        if (attackerArray[0] == 0)
        {   die.setImageResource(R.drawable.images);}
        else {
            imageName = die.color + "_die_" + attackerArray[0] + "_disabled";
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);
            die.setClickable(false);
            die.setEnabled(false);
        }

        die = (RedDie) findViewById(R.id.redDie2);
        if (attackerArray[1] == 0)
        {   die.setImageResource(R.drawable.images);}
        else {
            imageName = die.color + "_die_" + attackerArray[1] + "_disabled";
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);
            die.setClickable(false);
            die.setEnabled(false);
        }

        die = (RedDie) findViewById(R.id.redDie3);
        if (attackerArray[2] == 0)
        {   die.setImageResource(R.drawable.images);}
        else {
            imageName = die.color + "_die_" + attackerArray[2] + "_disabled";
            resId = getResources().getIdentifier(imageName, "drawable", PACKAGE_NAME);
            die.setImageResource(resId);
            die.setClickable(false);
            die.setEnabled(false);
        }
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
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        context = getApplicationContext();
        start = false;

        winnerUpdateText = (TextView) findViewById(R.id.winnerText);
        attackerUpdateText = (TextView) findViewById(R.id.attackersProgressDisplayText);
        defenderUpdateText = (TextView) findViewById(R.id.defendersProgressDisplayText);

        attackerUpdateText.setText("Attackers: " );
        defenderUpdateText.setText("Defenders: " );
        winnerUpdateText.setText("");

        final EditText attackerStartText = (EditText) findViewById(R.id.attackerInputText);
        final EditText defendersStartText = (EditText) findViewById(R.id.defenderInputText);

        attackerStartText.setText("");
        defendersStartText.setText("");

        but = (Button) findViewById(R.id.start);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
                bar.setProgress(0);
                bar = (ProgressBar) findViewById(R.id.progressBar2);
                bar.setProgress(0);

                if (!isStringInt(attackerStartText.getText().toString()) ||
                        !isStringInt(defendersStartText.getText().toString())) {
                    doToast("Incorrect solder numbers.");
                    return;
                }

                attackersBeginNumber = Integer.parseInt(attackerStartText.getText().toString());
                defendersBeginNumber = Integer.parseInt(defendersStartText.getText().toString());

                if (attackersBeginNumber <= 0 || defendersBeginNumber <=0) {

                    doToast("Start by entering positive values for soldiers.");
                    return;
                }


                defenderArray = new int[2];
                attackerArray = new int[3];



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
