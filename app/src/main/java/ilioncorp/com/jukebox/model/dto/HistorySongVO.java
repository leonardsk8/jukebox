package ilioncorp.com.jukebox.model.dto;

public class HistorySongVO {
    private int id;
    private String nameSong;
    private String urlSong;
    private String thumnailSong;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getUrlSong() {
        return urlSong;
    }

    public void setUrlSong(String urlSong) {
        this.urlSong = urlSong;
    }

    public String getThumnailSong() {
        return thumnailSong;
    }

    public void setThumnailSong(String thumnailSong) {
        this.thumnailSong = thumnailSong;
    }
}
