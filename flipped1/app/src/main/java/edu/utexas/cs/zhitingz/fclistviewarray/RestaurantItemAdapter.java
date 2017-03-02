package edu.utexas.cs.zhitingz.fclistviewarray;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhitingz on 9/9/16.
 */
public class RestaurantItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String[]> list;

    public RestaurantItemAdapter(Context context, List<String[]> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (list != null) {
            return list.get(position).hashCode();
        } else {
            return 0;
        }
    }

    public void bindView(final String[] data, View view, ViewGroup parent) {
        // XXX Write this function.
        // XXX the phone number should send an ACTION_DIAL indirect intent

        // XXX the provided code is not correct, though the initialization of
        // the restuarantUrl is correct (and a pretty cool way to send an
        // implicit intent)


        TextView restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
        restaurantName.setText(data[0]);


        TextView restaurantPhone = (TextView) view.findViewById(R.id.restaurant_phone);
        restaurantPhone.setText(data[1]);
        final Uri number = Uri.fromParts("tel", data[1], null);
        final Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        restaurantPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.getContext().startActivity(callIntent);
            }
        });


        TextView restaurantAddress = (TextView) view.findViewById(R.id.restaurant_address);
        restaurantAddress.setText(data[2]);


        TextView restaurantUrl = (TextView) view.findViewById(R.id.restaurant_url);
        restaurantUrl.setText(Html.fromHtml("<a href=\"" + data[3] + "\">website</a>"));
        restaurantUrl.setMovementMethod(LinkMovementMethod.getInstance());


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] data = (String[]) getItem(position);
        if (data == null) {
            throw new IllegalStateException("this should be called when list is not null");
        }

        // XXX Some code that eventually calls bindView
        // change to a spinner

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.restaurant_item, parent, false);
        }


        bindView(data,convertView,parent);

        return convertView;
    }

    public void changeList(List<String[]> newList) {
        // XXX write me.

        this.list = newList;
        //reset android api base adaptor
        this.notifyDataSetChanged();

    }
}
