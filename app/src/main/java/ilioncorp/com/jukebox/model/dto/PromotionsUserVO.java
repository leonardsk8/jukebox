package ilioncorp.com.jukebox.model.dto;

public class PromotionsUserVO {
    private String proU_code;
    private String proU_limit;
    private String proU_times;
    private String proU_idBar;
    private String proU_userId;

    public PromotionsUserVO() {
    }

    public PromotionsUserVO(String proU_code, String proU_limit, String proU_times, String proU_idBar, String proU_userId) {
        this.proU_code = proU_code;
        this.proU_limit = proU_limit;
        this.proU_times = proU_times;
        this.proU_idBar = proU_idBar;
        this.proU_userId = proU_userId;
    }

    public String getProU_code() {
        return proU_code;
    }

    public void setProU_code(String proU_code) {
        this.proU_code = proU_code;
    }

    public String getProU_limit() {
        return proU_limit;
    }

    public void setProU_limit(String proU_limit) {
        this.proU_limit = proU_limit;
    }

    public String getProU_times() {
        return proU_times;
    }

    public void setProU_times(String proU_times) {
        this.proU_times = proU_times;
    }

    public String getProU_idBar() {
        return proU_idBar;
    }

    public void setProU_idBar(String proU_idBar) {
        this.proU_idBar = proU_idBar;
    }

    public String getProU_userId() {
        return proU_userId;
    }

    public void setProU_userId(String proU_userId) {
        this.proU_userId = proU_userId;
    }
}
