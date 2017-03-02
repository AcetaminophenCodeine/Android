package edu.utexas.cs371m.rs47345.redfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.app.Activity;
import java.net.URL;
import java.util.ArrayList;



public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicViewHolder>
                                                                implements URLFetch.Callback
{
    private ArrayList<RedditRecord> redditRecordsArrayList;
    public Context context;
    RedditRecord record;
    SwipeDetector sd;

    public DynamicAdapter(SwipeDetector sd, Context context)
    {
        redditRecordsArrayList = new ArrayList<>();
        this.sd = sd;
        this.context = context;

    }

    public void addItem(RedditRecord redditRecord) {
        redditRecordsArrayList.add(redditRecord);
        notifyDataSetChanged();
    }

    public class DynamicViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView thumbnail;
        public TextView title;
        public View container;

        public DynamicViewHolder(View view)
        {
            super(view);
            container = view;
            title = (TextView) view.findViewById(R.id.picTextRowText);
            thumbnail = (ImageView) view.findViewById(R.id.picTextRowPic);
        }

    }

    @Override
    public DynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_text_row, parent, false);

        return new DynamicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DynamicViewHolder holder, final int position)
    {

        TextView tv = (TextView) holder.container.findViewById(R.id.picTextRowText);
        record = redditRecordsArrayList.get(position);

        if (BitmapCache.getInstance().getBitmap(record.thumbnailURL.toString()) != null) {
            //Log.d(MainActivity.AppName, "Thumbnail is set by default");
            holder.thumbnail.setImageBitmap(BitmapCache.getInstance().getBitmap(record.thumbnailURL.toString()));
        }
        else
        {
           // Log.d(MainActivity.AppName, "Thumbnail was not set by default");
            holder.thumbnail.setImageBitmap(BitmapCache.defaultThumbnailBitmap);
        }

        holder.title.setText(record.title);

        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sd.swipeDetected() && sd.getAction() == SwipeDetector.Action.RL)
                {
                    redditRecordsArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, redditRecordsArrayList.size());
                    notifyDataSetChanged();
                }
                else
                {
                    goToOnePost(record.imageURL, record.title);
                }
            }
        });



    }

    @Override
    public int getItemCount()
    {
        if (redditRecordsArrayList == null)
            return 0;
        else
            return redditRecordsArrayList.size();
    }


    @Override
    public void fetchStart() {}

    @Override
    public void fetchComplete(String result){

        notifyDataSetChanged();
    }

    @Override
    public void fetchCancel(String url) {}

    protected void goToOnePost(URL imageURL, String title)
    {
        Intent intent = new Intent(context, OnePost.class);
        Bundle b = new Bundle();
        b.putSerializable("imageURL",imageURL);
        b.putString("title",title);
        b.putSerializable("ArrayList", redditRecordsArrayList);
        intent.putExtras(b);
        ((Activity)context).startActivityForResult(intent, 1);
    }
}

