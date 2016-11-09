package lamtv.project.com.myapplication.Object;

/**
 * Created by Le Xuan on 07-Nov-16.
 */

public class Translate {
    private String en;
    private String vi;
    private boolean isEnglish;
    public Translate(String en, String vi, boolean isEnglish) {
        this.en = en;
        this.vi = vi;
        this.isEnglish = isEnglish;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public boolean isEnglish() {
        return isEnglish;
    }

    public void setEnglish(boolean english) {
        isEnglish = english;
    }
}
