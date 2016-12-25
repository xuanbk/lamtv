package lamtv.project.com.myapplication.Object;



public class Interested {
    private int like_id;
    private String trave_like_id;
    public  Interested(String trave_like_id,int like_id){
        this.trave_like_id = trave_like_id;
        this.like_id = like_id;
    }

    public Interested() {

    }

    public int getLike_id() {
        return like_id;
    }

    public void setLike_id(int like_id) {
        this.like_id = like_id;
    }

    public String getTrave_like_id() {
        return trave_like_id;
    }

    public void setTrave_like_id(String trave_like_id) {
        this.trave_like_id = trave_like_id;
    }
}
