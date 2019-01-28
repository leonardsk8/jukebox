package ilioncorp.com.jukebox.model.dto;

public class HistorySongVO {

    private String idUser;
    private String nameSong;
    private String dateSong;
    private String stateSong;
    private String thumnailSong;
    private String nameBar;
    private String videoIdSong;


    public String getVideoIdSong() {
        return videoIdSong;
    }

    public void setVideoIdSong(String videoIdSong) {
        this.videoIdSong = videoIdSong;
    }

    public String getNameBar() {
        return nameBar;
    }

    public void setNameBar(String nameBar) {
        this.nameBar = nameBar;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDateSong() {
        return dateSong;
    }

    public void setDateSong(String dateSong) {
        this.dateSong = dateSong;
    }

    public String getStateSong() {
        return stateSong;
    }

    public void setStateSong(String stateSong) {
        this.stateSong = stateSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getThumnailSong() {
        return thumnailSong;
    }

    public void setThumnailSong(String thumnailSong) {
        this.thumnailSong = thumnailSong;
    }
}
