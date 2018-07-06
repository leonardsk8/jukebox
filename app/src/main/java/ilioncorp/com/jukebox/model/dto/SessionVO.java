package ilioncorp.com.jukebox.model.dto;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class SessionVO implements Serializable{

    private String sessionUserId;
    private String sessionState;
    private String sessionDateStart;
    private String sessionUserToken;
    private String sessionUserName;
    private String sessionUserImage;
    private String sessionUserBar;


    public String getSessionUserBar() {
        return sessionUserBar;
    }

    public void setSessionUserBar(String sessionUserBar) {
        this.sessionUserBar = sessionUserBar;
    }

    public String getSessionUserImage() {
        return sessionUserImage;
    }

    public void setSessionUserImage(String sessionUserImage) {
        this.sessionUserImage = sessionUserImage;
    }

    public String getSessionUserName() {
        return sessionUserName;
    }

    public void setSessionUserName(String sessionUserName) {
        this.sessionUserName = sessionUserName;
    }

    public String getSessionUserToken() {
        return sessionUserToken;
    }

    public void setSessionUserToken(String sessionUserToken) {
        this.sessionUserToken = sessionUserToken;
    }

    public String getSessionUserId() {
        return sessionUserId;
    }

    public void setSessionUserId(String sessionUserId) {
        this.sessionUserId = sessionUserId;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getSessionDateStart() {
        return sessionDateStart;
    }

    public void setSessionDateStart(String sessionDateStart) {
        this.sessionDateStart = sessionDateStart;
    }
}
