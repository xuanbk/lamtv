package lamtv.project.com.myapplication;


import java.util.ArrayList;

import lamtv.project.com.myapplication.Object.Translate;

public class Application extends android.app.Application{
    private ArrayList<Translate> translates;
    @Override
    public void onCreate() {
        super.onCreate();
        translates = new ArrayList<>();
    }

    public ArrayList<Translate> getTranslates() {
        return translates;
    }

    public void setTranslates(ArrayList<Translate> translates) {
        this.translates = translates;
    }
}
