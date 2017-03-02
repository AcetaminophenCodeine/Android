package edu.utexas.cs.thunt.fcmaps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by zhitingz on 9/9/16.
 */
public class RestaurantItemAdapter extends CursorAdapter {

    MainActivity activity;

    public RestaurantItemAdapter(MainActivity act, Cursor c, boolean autoRequery) {
        super(act, c, autoRequery);

        activity = act;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.restaurant_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
        TextView restaurantPhone = (TextView) view.findViewById(R.id.restaurant_phone);
        final TextView restaurantAddress = (TextView) view.findViewById(R.id.restaurant_address);
        TextView restaurantUrl = (TextView) view.findViewById(R.id.restaurant_url);
        TextView restaurantPrice = (TextView) view.findViewById(R.id.price);
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        final String address = cursor.getString(cursor.getColumnIndexOrThrow("full_address"));
        final String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
        String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
        String priceCountStr = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        restaurantName.setText(name);
        restaurantPhone.setText(phone);
        restaurantPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                v.getContext().startActivity(intent);
            }
        });
        restaurantAddress.setText(address);
        restaurantAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String s = (String) restaurantAddress.getText();
                    activity.toMapFragment(s);
            }
        });

        /*
            XXX Write me
            Hint: something something onClick
         */
        restaurantUrl.setText(Html.fromHtml("<a href=\"" + url + "\">website</a>"));
        restaurantUrl.setMovementMethod(LinkMovementMethod.getInstance());
        int priceCount = 1;
        if (priceCountStr != null)
            priceCount = Integer.parseInt(priceCountStr);
        String price = "";
        for (int i = 0; i < priceCount; i++) {
            price += "$";
        }
        restaurantPrice.setText(price);
    }
}
