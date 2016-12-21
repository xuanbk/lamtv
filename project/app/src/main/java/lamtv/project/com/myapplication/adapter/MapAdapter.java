package lamtv.project.com.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lamtv.project.com.myapplication.DetailActivity;
import lamtv.project.com.myapplication.Object.Travles;
import lamtv.project.com.myapplication.R;


public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private ArrayList<String>mDataset;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
        }
    }
    public MapAdapter(ArrayList<String>mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    public MapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        holder.tvName.setText(Html.fromHtml(mDataset.get(position)));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();}
}
