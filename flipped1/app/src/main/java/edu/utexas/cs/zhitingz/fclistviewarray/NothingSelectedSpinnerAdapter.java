package edu.utexas.cs.zhitingz.fclistviewarray;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * Created by zhitingz on 9/8/16.
 */
public class NothingSelectedSpinnerAdapter implements SpinnerAdapter, ListAdapter {

    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected int nothingSelectedLayout;
    protected int nothingSelectedDropDownLayout;
    protected LayoutInflater layoutInflater;

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         int nothingSelectedLayout, Context context) {
        this(spinnerAdapter, nothingSelectedLayout, -1, context);
    }

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         int nothingSelectedLayout,
                                         int nothingSelectedDropDownLayout,
                                         Context context) {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.nothingSelectedDropDownLayout = nothingSelectedDropDownLayout;
        this.nothingSelectedLayout = nothingSelectedLayout;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return i != 0;
    }

    protected View getNothingSelectedDropdownView(ViewGroup viewGroup) {
        return layoutInflater.inflate(nothingSelectedDropDownLayout, viewGroup, false);
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (i == 0) {
            return nothingSelectedDropDownLayout == -1 ?
                    new View(context) :
                    getNothingSelectedDropdownView(viewGroup);
        }

        return adapter.getDropDownView(i - EXTRA, null, viewGroup);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        adapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        adapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int i) {
        return i == 0 ? null : adapter.getItem(i - EXTRA);
    }

    @Override
    public long getItemId(int i) {
        return i >= EXTRA ? adapter.getItemId(i - EXTRA) : i;
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    protected View getNothingSelectedView(ViewGroup viewGroup) {
        return layoutInflater.inflate(nothingSelectedLayout, viewGroup, false);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == 0) {
            return getNothingSelectedView(viewGroup);
        }
        return adapter.getView(i - EXTRA, null, viewGroup);
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }
}
