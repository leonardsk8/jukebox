package ilioncorp.com.jukebox.utils.constantes;

import ilioncorp.com.jukebox.model.dto.UserVO;

public enum ERoutes {
    USER_SERVICE("/service","Route for all the service"),
    DOMAIN("http://ilioncorp.com:8080/sellerApp","Domain of the project"),
    CHECKLOGIN("checkLogin","Option for login on the application");
    ;

    private String url;
    private String description;


    ERoutes(String url, String description) {

        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
