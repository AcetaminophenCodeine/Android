package edu.utexas.cs.zhitingz.fcsql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by zhitingz on 9/9/16.
 */
public class RestaurantItemAdapter extends CursorAdapter {

    public RestaurantItemAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.restaurant_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
        TextView restaurantPhone = (TextView) view.findViewById(R.id.restaurant_phone);
        TextView restaurantAddress = (TextView) view.findViewById(R.id.restaurant_address);
        TextView restaurantUrl = (TextView) view.findViewById(R.id.restaurant_url);
        TextView restaurantPrice = (TextView) view.findViewById(R.id.price);

        // XXX WRITE ME: Fill the TextView using data in cursor.
        // price will be in the form of "$", e.g. 1 will be "$" and 2 will be "$$".

        String name = null;
        final String phone = null;
        String address = null;
        String url = null;
        String price = null;

        name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        address = cursor.getString(cursor.getColumnIndexOrThrow("full_address"));
        Integer price1 = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        if (price1 == 1)
            price = "$";
        else if (price1 == 2)
            price = "$$";
        else if (price1 == 3)
            price = "$$$";
        else if (price1 == 4)
            price = "$$$$";

        restaurantName.setText(name);
        restaurantAddress.setText(address);
        restaurantPrice.setText(price);

        restaurantPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                v.getContext().startActivity(intent);
            }
        });
        restaurantUrl.setText(Html.fromHtml("<a href=\"" + url + "\">website</a>"));
        restaurantUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
