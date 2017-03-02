package cs371m.musicapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.Intent;

public class Settings extends Activity {

    Button okButton;
    Button cancelButton;
    ToggleButton toggleButton;
    boolean togglePressed = false;
    public static boolean looping = false;

    protected void modifications() {

        if (MainActivity.mp.isLooping()) {
            MainActivity.mp.setLooping(false);
            looping = false;
        } else {
            MainActivity.mp.setLooping(true);
            looping = true;
        }


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.looping);

        okButton = (Button) findViewById(R.id.OK);
        cancelButton = (Button) findViewById(R.id.cancel);
        toggleButton = (ToggleButton) findViewById(R.id.toggle);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (togglePressed) {
                    modifications();
                }
                Bundle data = getIntent().getExtras();
                Bundle settingsData = new Bundle();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtras(data);
                setResult(RESULT_OK, getIntent());
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = getIntent().getExtras();
                Bundle settingsData = new Bundle();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtras(data);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                togglePressed = true;

            }
        });

    }
}