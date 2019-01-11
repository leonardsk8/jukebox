package ilioncorp.com.jukebox.utils.constantes;

import ilioncorp.com.jukebox.model.dto.UserVO;

public enum ERoutes {
    USER_SERVICE("/service","Route for all the service"),
    DOMAIN("http://35.237.73.197:8080/JukeboxAdministrator","Domain of the project"),
    CHECKLOGIN("checkLogin","Option for activity_login on the application"),
    NOTIFY("/servletNotify","Notify a user of a new message");
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
