package lamtv.project.com.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lamtv.project.com.myapplication.Object.Route;
import lamtv.project.com.myapplication.R;


public class Map2Adapter extends RecyclerView.Adapter<Map2Adapter.ViewHolder> {
    private List<Route> mDataset;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
        }
    }
    public Map2Adapter(List<Route>mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    public Map2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_map, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvName.setText(mDataset.get(position).startAddress + " " +mDataset.get(position).endAddress);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();}
}
