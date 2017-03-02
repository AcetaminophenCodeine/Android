package edu.utexas.cs.zhitingz.fclistviewarray;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ListView restaurants;
    private RestaurantItemAdapter restaurantAdapter;
    private HashMap<String, List<String[]>> map;
    private String[] category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.restaurant_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.restaurant_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Please select one type of Restaurant!");

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        this));

        spinner.setOnItemSelectedListener(this);
        restaurants = (ListView) findViewById(R.id.restaurant_list);
        restaurantAdapter = new RestaurantItemAdapter(this, null);
        restaurants.setAdapter(restaurantAdapter);
        map = new HashMap<String, List<String[]>>();
        category = getResources().getStringArray(R.array.restaurant_type);
        category[0] = "newamerican";
        category[1] = "breakfast_brunch";
        for (int i = 2; i < category.length; i++) {
            category[i] = category[i].toLowerCase();
        }
        for (String cat : category) {
            List<String[]> l = new ArrayList<String[]>();
            String json = loadJsonFromAssets(cat);
            if (json == null) {
                Log.e("Load Json", "Cannot load json file");
            }
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject e = arr.getJSONObject(i);
                    String[] element = new String[4];
                    element[0] = e.getString("name");
                    element[1] = e.getString("phone");
                    element[2] = e.getString("address");
                    element[3] = e.getString("url");
                    l.add(element);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put(cat, l);
        }
    }

    public String loadJsonFromAssets(String cat) {
        String json = null;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getAssets().open(cat + ".json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((json = br.readLine()) != null) {
                sb.append(json).append("\n");
            }
            return sb.toString();
        } catch (IOException ex) {
            Log.e("Load Json", "IOException");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // i == 0 is a special marker for the spinner
        if (i != 0) {
            List<String[]> list = map.get(category[i - 1]);
            // XXX Spinner has chosen new restaurant list
            // Inform restaurantAdapter of the change
            restaurantAdapter.changeList(list);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
