package edu.utexas.cs.zhitingz.fcsql;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner restaurantTypeSpinner;
    private Spinner citySpinner;
    private Spinner timeSpinner;
    private Spinner daySpinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // private string arrays
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

        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }
        initRestaurantDB();
    }

    private void initRestaurantDB() {
        restaurants = (ListView) findViewById(R.id.restaurant_list);
        restaurantDb = dbHelper.getReadableDatabase();
        restaurantAdapter = new RestaurantItemAdapter(this, null, false);
        restaurants.setAdapter(restaurantAdapter);
    }

    @Override
    public void onClick(View v) {
        // where contains the selection clause and args contains the corresponding arguments
        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        // XXX WRITE ME: Create table and columns string.
        String table = "businesses";
        String[] columns = {"businesses._id as _id", "name" , "phone", "full_address", "url", "price" };

        // XXX WRITE ME: Generate selection clause for query city and restaurant type
        // Please use query instead of rawQuery.
        // There are two helper function you can use
        //          handleCity
        //          handleRestaurant
        // The query for restaurant type is provided as an example.
        handleCity(where, args);
        table = handleRestaurant(table, where, args);

        if (where.size() != 0)
        {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++)
            {
                queryString += " AND " + where.get(i);
            }
        }

        // XXX WRITE ME: Handle ORDER BY and LIMIT request
        // XXX WRITE ME: query database and show result in the ListView.
        // If the query result is empty, generate a toast.

        restaurantDb = dbHelper.getReadableDatabase();

        String[] args_array = args.toArray(new String[0]);
        //query(String table,
        //      String[] columns,
        //      String selection,
        //      String[] selectionArgs,
        //      String groupBy,
        //      String having,
        //      String orderBy,
        //      String limit)
        String orderby;
        if (orderByPrice.isChecked())
        {
            orderby = handleOrderBy();
        }
        else
        {
            orderby = null;
        }

        Cursor cursor = restaurantDb.query(table, columns, queryString, args_array, null, null,
                                            orderby, handleLimit());

        if (cursor.getCount() != 0) {
            restaurantAdapter.changeCursor(cursor);
        }
        else
        {
            Toast.makeText(this, "No options available!", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method for generate selection clause for query city
    private void handleCity(List<String> where, List<String> args) {
        // XXX WRITE ME

        int cityPos = citySpinner.getSelectedItemPosition();
        if (cityPos != 0)
        {
            String categoryFilter = "";

            if (cityPos > 0) {
                categoryFilter = cities[cityPos];
            }

            where.add("(businesses.city = ?)");
            args.add(categoryFilter);
        }
    }

    // Helper method for generate selection clause for query restaurant type.
    private String handleRestaurant(String table, List<String> where, List<String> args)
    {
        int restaurantTypePos = restaurantTypeSpinner.getSelectedItemPosition();

        if (restaurantTypePos != 0)
        {
            String categoryFilter = "";

            if (restaurantTypePos == 1)
            {
                categoryFilter = "newamerican";
            }
            else if (restaurantTypePos == 2) {
                categoryFilter = "breakfast_brunch";
            }
            else if (restaurantTypePos > 0) {
                categoryFilter = restarantTypes[restaurantTypePos].toLowerCase();
            }

            table += ", categories";
            where.add("(businesses._id = categories._id) AND (categories.category_name = ?)");
            args.add(categoryFilter);
        }
        return table;
    }

    private String handleLimit()
    {
        int limitPos = limitSpinner.getSelectedItemPosition();
        Integer categoryFilter = null;
        if (limitPos != 0)
        {
            if (limitPos == 1)
            {
                categoryFilter = 10;
            }
            else if (limitPos == 2) {
                categoryFilter = 20;
            }
            else if (limitPos == 3) {
                categoryFilter = 50;
            }
        }

        return categoryFilter.toString();
    }

    private String handleOrderBy()
    {
        int restaurantTypePos = restaurantTypeSpinner.getSelectedItemPosition();
        String categoryFilter = "price";

        if (restaurantTypePos != 0)
        {
            if (restaurantTypePos == 1)
            {
                categoryFilter = "price ASC";
            }
            else if (restaurantTypePos == 2) {
                categoryFilter = "price DESC";
            }

        }

        return categoryFilter;
    }
}
