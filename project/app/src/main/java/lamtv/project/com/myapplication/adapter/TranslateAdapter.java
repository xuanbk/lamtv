package lamtv.project.com.myapplication.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.R;

/**
 * Created by Le Xuan on 06-Nov-16.
 */

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.ViewHolder> {
    private ArrayList<Translate> itemsData;
    private TextToSpeech textToSpeech;
    public TranslateAdapter(ArrayList<Translate> itemsData,TextToSpeech textToSpeech) {
        this.itemsData = itemsData;
        this.textToSpeech = textToSpeech;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TranslateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_translate, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtEn.setText(itemsData.get(position).getEn());
        viewHolder.txtVi.setText(itemsData.get(position).getVi());
        viewHolder.txtVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textToSpeech.speak(itemsData.get(position).getVi(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        viewHolder.txtEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(itemsData.get(position).getEn(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEn,txtVi;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEn = (TextView) itemLayoutView.findViewById(R.id.tvTranslate);
            txtVi = (TextView) itemLayoutView.findViewById(R.id.tvTranslateVi);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

