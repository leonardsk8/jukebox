package ilioncorp.com.jukebox.model.dto;

import java.io.Serializable;

public class PromotionsVO implements Serializable{

    private String pro_code;
    private String pro_name;
    private String pro_description;
    private String pro_image;
    private int pro_count;
    private String pro_expiration_date;
    private String pro_creation_date;
    private String pro_limit;

    public String getPro_limit() {
        return pro_limit;
    }

    public void setPro_limit(String pro_limit) {
        this.pro_limit = pro_limit;
    }

    public String getPro_code() {
        return pro_code;
    }

    public void setPro_code(String pro_code) {
        this.pro_code = pro_code;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_description() {
        return pro_description;
    }

    public void setPro_description(String pro_description) {
        this.pro_description = pro_description;
    }

    public String getPro_image() {
        return pro_image;
    }

    public void setPro_image(String pro_image) {
        this.pro_image = pro_image;
    }

    public int getPro_count() {
        return pro_count;
    }

    public void setPro_count(int pro_count) {
        this.pro_count = pro_count;
    }

    public String getPro_expiration_date() {
        return pro_expiration_date;
    }

    public void setPro_expiration_date(String pro_expiration_date) {
        this.pro_expiration_date = pro_expiration_date;
    }

    public String getPro_creation_date() {
        return pro_creation_date;
    }

    public void setPro_creation_date(String pro_creation_date) {
        this.pro_creation_date = pro_creation_date;
    }
}
