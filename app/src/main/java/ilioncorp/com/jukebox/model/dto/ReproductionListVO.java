package ilioncorp.com.jukebox.model.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReproductionListVO implements Serializable,Comparable<ReproductionListVO>{

    private String name;
    private String thumbnail;
    private String video_id;
    private String user;
    private String num;
    private String userId;
    private boolean reproducing;
    private int likes;
    private boolean approved;
    private String token;
    private int sum;
    private Map<String, UserLikeVO> listLikes;

    public Map<String, UserLikeVO> getListLikes() {
        if(listLikes!=null)
        return listLikes;
        else
            return new HashMap<>();
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setListLikes(Map<String, UserLikeVO> listLikes) {
        this.listLikes = listLikes;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isReproducing() {
        return reproducing;
    }

    public void setReproducing(boolean reproducing) {
        this.reproducing = reproducing;
    }

    public int getLikes() {
        if(getListLikes()!=null)
            likes = getListLikes().size()+sum;
        if(isReproducing())
            likes = 1000;
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getNum() {
        return num;
    }


    public void setNum(String num) {
        this.num = num;
    }
    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public int compareTo(@NonNull ReproductionListVO o) {
        if (Integer.parseInt(num) < Integer.parseInt(o.num)) {
            return -1;
        }
        if (Integer.parseInt(num)> Integer.parseInt(o.num)) {
            return 1;
        }
        return 0;
    }
}
