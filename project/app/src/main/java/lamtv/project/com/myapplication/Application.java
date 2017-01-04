package lamtv.project.com.myapplication;


import java.util.ArrayList;

import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.Object.Travles;

public class Application extends android.app.Application{
    private ArrayList<Translate> translates;
    private ArrayList<Travles> arrTemp;
    MyDatabaseHelper db = new MyDatabaseHelper(this);
    @Override
    public void onCreate() {
        super.onCreate();
        translates = new ArrayList<>();
        arrTemp = new ArrayList<>();
    }

    public ArrayList<Translate> getTranslates() {
        translates =  db.getAllNotes();
        return translates;
    }

    public void setTranslates(ArrayList<Translate> translates) {
        this.translates = translates;
    }

    public ArrayList<Travles> getArrTemp() {
        return arrTemp;
    }

    public void setArrTemp(ArrayList<Travles> arrTemp) {
        this.arrTemp = arrTemp;
    }
}
