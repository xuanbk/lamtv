package lamtv.project.com.myapplication.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.R;



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
        if (itemsData.get(position).isEnglish()){
            viewHolder.ivFlagEN.setBackgroundResource(R.mipmap.english_flag);
            viewHolder.ivFlagVI.setBackgroundResource(R.mipmap.vn_flag);
        }else {
            viewHolder.ivFlagVI.setBackgroundResource(R.mipmap.english_flag);
            viewHolder.ivFlagEN.setBackgroundResource(R.mipmap.vn_flag);
        }
        viewHolder.txtEn.setText(itemsData.get(position).getEn());
        viewHolder.txtVi.setText(itemsData.get(position).getVi());
        viewHolder.txtVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemsData.get(position).isEnglish()){
                    textToSpeech.setLanguage(new Locale("vi_VN"));
                }else {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }

                textToSpeech.speak(itemsData.get(position).getVi(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        viewHolder.txtEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemsData.get(position).isEnglish()){

                    textToSpeech.setLanguage(Locale.ENGLISH);
                }else {
                    textToSpeech.setLanguage(new Locale("vi_VN"));
                }
                textToSpeech.speak(itemsData.get(position).getEn(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEn,txtVi;
        public ImageView ivFlagVI,ivFlagEN;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEn = (TextView) itemLayoutView.findViewById(R.id.tvTranslate);
            txtVi = (TextView) itemLayoutView.findViewById(R.id.tvTranslateVi);
            ivFlagEN = (ImageView) itemLayoutView.findViewById(R.id.ivFlagEN);
            ivFlagVI = (ImageView) itemLayoutView.findViewById(R.id.ivFlagVI);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

