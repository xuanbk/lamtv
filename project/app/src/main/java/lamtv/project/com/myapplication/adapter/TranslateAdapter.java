package lamtv.project.com.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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
    private Context context;
    public TranslateAdapter(ArrayList<Translate> itemsData,TextToSpeech textToSpeech,Context context) {
        this.itemsData = itemsData;
        this.textToSpeech = textToSpeech;
        this.context = context;
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
        viewHolder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete All Data")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO gọi hàm xoá dữ liệu theo
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEn,txtVi;
        public ImageView ivFlagVI,ivFlagEN,imvDelete;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEn = (TextView) itemLayoutView.findViewById(R.id.tvTranslate);
            txtVi = (TextView) itemLayoutView.findViewById(R.id.tvTranslateVi);
            ivFlagEN = (ImageView) itemLayoutView.findViewById(R.id.ivFlagEN);
            ivFlagVI = (ImageView) itemLayoutView.findViewById(R.id.ivFlagVI);
            imvDelete = (ImageView) itemLayoutView.findViewById(R.id.imvDelete);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

