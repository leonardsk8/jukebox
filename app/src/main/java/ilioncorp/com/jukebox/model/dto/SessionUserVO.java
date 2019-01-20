package ilioncorp.com.jukebox.model.dto;

public class SessionUserVO  {

    private String userId;
    private String establishmentId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(String establishmentId) {
        this.establishmentId = establishmentId;
    }
}
