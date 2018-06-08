package ilioncorp.com.jukebox.model.dto;

import java.io.Serializable;

public class ReproductionListVO implements Serializable{

    private String name;
    private String thumbnail;
    private String video_id;
    private String user;
    private boolean approved;

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
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
}
