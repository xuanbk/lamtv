package lamtv.project.com.myapplication.Object;

import java.io.Serializable;


public class Travles implements Serializable{
    private String id;
    private String coordinate_1;
    private String coordinate_2;
    private String description;
    private String like="";
    private String linkimage_1;
    private String linkimage_2;
    private String loacation;
    private String name;

    public Travles() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoordinate_1() {
        return coordinate_1;
    }

    public void setCoordinate_1(String coordinate_1) {
        this.coordinate_1 = coordinate_1;
    }

    public String getCoordinate_2() {
        return coordinate_2;
    }

    public void setCoordinate_2(String coordinate_2) {
        this.coordinate_2 = coordinate_2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLinkimage_1() {
        return linkimage_1;
    }

    public void setLinkimage_1(String linkimage_1) {
        this.linkimage_1 = linkimage_1;
    }

    public String getLinkimage_2() {
        return linkimage_2;
    }

    public void setLinkimage_2(String linkimage_2) {
        this.linkimage_2 = linkimage_2;
    }

    public String getLoacation() {
        return loacation;
    }

    public void setLoacation(String loacation) {
        this.loacation = loacation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
