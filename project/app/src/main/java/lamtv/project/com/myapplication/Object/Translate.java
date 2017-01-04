package lamtv.project.com.myapplication.Object;

/**
 * Created by LamTV on 07-Nov-16.
 */

public class Translate {
    private int translate_id;
    private String en;
    private String vi;
    private boolean isEnglish;
    public Translate(String en, String vi, boolean isEnglish) {
        this.en = en;
        this.vi = vi;
        this.isEnglish = isEnglish;
    }

    public Translate() {
    }

    public int getTranslate_id() {
        return translate_id;
    }

    public void setTranslate_id(int translate_id) {
        this.translate_id = translate_id;
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
