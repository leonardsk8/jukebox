package ilioncorp.com.jukebox.model.dto;

import java.io.Serializable;

public class UserLikeVO implements Serializable{
    private String userId;
    private String videoId;
    private boolean like;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
