package lamtv.project.com.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Travles>mDataset;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView,tvName;
        public ImageView imgTravles;
        public CardView card_view;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tvDescription);
            tvName = (TextView) v.findViewById(R.id.tvName);
            imgTravles=(ImageView)v.findViewById(R.id.imgTralves);
            card_view = (CardView)v.findViewById(R.id.card_view);
        }
    }
    public MyAdapter(ArrayList<Travles>mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getDescription());
        holder.tvName.setText(mDataset.get(position).getName());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("TRAVLES",mDataset.get(position));
                context.startActivity(intent);
            }
        });
        Glide.with(context)
                .load(mDataset.get(position).getLinkimage_1())
                .crossFade()
                .centerCrop()
                .into(holder.imgTravles);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();}
}
