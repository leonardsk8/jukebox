package ilioncorp.com.jukebox.model.dto;

import android.net.Uri;

public class UserVO {

    private String userUID;
    private String userName;
    private String userEmail;
    private String userUrlImage;
    private String userAbout;
    private String userBirthday;
    private boolean userProfileComplete;
    private String gender;


    public UserVO(String userUID, String userName, String userEmail, String userUrlImage, String userAbout, String userBirthday, boolean userProfileComplete) {
        this.userUID = userUID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userUrlImage = userUrlImage;
        this.userAbout = userAbout;
        this.userBirthday = userBirthday;
        this.userProfileComplete = userProfileComplete;
    }

    public UserVO(String userUID, String userName, String userEmail) {
        this.userUID = userUID;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isUserProfileComplete() {
        return userProfileComplete;
    }

    public void setUserProfileComplete(boolean userProfileComplete) {
        this.userProfileComplete = userProfileComplete;
    }

    public UserVO() {
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserUrlImage() {
        return userUrlImage;
    }

    public void setUserUrlImage(String userUrlImage) {
        this.userUrlImage = userUrlImage;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }
}
