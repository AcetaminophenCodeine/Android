package edu.utexas.cs.thunt.fcmaps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.SupportMapFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner restaurantTypeSpinner;
    private Spinner citySpinner;
    private Spinner limitSpinner;
    private Spinner orderSpinner;
    private ListView restaurants;
    private SQLiteDatabase restaurantDb;
    private DatabaseHelper dbHelper;
    private RestaurantItemAdapter restaurantAdapter;
    private CheckBox orderByPrice;
    private Button queryButton;
    private String[] cities;
    private String[] restarantTypes;
    private String[] order;
    private String[] limit;

    private SupportMapFragment mapFragment;
    private MapHolder mapHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cities = getResources().getStringArray(R.array.city);
        restarantTypes = getResources().getStringArray(R.array.restaurant_type);
        order = getResources().getStringArray(R.array.order);
        limit = getResources().getStringArray(R.array.limit);

        restaurantTypeSpinner = (Spinner) findViewById(R.id.restaurant_type);
        ArrayAdapter<CharSequence> restaurantTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.restaurant_type,
                android.R.layout.simple_spinner_item);
        restaurantTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantTypeSpinner.setAdapter(restaurantTypeAdapter);

        citySpinner = (Spinner) findViewById(R.id.city);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city,
                android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        limitSpinner = (Spinner) findViewById(R.id.limit);
        ArrayAdapter<CharSequence> limitAdapter = ArrayAdapter.createFromResource(this,
                R.array.limit,
                android.R.layout.simple_spinner_item);
        limitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        limitSpinner.setAdapter(limitAdapter);

        orderSpinner = (Spinner) findViewById(R.id.choose_order);
        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(this,
                R.array.order,
                android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderAdapter);

        orderByPrice = (CheckBox) findViewById(R.id.price_order);

        queryButton = (Button) findViewById(R.id.query);
        queryButton.setOnClickListener(this);

        dbHelper = new DatabaseHelper(this);
        try
        {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }
        initRestaurantDB();

        /* XXX Write me */
        mapFragment = new SupportMapFragment();
        mapHolder = new MapHolder(this);
        mapFragment.getMapAsync(mapHolder);

        /* Notice the handy method chaining idiom for fragment transactions */
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment, mapFragment)
                .hide(mapFragment)
                .commit();
    }

    private void initRestaurantDB()
    {
        restaurants = (ListView) findViewById(R.id.restaurant_list);
        restaurantDb = dbHelper.getReadableDatabase();
        restaurantAdapter = new RestaurantItemAdapter(this, null, false);
        restaurants.setAdapter(restaurantAdapter);
    }

    @Override
    public void onClick(View v)
    {
        String[] columns = {"businesses._id as _id",
                            "name",
                            "phone",
                            "full_address",
                            "url",
                            "price"};

        String table = "businesses";

        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();

        handleCity(where, args);
        table = handleRestaurant(table, where, args);

        String queryString = "";

        if (where.size() != 0)
        {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        String orderByStr = null;

        if (orderByPrice.isChecked())
        {
            orderByStr = "price";
            int orderPosition = orderSpinner.getSelectedItemPosition();

            if (orderPosition != 0)
            {
                orderByStr += " " + order[orderPosition];
            }
        }

        String limitStr = null;

        int limitPosition = limitSpinner.getSelectedItemPosition();

        if (limitPosition != 0)
        {
            limitStr = limit[limitPosition];
        }

        String[] argsStr = args.toArray(new String[args.size()]);

        Cursor restaurantCursor = restaurantDb.query(table, columns, queryString, argsStr, null, null, orderByStr, limitStr);

        if (restaurantCursor.getCount() < 1)
        {
            Toast.makeText(v.getContext(), "No Results Found!", Toast.LENGTH_SHORT).show();
        }

        restaurantAdapter.changeCursor(restaurantCursor);
    }

    private void handleCity(List<String> where, List<String> args) {
        int cityPos=  citySpinner.getSelectedItemPosition();
        if (cityPos != 0) {
            where.add("businesses.city = ?");
            args.add(cities[cityPos]);
        }
    }

    private String handleRestaurant(String table, List<String> where, List<String> args) {
        int restaurantTypePos = restaurantTypeSpinner.getSelectedItemPosition();
        if (restaurantTypePos != 0) {
            String categoryFilter = "";
            if (restaurantTypePos == 1) {
                categoryFilter = "newamerican";
            } else if (restaurantTypePos == 2) {
                categoryFilter = "breakfast_brunch";
            } else if (restaurantTypePos > 0) {
                categoryFilter = restarantTypes[restaurantTypePos].toLowerCase();
            }
            table += ", categories";
            where.add("(businesses._id = categories._id) AND (categories.category_name = ?)");
            args.add(categoryFilter);
        }
        return table;
    }

    public void toMapFragment(String address) {
        /* XXX write me
            HINT: We need to tell the map what to show
             zoom level 15 is good
         */

        mapHolder.showAddress(address);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .show(mapFragment)
                .commit();
    }
}
