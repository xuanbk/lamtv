package lamtv.project.com.myapplication.adapter;

import android.content.Context;
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

public class TranslateAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Translate> arr;

    public TranslateAdapter(Context context, ArrayList<Translate> arrString) {
        this.arr = arrString;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if(holder == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_translate,null,false);
            holder.tvText = (TextView) view.findViewById(R.id.tvTranslate);
            holder.tvTranslate = (TextView) view.findViewById(R.id.tvTranslateVi);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        holder.tvText.setText(arr.get(i).getEn());
        holder.tvTranslate.setText(arr.get(i).getVi());
        return view;
    }

    private class Holder {
        TextView tvText,tvTranslate;
    }
}
